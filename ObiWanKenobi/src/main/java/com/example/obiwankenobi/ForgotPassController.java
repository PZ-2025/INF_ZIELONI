package com.example.obiwankenobi;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.Pattern;

public class ForgotPassController {

    @FXML
    private TextField emailField;
    
    @FXML
    private Button closeButton;

    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@(.+)$";

    @FXML
    private void initialize() {
        emailField.setOnAction(event -> {
            try {
                handlePasswordReset();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @FXML
    private void handlePasswordReset() throws SQLException {
        String email = emailField.getText();

        if (email.isEmpty()) {
            showAlert("Proszę uzupełnić pole e-mail!");
            return;
        }

        if (!Pattern.matches(EMAIL_REGEX, email)) {
            showAlert("Podano niepoprawny adres e-mail!");
            return;
        }

        if (isEmailExists(email)) {
            resetPassword(email);
            showAlert("Hasło zostało zmienione dla: " + email);
        } else {
            showAlert("E-mail nie istnieje w systemie.");
        }

        closeWindow();
        closeWindow();
    }

    private boolean isEmailExists(String email) throws SQLException {
        boolean exists = false;

        String query = "SELECT 1 FROM users WHERE email = ? LIMIT 1";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                exists = rs.next();
            }

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Błąd połączenia z bazą danych.");
        }

        return exists;
    }

    private void resetPassword(String email) {
        String query = "UPDATE users SET password = ? WHERE email = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, "zaq1@WSX");
            stmt.setString(2, email);

            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated == 0) {
                showAlert("Nie udało się zmienić hasła.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Błąd połączenia z bazą danych.");
        }
    }

    @FXML
    private void handleClose() {
        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

    private void showAlert( String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("wiadomosc");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
