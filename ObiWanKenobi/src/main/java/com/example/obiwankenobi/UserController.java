package com.example.obiwankenobi;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class UserController {
    @FXML
    private Button logoutBtn;

    @FXML
    private void logout(ActionEvent event) {
        try {
            // Zamknięcie aktualnego okna
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close();

            // Ponowne uruchomienie aplikacji
            new Launcher().start(new Stage());

        } catch (Exception e) {
            e.printStackTrace();
            // Opcjonalna obsługa błędu
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Błąd");
            alert.setHeaderText("Nie można wylogować");
            alert.setContentText("Wystąpił problem podczas wylogowywania.");
            alert.showAndWait();
        }
    }
}
