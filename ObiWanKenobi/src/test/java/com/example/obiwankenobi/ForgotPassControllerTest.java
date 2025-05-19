package com.example.obiwankenobi;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static org.junit.jupiter.api.Assertions.*;

class ForgotPassControllerTest {

    private ForgotPassController controller;

    @BeforeAll
    static void initJavaFX() {
        // Inicjalizacja JavaFX (blokuje tylko raz na starcie
        Platform.startup(() -> {});
    }

    @BeforeEach
    void setUp() throws Exception {
        controller = new ForgotPassController();

        // Inicjalizacja pól FXML
        controller.emailField = new TextField();
        controller.closeButton = new Button();

        try (Connection conn = DatabaseConnection.getConnection()) {
            // Czyszczenie na wszelki wypadek
            conn.prepareStatement("DELETE FROM users WHERE email = 'test@example.com'").execute();

            PreparedStatement stmt = conn.prepareStatement(
                    "INSERT INTO users (id, email, password, first_name, last_name, city, salary, department_id, role_id, created_at) " +
                            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

            stmt.setInt(1, 999);
            stmt.setString(2, "test@example.com");
            stmt.setString(3, "originalPassword");
            stmt.setString(4, "Jan");
            stmt.setString(5, "Testowy");
            stmt.setString(6, "Warszawa");
            stmt.setDouble(7, 5000.00);
            stmt.setInt(8, 1); // Upewnij się, że department_id 1 istnieje
            stmt.setInt(9, 1); // Upewnij się, że role_id 1 istnieje
            stmt.setTimestamp(10, new java.sql.Timestamp(System.currentTimeMillis()));

            stmt.executeUpdate();
        }
    }

    @Test
    void testHandlePasswordReset_emptyEmail() {
        controller.emailField.setText("");
        assertDoesNotThrow(() -> controller.handlePasswordReset());
    }

    @Test
    void testHandlePasswordReset_invalidEmail() {
        controller.emailField.setText("invalid-email");
        assertDoesNotThrow(() -> controller.handlePasswordReset());
    }

    @Test
    void testHandlePasswordReset_nonexistentEmail() {
        controller.emailField.setText("notfound@example.com");
        assertDoesNotThrow(() -> controller.handlePasswordReset());
    }


    @Test
    void testIsEmailExists_true() throws Exception {
        var method = ForgotPassController.class.getDeclaredMethod("isEmailExists", String.class);
        method.setAccessible(true);
        boolean exists = (boolean) method.invoke(controller, "test@example.com");
        assertTrue(exists);
    }

    @Test
    void testIsEmailExists_false() throws Exception {
        var method = ForgotPassController.class.getDeclaredMethod("isEmailExists", String.class);
        method.setAccessible(true);
        boolean exists = (boolean) method.invoke(controller, "ghost@example.com");
        assertFalse(exists);
    }

    @AfterEach
    void tearDown() throws Exception {
        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.prepareStatement("DELETE FROM users WHERE email = 'test@example.com'").execute();
        }
    }
}
