package com.github.joel003.service;

import com.github.joel003.dao.BookDAO;
import com.github.joel003.dao.LoanDAO;
import com.github.joel003.entity.Loan;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Scanner;

public class LoanService {

    private final LoanDAO loanDAO;
    private final BookDAO bookDAO;
    public LoanService()
    {
        this.bookDAO = new BookDAO();
        this.loanDAO = new LoanDAO();
    }

    public boolean issueBook(Loan loan) {
        try {
            Integer copyId = bookDAO.getAvailableCopy(loan.getBookId());

            if (copyId == null) {
                System.out.println(" No copies available for this book!");
                return false;
            }

            loan.setCopyId(copyId);
            return loanDAO.issueBook(loan);

        } catch (SQLException e) {
            System.out.println("SQLException (Issue Book): " + e.getMessage());
            return false;
        }
    }


    public int computeFine(Loan loan, int finePerDay) {
        try {
            LocalDate dueDate = loanDAO.getDueDate(loan);
            LocalDate returnDate = loan.getReturnDate();

            if (dueDate == null || returnDate == null) {
                System.out.println("Due date or return date not available.");
                return 0;
            }

            long daysLate = ChronoUnit.DAYS.between(dueDate, returnDate);

            if (daysLate > 0) {
                int fine = (int) daysLate * finePerDay;
                System.out.println("Late by " + daysLate + " days. Fine: ₹" + fine);
                return fine;
            } else {
                System.out.println("Returned on time. No fine.");
                return 0;
            }

        } catch (SQLException e) {
            System.out.println("SQLException (Fine-Date): " + e.getMessage());
            return 0;
        }
    }

    public boolean returnBookForUser(int borrowerId, int bookId) {
        try {
            Loan activeLoan = loanDAO.getActiveLoan(borrowerId, bookId);

            if (activeLoan == null) {
                System.out.println("You haven't borrowed this book.");
                return false;
            }

            Loan loan = new Loan();
            loan.setCopyId(activeLoan.getCopyId());
            loan.setReturnDate(LocalDate.now());

            int fine = computeFine(loan, 50);

            if (fine > 0) {
                System.out.println("Fine must be paid before returning.");
                return false;
            }

            return loanDAO.returnBook(loan);

        } catch (Exception e) {
            System.out.println("Return Error: " + e.getMessage());
            return false;
        }
    }

    public boolean returnBookForStaff(int borrowerId, int bookId, LocalDate returnDate) {
        try {
            Loan activeLoan = loanDAO.getActiveLoan(borrowerId, bookId);

            if (activeLoan == null) {
                System.out.println("No active loan found.");
                return false;
            }

            Loan loan = new Loan();
            loan.setCopyId(activeLoan.getCopyId());
            loan.setReturnDate(returnDate);

            int fine = computeFine(loan, 50);

            if (fine > 0) {
                Scanner sc = new Scanner(System.in);
                System.out.print("Fine Paid? (Y/N): ");
                if (!sc.nextLine().equalsIgnoreCase("Y")) return false;
            }

            return loanDAO.returnBook(loan);

        } catch (Exception e) {
            System.out.println("Staff Return Error: " + e.getMessage());
            return false;
        }
    }


    public List<Loan> viewActiveLoans() {
        try {
            return loanDAO.viewActiveLoans();
        }catch (SQLException e){
            System.out.println("SQLException (View-Active-Loans): " + e.getMessage());
            return null;
        }
    }

    public boolean issueBookForUser(int borrowerId, int bookId) {
        Loan loan = new Loan();
        loan.setBookId(bookId);
        loan.setBorrowerId(borrowerId);
        loan.setIssueDate(LocalDate.now());
        loan.setDueDate(LocalDate.now().plusDays(14));

        return issueBook(loan); // reuse staff method
    }



}
