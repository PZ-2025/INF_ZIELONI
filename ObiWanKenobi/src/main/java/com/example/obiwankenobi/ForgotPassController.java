package com.example.obiwankenobi;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.Pattern;

/**
 * Kontroler odpowiedzialny za obsługę resetowania hasła użytkownika po podaniu adresu e-mail.
 */
public class ForgotPassController {

    @FXML
    private TextField emailField;

    @FXML
    private Button closeButton;

    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@(.+)$";
    private static final String DEFAULT_PASSWORD = "zaq1@WSX"; // domyślne hasło

    /**
     * Inicjalizuje kontroler – obsługuje naciśnięcie Entera w polu e-mail.
     */
    @FXML
    private void initialize() {
        emailField.setOnAction(event -> {
            try {
                handlePasswordReset();
            } catch (SQLException e) {
                showAlert("Błąd", "Błąd podczas resetowania hasła.");
                e.printStackTrace();
            }
        });
    }

    /**
     * Obsługuje reset hasła użytkownika po wprowadzeniu e-maila.
     */
    @FXML
    private void handlePasswordReset() throws SQLException {
        String email = emailField.getText();

        if (email.isEmpty()) {
            showAlert("Uwaga", "Proszę uzupełnić pole e-mail!");
            return;
        }

        if (!Pattern.matches(EMAIL_REGEX, email)) {
            showAlert("Uwaga", "Podano niepoprawny adres e-mail!");
            return;
        }

        if (isEmailExists(email)) {
            resetPassword(email);
            showAlert("Sukces", "Hasło zostało zresetowane dla: " + email);
            closeWindow();
        } else {
            showAlert("Błąd", "E-mail nie istnieje w systemie.");
        }
    }

    /**
     * Sprawdza, czy podany e-mail istnieje w bazie danych.
     *
     * @param email e-mail do sprawdzenia
     * @return true jeśli istnieje, false w przeciwnym razie
     */
    private boolean isEmailExists(String email) {
        String query = "SELECT 1 FROM users WHERE email = ? LIMIT 1";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Błąd", "Błąd połączenia z bazą danych.");
            return false;
        }
    }

    /**
     * Resetuje hasło użytkownika do wartości domyślnej.
     *
     * @param email e-mail użytkownika
     */
    private void resetPassword(String email) {
        String query = "UPDATE users SET password = ? WHERE email = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, DEFAULT_PASSWORD);
            stmt.setString(2, email);

            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated == 0) {
                showAlert("Błąd", "Nie udało się zmienić hasła.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Błąd", "Wystąpił problem z aktualizacją hasła.");
        }
    }

    /**
     * Obsługuje zamknięcie okna po kliknięciu przycisku "Zamknij".
     */
    @FXML
    private void handleClose() {
        closeWindow();
    }

    /**
     * Zamyka bieżące okno.
     */
    private void closeWindow() {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

    /**
     * Pokazuje alert z przekazanym tytułem i wiadomością.
     *
     * @param title   tytuł alertu
     * @param message treść wiadomości
     */
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
