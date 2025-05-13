package com.example.obiwankenobi;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class WarehouseReportController implements Initializable {
    public Button closeButton;
    public ChoiceBox<String> categoryChoiceBox;
    public TextField minQuantityField;
    public TextField maxQuantityField;
    public Button generateButton;
    public Button clearButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadDeps();
    }

    private void loadDeps() {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String sql = "SELECT name FROM departments ORDER BY name";
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            categoryChoiceBox.getItems().add("wszystkie");

            while (rs.next()) {
                String departmentName = rs.getString("name");
                categoryChoiceBox.getItems().add(departmentName);

            }

            categoryChoiceBox.setValue("wszystkie");



        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void reportClose(ActionEvent event) {
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.close();
    }
    @FXML
    void handleClearTask(ActionEvent event) {
        categoryChoiceBox.setValue(null);
        minQuantityField.clear();
        maxQuantityField.clear();

    }
    @FXML
    public void generateWarehouseReport() {
        int minQuantity = Integer.parseInt(minQuantityField.getText());
        int maxQuantity = Integer.parseInt(maxQuantityField.getText());
        String category = categoryChoiceBox.getValue();

        try (Connection connection = DatabaseConnection.getConnection()){

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
