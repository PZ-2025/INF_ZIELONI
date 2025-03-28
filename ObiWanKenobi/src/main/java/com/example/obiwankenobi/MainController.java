package com.example.obiwankenobi;

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
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;

public class MainController implements Initializable{

    @FXML
    private VBox vbox;
    private Parent fxml;
    @FXML
    private TextField loginMail;
    @FXML
    private PasswordField loginPassword;
    @FXML
    private Button closeButton;


    @Override
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

    }

    @FXML
    private void login(ActionEvent event) {
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
                showErrorAlert("Nieprawidłowy email lub hasło");
            }
        } catch (IOException e) {
            e.printStackTrace();
            showErrorAlert("Wystąpił błąd podczas logowania");
        }
    }

    @FXML
    private void openLogin(ActionEvent event){
        TranslateTransition t = new TranslateTransition(Duration.seconds(1), vbox);
        t.setToX(-450);
        t.play();
        t.setOnFinished((e) -> {
            try {
                fxml = FXMLLoader.load(getClass().getResource("/com/example/obiwankenobi/views/login.fxml"));
                vbox.getChildren().clear();
                vbox.getChildren().setAll(fxml);

            } catch(IOException ex) {

            }
        });
    }

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

    private String authenticateUser(String login, String password) {
        String query = "SELECT u.id, u.first_name, u.last_name, r.name FROM users u " +
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

                // Zapisujemy dane użytkownika w klasie User
                User.setUserId(userId);
                User.setEmail(login);

                showSuccessAlert("Witaj, " + firstName + "!");

                return role;
            }
        } catch (Exception e) {
            e.printStackTrace();
            showErrorAlert("Wystąpił problem podczas logowania");
        }
        return null;
    }

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

    private void showSuccessAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Sukces");
        alert.setHeaderText(null);
        alert.setContentText(message);

        alert.showAndWait();
    }

    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Błąd");
        alert.setHeaderText(null);
        alert.setContentText(message);

        alert.showAndWait();
    }

    @FXML
    private void handleClose() {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }
}