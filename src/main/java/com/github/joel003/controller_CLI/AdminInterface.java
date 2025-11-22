package com.github.joel003.controller_CLI;

import com.github.joel003.entity.*;
import com.github.joel003.service.AdminService;
import com.github.joel003.service.BookService;
import com.github.joel003.service.LoanService;
import com.github.joel003.util.ExtraFunctions;
import com.github.joel003.util.InputValidation;

import java.util.List;
import java.util.Scanner;

public class AdminInterface {

    private final AdminService adminService;
    private final BookService bookService;
    private final LoanService loanService;

    public AdminInterface() {
        this.adminService = new AdminService();
        this.bookService = new BookService();
        this.loanService = new LoanService();
    }

    /*---------------------------------------------------------
                         ADMIN LOGIN
    ----------------------------------------------------------*/
    public void adminLogin(Scanner sc) {

        while (true) {
            ExtraFunctions.clscr(3);

            System.out.println("-------------------------------------------------");
            System.out.println("             Login to Admin Portal");
            System.out.println("-------------------------------------------------\n");

            System.out.print("Enter Admin User-ID: ");
            String userID = sc.nextLine();


            String password = InputValidation.passwordValidation(sc);

            if (adminService.loginAdmin(userID, password)) {
                System.out.println("-------------------------------------------------");
                System.out.println("Login successful!");
                System.out.println("-------------------------------------------------");
                adminPage(sc);
                return;
            }
            System.out.println("--------------------------------------------------");
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
    private void adminPage(Scanner sc) {

        while (true) {
            ExtraFunctions.clscr(5);

            System.out.println("-------------------------------------------------");
            System.out.println("             Welcome to Admin Portal");
            System.out.println("-------------------------------------------------\n");

            System.out.println("""
                1. Clerk Functions
                2. Librarian Functions
                3. View All Staff
                4. View Issued Books
                5. View All Books
                6. Logout
                """);

            int choice = InputValidation.choiceValidate(1, 6, sc);

            switch (choice) {
                case 1 -> clerkMenu(sc);
                case 2 -> librarianMenu(sc);
                case 3 -> viewAllStaff();
                case 4 -> viewActiveLoans();
                case 5 -> viewAllBook();
                case 6 -> {
                    System.out.println("-------------------------------------------------");
                    System.out.println("You have logged out.");
                    System.out.println("-------------------------------------------------");
                    return;
                }
            }
        }
    }

    /*---------------------------------------------------------
                        CLERK MAIN MENU
    ----------------------------------------------------------*/
    private void clerkMenu(Scanner sc) {

        while (true) {
            ExtraFunctions.clscr(5);

            System.out.println("-----------------------------------------------");
            System.out.println("                Clerk Functions");
            System.out.println("-----------------------------------------------");

            System.out.println("""
                1. Add Clerk
                2. Update Clerk
                3. Delete Clerk
                4. Get Clerk By ID
                5. Get All Clerks
                6. Back
                """);

            int choice = InputValidation.choiceValidate(1, 6, sc);

            switch (choice) {
                case 1 -> addClerk(sc);
                case 2 -> updateClerk(sc);
                case 3 -> deleteClerk(sc);
                case 4 -> getClerkById(sc);
                case 5 -> listAllClerks();
                case 6 -> { return; }
            }
        }
    }

    /*---------------------------------------------------------
                        ADD CLERK
    ----------------------------------------------------------*/
    private void addClerk(Scanner sc) {

        Clerk clerk = new Clerk();

        System.out.println("-------------------------------------");
        System.out.println("              Add Clerk");
        System.out.println("-------------------------------------");

        clerk.setName(InputValidation.verifyName(sc));
        clerk.setEmail(InputValidation.verifyEmail(sc));
        clerk.setPassword(InputValidation.passwordValidation(sc));
        clerk.setPhoneNumber(InputValidation.verifyPhoneNumber(sc));

        System.out.print("Enter Salary: ");
        clerk.setSalary(sc.nextDouble());
        sc.nextLine();

        System.out.print("Enter Role: ");
        clerk.setRole(sc.nextLine().trim().toUpperCase());

        System.out.print("Enter Desk-ID: ");
        clerk.setDeskID(sc.nextLine());

        boolean success = adminService.addClerk(clerk);
        System.out.println("-------------------------------------------------");
        System.out.println(success ? "Clerk added successfully" : "Failed to add clerk.");
        System.out.println("-------------------------------------------------");
    }

    /*---------------------------------------------------------
                       UPDATE CLERK
    ----------------------------------------------------------*/
    private void updateClerk(Scanner sc) {

        System.out.println("-------------------------------------");
        System.out.println("             Update Clerk");
        System.out.println("-------------------------------------");

        System.out.println("""
            1. Name
            2. Email
            3. Password
            4. Phone
            5. Salary
            6. Desk-ID
            7. Back
            """);

        int choice = InputValidation.choiceValidate(1, 7, sc);
        if (choice == 7) return;

        int id = InputValidation.validateID(sc,"Clerk");

        Clerk clerk = new Clerk();

        switch (choice) {
            case 1 -> clerk.setName(InputValidation.verifyName(sc));
            case 2 -> clerk.setEmail(InputValidation.verifyEmail(sc));
            case 3 -> clerk.setPassword(InputValidation.passwordValidation(sc));
            case 4 -> clerk.setPhoneNumber(InputValidation.verifyPhoneNumber(sc));
            case 5 -> {
                System.out.print("Enter Salary: ");
                clerk.setSalary(sc.nextDouble());
                sc.nextLine();
            }
            case 6 -> {
                System.out.print("Enter Desk-ID: ");
                clerk.setDeskID(sc.nextLine());
            }
        }

        boolean success = adminService.updateClerk(id, choice, clerk);
        System.out.println("-------------------------------------------------");
        System.out.println(success ? " Clerk updated!" : " Update failed ");
        System.out.println("-------------------------------------------------");
    }

    /*---------------------------------------------------------
                        DELETE CLERK
    ----------------------------------------------------------*/
    private void deleteClerk(Scanner sc) {

        int id = InputValidation.validateID(sc,"Clerk");

        boolean success = adminService.deleteClerk(id);
        System.out.println("-------------------------------------------------");
        System.out.println(success ? "Clerk deleted!" : "Delete failed.");
        System.out.println("-------------------------------------------------");
    }

    /*---------------------------------------------------------
                       GET CLERK BY ID
    ----------------------------------------------------------*/
    private void getClerkById(Scanner sc) {

        int id = InputValidation.validateID(sc,"Clerk");

        Clerk c = adminService.getClerkByID(id);

        if (c == null) {
            System.out.println("-------------------------------------------------");
            System.out.println(" Clerk not found.");
            return;
        }

        System.out.println("---------------------------------------------------------------------------------------------------------------------");
        System.out.printf("%-8s %-15s %-25s %-15s %-15s %-8s %-10s %-10s%n",
                "ID", "Name", "Email", "Password", "Phone", "Salary", "Role", "Desk-ID");
        System.out.println("---------------------------------------------------------------------------------------------------------------------");

        System.out.printf("%-8d %-15s %-25s %-15s %-15s %-8.2f %-10s %-10s%n",
                c.getId(), c.getName(), c.getEmail(),
                c.getPassword(), c.getPhoneNumber(), c.getSalary(),
                c.getRole(), c.getDeskID());

        System.out.println("---------------------------------------------------------------------------------------------------------------------");
    }

    /*---------------------------------------------------------
                     LIST ALL CLERKS
    ----------------------------------------------------------*/
    private void listAllClerks() {

        List<Clerk> list = adminService.getAllClerk();

        if (list.isEmpty()) {
            System.out.println(" No clerks found.");
            return;
        }

        System.out.println("---------------------------------------------------------------------------------------------------------------------");
        System.out.printf("%-8s %-15s %-25s %-15s %-15s %-8s %-10s %-10s%n",
                "ID", "Name", "Email", "Password", "Phone", "Salary", "Role", "Desk-ID");
        System.out.println("---------------------------------------------------------------------------------------------------------------------");

        for (Clerk c : list) {
            System.out.printf("%-8d %-15s %-25s %-15s %-15s %-8.2f %-10s %-10s%n",
                    c.getId(), c.getName(), c.getEmail(),
                    c.getPassword(), c.getPhoneNumber(), c.getSalary(),
                    c.getRole(), c.getDeskID());
        }

        System.out.println("---------------------------------------------------------------------------------------------------------------------");
    }

    /*---------------------------------------------------------
                        LIBRARIAN MENU
    ----------------------------------------------------------*/
    private void librarianMenu(Scanner sc) {

        while (true) {
            ExtraFunctions.clscr(5);

            System.out.println("-----------------------------------------------");
            System.out.println("             Librarian Functions");
            System.out.println("-----------------------------------------------");

            System.out.println("""
                1. Add Librarian
                2. Update Librarian
                3. Delete Librarian
                4. Get Librarian By ID
                5. Get All Librarians
                6. Back
                """);

            int choice = InputValidation.choiceValidate(1, 6, sc);

            switch (choice) {
                case 1 -> addLibrarian(sc);
                case 2 -> updateLibrarian(sc);
                case 3 -> deleteLibrarian(sc);
                case 4 -> getLibrarianById(sc);
                case 5 -> listAllLibrarians();
                case 6 -> { return; }
            }
        }
    }

    /*---------------------------------------------------------
                        ADD LIBRARIAN
    ----------------------------------------------------------*/
    private void addLibrarian(Scanner sc) {

        Librarian lib = new Librarian();

        System.out.println("---------------------------------------------");
        System.out.println("              Add Librarian");
        System.out.println("---------------------------------------------");

        lib.setName(InputValidation.verifyName(sc));
        lib.setEmail(InputValidation.verifyEmail(sc));
        lib.setPassword(InputValidation.passwordValidation(sc));
        lib.setPhoneNumber(InputValidation.verifyPhoneNumber(sc));

        System.out.print("Enter Salary: ");
        lib.setSalary(sc.nextDouble());
        sc.nextLine();
        System.out.print("Enter Role: ");
        lib.setRole(sc.nextLine().trim().toUpperCase());
        System.out.print("Enter Office-ID: ");
        lib.setOfficeId(sc.nextLine());

        boolean success = adminService.addLibrarian(lib);

        System.out.println("-------------------------------------------------");
        System.out.println(success ? " Librarian added!" : " Adding librarian failed.");
        System.out.println("-------------------------------------------------");
    }

    /*---------------------------------------------------------
                      UPDATE LIBRARIAN
    ----------------------------------------------------------*/
    private void updateLibrarian(Scanner sc) {

        System.out.println("---------------------------------------------");
        System.out.println("              Update Librarian");
        System.out.println("---------------------------------------------");

        System.out.println("""
            1. Name
            2. Email
            3. Password
            4. Phone
            5. Salary
            6. Office-ID
            7. Back
            """);

        int choice = InputValidation.choiceValidate(1, 7, sc);
        if (choice == 7) return;

        int id = InputValidation.validateID(sc,"Librarian");

        Librarian librarian = new Librarian();

        switch (choice) {
            case 1 -> librarian.setName(InputValidation.verifyName(sc));
            case 2 -> librarian.setEmail(InputValidation.verifyEmail(sc));
            case 3 -> librarian.setPassword(InputValidation.passwordValidation(sc));
            case 4 -> librarian.setPhoneNumber(InputValidation.verifyPhoneNumber(sc));
            case 5 -> {
                System.out.print("Enter Salary: ");
                librarian.setSalary(sc.nextDouble());
                sc.nextLine();
            }
            case 6 -> {
                System.out.print("Enter Office-ID: ");
                librarian.setOfficeId(sc.nextLine());
            }
        }

        boolean success = adminService.updateLibrarian(id, choice, librarian);
        System.out.println("-------------------------------------------------");
        System.out.println(success ? " Librarian updated!" : " Update failed.");
        System.out.println("-------------------------------------------------");
    }

    /*---------------------------------------------------------
                         DELETE LIBRARIAN
    ----------------------------------------------------------*/
    private void deleteLibrarian(Scanner sc) {

        int id = InputValidation.validateID(sc,"Librarian");

        boolean success = adminService.deleteLibrarian(id);
        System.out.println("-------------------------------------------------");
        System.out.println(success ? " Librarian deleted!" : " Deletion failed.");
        System.out.println("-------------------------------------------------");
    }

    /*---------------------------------------------------------
                       GET LIBRARIAN BY ID
    ----------------------------------------------------------*/
    private void getLibrarianById(Scanner sc) {

        int id =InputValidation.validateID(sc,"Librarian");

        Librarian l = adminService.getLibrarianById(id);

        if (l == null) {
            System.out.println("-------------------------------------------------");
            System.out.println(" Librarian not found.");
            return;
        }

        System.out.println("-----------------------------------------------------------------------------------------------------------");
        System.out.printf("%-8s %-15s %-25s %-15s %-15s %-10s%n",
                "ID", "Name", "Email", "Password", "Phone", "Office-ID");
        System.out.println("-----------------------------------------------------------------------------------------------------------");

        System.out.printf("%-8d %-15s %-25s %-15s %-15s %-10s%n",
                l.getId(), l.getName(), l.getEmail(),
                l.getPassword(), l.getPhoneNumber(), l.getOfficeId());

        System.out.println("-----------------------------------------------------------------------------------------------------------");
    }

    /*---------------------------------------------------------
                     GET ALL LIBRARIANS
    ----------------------------------------------------------*/
    private void listAllLibrarians() {

        List<Librarian> list = adminService.getAllLibrarian();

        if (list.isEmpty()) {
            System.out.println("-------------------------------------------------");
            System.out.println(" No librarians found.");
            return;
        }

        System.out.println("-----------------------------------------------------------------------------------------------------------");
        System.out.printf("%-8s %-15s %-25s %-15s %-15s %-10s%n",
                "ID", "Name", "Email", "Password", "Phone", "Office-ID");
        System.out.println("-----------------------------------------------------------------------------------------------------------");

        for (Librarian l : list) {
            System.out.printf("%-8d %-15s %-25s %-15s %-15s %-10s%n",
                    l.getId(), l.getName(), l.getEmail(),
                    l.getPassword(), l.getPhoneNumber(), l.getOfficeId());
        }

        System.out.println("-----------------------------------------------------------------------------------------------------------");
    }

    /*---------------------------------------------------------
                     VIEW ALL STAFF
----------------------------------------------------------*/
    private void viewAllStaff() {

        List<Staff> list = adminService.viewAllStaff();

        if (list.isEmpty()) {
            System.out.println(" No staff found.");
            return;
        }

        System.out.println("-----------------------------------------------------------------------------------------------------------");
        System.out.println("                                                STAFF LIST");
        System.out.println("-----------------------------------------------------------------------------------------------------------");

        System.out.printf("%-8s %-15s %-25s %-15s %-15s %-10s %-10s%n",
                "ID", "Name", "Email", "Password", "Phone", "Salary", "Role");

        System.out.println("-----------------------------------------------------------------------------------------------------------");

        for (Staff s : list) {
            System.out.printf("%-8d %-15s %-25s %-15s %-15s %-10.2f %-10s%n",
                    s.getId(), s.getName(), s.getEmail(),
                    s.getPassword(), s.getPhoneNumber(),
                    s.getSalary(), s.getRole());
        }

        System.out.println("-----------------------------------------------------------------------------------------------------------");
    }

    /*---------------------------------------------------------
                        VIEW ALL BOOKS
    ----------------------------------------------------------*/

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


    /*---------------------------------------------------------
                        VIEW ISSUED BOOKS
    ----------------------------------------------------------*/
    private void viewActiveLoans() {
        List<Loan> loans = loanService.viewActiveLoans();

        if (loans.isEmpty()) {
            System.out.println("No Active Loans found.");
            return;
        }

        System.out.println("-----------------------------------------------------------------------------------------------------------------------------------------");
        System.out.printf("%-10s %-10s %-10s %-15s %-15s %-15s %-15s%n",
                "Loan-ID", "Book-ID", "Copy-ID", "Borrower-ID",
                "Issue-Date", "Due-Date", "Return-Date");
        System.out.println("-----------------------------------------------------------------------------------------------------------------------------------------");

        for (Loan loan : loans) {
            System.out.printf("%-10d %-10d %-10d %-15d %-15s %-15s %-15s%n",
                    loan.getLoanId(), loan.getBookId(), loan.getCopyId(), loan.getBorrowerId(),
                    loan.getIssueDate(), loan.getDueDate(), loan.getReturnDate());
        }
        System.out.println("-----------------------------------------------------------------------------------------------------------------------------------------");

    }

}
