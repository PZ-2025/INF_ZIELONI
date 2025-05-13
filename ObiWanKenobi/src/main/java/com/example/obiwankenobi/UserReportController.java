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

public class UserReportController implements Initializable{

    public Button closeButton;
    @FXML
    private TextField cityField;

    @FXML
    private ChoiceBox<String> departmentChoiceBox;

    @FXML
    private ChoiceBox<String> salaryChoiceBox;

    @FXML
    private ChoiceBox<String> taskRangeChoiceBox;

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
        departmentChoiceBox.setValue(null);
        salaryChoiceBox.setValue(null);
        taskRangeChoiceBox.setValue(null);
    }
    @FXML
    private void generateUserReport() throws SQLException {

        String selectedDepartment = departmentChoiceBox.getValue();
        String salary = salaryChoiceBox.getValue();
        String taskRange = taskRangeChoiceBox.getValue();
        String city = cityField.getText();
        int salaryMin = 0;
        int salaryMax = 0;
        int taskMin = 0;
        int taskMax = 0;

        switch (salary) {
            case "1 - 2000":
                salaryMin = 1;
                salaryMax = 2000;
                break;
            case "2001 - 4000":
                salaryMin = 2001;
                salaryMax = 4000;
                break;
            case "4001 - 6000":
                salaryMin = 4001;
                salaryMax = 6000;
                break;
            case "6001 - 8000":
                salaryMin = 6001;
                salaryMax = 8000;
                break;
            case "Powyżej 8001":
                salaryMin = 8001;
                salaryMax = Integer.MAX_VALUE;
                break;
            default:
                break;
        }

        switch (taskRange) {
            case "0 - 10":
                taskMin = 0;
                taskMax = 10;
                break;
            case "11 - 20":
                taskMin = 11;
                taskMax = 20;
                break;
            case "21 - 50":
                taskMin = 21;
                taskMax = 50;
                break;
            case "51 - 80":
                taskMin = 51;
                taskMax = 80;
                break;
            case "Powyżej 80":
                taskMin = 81;
                taskMax = Integer.MAX_VALUE;
                break;
            default:
                break;
        }

        try (Connection connection = DatabaseConnection.getConnection()){

    } catch (Exception e) {
        e.printStackTrace();
    }
    }

}
