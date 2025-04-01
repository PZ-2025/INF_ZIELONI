package com.example.obiwankenobi;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class ManagerController {

    @FXML
    private Button addTaskBtn;

    @FXML
    private Button logoutBtn;

    @FXML
    void addTask(ActionEvent event) {

    }

    @FXML
    private void logout(ActionEvent event) {
        try {
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close();

            new Launcher().start(new Stage());

        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("blad");
            alert.setHeaderText(null);
            alert.setContentText("wystapil problem");
            alert.showAndWait();
        }
    }

}
