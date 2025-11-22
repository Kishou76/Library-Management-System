package com.github.joel003.service;

import com.github.joel003.dao.StaffDAO;
import com.github.joel003.entity.Borrower;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

public class StaffService {

    private final StaffDAO staffDAO;
    public StaffService()
    {
        this.staffDAO = new StaffDAO();
    }

    /*---------------------------------------------------------
                        STAFF LOGIN
    ----------------------------------------------------------*/
    public String loginStaff(String staffEmail, String password) {
        try{
            return staffDAO.loginStaff(staffEmail,password);
        }catch (SQLException e){
            System.err.println("Database Error (Login): " + e.getMessage());
            return null;
        }
    }

    public boolean addBorrower(Borrower borrower) {
        try {
            return staffDAO.addBorrower(borrower);
        } catch (SQLException e) {
            System.err.println("Database Error (Add Borrower): " + e.getMessage());
            return false;
        }
    }

    public boolean deleteBorrower(int id) {
        try{
            return staffDAO.deleteBorrower(id);
        }catch (SQLException e){
            System.err.println("Database Error (Delete Borrower): " + e.getMessage());
            return false;
        }
    }

    public boolean updateBorrower(int id, int choice, Borrower borrower) {
        try{
            return staffDAO.updateBorrower(id,choice,borrower);
        }catch (SQLException e){
            System.err.println("Database Error (Update Borrower): " + e.getMessage());
            return false;
        }
    }

    public List<Borrower> viewAllBorrowers() {
        try {
            return staffDAO.viewAllBorrowers();
        } catch (SQLException e) {
            System.err.println("Database Error (View All Borrower): " + e.getMessage());
            return Collections.emptyList();
        }
    }

    public List<Borrower> searchBorrower(int choice, Borrower borrower) {
        try {
            return staffDAO.searchBorrower(choice,borrower);
        } catch (SQLException e) {
            System.err.println("Database Error (View All Borrower): " + e.getMessage());
            return Collections.emptyList();
        }
    }


}
