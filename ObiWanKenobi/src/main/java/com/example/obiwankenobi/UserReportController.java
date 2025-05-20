package com.example.obiwankenobi;

import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import org.example.ReportGenerator;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class UserReportController implements Initializable{

    public Button closeButton;
    @FXML
    private TextField cityField;
    @FXML
    private ChoiceBox<String> departmentChoiceBox;
    @FXML
    private TextField salaryMax;
    @FXML
    private TextField salaryMin;
    @FXML
    private TextField tasksMax;
    @FXML
    private TextField tasksMin;

    @FXML
    public void initialize(URL location, ResourceBundle resources) {
        loadDeps();
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
    @FXML
    void reportClose(ActionEvent event) {
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.close();
    }
    @FXML
    void handleClearTask(ActionEvent event) {
        cityField.clear();
        departmentChoiceBox.setValue("wszystkie");
        tasksMin.clear();
        tasksMax.clear();
        salaryMax.clear();
        salaryMin.clear();
    }

    @FXML
    private void generateUsersButton() throws SQLException {

        String selectedDepartment = departmentChoiceBox.getValue();
        String city = cityField.getText();
        String minSalaryText = salaryMin.getText();
        String maxSalaryText = salaryMax.getText();
        String minTasksText = tasksMin.getText();
        String maxTasksText = tasksMax.getText();

        Integer minSalary = null;
        Integer maxSalary = null;
        Integer minTasks = null;
        Integer maxTasks = null;

        if ("wszystkie".equals(selectedDepartment)) {
            selectedDepartment = null;
        }

        if (minSalaryText != null && !minSalaryText.isEmpty()) {
            try {
                minSalary = Integer.parseInt(minSalaryText);
                if (minSalary < 0) {
                    shakeNode(salaryMin);
                    System.err.println("Minimalna pensja nie może być ujemna.");
                    return;
                }
            } catch (NumberFormatException e) {
                shakeNode(salaryMin);
                System.err.println("Nieprawidłowa minimalna pensja.");
                return;
            }
        }

        if (maxSalaryText != null && !maxSalaryText.isEmpty()) {
            try {
                maxSalary = Integer.parseInt(maxSalaryText);
                if (maxSalary < 0) {
                    shakeNode(salaryMax);
                    System.err.println("Maksymalna pensja nie może być ujemna.");
                    return;
                }
            } catch (NumberFormatException e) {
                shakeNode(salaryMax);
                System.err.println("Nieprawidłowa maksymalna pensja.");
                return;
            }
        }

        if (minSalary != null && maxSalary != null && maxSalary < minSalary) {
            shakeNode(salaryMin);
            shakeNode(salaryMax);
            System.err.println("Maksymalna pensja nie może być mniejsza od minimalnej pensji.");
            return;
        }

        if (minTasksText != null && !minTasksText.isEmpty()) {
            try {
                minTasks = Integer.parseInt(minTasksText);
                if (minTasks < 0) {
                    shakeNode(tasksMin);
                    System.err.println("Minimalna liczba zadań nie może być ujemna.");
                    return;
                }
            } catch (NumberFormatException e) {
                shakeNode(tasksMin);
                System.err.println("Nieprawidłowa minimalna liczba zadań.");
                return;
            }
        }

        if (maxTasksText != null && !maxTasksText.isEmpty()) {
            try {
                maxTasks = Integer.parseInt(maxTasksText);
                if (maxTasks < 0) {
                    shakeNode(tasksMax);
                    System.err.println("Maksymalna liczba zadań nie może być ujemna.");
                    return;
                }
            } catch (NumberFormatException e) {
                shakeNode(tasksMax);
                System.err.println("Nieprawidłowa maksymalna liczba zadań.");
                return;
            }
        }

        if (minTasks != null && maxTasks != null && maxTasks < minTasks) {
            shakeNode(tasksMin);
            shakeNode(tasksMax);
            System.err.println("Maksymalna liczba zadań nie może być mniejsza od minimalnej liczby zadań.");
            return;
        }

        try {
            ReportGenerator.generateUsers(selectedDepartment, city, minSalary, maxSalary, minTasks, maxTasks);
        } catch (Exception e) {
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
