package com.example.obiwankenobi;

import java.sql.*;

public class DatabaseConnection {

    private static final String URL = "jdbc:mysql://localhost:3306/obiwanshopdb";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public static Connection getConnection() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Połączono z bazą danych");
        } catch (SQLException e) {
            // Log the error or display a user-friendly message
            System.err.println("Nieudane połączenie z bazą danych");
            e.printStackTrace();
        }
        return connection;
    }
}
