package com.github.joel003.service;

import com.github.joel003.dao.UserDAO;
import com.github.joel003.entity.Book;
import com.github.joel003.entity.Loan;

import java.util.Collections;
import java.util.List;

public class UserService {

    private final UserDAO userDAO;
    private final BookService bookService;
    private final LoanService loanService;

    private int loggedUserId;

    public UserService() {
        this.userDAO = new UserDAO();
        this.bookService = new BookService();
        this.loanService = new LoanService();
    }

    /*---------------------------------------------------------
                          LOGIN
    ----------------------------------------------------------*/
    public boolean loginUser(String email, String password) {
        try {
            Integer id = userDAO.loginUser(email, password);
            if (id != null) {
                this.loggedUserId = id;
                return true;
            }
            return false;

        } catch (Exception e) {
            System.err.println("Login Error: " + e.getMessage());
            return false;
        }
    }

    /*---------------------------------------------------------
                    VIEW AVAILABLE BOOKS
    ----------------------------------------------------------*/
    public List<Book> viewAvailableBooks() {

        try {
            return bookService.viewAllBooks().stream()
                    .filter(b -> b.getAvailableCopies() > 0)
                    .toList();
        } catch (Exception e) {
            System.err.println("Book Error: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    /*---------------------------------------------------------
                        SEARCH BOOK
    ----------------------------------------------------------*/
    public List<Book> searchBook(int choice,String keyword) {
        try {
            return bookService.searchBook(choice, keyword);
        } catch (Exception e) {
            System.err.println("Search Error: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    /*---------------------------------------------------------
                          BORROW
    ----------------------------------------------------------*/
    public boolean borrowBook(int bookId) {
        try {
            return loanService.issueBookForUser(loggedUserId, bookId);
        } catch (Exception e) {
            System.err.println("Borrow Error: " + e.getMessage());
            return false;
        }
    }

    /*---------------------------------------------------------
                          RETURN
    ----------------------------------------------------------*/
    public boolean returnBook(int bookId) {
        try {
            return loanService.returnBookForUser(loggedUserId, bookId);
        } catch (Exception e) {
            System.err.println("Return Error: " + e.getMessage());
            return false;
        }
    }

    /*---------------------------------------------------------
                   VIEW MY LOANS
    ----------------------------------------------------------*/
    public List<Loan> viewMyBorrowedBooks() {
        try {
            return userDAO.getMyLoans(loggedUserId);
        } catch (Exception e) {
            System.err.println("Loan Error: " + e.getMessage());
            return Collections.emptyList();
        }
    }
}
