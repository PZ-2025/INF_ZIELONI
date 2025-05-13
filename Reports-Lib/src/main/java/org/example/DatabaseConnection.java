package org.example;

import java.sql.*;

/**
 * Klasa odpowiedzialna za połączenie z bazą danych MySQL.
 * Umożliwia uzyskanie instancji połączenia (Connection) do bazy danych `obiwanshop`.
 */
public class DatabaseConnection {

    /** URL połączenia z bazą danych MySQL */
    private static String URL = "jdbc:mysql://localhost:3306/obiwanshop";

    /** Nazwa użytkownika bazy danych */
    private static String USER = "root";

    /** Hasło użytkownika bazy danych */
    private static String PASSWORD = "";

    /**
     * Ustawia niestandardowe dane połączenia – używane np. w testach.
     *
     * @param dbUrl  URL bazy danych
     * @param dbUser nazwa użytkownika
     * @param dbPass hasło
     */
    public static void setConfig(String dbUrl, String dbUser, String dbPass) {
        URL = dbUrl;
        USER = dbUser;
        PASSWORD = dbPass;
    }

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
