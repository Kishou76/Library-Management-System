package com.github.joel003.controller_CLI;

import com.github.joel003.entity.Book;
import com.github.joel003.entity.Loan;
import com.github.joel003.service.UserService;
import com.github.joel003.util.ExtraFunctions;
import com.github.joel003.util.InputValidation;

import java.util.List;
import java.util.Scanner;

public class UserInterface {

    private final UserService userService;

    public UserInterface() {
        this.userService = new UserService();
    }

    /*---------------------------------------------------------
                          USER LOGIN
    ----------------------------------------------------------*/
    public void userLogin(Scanner sc) {

        while (true) {
            ExtraFunctions.clscr(5);

            System.out.println("-------------------------------------------------");
            System.out.println("                Login to User Portal");
            System.out.println("-------------------------------------------------\n");

            System.out.print("Enter Email: ");
            String email = sc.nextLine();

            System.out.print("Enter Password: ");
            String password = sc.nextLine();

            boolean loggedIn = userService.loginUser(email, password);

            if (loggedIn) {
                System.out.println("\nLogin successful!");
                userPage(sc);
                return;
            }

            System.out.println("\nInvalid Credentials.");
            System.out.println("1. Try Again");
            System.out.println("2. Back");

            int choice = InputValidation.choiceValidate(1, 2, sc);
            if (choice == 2) return;
        }
    }

    /*---------------------------------------------------------
                          USER MENU
    ----------------------------------------------------------*/
    private void userPage(Scanner sc) {

        while (true) {
            ExtraFunctions.clscr(5);

            System.out.println("-------------------------------------------------");
            System.out.println("                    USER PORTAL");
            System.out.println("-------------------------------------------------\n");

            System.out.println("""
                    1. View Available Books
                    2. Search Books
                    3. Borrow Book
                    4. Return Book
                    5. View My Borrowed Books
                    6. Logout
                    """);

            int choice = InputValidation.choiceValidate(1, 6, sc);

            switch (choice) {
                case 1 -> viewAvailableBooks();
                case 2 -> searchBooks(sc);
                case 3 -> borrowBook(sc);
                case 4 -> returnBook(sc);
                case 5 -> viewMyBorrowedBooks();
                case 6 -> { return; }
            }
        }
    }

    /*---------------------------------------------------------
                        VIEW AVAILABLE BOOKS
    ----------------------------------------------------------*/
    private void viewAvailableBooks() {

        List<Book> books = userService.viewAvailableBooks();

        if (books.isEmpty()) {
            System.out.println("No available books.");
            return;
        }

        System.out.println("--------------------------------------------------------------------------");
        System.out.printf("%-10s %-40s %-20s%n", "Book-ID", "Title", "Author");
        System.out.println("--------------------------------------------------------------------------");

        for (Book b : books) {
            System.out.printf("%-10d %-40s %-20s%n",
                    b.getBookId(), b.getTitle(), b.getAuthor());
        }
        System.out.println("--------------------------------------------------------------------------");

    }

    /*---------------------------------------------------------
                            SEARCH BOOKS
    ----------------------------------------------------------*/
    private void searchBooks(Scanner sc) {

        System.out.println("-------------------------------------");
        System.out.println("            Search Books");
        System.out.println("-------------------------------------");
        System.out.println("""
        1. Title
        2. Author
        3. Subject
        4. Back
        """);

        int choice = InputValidation.choiceValidate(1, 4, sc);
        if (choice == 4) return;

        String value = null;
        switch (choice) {
            case 1 -> {
                System.out.println("Enter Title: ");
                value = sc.nextLine();
            }
            case 2 -> {
                System.out.println("Enter Author: ");
                value = sc.nextLine();
            }
            case 3 -> {
                System.out.println("Enter Subject: ");
                value = sc.nextLine();
            }
        }


        List<Book> books = userService.searchBook(choice+1,value);

        if (books.isEmpty()) {
            System.out.println("No books found.");
            return;
        }

        System.out.println("--------------------------------------------------------------------------");
        System.out.printf("%-10s %-40s %-20s%n", "Book-ID", "Title", "Author");
        System.out.println("--------------------------------------------------------------------------");

        for (Book b : books) {
            System.out.printf("%-10d %-40s %-20s%n",
                    b.getBookId(), b.getTitle(), b.getAuthor());
        }
        System.out.println("--------------------------------------------------------------------------");

    }

    /*---------------------------------------------------------
                            BORROW BOOK
    ----------------------------------------------------------*/
    private void borrowBook(Scanner sc) {

        int bookId = InputValidation.validateID(sc,"Book");

        boolean ok = userService.borrowBook(bookId);

        System.out.println(ok ? "Book borrowed successfully!" : "Cannot borrow this book.");
    }

    /*---------------------------------------------------------
                            RETURN BOOK
    ----------------------------------------------------------*/
    private void returnBook(Scanner sc) {

        int bookId = InputValidation.validateID(sc,"Book");

        boolean ok = userService.returnBook(bookId);
        System.out.println("-------------------------------------------------");
        System.out.println(ok ? "Book returned successfully!" : "Return failed.");
        System.out.println("-------------------------------------------------");
    }

    /*---------------------------------------------------------
                       VIEW MY BORROWED BOOKS
    ----------------------------------------------------------*/
    private void viewMyBorrowedBooks() {

        List<Loan> loans = userService.viewMyBorrowedBooks();

        if (loans.isEmpty()) {
            System.out.println("You have not borrowed any books.");
            return;
        }

        System.out.println("-------------------------------------------------");
        System.out.printf("%-10s %-15s %-15s%n",
                "Book-ID", "Issue-Date", "Due-Date");
        System.out.println("-------------------------------------------------");

        for (Loan l : loans) {
            System.out.printf("%-10d %-15s %-15s%n",
                    l.getBookId(), l.getIssueDate(), l.getDueDate());
        }

        System.out.println("-------------------------------------------------");
    }
}
