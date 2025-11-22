package com.github.joel003.dao;

import com.github.joel003.entity.Clerk;
import com.github.joel003.entity.Librarian;
import com.github.joel003.entity.Staff;
import com.github.joel003.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AdminDAO {

    private final  Connection con = DBConnection.getConnection() ;

    /*---------------------------------------------------------
                       ADMIN LOGIN
    ----------------------------------------------------------*/

    public boolean verifyAdmin(String userID, String password) throws SQLException {
        String sql = "SELECT 1 FROM admin WHERE user_id=? AND password=?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, userID);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();
            return rs.next();
        }
    }

    /*---------------------------------------------------------
                       VIEW ALL STAFF
    ----------------------------------------------------------*/

    public List<Staff> viewAllStaff() throws SQLException {

        String query = "SELECT * FROM Staff ORDER BY id ASC";
        List<Staff> staffList = new ArrayList<>();

        try (PreparedStatement ps = con.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                Staff staff = new Staff();
                staff.setId(rs.getInt("id"));
                staff.setName(rs.getString("name"));
                staff.setPassword(rs.getString("password"));
                staff.setEmail(rs.getString("email"));
                staff.setPhoneNumber(rs.getString("phno"));
                staff.setSalary(rs.getDouble("salary"));
                staff.setRole(rs.getString("role"));

                staffList.add(staff);
            }
        }

        return staffList;
    }

    /*---------------------------------------------------------
                       ADD CLERK
    ----------------------------------------------------------*/

    public boolean addClerk(Clerk clerk) throws SQLException {

        String insertStaff =
                "INSERT INTO Staff (name, password, email, phno, salary, role) " +
                        "VALUES (?, ?, ?, ?, ?, ?) RETURNING id";

        String insertClerk = "INSERT INTO Clerk (id, deskid) VALUES (?, ?)";

        con.setAutoCommit(false);

        try {
            int staffId;

            // Insert staff row
            try (PreparedStatement ps = con.prepareStatement(insertStaff)) {
                ps.setString(1, clerk.getName());
                ps.setString(2, clerk.getPassword());
                ps.setString(3, clerk.getEmail());
                ps.setString(4, clerk.getPhoneNumber());
                ps.setDouble(5, clerk.getSalary());
                ps.setString(6, clerk.getRole());

                ResultSet rs = ps.executeQuery();
                if (!rs.next()) throw new SQLException("Failed to generate Staff ID");

                staffId = rs.getInt("id");
            }

            // Insert clerk row
            try (PreparedStatement ps2 = con.prepareStatement(insertClerk)) {
                ps2.setInt(1, staffId);
                ps2.setString(2, clerk.getDeskID());
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

    /*---------------------------------------------------------
                       UPDATE CLERK
    ----------------------------------------------------------*/

    public boolean updateClerk(Clerk clerk, int choice, int id) throws SQLException {

        String query = switch (choice) {
            case 1 -> "UPDATE Staff SET name=? WHERE id=?";
            case 2 -> "UPDATE Staff SET email=? WHERE id=?";
            case 3 -> "UPDATE Staff SET password=? WHERE id=?";
            case 4 -> "UPDATE Staff SET phno=? WHERE id=?";
            case 5 -> "UPDATE Staff SET salary=? WHERE id=?";
            case 6 -> "UPDATE Clerk SET deskid=? WHERE id=?";
            default -> throw new IllegalStateException("Invalid update choice");
        };

        try (PreparedStatement ps = con.prepareStatement(query)) {

            switch (choice) {
                case 1 -> ps.setString(1, clerk.getName());
                case 2 -> ps.setString(1, clerk.getEmail());
                case 3 -> ps.setString(1, clerk.getPassword());
                case 4 -> ps.setString(1, clerk.getPhoneNumber());
                case 5 -> ps.setDouble(1, clerk.getSalary());
                case 6 -> ps.setString(1, clerk.getDeskID());
            }
            ps.setInt(2, id);

            return ps.executeUpdate() > 0;
        }
    }

    /*---------------------------------------------------------
                       DELETE CLERK
    ----------------------------------------------------------*/

    public boolean deleteClerk(int id) throws SQLException {
        String sql = "DELETE FROM Staff WHERE id=?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    /*---------------------------------------------------------
                     GET CLERK BY ID
    ----------------------------------------------------------*/

    public Clerk getClerkById(int id) throws SQLException {

        String query =
                "SELECT s.id, s.name, s.password, s.email, s.phno, s.salary, s.role, c.deskid " +
                        "FROM Staff s INNER JOIN Clerk c ON s.id = c.id WHERE s.id = ?";

        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Clerk clerk = new Clerk();
                clerk.setId(rs.getInt("id"));
                clerk.setName(rs.getString("name"));
                clerk.setPassword(rs.getString("password"));
                clerk.setEmail(rs.getString("email"));
                clerk.setPhoneNumber(rs.getString("phno"));
                clerk.setSalary(rs.getDouble("salary"));
                clerk.setRole(rs.getString("role"));
                clerk.setDeskID(rs.getString("deskid"));

                return clerk;
            }
        }

        return null;
    }

    /*---------------------------------------------------------
                     GET ALL CLERKS
    ----------------------------------------------------------*/

    public List<Clerk> getAllClerk() throws SQLException {

        String query =
                "SELECT s.id, s.name, s.password, s.email, s.phno, s.salary, s.role, c.deskid " +
                        "FROM Staff s INNER JOIN Clerk c ON s.id = c.id";

        List<Clerk> list = new ArrayList<>();

        try (PreparedStatement ps = con.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Clerk clerk = new Clerk();
                clerk.setId(rs.getInt("id"));
                clerk.setName(rs.getString("name"));
                clerk.setPassword(rs.getString("password"));
                clerk.setEmail(rs.getString("email"));
                clerk.setPhoneNumber(rs.getString("phno"));
                clerk.setSalary(rs.getDouble("salary"));
                clerk.setRole(rs.getString("role"));
                clerk.setDeskID(rs.getString("deskid"));

                list.add(clerk);
            }
        }

        return list;
    }

    /*---------------------------------------------------------
                       ADD LIBRARIAN
    ----------------------------------------------------------*/

    public boolean addLibrarian(Librarian librarian) throws SQLException {

        String insertStaff =
                "INSERT INTO Staff (name, password, email, phno, salary, role) " +
                        "VALUES (?, ?, ?, ?, ?, ?) RETURNING id";

        String insertLibrarian = "INSERT INTO Librarian (id, officeid) VALUES (?, ?)";

        con.setAutoCommit(false);

        try {
            int staffId;

            try (PreparedStatement ps = con.prepareStatement(insertStaff)) {
                ps.setString(1, librarian.getName());
                ps.setString(2, librarian.getPassword());
                ps.setString(3, librarian.getEmail());
                ps.setString(4, librarian.getPhoneNumber());
                ps.setDouble(5, librarian.getSalary());
                ps.setString(6, librarian.getRole());

                ResultSet rs = ps.executeQuery();
                if (!rs.next()) throw new SQLException("Failed to generate Staff ID");

                staffId = rs.getInt("id");
            }

            try (PreparedStatement ps2 = con.prepareStatement(insertLibrarian)) {
                ps2.setInt(1, staffId);
                ps2.setString(2, librarian.getOfficeId());
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

    /*---------------------------------------------------------
                     UPDATE LIBRARIAN
    ----------------------------------------------------------*/

    public boolean updateLibrarian(Librarian librarian, int choice, int id) throws SQLException {

        String query = switch (choice) {
            case 1 -> "UPDATE Staff SET name=? WHERE id=?";
            case 2 -> "UPDATE Staff SET email=? WHERE id=?";
            case 3 -> "UPDATE Staff SET password=? WHERE id=?";
            case 4 -> "UPDATE Staff SET phno=? WHERE id=?";
            case 5 -> "UPDATE Staff SET salary=? WHERE id=?";
            case 6 -> "UPDATE Librarian SET officeid=? WHERE id=?";
            default -> throw new IllegalStateException("Invalid choice");
        };

        try (PreparedStatement ps = con.prepareStatement(query)) {

            switch (choice) {
                case 1 -> ps.setString(1, librarian.getName());
                case 2 -> ps.setString(1, librarian.getEmail());
                case 3 -> ps.setString(1, librarian.getPassword());
                case 4 -> ps.setString(1, librarian.getPhoneNumber());
                case 5 -> ps.setDouble(1, librarian.getSalary());
                case 6 -> ps.setString(1, librarian.getOfficeId());
            }

            ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        }
    }

    /*---------------------------------------------------------
                     DELETE LIBRARIAN
    ----------------------------------------------------------*/

    public boolean deleteLibrarian(int id) throws SQLException {
        String sql = "DELETE FROM Staff WHERE id=?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    /*---------------------------------------------------------
                     GET LIBRARIAN BY ID
    ----------------------------------------------------------*/

    public Librarian getLibrarianById(int id) throws SQLException {

        String query =
                "SELECT s.id, s.name, s.password, s.email, s.phno, s.salary, s.role, l.officeid " +
                        "FROM Staff s INNER JOIN Librarian l ON s.id = l.id WHERE s.id = ?";

        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Librarian librarian = new Librarian();
                librarian.setId(rs.getInt("id"));
                librarian.setName(rs.getString("name"));
                librarian.setPassword(rs.getString("password"));
                librarian.setEmail(rs.getString("email"));
                librarian.setPhoneNumber(rs.getString("phno"));
                librarian.setSalary(rs.getDouble("salary"));
                librarian.setRole(rs.getString("role"));
                librarian.setOfficeId(rs.getString("officeid"));

                return librarian;
            }
        }

        return null;
    }

    /*---------------------------------------------------------
                     GET ALL LIBRARIANS
    ----------------------------------------------------------*/

    public List<Librarian> getAllLibrarian() throws SQLException {

        String query =
                "SELECT s.id, s.name, s.password, s.email, s.phno, s.salary, s.role, l.officeid " +
                        "FROM Staff s INNER JOIN Librarian l ON s.id = l.id";

        List<Librarian> list = new ArrayList<>();

        try (PreparedStatement ps = con.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Librarian librarian = new Librarian();

                librarian.setId(rs.getInt("id"));
                librarian.setName(rs.getString("name"));
                librarian.setPassword(rs.getString("password"));
                librarian.setEmail(rs.getString("email"));
                librarian.setPhoneNumber(rs.getString("phno"));
                librarian.setSalary(rs.getDouble("salary"));
                librarian.setRole(rs.getString("role"));
                librarian.setOfficeId(rs.getString("officeid"));

                list.add(librarian);
            }
        }

        return list;
    }
}
