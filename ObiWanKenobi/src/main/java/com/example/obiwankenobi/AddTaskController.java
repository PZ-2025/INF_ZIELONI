package com.example.obiwankenobi;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

/**
 * Kontroler okna dodawania nowego zadania.
 * Obsługuje formularze i interakcje związane z dodawaniem zadań.
 */
public class AddTaskController implements Initializable {

    @FXML
    private Button addTaskButton;

    @FXML
    private Button clearTaskButton;

    @FXML
    private Button closeButton;

    @FXML
    private ChoiceBox<Employee> employeeChoiceBox;

    @FXML
    private DatePicker taskDeadlineField;

    @FXML
    private TextArea taskDescriptionField;

    @FXML
    private TextField taskTitleField;

    // Lista przechowująca pracowników
    private ObservableList<Employee> employeeList;

    // Referencja do połączenia z bazą danych
    private Connection connection;

    /**
     * Inicjalizuje kontroler.
     * Metoda wywoływana automatycznie po załadowaniu FXML.
     *
     * @param location Lokalizacja używana do rozwiązywania ścieżek względnych
     * @param resources Zasoby używane do lokalizacji korzenia obiektu
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Ustawienie dzisiejszej daty jako domyślnej w polu daty
        taskDeadlineField.setValue(LocalDate.now());

        // Inicjalizacja listy pracowników
        employeeList = FXCollections.observableArrayList();

        // Pobierz połączenie z bazą
        connection = DatabaseConnection.getConnection();

        // Załaduj pracowników do wyboru
        loadEmployees();

        // Ustaw pracowników w polu wyboru
        employeeChoiceBox.setItems(employeeList);
    }

    /**
     * Obsługuje akcję dodawania nowego zadania.
     * Waliduje dane, zapisuje je do bazy danych i zamyka okno.
     *
     * @param event zdarzenie kliknięcia przycisku
     */
    @FXML
    void handleAddTask(ActionEvent event) {
        // Pobierz wartości z pól formularza
        String title = taskTitleField.getText().trim();
        String description = taskDescriptionField.getText().trim();
        Employee selectedEmployee = employeeChoiceBox.getValue();
        LocalDate deadline = taskDeadlineField.getValue();

        // Walidacja danych
        if (title.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Błąd walidacji", "Nazwa zadania nie może być pusta.");
            return;
        }

        if (selectedEmployee == null) {
            showAlert(Alert.AlertType.ERROR, "Błąd walidacji", "Musisz wybrać pracownika.");
            return;
        }

        if (deadline == null) {
            showAlert(Alert.AlertType.ERROR, "Błąd walidacji", "Data wykonania musi być określona.");
            return;
        }

        if (deadline.isBefore(LocalDate.now())) {
            showAlert(Alert.AlertType.WARNING, "Ostrzeżenie", "Data wykonania jest wcześniejsza niż dzisiejsza data.");
        }

        // Zapis do bazy danych
        if (saveTask(title, description, selectedEmployee.getId(), deadline)) {
            showAlert(Alert.AlertType.INFORMATION, "Sukces", "Zadanie zostało dodane pomyślnie.");

            // Zamknij okno po dodaniu zadania
            Stage stage = (Stage) addTaskButton.getScene().getWindow();
            stage.close();
        }
    }

    /**
     * Obsługuje akcję czyszczenia formularza.
     * Resetuje wszystkie pola do wartości domyślnych.
     *
     * @param event zdarzenie kliknięcia przycisku
     */
    @FXML
    void handleClearTask(ActionEvent event) {
        taskTitleField.clear();
        taskDescriptionField.clear();
        employeeChoiceBox.setValue(null);
        taskDeadlineField.setValue(LocalDate.now());
    }

    /**
     * Obsługuje akcję zamknięcia okna.
     */
    @FXML
    private void handleClose() {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

    /**
     * Zapisuje nowe zadanie do bazy danych.
     *
     * @param title nazwa zadania
     * @param description opis zadania
     * @param employeeId identyfikator pracownika
     * @param deadline data wykonania
     * @return true jeśli zapis powiódł się, false w przeciwnym razie
     */
    private boolean saveTask(String title, String description, int employeeId, LocalDate deadline) {
        String sql = "INSERT INTO tasks (title, description, user_id, status, deadline) VALUES (?, ?, ?, 'pending', ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, title);
            statement.setString(2, description);
            statement.setInt(3, employeeId);
            statement.setString(4, deadline.toString());

            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Błąd bazy danych", "Nie udało się zapisać zadania: " + e.getMessage());
            return false;
        }
    }

    /**
     * Ładuje listę pracowników z bazy danych.
     */
    private void loadEmployees() {
        String sql = "SELECT id, username FROM users WHERE role = 'WORKER'";

        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String username = resultSet.getString("username");

                Employee employee = new Employee(id, username);
                employeeList.add(employee);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Błąd bazy danych", "Nie udało się załadować pracowników: " + e.getMessage());
        }
    }

    /**
     * Wyświetla alert z określonym typem, tytułem i treścią.
     *
     * @param type typ alertu
     * @param title tytuł alertu
     * @param content treść alertu
     */
    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    /**
     * Wewnętrzna klasa reprezentująca pracownika.
     */
    public static class Employee {
        private final int id;
        private final String name;

        public Employee(int id, String name) {
            this.id = id;
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        @Override
        public String toString() {
            return name;
        }
    }
}