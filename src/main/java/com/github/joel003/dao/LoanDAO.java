package com.github.joel003.dao;

import com.github.joel003.entity.Loan;
import com.github.joel003.util.DBConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class LoanDAO {

    private final Connection con = DBConnection.getConnection() ;

    public boolean issueBook(Loan loan) throws SQLException {

        String insertLoan = """
        INSERT INTO loans (book_id, copy_id, borrower_id, issue_date, due_date)
        VALUES (?, ?, ?, ?, ?)
    """;

        String updateCopy = """
        UPDATE book_copies
        SET is_issued = TRUE
        WHERE copy_id = ?
    """;

        try {
            con.setAutoCommit(false);

            // 1. Insert loan
            try (PreparedStatement ps = con.prepareStatement(insertLoan)) {
                ps.setInt(1, loan.getBookId());
                ps.setInt(2, loan.getCopyId());
                ps.setInt(3, loan.getBorrowerId());
                ps.setDate(4, Date.valueOf(loan.getIssueDate()));
                ps.setDate(5, Date.valueOf(loan.getDueDate()));
                ps.executeUpdate();
            }

            // 2. Update book copy status
            try (PreparedStatement ps2 = con.prepareStatement(updateCopy)) {
                ps2.setInt(1, loan.getCopyId());
                ps2.executeUpdate();
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


    public LocalDate getDueDate(Loan loan) throws SQLException {
        String query = """
        SELECT due_date
        FROM loans
        WHERE copy_id = ? AND return_date IS NULL
        """;

        try(PreparedStatement ps = con.prepareStatement(query)) {

            ps.setInt(1, loan.getCopyId());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getDate("due_date").toLocalDate();
            }
        }
        return null;
    }

    public boolean returnBook(Loan loan) throws SQLException {

        String updateLoan = """
        UPDATE loans
        SET return_date = ?
        WHERE copy_id = ? AND return_date IS NULL
    """;

        String updateCopy = """
        UPDATE book_copies
        SET is_issued = FALSE
        WHERE copy_id = ?
    """;

        try {
            con.setAutoCommit(false);

            // 1. Update loan return date
            try (PreparedStatement ps = con.prepareStatement(updateLoan)) {
                ps.setDate(1, Date.valueOf(loan.getReturnDate()));
                ps.setInt(2, loan.getCopyId());
                ps.executeUpdate();
            }

            // 2. Mark book copy as available
            try (PreparedStatement ps2 = con.prepareStatement(updateCopy)) {
                ps2.setInt(1, loan.getCopyId());
                ps2.executeUpdate();
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


    public List<Loan> viewActiveLoans() throws SQLException {

        String sql = """
            SELECT loan_id, book_id, copy_id, borrower_id, issue_date, due_date
            FROM loans
            WHERE return_date IS NULL
        """;

        List<Loan> loans = new ArrayList<>();
        try (PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Loan loan = new Loan();
                loan.setLoanId(rs.getInt("loan_id"));
                loan.setBookId(rs.getInt("book_id"));
                loan.setCopyId(rs.getInt("copy_id"));
                loan.setBorrowerId(rs.getInt("borrower_id"));
                loan.setIssueDate(rs.getDate("issue_date").toLocalDate());
                loan.setDueDate(rs.getDate("due_date").toLocalDate());
                loan.setReturnDate(null);

                loans.add(loan);
            }
        }
        return loans;
    }

    public Loan getActiveLoan(int borrowerId, int bookId) throws SQLException {
            String sql = """
                            SELECT copy_id, due_date
                            FROM loans
                            WHERE borrower_id = ? AND book_id = ? AND return_date IS NULL
                            LIMIT 1
                        """;

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, borrowerId);
            ps.setInt(2, bookId);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Loan loan = new Loan();
                loan.setCopyId(rs.getInt("copy_id"));
                loan.setDueDate(rs.getDate("due_date").toLocalDate());
                return loan;
            }
        }
        return null;
    }

}

