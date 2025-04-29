package com.example.obiwankenobi;

import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

public class DatabaseConnectionTest {

    @Test
    void testConnectionNotNull() {
        Connection conn = DatabaseConnection.getConnection();
        assertNotNull(conn, "Połączenie z bazą danych powinno zostać nawiązane");
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
            }
        } catch (Exception e) {
            fail("Błąd przy zamykaniu połączenia: " + e.getMessage());
        }
    }
}
