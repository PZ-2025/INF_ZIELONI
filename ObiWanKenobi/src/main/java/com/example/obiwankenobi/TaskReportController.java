package com.example.obiwankenobi;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.stage.Stage;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.ResourceBundle;
import org.example.ReportGenerator;
import javafx.animation.TranslateTransition;
import javafx.util.Duration;

public class TaskReportController implements Initializable{


    public Button closeButton;
    @FXML
    public ChoiceBox<String> statusChoiceBox;
    public DatePicker startDatePicker;
    public DatePicker endDatePicker;
    @FXML
    public ChoiceBox<String> priorityChoiceBox;
    @FXML
    public ChoiceBox<String> departmentChoiceBox;
    @FXML
    public ChoiceBox<String> employeeChoiceBox;
    public Button generateButton;
    public Button clearButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        statusChoiceBox.setValue("wszystkie");
        priorityChoiceBox.setValue("wszystkie");

        loadDeps();
        loadEmps();
    }
    private void loadDeps() {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String sql = "SELECT name FROM departments ORDER BY name";
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            departmentChoiceBox.getItems().add("wszystkie");

            while (rs.next()) {
                String departmentName = rs.getString("name");
                departmentChoiceBox.getItems().add(departmentName);

            }

            departmentChoiceBox.setValue("wszystkie");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadEmps() {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String sql = "SELECT * FROM users ORDER BY id ASC";
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            employeeChoiceBox.getItems().add("wszyscy");

            while (rs.next()) {
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                int id = rs.getInt("id");
                employeeChoiceBox.getItems().add(id + " " + firstName+ " " + lastName);

            }

            employeeChoiceBox.setValue("wszyscy");



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
        startDatePicker.setValue(null);
        endDatePicker.setValue(null);
        departmentChoiceBox.setValue("wszystkie");
        priorityChoiceBox.setValue("wszystkie");
        employeeChoiceBox.setValue("wszyscy");
        statusChoiceBox.setValue("wszystkie");
    }

    @FXML
    public void generateTaskButton() throws SQLException {

        String taskStatus = statusChoiceBox.getValue();
        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();
        String priority = priorityChoiceBox.getValue();
        String department = departmentChoiceBox.getValue();
        String empChoiceBox = employeeChoiceBox.getValue();
        Integer userId = null;

        if (empChoiceBox != null && !empChoiceBox.equals("wszyscy")) {
            try {
                userId = Integer.parseInt(empChoiceBox.split(" ")[0].trim());
            } catch (NumberFormatException e) {
                e.printStackTrace();
                return;
            }
        }

        if ("wszystkie".equals(taskStatus)) taskStatus = null;
        if ("wszystkie".equals(priority)) priority = null;
        if ("wszystkie".equals(department)) department = null;
        if ("wszyscy".equals(empChoiceBox)) userId = null;

        if (startDate != null && endDate != null && endDate.isBefore(startDate)) {
            shakeNode(startDatePicker);
            shakeNode(endDatePicker);
            System.err.println("Data zakończenia nie może być wcześniejsza niż data rozpoczęcia.");
            return;
        }

        try {
            ReportGenerator.generateTask(taskStatus, startDate, endDate, priority, department, userId);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void shakeNode(Node node) {
        TranslateTransition tt = new TranslateTransition(Duration.millis(50), node);
        node.setStyle("-fx-border-color: red; -fx-border-width: 2;");
        tt.setFromX(0);
        tt.setByX(10);
        tt.setCycleCount(20);
        tt.setAutoReverse(true);
        tt.play();
    }

}
