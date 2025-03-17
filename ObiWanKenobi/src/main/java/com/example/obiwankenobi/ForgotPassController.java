package com.example.obiwankenobi;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.util.regex.Pattern;

public class ForgotPassController {

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField newPasswordField;

    @FXML
    private Button sendButton;

    @FXML
    private Button closeButton;

    // REGEX do walidacji e-maila
    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@(.+)$";

    @FXML
    private void handlePasswordReset() {
        String email = emailField.getText();
        String newPassword = newPasswordField.getText();

        if (email.isEmpty() || newPassword.isEmpty()) {
            showAlert("Błąd", "Proszę uzupełnić wszystkie pola!");
            return;
        }

        if (!Pattern.matches(EMAIL_REGEX, email)) {
            showAlert("Błąd", "Podano niepoprawny adres e-mail!");
            return;
        }

        if (newPassword.length() < 6) {
            showAlert("Błąd", "Hasło musi mieć co najmniej 6 znaków!");
            return;
        }

        // Tutaj można dodać logikę resetowania hasła (np. zapis w bazie danych)
        showAlert("Sukces", "Hasło zostało zmienione dla: " + email);

        // Zamknięcie okna
        closeWindow();
    }

    @FXML
    private void handleClose() {
        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
