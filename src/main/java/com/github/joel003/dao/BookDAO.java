package com.github.joel003.dao;

import com.github.joel003.entity.Book;
import com.github.joel003.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookDAO {

    private final  Connection con = DBConnection.getConnection() ;

    public boolean addBook(Book book) throws SQLException {

        String insertBook = "INSERT INTO books (title, author, subject, isbn) VALUES (?, ?, ?, ?) RETURNING book_id";

        try (PreparedStatement ps = con.prepareStatement(insertBook)) {

            ps.setString(1, book.getTitle());
            ps.setString(2, book.getAuthor());
            ps.setString(3, book.getSubject());
            ps.setString(4, book.getIsbn());

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                int bookId = rs.getInt("book_id");
                int numberOfCopies = book.getTotalCopies();
                String insertCopy =
                        "INSERT INTO book_copies (book_id) VALUES (?)";

                try (PreparedStatement copyPS = con.prepareStatement(insertCopy)) {
                    for (int i = 0; i < numberOfCopies; i++) {
                        copyPS.setInt(1, bookId);
                        copyPS.addBatch();
                    }
                    copyPS.executeBatch();
                }
            }

            return true;

        }
    }

    public boolean updateBook(int id, int choice, Book book) throws SQLException {
        String query = switch (choice) {
            case 1 -> "UPDATE books SET title = ? WHERE book_id = ?";
            case 2 -> "UPDATE books SET author = ? WHERE book_id = ?";
            case 3 -> "UPDATE books SET subject = ? WHERE book_id = ?";
            case 4 -> "UPDATE books SET isbn = ? WHERE book_id = ?";
            default -> throw new IllegalStateException("Invalid update choice");
        };

        try (PreparedStatement ps = con.prepareStatement(query)) {

            switch (choice) {
                case 1 -> ps.setString(1, book.getTitle());
                case 2 -> ps.setString(1, book.getAuthor());
                case 3 -> ps.setString(1, book.getSubject());
                case 4 -> ps.setString(1, book.getIsbn());
            }

            ps.setInt(2, id);

            return ps.executeUpdate() > 0;

        }
    }

    public boolean updateBookCopies(int id, Book book)throws SQLException {

        try {
            con.setAutoCommit(false);

            // 1. Get current copy count
            String countCopies = "SELECT COUNT(*) AS cnt FROM book_copies WHERE book_id = ?";
            int currentCount = 0;
            int newTotalCopies = book.getTotalCopies();


            try (PreparedStatement ps = con.prepareStatement(countCopies)) {
                ps.setInt(1, id);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) currentCount = rs.getInt("cnt");
            }

            // CASE A: Need to ADD copies
            if (newTotalCopies > currentCount) {

                String insertCopy = "INSERT INTO book_copies (book_id) VALUES (?)";

                try (PreparedStatement ps = con.prepareStatement(insertCopy)) {

                    int toAdd = newTotalCopies - currentCount;

                    for (int i = 0; i < toAdd; i++) {
                        ps.setInt(1, id);
                        ps.addBatch();
                    }

                    ps.executeBatch();
                }
            }

            // CASE B: Need to REMOVE copies
            else if (newTotalCopies < currentCount) {

                int toRemove = currentCount - newTotalCopies;

                String deleteSql = """
                DELETE FROM book_copies
                WHERE copy_id IN (
                    SELECT copy_id FROM book_copies
                    WHERE book_id = ? AND is_issued = FALSE
                    LIMIT ?
                )
                """;

                try (PreparedStatement ps = con.prepareStatement(deleteSql)) {
                    ps.setInt(1, id);
                    ps.setInt(2, toRemove);
                    ps.executeUpdate();
                }
            }

            con.commit();
            return true;

        } catch (SQLException e) {
            con.rollback();
            throw e;
        } finally {
            con.setAutoCommit(true);
        }
    }

    public boolean deleteBook(int id) throws SQLException {
        String sql = "DELETE FROM books WHERE book_id = ?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    public List<Book> viewAllBooks() throws SQLException {

        List<Book> list = new ArrayList<>();

        String sql = """
        SELECT b.book_id, b.title, b.author, b.subject,b.isbn,
               COUNT(c.copy_id) AS total_copies,
               SUM(CASE WHEN c.is_issued = FALSE THEN 1 ELSE 0 END) AS available_copies
        FROM books b
        LEFT JOIN book_copies c ON b.book_id = c.book_id
        GROUP BY b.book_id
        ORDER BY b.book_id
    """;

        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Book b = new Book();

                b.setBookId(rs.getInt("book_id"));
                b.setTitle(rs.getString("title"));
                b.setAuthor(rs.getString("author"));
                b.setSubject(rs.getString("subject"));
                b.setIsbn(rs.getString("isbn"));
                b.setTotalCopies(rs.getInt("total_copies"));
                b.setAvailableCopies(rs.getInt("available_copies"));

                list.add(b);
            }

        }
        return list;
    }

    public List<Book> searchBook(int choice, String value) throws SQLException {

        List<Book> list = new ArrayList<>();

        String finalQuery = FinalSearchQuery(choice);

        try (PreparedStatement ps = con.prepareStatement(finalQuery)) {

            // SWITCH → bind parameter
            if (choice == 1 ){
                ps.setInt(1, Integer.parseInt(value));
            } else if(choice == 5){
                ps.setString(1, value);
            }
            else {
                ps.setString(1, "%" + value + "%");
            }

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Book b = new Book();

                b.setBookId(rs.getInt("book_id"));
                b.setTitle(rs.getString("title"));
                b.setAuthor(rs.getString("author"));
                b.setSubject(rs.getString("subject"));
                b.setIsbn(rs.getString("isbn"));
                b.setTotalCopies(rs.getInt("total_copies"));
                b.setAvailableCopies(rs.getInt("available_copies"));

                list.add(b);
            }
        }
        return list;
    }

    private static String FinalSearchQuery(int choice) {
        String baseQuery = """
                            SELECT b.book_id, b.title, b.author, b.subject, b.isbn,
                                   COUNT(c.copy_id) AS total_copies,
                                   SUM(CASE WHEN c.is_issued = FALSE THEN 1 ELSE 0 END) AS available_copies
                            FROM books b
                            LEFT JOIN book_copies c ON b.book_id = c.book_id
                            WHERE
                            """;

        // SWITCH → build WHERE condition
        String filter = switch (choice) {
            case 1 -> "b.book_id = ?";                         // ID
            case 2 -> "LOWER(b.title) LIKE LOWER(?)";         // Title
            case 3 -> "LOWER(b.author) LIKE LOWER(?)";        // Author
            case 4 -> "LOWER(b.subject) LIKE LOWER(?)";       // Subject/Category
            case 5 -> "b.isbn = ?";                           // ISBN
            default -> throw new IllegalStateException("Invalid search option");
        };

        return baseQuery + filter +
                " GROUP BY b.book_id ORDER BY b.book_id";

    }

    public Integer getAvailableCopy(int bookId) throws SQLException {
        String sql = """
                        SELECT copy_id
                        FROM book_copies
                        WHERE book_id = ? AND is_issued = FALSE
                        ORDER BY copy_id ASC
                        LIMIT 1
                    """;

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, bookId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt("copy_id");
            }
        }
        return null;
    }

}
