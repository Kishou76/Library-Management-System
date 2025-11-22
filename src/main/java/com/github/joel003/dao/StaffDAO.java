package com.github.joel003.dao;

import com.github.joel003.entity.Borrower;
import com.github.joel003.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StaffDAO {

    private final  Connection con = DBConnection.getConnection();

    public String loginStaff(String staffEmail, String password) throws  SQLException {
        String query ="SELECT role FROM staff WHERE email=? AND password=?";

        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, staffEmail);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getString("role");
            }
        }
        return null;
    }

    public boolean addBorrower(Borrower borrower) throws SQLException {

        String query = "INSERT INTO borrowers (name, email, password, phone_number) VALUES (?, ?, ?, ?)";

        try (PreparedStatement ps = con.prepareStatement(query)) {

            ps.setString(1, borrower.getName());
            ps.setString(2, borrower.getEmail());
            ps.setString(3, borrower.getPassword());
            ps.setString(4, borrower.getPhoneNumber());

            int rows = ps.executeUpdate();
            return rows > 0;
        }
    }

    public boolean deleteBorrower(int id) throws SQLException {
        String sql = "DELETE FROM borrowers WHERE borrower_id = ?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);

            int rows = ps.executeUpdate();
            return rows > 0;

        }
    }

    public boolean updateBorrower(int id, int choice, Borrower borrower) throws SQLException {
        String query = switch (choice) {
            case 1 -> "UPDATE borrowers SET name = ? WHERE borrower_id = ?";
            case 2 -> "UPDATE borrowers SET email = ? WHERE borrower_id = ?";
            case 3 -> "UPDATE borrowers SET phone_number = ? WHERE borrower_id = ?";
            default -> throw new IllegalStateException("Invalid update choice");
        };

        try (PreparedStatement ps = con.prepareStatement(query)) {

            switch (choice) {
                case 1 -> ps.setString(1, borrower.getName());
                case 2 -> ps.setString(1, borrower.getEmail());
                case 3 -> ps.setString(1, borrower.getPhoneNumber());
            }
            ps.setInt(2, id);

            return ps.executeUpdate() > 0;
        }
    }

    public List<Borrower> viewAllBorrowers() throws SQLException {
        List<Borrower> list = new ArrayList<>();

        String query = "SELECT borrower_id, name, email, phone_number FROM borrowers ORDER BY borrower_id";

        try (PreparedStatement ps = con.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Borrower b = new Borrower();

                b.setId(rs.getInt("borrower_id"));
                b.setName(rs.getString("name"));
                b.setEmail(rs.getString("email"));
                b.setPhoneNumber(rs.getString("phone_number"));

                list.add(b);
            }
        }
        return list;
    }

    public List<Borrower> searchBorrower(int choice, Borrower borrower) throws SQLException{

        List<Borrower> list = new ArrayList<>();

        String sql = switch (choice) {
            case 1 -> "SELECT borrower_id, name, email, phone_number FROM borrowers WHERE borrower_id = ?";
            case 2 -> "SELECT borrower_id, name, email, phone_number FROM borrowers WHERE LOWER(name) LIKE LOWER(?)";
            case 3 -> "SELECT borrower_id, name, email, phone_number FROM borrowers WHERE email = ?";
            default -> throw new IllegalStateException("Invalid search choice");
        };

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            switch (choice) {
                case 1 -> ps.setInt(1, borrower.getId());
                case 2 -> ps.setString(1, "%" + borrower.getName() + "%");
                case 3 -> ps.setString(1, borrower.getEmail());
            }

            ResultSet rs = ps.executeQuery();

            while (rs.next()){
                Borrower b = new Borrower();

                b.setId(rs.getInt("borrower_id"));
                b.setName(rs.getString("name"));
                b.setEmail(rs.getString("email"));
                b.setPhoneNumber(rs.getString("phone_number"));

                list.add(b);
            }

        }
        return list;
    }


}
