package com.example.obiwankenobi;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class UserController {
    @FXML
    private Button logoutBtn;

    @FXML
    private void logout(ActionEvent event) {
        try {
            // Załaduj ekran logowania
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/obiwankenobi/views/login.fxml"));
            Parent root = loader.load();

            // Tworzenie nowego okna (Stage)
            Stage loginStage = new Stage();
            loginStage.setScene(new Scene(root));
            loginStage.setTitle("Logowanie");
            loginStage.show();

            // Zamknięcie obecnego okna (opcjonalnie)
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
