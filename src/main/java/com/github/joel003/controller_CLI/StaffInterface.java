package com.github.joel003.controller_CLI;

import com.github.joel003.entity.Book;
import com.github.joel003.entity.Borrower;
import com.github.joel003.entity.HoldRequest;
import com.github.joel003.entity.Loan;
import com.github.joel003.service.BookService;
import com.github.joel003.service.HoldRequestService;
import com.github.joel003.service.LoanService;
import com.github.joel003.service.StaffService;
import com.github.joel003.util.ExtraFunctions;
import com.github.joel003.util.InputValidation;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class StaffInterface {
    
    private final StaffService staffService;
    private final BookService bookService;
    private final LoanService loanService;
    private final HoldRequestService holdRequestService;
    
    public StaffInterface() {
        this.staffService=new StaffService();
        this.bookService=new BookService();
        this.loanService=new LoanService();
        this.holdRequestService=new HoldRequestService();
    }

    /*---------------------------------------------------------
                         STAFF LOGIN
    ----------------------------------------------------------*/
    public void staffLogin(Scanner sc) {

        while (true) {
            ExtraFunctions.clscr(5);

            System.out.println("-------------------------------------------------");
            System.out.println("             Login to Staff Portal");
            System.out.println("-------------------------------------------------\n");

            System.out.print("Enter Staff-Email: ");
            String staffEmail = sc.nextLine();

            System.out.print("Enter Password: ");
            String password = sc.nextLine();

            String role = staffService.loginStaff(staffEmail, password);
            if (role != null) {
                System.out.println("-------------------------------------------------");
                System.out.println("Login successful!");
                System.out.println("-------------------------------------------------");
                staffPage(sc,role);
                return;
            }
            System.out.println("-------------------------------------------------");
            System.out.println("\nInvalid ID or Password.");
            System.out.println("1. Try Again");
            System.out.println("2. Back to Home");

            int choice = InputValidation.choiceValidate(1, 2, sc);

            if (choice == 2) return;
        }
    }

    /*---------------------------------------------------------
                         ADMIN MENU
    ----------------------------------------------------------*/
    private void staffPage(Scanner sc,String role) {
        while (true) {
            ExtraFunctions.clscr(5);

            System.out.println("-------------------------------------------------");
            System.out.println("             Welcome to Staff Portal");
            System.out.println("-------------------------------------------------\n");
            System.out.println("""
                    1. Borrower Management
                    2. Book Management
                    3. Issue / Return
                    4. Librarian Functions
                    5. Logout
                    """);
            System.out.println("-------------------------------------------------\n");

            int choice = InputValidation.choiceValidate(1, 5, sc);

            switch (choice) {
                case 1 -> borrowerMenu(sc);
                case 2 -> bookMenu(sc);
                case 3 -> issueReturnMenu(sc);
                case 4 -> {
                    if (role.equalsIgnoreCase("Librarian")) {
                        librarianMenu(sc);
                    } else {
                        System.out.println("Access Denied.");
                    }
                }
                case 5 -> {
                    return;
                }
            }
        }
    }

    /*---------------------------------------------------------
                         BORROWER MENU
    ----------------------------------------------------------*/
    private void borrowerMenu(Scanner sc) {

        while (true) {
            System.out.println("-----------------------------------------------");
            System.out.println("                BORROWER MANAGEMENT");
            System.out.println("-----------------------------------------------");
            System.out.println("""
                    1. Add Borrower
                    2. Update Borrower
                    3. Delete Borrower
                    4. View All Borrowers
                    5. Search Borrower
                    6. Back
                    """);
            System.out.println("-----------------------------------------------");

            int choice = InputValidation.choiceValidate(1, 6, sc);

            switch (choice) {
                case 1 -> addBorrower(sc);
                case 2 -> updateBorrower(sc);
                case 3 -> deleteBorrower(sc);
                case 4 -> viewAllBorrowers();
                case 5 -> searchBorrower(sc);
                case 6 -> { return; }
            }
        }
    }
    /*================> ADD BORROWER <================*/
    private void addBorrower(Scanner sc) {

        Borrower borrower = new Borrower();

        System.out.println("-------------------------------------");
        System.out.println("              Add Borrower");
        System.out.println("-------------------------------------");


        borrower.setName(InputValidation.verifyName(sc));
        borrower.setEmail(InputValidation.verifyEmail(sc));
        borrower.setPassword(InputValidation.passwordValidation(sc));
        borrower.setPhoneNumber(InputValidation.verifyPhoneNumber(sc));

        boolean success = staffService.addBorrower(borrower);
        System.out.println("-------------------------------------------------");
        System.out.println(success ? "Borrower added successfully" : "Failed to add Borrower.");
        System.out.println("-------------------------------------------------");
    }
    /*================> UPDATE BORROWER <================*/
    private void updateBorrower(Scanner sc) {

        System.out.println("-------------------------------------");
        System.out.println("            Update Borrower");
        System.out.println("-------------------------------------");

        System.out.println("""
            1. Name
            2. Email
            3. Phone
            4. Back
            """);

        int choice = InputValidation.choiceValidate(1, 4, sc);
        if (choice == 4) return;

        int id = InputValidation.validateID(sc,"Borrower");

        Borrower borrower = new Borrower();

        switch (choice) {
            case 1 -> borrower.setName(InputValidation.verifyName(sc));
            case 2 -> borrower.setEmail(InputValidation.verifyEmail(sc));
            case 3 -> borrower.setPhoneNumber(InputValidation.verifyPhoneNumber(sc));
        }

        boolean success = staffService.updateBorrower(id, choice, borrower);
        System.out.println("-------------------------------------------------");
        System.out.println(success ? " Borrower updated!" : " Update failed ");
        System.out.println("-------------------------------------------------");
    }

    /*================> DELETE BORROWER <================*/
    private void deleteBorrower(Scanner sc) {
        int id = InputValidation.validateID(sc,"Borrower");

        boolean success = staffService.deleteBorrower(id);
        System.out.println("-------------------------------------------------");
        System.out.println(success ? "Borrower deleted!" : "Delete failed.");
        System.out.println("-------------------------------------------------");
    }

    /*================> VIEW ALL BORROWER <================*/
    private void viewAllBorrowers() {

        List<Borrower> borrower = staffService.viewAllBorrowers();

        if (borrower.isEmpty()) {
            System.out.println(" No Borrower found.");
            return;
        }

        System.out.println("-----------------------------------------------------------------");
        System.out.printf("%-10s %-20s %-30s %-15s%n",
                "Borrower-ID", "Name", "Email", "Phone");
        System.out.println("-----------------------------------------------------------------");

        for (Borrower c : borrower) {
            System.out.printf("%-10d %-20s %-30s %-15s%n",
                    c.getId(), c.getName(), c.getEmail(), c.getPhoneNumber());
        }
        System.out.println("-----------------------------------------------------------------");

    }

    /*================> SEARCH BORROWER <================*/
    private void searchBorrower(Scanner sc) {

        System.out.println("-------------------------------------");
        System.out.println("            Search Borrower");
        System.out.println("-------------------------------------");
        System.out.println("""
            1. id
            2. Name
            3. Email
            4. Back
        """);

        int choice = InputValidation.choiceValidate(1, 4, sc);
        if (choice == 4) return;

        Borrower borrower = new Borrower();

        switch (choice) {
            case 1 -> borrower.setId(InputValidation.validateID(sc,"Borrower"));
            case 2 -> borrower.setName(InputValidation.verifyName(sc));
            case 3 -> borrower.setEmail(InputValidation.verifyEmail(sc));
        }

        List<Borrower> getBorrower = staffService.searchBorrower( choice, borrower);
        System.out.println("-----------------------------------------------------------------");
        System.out.printf("%-8s %-15s %-25s %-15s %n", "ID", "Name", "Email", "Phone");
        System.out.println("-----------------------------------------------------------------");
        for (Borrower c : getBorrower) {
            System.out.printf("%-8d %-15s %-25s %-15s %n", c.getId(), c.getName(), c.getEmail(), c.getPhoneNumber());
        }
        System.out.println("-----------------------------------------------------------------");
    }

    /*---------------------------------------------------------
                         BOOK MENU
    ----------------------------------------------------------*/
    private void bookMenu(Scanner sc) {

            while (true) {
                System.out.println("-----------------------------------------------");
                System.out.println("                 BOOK MANAGEMENT");
                System.out.println("-----------------------------------------------");
                System.out.println("""
                        1. View All Books
                        2. Search Book
                        3. Back
                    """);

                int choice = InputValidation.choiceValidate(1, 3, sc);

                switch (choice) {
                    case 1 -> viewAllBook();
                    case 2 -> searchBook(sc);
                    case 3 -> { return; }
                }
            }
    }


    /*---------------------------------------------------------
                         ISSUE / RETURN MENU
    ----------------------------------------------------------*/
    private void issueReturnMenu(Scanner sc) {

        while (true) {

            System.out.println("-----------------------------------------------");
            System.out.println("                  ISSUE / RETURN");
            System.out.println("-----------------------------------------------");
            System.out.println("""
                    1. Issue Book
                    2. Return Book
                    3. Add Hold Request
                    4. View Hold Requests
                    5. View Active Loan
                    6. Back
                    """);
            System.out.println("-----------------------------------------------");

            int choice = InputValidation.choiceValidate(1, 6, sc);

            switch (choice) {
                case 1 -> issueBook(sc);
                case 2 -> returnBook(sc);
                case 3 -> addHoldRequest(sc);
                case 4 -> viewHoldRequests();
                case 5 -> viewActiveLoans();
                case 6 -> { return; }
            }
        }
    }

    /*================> ISSUE BOOK <================*/
    private void issueBook(Scanner sc) {

        Loan loan = new Loan();

        System.out.print("Book ID: ");
        loan.setBookId(Integer.parseInt(sc.nextLine()));

        System.out.print("Borrower ID: ");
        loan.setBorrowerId(Integer.parseInt(sc.nextLine()));

        System.out.print("Issue Date (yyyy-mm-dd): ");
        loan.setIssueDate(LocalDate.parse(sc.nextLine()));

        System.out.print("Due Date (yyyy-mm-dd): ");
        loan.setDueDate(LocalDate.parse(sc.nextLine()));

        boolean success = loanService.issueBook(loan);

        System.out.println("-------------------------------------------------");
        System.out.println(success ? "Book issued successfully" : "Failed to issue book.");
        System.out.println("-------------------------------------------------");
    }


    /*================> RETURN BOOK <================*/
    private void returnBook(Scanner sc) {

        System.out.print("Book ID: ");
        int bookId = Integer.parseInt(sc.nextLine());

        System.out.print("Borrower ID: ");
        int borrowerId = Integer.parseInt(sc.nextLine());

        System.out.print("Return Date (yyyy-mm-dd): ");
        LocalDate returnDate = LocalDate.parse(sc.nextLine());

        boolean ok = loanService.returnBookForStaff(borrowerId, bookId, returnDate);
        System.out.println("-------------------------------------------------");
        System.out.println(ok ? "Book returned successfully" : "Return failed.");
        System.out.println("-------------------------------------------------");
    }


    /*================> ADD HOLD-REQUEST <================*/
    private void addHoldRequest(Scanner sc) {
        HoldRequest holdRequest = new HoldRequest();

        System.out.print("Book ID: ");
        holdRequest.setBookId(Integer.parseInt(sc.nextLine()));

        System.out.print("Borrower ID: ");
        holdRequest.setBorrowerId(Integer.parseInt(sc.nextLine()));

        System.out.print("Request Date (yyyy-mm-dd): ");
        holdRequest.setRequestDate(LocalDate.parse(sc.nextLine()));

        boolean success = holdRequestService.addHoldRequest(holdRequest);
        System.out.println("-------------------------------------------------");
        System.out.println(success ? "Hold-Request added successfully" : "Failed to add Hold-Request.");
        System.out.println("-------------------------------------------------");
    }

    /*================> VIEW HOLD-REQUEST <================*/
    private void viewHoldRequests() {
        List<HoldRequest> holdRequests = holdRequestService.viewHoldRequests();
        if (holdRequests.isEmpty()) {
            System.out.println("No Hold Requests found.");
            return;
        }

        System.out.println("-----------------------------------------------------------------");
        System.out.printf("%-12s %-12s %-15s %-15s%n",
                "Hold-ID", "Book-ID", "Borrower-ID", "Request-Date");
        System.out.println("-----------------------------------------------------------------");

        for (HoldRequest hr : holdRequests) {
            System.out.printf("%-12d %-12d %-15d %-15s%n",
                    hr.getHoldId(), hr.getBookId(), hr.getBorrowerId(), hr.getRequestDate());
        }
        System.out.println("-----------------------------------------------------------------");

    }

    /*================> VIEW ACTIVE-LOANS <================*/
    private void viewActiveLoans() {
        List<Loan> loans = loanService.viewActiveLoans();

        if (loans.isEmpty()) {
            System.out.println("No Active Loans found.");
            return;
        }

        System.out.println("------------------------------------------------------------------------------------------------------------------");
        System.out.printf("%-10s %-10s %-10s %-15s %-15s %-15s %-15s%n",
                "Loan-ID", "Book-ID", "Copy-ID", "Borrower-ID",
                "Issue-Date", "Due-Date", "Return-Date");
        System.out.println("------------------------------------------------------------------------------------------------------------------");

        for (Loan loan : loans) {
            System.out.printf("%-10d %-10d %-10d %-15d %-15s %-15s %-15s%n",
                    loan.getLoanId(), loan.getBookId(), loan.getCopyId(), loan.getBorrowerId(),
                    loan.getIssueDate(), loan.getDueDate(), loan.getReturnDate());
        }
        System.out.println("------------------------------------------------------------------------------------------------------------------");

    }

    /*---------------------------------------------------------
                         LIBRARIAN MENU
    ----------------------------------------------------------*/
    private void librarianMenu(Scanner sc) {

        while (true) {
            System.out.println("-----------------------------------------------");
            System.out.println("                LIBRARIAN FUNCTION");
            System.out.println("-----------------------------------------------");
            System.out.println("""
                    1. Add BOOK
                    2. Update BOOK
                    3. Delete BOOK
                    4. View All BOOK
                    5. Search Book
                    6. Back
                    """);
            System.out.println("-----------------------------------------------");

            int choice = InputValidation.choiceValidate(1, 6, sc);

            switch (choice) {
                case 1 -> addBook(sc);
                case 2 -> updateBook(sc);
                case 3 -> deleteBook(sc);
                case 4 -> viewAllBook();
                case 5 -> searchBook(sc);
                case 6 -> { return; }
            }
        }
    }

    /*================> ADD BOOK <================*/
    private void addBook(Scanner sc) {

        Book book = new Book();

        System.out.println("-------------------------------------");
        System.out.println("               Add BOOK");
        System.out.println("-------------------------------------");
        System.out.print("Enter Title: ");
        book.setTitle(sc.nextLine());
        System.out.print("Enter Author: ");
        book.setAuthor(sc.nextLine());
        System.out.println("Enter Subject: ");
        book.setSubject(sc.nextLine());
        System.out.print("Enter ISBN: ");
        book.setIsbn(sc.nextLine());
        System.out.println("Enter Total Copies: ");
        book.setTotalCopies(sc.nextInt());
        sc.nextLine();

        boolean success = bookService.addBook(book);
        System.out.println("-------------------------------------------------");
        System.out.println(success ? "Book added successfully" : "Failed to add Book.");
        System.out.println("-------------------------------------------------");

    }
    /*================> UPDATE BOOK <================*/
    private void updateBook(Scanner sc) {

        System.out.println("-------------------------------------");
        System.out.println("             Update Book");
        System.out.println("-------------------------------------");

        System.out.println("""
            1. Title
            2. Author
            3. Subject
            4. ISBN
            5. No of Copies
            6. Back
            """);

        int choice = InputValidation.choiceValidate(1, 6, sc);
        if (choice == 6) return;

        int id = InputValidation.validateID(sc,"Book");

        Book book = new Book();

        switch (choice) {
            case 1 -> {
                System.out.println("Enter Title: ");
                book.setTitle(sc.nextLine());
            }
            case 2 -> {
                System.out.println("Enter Author: ");
                book.setAuthor(sc.nextLine());
            }
            case 3 -> {
                System.out.println("Enter Subject: ");
                book.setSubject(sc.nextLine());
            }
            case 4 ->{
                System.out.println("Enter the ISBN : ");
                book.setIsbn(sc.nextLine());
            }
            case 5 -> {
                System.out.println("Enter Total Copies: ");
                book.setTotalCopies(sc.nextInt());
                sc.nextLine();
            }
        }

        boolean success;
        if (choice == 1 || choice == 2 || choice == 3 || choice == 4) {
            success = bookService.updateBook(id, choice, book);
        }
        else{
            success = bookService.updateBookCopies(id, book);

        }
        System.out.println("-------------------------------------------------");
        System.out.println(success ? " Book updated!" : " Update failed ");
        System.out.println("-------------------------------------------------");

    }
    /*================> DELETE BOOK <================*/
    private void deleteBook(Scanner sc) {
        int id = InputValidation.validateID(sc,"Book");

        boolean success = bookService.deleteBook(id);
        System.out.println("-------------------------------------------------");
        System.out.println(success ? "Book deleted!" : "Delete failed.");
        System.out.println("-------------------------------------------------");
    }
    /*================> VIEW ALL BOOK <================*/
    private void viewAllBook() {
        List<Book> book = bookService.viewAllBooks();

        if (book.isEmpty()) {
            System.out.println(" No Book found.");
            return;
        }

        System.out.println("-----------------------------------------------------------------------------------------------------------------------------------------");
        System.out.printf("%-10s %-40s %-20s %-20s %-15s %-15s %-20s%n",
                "Book-ID", "Title", "Author", "Subject", "ISBN", "Total", "Available");
        System.out.println("-----------------------------------------------------------------------------------------------------------------------------------------");

        for (Book b : book) {
            System.out.printf("%-10d %-40s %-20s %-20s %-15s %-15d %-20d%n",
                    b.getBookId(), b.getTitle(), b.getAuthor(), b.getSubject(), b.getIsbn(),
                    b.getTotalCopies(), b.getAvailableCopies());
        }
        System.out.println("-----------------------------------------------------------------------------------------------------------------------------------------");

    }
    /*================> SEARCH BOOK <================*/
    private void searchBook(Scanner sc) {

        System.out.println("-------------------------------------");
        System.out.println("            Search Books");
        System.out.println("-------------------------------------");
        System.out.println("""
        1. Book-ID
        2. Title
        3. Author
        4. Subject
        5. ISBN
        6. Back
        """);

        int choice = InputValidation.choiceValidate(1, 6, sc);
        if (choice == 6) return;

        String value = null;

        switch (choice) {
            case 1 -> value  = String.valueOf((InputValidation.validateID(sc,"Book")));
            case 2 -> {
                System.out.println("Enter Title: ");
                value = sc.nextLine();
            }
            case 3 -> {
                System.out.println("Enter Author: ");
                value = sc.nextLine();
            }
            case 4 -> {
                System.out.println("Enter Subject: ");
                value = sc.nextLine();
            }
            case 5 ->{
                System.out.println("Enter the ISBN : ");
                value = sc.nextLine();
            }

        }

        List<Book> book = bookService.searchBook(choice,value);

        if (book.isEmpty()) {
            System.out.println(" No Book found.");
            return;
        }

        System.out.println("-------------------------------------------------------------------------------------------------------------------------------------------------");
        System.out.printf("%-10s %-40s %-20s %-20s %-15s %-15s %-15s %n", "Book-ID", "Title", "Author", "Subject","ISBN", "Total-Copies","Available-Copies");
        System.out.println("-------------------------------------------------------------------------------------------------------------------------------------------------");
        for (Book b : book) {
            System.out.printf("%-10s %-40s %-20s %-20s %-15s %-15s %-15s %n", b.getBookId(), b.getTitle(), b.getAuthor(), b.getSubject(), b.getIsbn(), b.getTotalCopies(), b.getAvailableCopies());
        }
        System.out.println("-------------------------------------------------------------------------------------------------------------------------------------------------");

    }

}
