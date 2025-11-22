package com.github.joel003.service;

import com.github.joel003.dao.AdminDAO;
import com.github.joel003.entity.Clerk;
import com.github.joel003.entity.Librarian;
import com.github.joel003.entity.Staff;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

public class AdminService {

    private final AdminDAO adminDAO;

    public AdminService() {
        this.adminDAO = new AdminDAO();
    }

    /*---------------------------------------------------------
                        ADMIN LOGIN
    ----------------------------------------------------------*/
    public boolean loginAdmin(String userID, String password) {
        try {
            return adminDAO.verifyAdmin(userID, password);
        } catch (SQLException e) {
            System.err.println("Database Error (Login): " + e.getMessage());
            return false;
        }
    }

    /*---------------------------------------------------------
                        VIEW ALL STAFF
    ----------------------------------------------------------*/
    public List<Staff> viewAllStaff() {
        try {
            return adminDAO.viewAllStaff();
        } catch (SQLException e) {
            System.err.println("Database Error (View Staff): " + e.getMessage());
            return Collections.emptyList();
        }
    }

    /*---------------------------------------------------------
                        CLERK SERVICES
    ----------------------------------------------------------*/

    public boolean addClerk(Clerk clerk) {
        try {
            return adminDAO.addClerk(clerk);
        } catch (SQLException e) {
            System.err.println("Database Error (Add Clerk): " + e.getMessage());
            return false;
        }
    }

    public boolean updateClerk(int clerkID, int fieldChoice, Clerk clerk) {
        try {
            return adminDAO.updateClerk(clerk, fieldChoice, clerkID);
        } catch (SQLException e) {
            System.err.println("Database Error (Update Clerk): " + e.getMessage());
            return false;
        }
    }

    public boolean deleteClerk(int clerkID) {
        try {
            return adminDAO.deleteClerk(clerkID);
        } catch (SQLException e) {
            System.err.println("Database Error (Delete Clerk): " + e.getMessage());
            return false;
        }
    }

    public Clerk getClerkByID(int id) {
        try {
            return adminDAO.getClerkById(id);
        } catch (SQLException e) {
            System.err.println("Database Error (Get Clerk): " + e.getMessage());
            return null;
        }
    }

    public List<Clerk> getAllClerk() {
        try {
            return adminDAO.getAllClerk();
        } catch (SQLException e) {
            System.err.println("Database Error (Get All Clerks): " + e.getMessage());
            return Collections.emptyList();
        }
    }

    /*---------------------------------------------------------
                        LIBRARIAN SERVICES
    ----------------------------------------------------------*/

    public boolean addLibrarian(Librarian librarian) {
        try {
            return adminDAO.addLibrarian(librarian);
        } catch (SQLException e) {
            System.err.println("Database Error (Add Librarian): " + e.getMessage());
            return false;
        }
    }

    public boolean updateLibrarian(int id, int fieldChoice, Librarian librarian) {
        try {
            return adminDAO.updateLibrarian(librarian, fieldChoice, id);
        } catch (SQLException e) {
            System.err.println("Database Error (Update Librarian): " + e.getMessage());
            return false;
        }
    }

    public boolean deleteLibrarian(int librarianID) {
        try {
            return adminDAO.deleteLibrarian(librarianID);
        } catch (SQLException e) {
            System.err.println("Database Error (Delete Librarian): " + e.getMessage());
            return false;
        }
    }

    public Librarian getLibrarianById(int librarianID) {
        try {
            return adminDAO.getLibrarianById(librarianID);
        } catch (SQLException e) {
            System.err.println("Database Error (Get Librarian): " + e.getMessage());
            return null;
        }
    }

    public List<Librarian> getAllLibrarian() {
        try {
            return adminDAO.getAllLibrarian();
        } catch (SQLException e) {
            System.err.println("Database Error (Get All Librarians): " + e.getMessage());
            return Collections.emptyList();
        }
    }
}
