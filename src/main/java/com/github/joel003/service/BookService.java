package com.github.joel003.service;

import com.github.joel003.dao.BookDAO;
import com.github.joel003.entity.Book;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

public class BookService {

    private final BookDAO bookDAO ;
    public BookService()
    {
        this.bookDAO = new BookDAO();
    }


    public boolean addBook(Book book) {
        try{
            return bookDAO.addBook(book);
        }catch (SQLException e){
            System.err.println("Database Error (Add Book): " + e.getMessage());
            return false;
        }
    }

    public boolean updateBook(int id, int choice, Book book) {
        try {
            return bookDAO.updateBook(id,choice,book);
        }catch (SQLException e){
            System.err.println("Database Error (Update Book): " + e.getMessage());
            return false;
        }
    }

    public boolean updateBookCopies(int id, Book book) {
        try {
            return bookDAO.updateBookCopies(id,book);

        }catch (SQLException e){
            System.err.println("Database Error (Update Book Copies): " + e.getMessage());
            return false;
        }
    }

    public boolean deleteBook(int id) {
        try {
            return bookDAO.deleteBook(id);
        }catch (SQLException e){
            System.err.println("Database Error (Delete Book): " + e.getMessage());
            return false;
        }
    }

    public List<Book> viewAllBooks() {
        try {
            return bookDAO.viewAllBooks();
        } catch (SQLException e) {
            System.err.println("Database Error (View All Books): " + e.getMessage());
            return Collections.emptyList();
        }

    }

    public List<Book> searchBook(int choice, String value) {
        try{
            return bookDAO.searchBook(choice,value);
        }catch (SQLException e){
            System.err.println("Database Error (Search Books): " + e.getMessage());
            return Collections.emptyList();
        }
    }
}
