package com.example.obiwankenobi;

import java.sql.*;

/**
 * Klasa odpowiedzialna za połączenie z bazą danych MySQL.
 * Umożliwia uzyskanie instancji połączenia (Connection) do bazy danych `obiwanshop`.
 */
public class DatabaseConnection {

    /** URL połączenia z bazą danych MySQL */
    private static final String URL = "jdbc:mysql://localhost:3306/obiwanshop";

    /** Nazwa użytkownika bazy danych */
    private static final String USER = "root";

    /** Hasło użytkownika bazy danych */
    private static final String PASSWORD = "";

    /**
     * Tworzy i zwraca połączenie z bazą danych.
     *
     * @return połączenie do bazy danych (lub null, jeśli połączenie się nie powiodło)
     */
    public static Connection getConnection() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Połączono z bazą danych");
        } catch (SQLException e) {
            // Logowanie błędu oraz informacja dla programisty
            System.err.println("Nieudane połączenie z bazą danych");
            e.printStackTrace();
        }
        return connection;
    }
}
