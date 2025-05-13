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

public class TaskReportController implements Initializable{


    public Button closeButton;
    @FXML
    private ChoiceBox<String> statusChoiceBox;
    public DatePicker startDatePicker;
    public DatePicker endDatePicker;
    @FXML
    private ChoiceBox<String> priorityChoiceBox;
    @FXML
    private ChoiceBox<String> departmentChoiceBox;
    @FXML
    private ChoiceBox<String> employeeChoiceBox;
    public Button generateButton;
    public Button clearButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
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
        departmentChoiceBox.setValue(null);
        priorityChoiceBox.setValue(null);
        employeeChoiceBox.setValue(null);
        statusChoiceBox.setValue(null);
    }

    @FXML
    private void generateUserReport() throws SQLException {

        String taskStatus = statusChoiceBox.getValue();
        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();
        String priority = priorityChoiceBox.getValue();
        String department = departmentChoiceBox.getValue();
        String empChoiceBox = employeeChoiceBox.getValue();
        int userId = Integer.parseInt(empChoiceBox.split(":")[0].trim());

        try (Connection connection = DatabaseConnection.getConnection()){

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
