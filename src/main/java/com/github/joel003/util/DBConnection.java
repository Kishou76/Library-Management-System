package com.github.joel003.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static final String URL = "jdbc:postgresql://localhost:5432/Library-Management-System";
    private static final String USER = "postgres";
    private static final String PASSWORD = "root";

    private final static Connection connection;

    static {
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(" Database Connection Failed: " + e.getMessage());
        }
    }

    public static Connection getConnection() {
        return connection;
    }
}
