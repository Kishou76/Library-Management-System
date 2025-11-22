package com.github.joel003.dao;

import com.github.joel003.entity.Loan;
import com.github.joel003.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    private final  Connection con = DBConnection.getConnection() ;

    /*---------------------------------------------------------
                          LOGIN
    ----------------------------------------------------------*/
    public Integer loginUser(String email, String password) throws SQLException {
        String sql = "SELECT borrower_id FROM borrowers WHERE email=? AND password=?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, email);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) return rs.getInt("borrower_id");
        }
        return null;
    }

    /*---------------------------------------------------------
                  VIEW USER ACTIVE LOANS
    ----------------------------------------------------------*/
    public List<Loan> getMyLoans(int borrowerId) throws SQLException {

        List<Loan> list = new ArrayList<>();

        String sql = """
        SELECT loan_id, book_id, copy_id, issue_date, due_date
        FROM loans
        WHERE borrower_id=? AND return_date IS NULL
        """;

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, borrowerId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Loan loan = new Loan();

                loan.setLoanId(rs.getInt("loan_id"));
                loan.setBookId(rs.getInt("book_id"));
                loan.setCopyId(rs.getInt("copy_id"));
                loan.setIssueDate(rs.getDate("issue_date").toLocalDate());
                loan.setDueDate(rs.getDate("due_date").toLocalDate());

                list.add(loan);
            }
        }
        return list;
    }
}
