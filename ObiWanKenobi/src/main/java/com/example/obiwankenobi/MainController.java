package com.example.obiwankenobi;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.ParallelTransition;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;

/**
 * Kontroler głównego panelu logowania i ekranu startowego.
 * Obsługuje przełączanie widoków oraz logowanie użytkowników z rozróżnieniem ról.
 */
public class MainController implements Initializable {

    @FXML
    private VBox vbox;

    private Parent fxml;

    @FXML
    private TextField loginMail;

    @FXML
    private PasswordField loginPassword;

    @FXML
    private Button closeButton;

    @FXML
    private Button addUserBtn;

    // Style CSS dla elementów z błędem
    private final String errorStyle = "-fx-border-color: red; -fx-border-width: 2px;";
    private final String normalStyle = "";

    /**
     * Inicjalizacja kontrolera – ładuje widok startowy do kontenera VBox.
     *
     * @param url URL lokalizacji zasobu
     * @param rb  zasoby lokalizacyjne
     */
    public void initialize(URL url, ResourceBundle rb) {
        if (vbox.getChildren().isEmpty()) {
            vbox.setTranslateX(450);
            try {
                fxml = FXMLLoader.load(getClass().getResource("/com/example/obiwankenobi/views/start.fxml"));
                vbox.getChildren().setAll(fxml);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        // Add this code to ensure text fields have visible text
        if (loginMail != null) {
            loginMail.setStyle("-fx-text-fill: black;");
            loginMail.textProperty().addListener((observable, oldValue, newValue) -> {
                // Keep the text color while resetting other styles
                String currentStyle = loginMail.getStyle();
                resetFieldStyle(loginMail);
                loginMail.setStyle(currentStyle + "; -fx-text-fill: black;");
            });
        }

        if (loginPassword != null) {
            loginPassword.setStyle("-fx-text-fill: black;");
            loginPassword.textProperty().addListener((observable, oldValue, newValue) -> {
                // Keep the text color while resetting other styles
                String currentStyle = loginPassword.getStyle();
                resetFieldStyle(loginPassword);
                loginPassword.setStyle(currentStyle + "; -fx-text-fill: black;");
            });
        }
    }


    /**
     * Resetuje styl pola do normalnego (bez błędu).
     *
     * @param field pole, którego styl ma zostać zresetowany
     */
    private void resetFieldStyle(javafx.scene.Node field) {
        field.setStyle(normalStyle + "; -fx-text-fill: black;");
    }

    /**
     * Animacja błędu dla pola formularza - miganie na czerwono i poruszanie się na boki.
     *
     * @param field pole, które ma być animowane
     */
    private void shakeAndFlashField(javafx.scene.Node field) {
        // Zapisujemy oryginalną pozycję X
        double originalX = field.getTranslateX();

        // Tworzymy animację migania koloru (czerwona ramka)
        Timeline colorAnimation = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(field.styleProperty(), errorStyle)),
                new KeyFrame(Duration.millis(200), new KeyValue(field.styleProperty(), normalStyle)),
                new KeyFrame(Duration.millis(400), new KeyValue(field.styleProperty(), errorStyle)),
                new KeyFrame(Duration.millis(600), new KeyValue(field.styleProperty(), normalStyle)),
                new KeyFrame(Duration.millis(800), new KeyValue(field.styleProperty(), errorStyle))
        );

        // Tworzymy animację poruszania się na boki
        Timeline shakeAnimation = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(field.translateXProperty(), originalX)),
                new KeyFrame(Duration.millis(100), new KeyValue(field.translateXProperty(), originalX - 10)),
                new KeyFrame(Duration.millis(200), new KeyValue(field.translateXProperty(), originalX + 10)),
                new KeyFrame(Duration.millis(300), new KeyValue(field.translateXProperty(), originalX - 10)),
                new KeyFrame(Duration.millis(400), new KeyValue(field.translateXProperty(), originalX + 10)),
                new KeyFrame(Duration.millis(500), new KeyValue(field.translateXProperty(), originalX - 10)),
                new KeyFrame(Duration.millis(600), new KeyValue(field.translateXProperty(), originalX + 10)),
                new KeyFrame(Duration.millis(700), new KeyValue(field.translateXProperty(), originalX - 10)),
                new KeyFrame(Duration.millis(800), new KeyValue(field.translateXProperty(), originalX))
        );

        // Łączymy obie animacje w jedną
        ParallelTransition parallelTransition = new ParallelTransition(colorAnimation, shakeAnimation);

        // Po zakończeniu animacji ustawiamy styl błędu
        parallelTransition.setOnFinished(event -> field.setStyle(errorStyle));

        // Odtwarzamy animację
        parallelTransition.play();
    }

    /**
     * Obsługuje logowanie użytkownika na podstawie e-maila i hasła.
     * Po poprawnej autoryzacji ładowany jest odpowiedni panel.
     *
     * @param event zdarzenie kliknięcia przycisku logowania
     */
    @FXML
    private void login(ActionEvent event) {
        // Resetujemy style pól przed walidacją
        resetFieldStyle(loginMail);
        resetFieldStyle(loginPassword);

        // Walidacja danych wejściowych
        boolean isValid = true;
        StringBuilder errorMessage = new StringBuilder();

        // Sprawdzenie pola login (email)
        if (loginMail.getText() == null || loginMail.getText().trim().isEmpty()) {
            errorMessage.append("Pole email nie może być puste!\n");
            shakeAndFlashField(loginMail);
            isValid = false;
        }

        // Sprawdzenie pola hasło
        if (loginPassword.getText() == null || loginPassword.getText().trim().isEmpty()) {
            errorMessage.append("Pole hasło nie może być puste!\n");
            shakeAndFlashField(loginPassword);
            isValid = false;
        }

        // Jeśli podstawowa walidacja nie przeszła, wyświetlamy błąd i kończymy
        if (!isValid) {
            showErrorAlert(errorMessage.toString());
            return;
        }

        try {
            String login = loginMail.getText();
            String password = loginPassword.getText();

            String role = authenticateUser(login, password);

            if (role != null) {
                Stage currentStage = (Stage) vbox.getScene().getWindow();
                currentStage.close();

                String viewPath = getViewForRole(role);

                if (viewPath != null) {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource(viewPath));
                    Parent root = loader.load();

                    Stage newStage = new Stage();
                    newStage.setScene(new Scene(root));
                    newStage.initStyle(StageStyle.TRANSPARENT);
                    newStage.setResizable(false);
                    newStage.show();
                } else {
                    showErrorAlert("Nieznana rola użytkownika!");
                }
            } else {
                // Gdy dane uwierzytelniania są nieprawidłowe, uruchamiamy animację dla obu pól
                shakeAndFlashField(loginMail);
                shakeAndFlashField(loginPassword);
                showErrorAlert("Nieprawidłowy email lub hasło");
            }
        } catch (IOException e) {
            e.printStackTrace();
            showErrorAlert("Wystąpił błąd podczas logowania");
        }
    }

    /**
     * Przełącza widok na panel logowania.
     *
     * @param event kliknięcie przycisku logowania
     */
    @FXML
    private void openLogin(ActionEvent event) {
        TranslateTransition t = new TranslateTransition(Duration.seconds(1), vbox);
        t.setToX(-450);
        t.play();
        t.setOnFinished((e) -> {
            try {
                fxml = FXMLLoader.load(getClass().getResource("/com/example/obiwankenobi/views/login.fxml"));
                vbox.getChildren().clear();
                vbox.getChildren().setAll(fxml);

                // Szukamy pól formularza po załadowaniu widoku
                if (vbox.lookup("#loginMail") != null) {
                    loginMail = (TextField) vbox.lookup("#loginMail");
                    loginMail.setStyle("-fx-text-fill: black;");
                    loginMail.textProperty().addListener((observable, oldValue, newValue) -> {
                        String currentStyle = loginMail.getStyle();
                        resetFieldStyle(loginMail);
                        loginMail.setStyle(currentStyle + "; -fx-text-fill: black;");
                    });
                }

                if (vbox.lookup("#loginPassword") != null) {
                    loginPassword = (PasswordField) vbox.lookup("#loginPassword");
                    loginPassword.setStyle("-fx-text-fill: black;");
                    loginPassword.textProperty().addListener((observable, oldValue, newValue) -> {
                        String currentStyle = loginPassword.getStyle();
                        resetFieldStyle(loginPassword);
                        loginPassword.setStyle(currentStyle + "; -fx-text-fill: black;");
                    });
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
    }

    /**
     * Otwiera okno do resetowania hasła użytkownika.
     *
     * @param event kliknięcie napisu "zapomniałeś hasła?"
     */
    @FXML
    private void forgotPass(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/obiwankenobi/views/forgotPass.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root);
            scene.setFill(Color.TRANSPARENT);
            Stage newStage = new Stage();
            newStage.setScene(scene);
            newStage.initStyle(StageStyle.TRANSPARENT);
            newStage.initModality(Modality.APPLICATION_MODAL);
            newStage.setTitle("Przypomnij hasło");
            newStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Autoryzuje użytkownika na podstawie danych logowania.
     *
     * @param login    adres e-mail
     * @param password hasło
     * @return rola użytkownika (jeśli logowanie się powiodło), w przeciwnym razie null
     */
    public String authenticateUser(String login, String password) {
        String query = "SELECT u.id, u.first_name, u.last_name, r.name, u.city FROM users u " +
                "JOIN roles r ON u.role_id = r.id " +
                "WHERE u.email = ? AND u.password = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, login);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int userId = rs.getInt("id");
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                String role = rs.getString("name");
                String city = rs.getString("city");

                User loggedInUser = new User(userId, firstName, lastName, login, password, city, role, null);
                UserController.setLoggedInUser(loggedInUser);

                showSuccessAlert("Witaj, " + firstName + "!");
                return role;
            }
        } catch (Exception e) {
            e.printStackTrace();
            showErrorAlert("Wystąpił problem podczas logowania");
        }
        return null;
    }

    /**
     * Zwraca ścieżkę do widoku odpowiadającego danej roli użytkownika.
     *
     * @param role rola użytkownika (np. admin, dyrektor)
     * @return ścieżka do pliku FXML danego panelu
     */
    private String getViewForRole(String role) {
        switch (role.toLowerCase()) {
            case "admin":
                return "/com/example/obiwankenobi/views/adminPanel.fxml";
            case "dyrektor":
                return "/com/example/obiwankenobi/views/directorPanel.fxml";
            case "kierownik":
                return "/com/example/obiwankenobi/views/managerPanel.fxml";
            case "pracownik":
                return "/com/example/obiwankenobi/views/mainPanel.fxml";
            default:
                return null;
        }
    }

    /**
     * Otwiera okno dodawania nowego użytkownika.
     *
     * @param event kliknięcie przycisku "Dodaj użytkownika"
     */
    @FXML
    void addUser(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/obiwankenobi/views/addUser.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Dodawanie użytkownika");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Pokazuje okno z informacją o sukcesie.
     *
     * @param message komunikat do wyświetlenia
     */
    private void showSuccessAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Sukces");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Pokazuje okno z informacją o błędzie.
     *
     * @param message komunikat błędu
     */
    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Błąd");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Obsługuje zamknięcie aplikacji.
     */
    @FXML
    private void handleClose() {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }
}