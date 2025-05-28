package com.example.obiwankenobi;

import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

public class DatabaseConnectionTest {

    @Test
    void testSuccessfulConnection() {
        DatabaseConnection.setConfig(
                "jdbc:mysql://localhost:3306/obiwanshop", "root", "admin"
        );

        Connection conn = DatabaseConnection.getConnection();
        assertNotNull(conn, "Połączenie powinno zostać nawiązane");

        try {
            assertFalse(conn.isClosed(), "Połączenie nie powinno być zamknięte");
            System.out.println("Test nawiązania połączenia zakończony sukcesem!");
        } catch (Exception e) {
            fail("Błąd podczas sprawdzania połączenia: " + e.getMessage());
        } finally {
            try {
                if (conn != null && !conn.isClosed()) {
                    conn.close();
                }
            } catch (Exception ignored) {}
        }
    }

    @Test
    void testFailedConnectionWithWrongCredentials() {
        DatabaseConnection.setConfig(
                "jdbc:mysql://localhost:3306/obiwanshop", "wrongUser", "wrongPassword"
        );

        Connection conn = DatabaseConnection.getConnection();
        if (conn == null) {
            System.out.println("Połączenie nie powiodło się zgodnie z oczekiwaniami.");
        }

        assertNull(conn, "Połączenie powinno się nie powieść i zwrócić null!");
    }
}
