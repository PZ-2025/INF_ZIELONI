package com.example.obiwankenobi;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.ReportGenerator;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class WarehouseReportController implements Initializable {
    public Button closeButton;
    public ChoiceBox<String> categoryChoiceBox;
    public ChoiceBox<String> menagerChoiceBox;
    public TextField minQuantityField;
    public TextField maxQuantityField;
    public Button generateButton;
    public Button clearButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadDeps();
        loadMenagers();
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

    private void loadMenagers(){
        try (Connection connection = DatabaseConnection.getConnection()){
            String sql = "SELECT first_name, last_name FROM users WHERE role_id = 3";
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet rs= stmt.executeQuery();

            menagerChoiceBox.getItems().add("wszyscy");

            while (rs.next()){
                String first = rs.getString("first_name");
                String last = rs.getString("last_name");
                String fullName = first + " " + last;
                menagerChoiceBox.getItems().add(fullName);

            }

            menagerChoiceBox.setValue("wszyscy");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void reportClose(ActionEvent event) {
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.close();
    }
    @FXML
    void handleClearTask(ActionEvent event) {
        categoryChoiceBox.setValue("wszystkie");
        minQuantityField.clear();
        maxQuantityField.clear();
        menagerChoiceBox.setValue("wszyscy");

    }
    @FXML
    private void generateWarehouseButton() throws SQLException {
        String selectedDepartment = categoryChoiceBox.getValue();
        String selectedManager = menagerChoiceBox.getValue();
        String minQtyText = minQuantityField.getText();
        String maxQtyText = maxQuantityField.getText();

        Integer minQty = null;
        Integer maxQty = null;

        if ("wszyscy".equals(selectedDepartment)) {
            selectedDepartment = null;
        }

        if ("wszyscy".equals(selectedManager)) {
            selectedManager = null;
        }

        if (minQtyText != null && !minQtyText.isEmpty()) {
            try {
                minQty = Integer.parseInt(minQtyText);
                if (minQty < 0) {
                    System.err.println("Minimalna ilość nie może być mniejsza niż 0.");
                    return;
                }
            } catch (NumberFormatException e) {
                System.err.println("Nieprawidłowa minimalna ilość.");
                return;
            }
        }

        if (maxQtyText != null && !maxQtyText.isEmpty()) {
            try {
                maxQty = Integer.parseInt(maxQtyText);
                if (maxQty < 0) {
                    System.err.println("Maksymalna ilość nie może być mniejsza niż 0.");
                    return;
                }
            } catch (NumberFormatException e) {
                System.err.println("Nieprawidłowa maksymalna ilość.");
                return;
            }
        }

        if (minQty != null && maxQty != null && minQty > maxQty) {
            System.err.println("Minimalna ilość nie może być większa niż maksymalna.");
            return;
        }

        try {
            ReportGenerator.generateWarehouse(selectedDepartment, selectedManager, minQty, maxQty);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
