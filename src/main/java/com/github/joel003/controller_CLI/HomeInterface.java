package com.github.joel003.controller_CLI;

import com.github.joel003.util.ExtraFunctions;
import com.github.joel003.util.InputValidation;

import java.util.Scanner;

public class HomeInterface {

    public void homePage(Scanner sc) {

        while (true) {
            ExtraFunctions.clscr(5);
            System.out.println("------------------------------------------------------");
            System.out.println("         Welcome to Library Management System         ");
            System.out.println("------------------------------------------------------");
            System.out.println("""
                1. User Login
                2. Staff Login
                3. Admin Login
                4. Exit
                """);
            System.out.println("------------------------------------------------------\n");

            int choice = InputValidation.choiceValidate(1, 4, sc);

            switch (choice) {

                case 1 -> {
                    UserInterface userUI = new UserInterface();
                    userUI.userLogin(sc);
                }

                case 2 -> {
                    StaffInterface staffUI = new StaffInterface();
                    staffUI.staffLogin(sc);
                }

                case 3 -> {
                    AdminInterface adminUI = new AdminInterface();
                    adminUI.adminLogin(sc);
                }

                case 4 -> {
                    System.out.println("\n------------------------------------------------------");
                    System.out.println("Thank you for using our Library Management System");
                    System.out.println("------------------------------------------------------");
                    System.exit(0);
                }
            }
        }
        }

}
