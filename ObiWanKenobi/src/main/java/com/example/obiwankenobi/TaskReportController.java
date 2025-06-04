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

/**
 * Kontroler raportu zadań.
 * Obsługuje wybór kryteriów raportowania oraz generowanie raportów na podstawie danych.
 */
public class TaskReportController implements Initializable{

    /** Przycisk zamykający okno raportu. */
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

    /** Przycisk do generowania raportu. */
    public Button generateButton;

    /** Przycisk do czyszczenia wyborów w formularzu. */
    public Button clearButton;

    /**
     * Inicjalizuje kontroler, ustawiając domyślne wartości dla pól wyboru.
     * @param url URL dla pliku FXML
     * @param resourceBundle Zasoby lokalizacyjne
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        statusChoiceBox.setValue("wszystkie");
        priorityChoiceBox.setValue("wszystkie");

        loadDeps();
        loadEmps();
    }

    /**
     * Ładuje listę dostępnych działów z bazy danych do pola wyboru.
     */
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

    /**
     * Ładuje listę pracowników z bazy danych do pola wyboru.
     */
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

    /**
     * Zamyka okno raportu.
     * @param event Zdarzenie wywołujące zamknięcie okna
     */
    @FXML
    void reportClose(ActionEvent event) {
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.close();
    }

    /**
     * Resetuje wszystkie wybory w formularzu raportu.
     * @param event Zdarzenie wywołujące czyszczenie formularza
     */
    @FXML
    void handleClearTask(ActionEvent event) {
        startDatePicker.setValue(null);
        endDatePicker.setValue(null);
        departmentChoiceBox.setValue("wszystkie");
        priorityChoiceBox.setValue("wszystkie");
        employeeChoiceBox.setValue("wszyscy");
        statusChoiceBox.setValue("wszystkie");
    }

    /**
     * Generuje raport zadań na podstawie wybranych kryteriów.
     * @throws SQLException W przypadku błędów związanych z bazą danych
     */
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

            javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
            alert.setTitle("Raport wygenerowany");
            alert.setHeaderText(null);
            alert.setContentText("Raport zadan zostal pomyslnie wygenerowany");
            alert.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();

            javafx.scene.control.Alert errorAlert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
            errorAlert.setTitle("Blad generowania raportu");
            errorAlert.setHeaderText("Nie udalo sie wygenerowac raportu");
            errorAlert.setContentText("Wystapil blad: " + e.getMessage());
            errorAlert.showAndWait();
        }
    }

    /**
     * Tworzy efekt drżenia dla podanego węzła, wizualnie wyróżniając go.
     * @param node Węzeł, który ma być potrząsany
     */
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
