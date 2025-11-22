package com.github.joel003.util;

import java.util.Scanner;

public class InputValidation {

    /*---------------------------------------------------------
                        CHOICE VALIDATION
    ----------------------------------------------------------*/
    public static int choiceValidate(int min, int max, Scanner sc) {
        while (true) {
            System.out.print("Please enter your choice: ");
            String input = sc.nextLine();

            try {
                int num = Integer.parseInt(input);

                if (num >= min && num <= max) {
                    return num;
                } else {
                    System.err.println("Choice out of range. Enter between " + min + " and " + max + ".");
                }

            } catch (NumberFormatException e) {
                System.err.println("Invalid input. Enter a valid number.");
            }
        }
    }

    /*---------------------------------------------------------
                        PASSWORD VALIDATION
    ----------------------------------------------------------*/
    public static String passwordValidation(Scanner sc) {
        String regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";

        while (true) {
            System.out.print("Enter your password: ");
            String pass = sc.nextLine();

            if (pass.matches(regex)) {
                return pass;
            }

            System.err.println("""
                Invalid password!
                Must contain:
                • Min 8 characters
                • One uppercase (A-Z)
                • One lowercase (a-z)
                • One digit (0-9)
                • One special character @ $ ! % * ? &
                """);
        }
    }

    /*---------------------------------------------------------
                        NAME VALIDATION
    ----------------------------------------------------------*/
    public static String verifyName(Scanner sc) {
        String regex = "^[A-Za-z][A-Za-z .'-]{2,}$"; // More realistic name pattern

        while (true) {
            System.out.print("Enter name: ");
            String name = sc.nextLine().trim();

            if (name.matches(regex)) {
                return name;
            }

            System.err.println("Invalid name. Use only letters and minimum 3 characters.");
        }
    }

    /*---------------------------------------------------------
                        EMAIL VALIDATION
    ----------------------------------------------------------*/
    public static String verifyEmail(Scanner sc) {
        String regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";

        while (true) {
            System.out.print("Enter email: ");
            String email = sc.nextLine().trim();

            if (email.matches(regex)) {
                return email;
            }

            System.err.println("Invalid email format. Try again.");
        }
    }

    /*---------------------------------------------------------
                        PHONE VALIDATION
    ----------------------------------------------------------*/
    public static String verifyPhoneNumber(Scanner sc) {
        String regex = "^(\\+91|91)?[6-9][0-9]{9}$";

        while (true) {
            System.out.print("Enter phone number: ");
            String phone = sc.nextLine().trim();

            if (phone.matches(regex)) {
                return phone;
            }

            System.err.println("Invalid phone number. Must be Indian format starting with 6-9.");
        }
    }

    /*---------------------------------------------------------
                        INPUT ID VALIDATION
    ----------------------------------------------------------*/
    public static int validateID(Scanner sc,String value) {
        while (true) {
            System.out.print("Enter "+value+" ID: ");
            String input = sc.nextLine().trim();

            // Check if numeric
            if (!input.matches("^[0-9]+$")) {
                System.err.println("Invalid ID. Only digits allowed. Try again.");
                continue;
            }

            // Convert to integer
            try {
                int id = Integer.parseInt(input);

                if (id > 0) {
                    return id;
                } else {
                    System.err.println("ID must be greater than 0.");
                }

            } catch (NumberFormatException e) {
                System.err.println("Number too large. Enter a valid ID.");
            }
        }
    }

}
