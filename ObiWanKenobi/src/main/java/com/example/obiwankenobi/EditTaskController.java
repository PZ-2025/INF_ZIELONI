package com.example.obiwankenobi;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.ResourceBundle;

/**
 * Kontroler okna edytowania zadania.
 * Obsługuje formularze i interakcje związane z edycją zadań.
 */

public class EditTaskController implements Initializable {

    @FXML private Button addTaskButton;
    @FXML private Button clearTaskButton;
    @FXML private Button closeButton;
    @FXML private ChoiceBox<String> employeeChoiceBox;
    @FXML private DatePicker taskDeadlineField;
    @FXML private TextArea taskDescriptionField;
    @FXML private TextField taskTitleField;

    // Style CSS dla elementów z błędem
    private final String errorStyle = "-fx-border-color: red; -fx-border-width: 2px;";
    private final String normalStyle = "";

    // ID edytowanego zadania
    private int taskId;

    /**
     * Inicjalizuje kontroler.
     * Metoda wywoływana automatycznie po załadowaniu FXML.
     *
     * @param location Lokalizacja używana do rozwiązywania ścieżek względnych
     * @param resources Zasoby używane do lokalizacji korzenia obiektu
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            fetchInfo();
            // Ustawienie bieżącej daty jako domyślnej dla pola deadline
            taskDeadlineField.setValue(LocalDate.now());
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Błąd połączenia", "Nie udało się nawiązać połączenia z bazą danych: " + e.getMessage());
        }

        // Resetowanie stanu błędów przy zmianie wartości pól
        taskTitleField.textProperty().addListener((observable, oldValue, newValue) -> {
            resetFieldStyle(taskTitleField);
        });

        taskDescriptionField.textProperty().addListener((observable, oldValue, newValue) -> {
            resetFieldStyle(taskDescriptionField);
        });

        employeeChoiceBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            resetFieldStyle(employeeChoiceBox);
        });

        taskDeadlineField.valueProperty().addListener((observable, oldValue, newValue) -> {
            resetFieldStyle(taskDeadlineField);
        });
    }
    private void resetFieldStyle(javafx.scene.Node field) {
        field.setStyle(normalStyle);
    }

    /**
     * Ustawia dane zadania w interfejsie użytkownika.
     *
     * @param taskId ID zadania
     * @param title Tytuł zadania
     * @param description Opis zadania
     * @param deadline Termin wykonania zadania
     * @param userId ID przypisanego pracownika
     * @param userName Imię i nazwisko przypisanego pracownika
     */
    public void setTaskData(int taskId, String title, String description, LocalDate deadline, int userId, String userName) {
        this.taskId = taskId;
        taskTitleField.setText(title);
        taskDescriptionField.setText(description);
        taskDeadlineField.setValue(deadline);
        employeeChoiceBox.setValue(userId + ": " + userName);
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
     * Obsługuje akcję czyszczenia formularza.
     * Resetuje wszystkie pola do wartości domyślnych.
     */
    @FXML
    private void handleClearTask() {
        taskTitleField.clear();
        taskDescriptionField.clear();
        taskDeadlineField.setValue(LocalDate.now());
        employeeChoiceBox.setValue(null);
    }

    /**
     * Aktualizuje zadanie w bazie danych na podstawie wprowadzonych danych.
     * Jeśli nie wybrano pracownika, wyświetla ostrzeżenie. Po zapisaniu zmiany,
     * zamyka okno edycji.
     *
     * @throws SQLException jeśli wystąpi błąd podczas zapisu do bazy danych
     */
    @FXML
    private void saveTask() throws SQLException {

        if (!validateInputData()) {
            return;
        }

        String title = taskTitleField.getText();
        String description = taskDescriptionField.getText();
        LocalDate deadline = taskDeadlineField.getValue();
        String selectedUser = employeeChoiceBox.getValue();

        if (selectedUser == null) {
            showAlert(Alert.AlertType.WARNING, "Brak pracownika", "Wybierz pracownika.");
            return;
        }

        int userId = Integer.parseInt(selectedUser.split(":")[0].trim());

        Connection con = DatabaseConnection.getConnection();
        String sql = "UPDATE tasks SET title = ?, description = ?, user_id = ?, deadline = ? WHERE id = ?";
        PreparedStatement stmt = con.prepareStatement(sql);

        stmt.setString(1, title);
        stmt.setString(2, description);
        stmt.setInt(3, userId);
        stmt.setDate(4, Date.valueOf(deadline));
        stmt.setInt(5, taskId);

        int updated = stmt.executeUpdate();

        if (updated > 0) {
            showAlert(Alert.AlertType.INFORMATION, "Sukces", "Zadanie zostało zaktualizowane.");
            handleClose();
        } else {
            showAlert(Alert.AlertType.ERROR, "Błąd", "Nie udało się zaktualizować zadania.");
        }
    }


    /**
     * Pobiera listę pracowników przypisanych do działu zalogowanego użytkownika
     * i aktualizuje ChoiceBox z ich danymi.
     *
     * @throws SQLException jeśli wystąpi błąd podczas pobierania danych z bazy
     */
    public void fetchInfo() throws SQLException {
        Connection con = DatabaseConnection.getConnection();
        int idRole = UserController.getLoggedInUser().getUserId();

        String sql = "SELECT * FROM users u JOIN departments d ON u.department_id = d.id WHERE u.role_id = 4 AND d.manager_id = ?";
        PreparedStatement statement = con.prepareStatement(sql);
        statement.setInt(1, idRole);
        ResultSet rs = statement.executeQuery();

        ObservableList<String> users = javafx.collections.FXCollections.observableArrayList();
        while (rs.next()) {
            users.add(rs.getInt("id") + ": " + rs.getString("first_name") + " " + rs.getString("last_name"));
        }
        employeeChoiceBox.setItems(users);
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

    private boolean validateInputData() {
        boolean isValid = true;
        StringBuilder errorMessage = new StringBuilder();

        // Resetowanie stylów wszystkich pól
        resetFieldStyle(taskDeadlineField);
        resetFieldStyle(taskDescriptionField);
        resetFieldStyle(taskTitleField);
        resetFieldStyle(taskDeadlineField);

        // Sprawdzenie czy tytuł zadania został wprowadzony
        if (taskTitleField.getText() == null || taskTitleField.getText().trim().isEmpty()) {
            errorMessage.append("Tytuł zadania nie może być pusty!\n");
            animateFieldError(taskTitleField);
            isValid = false;
        }

        // Sprawdzenie czy opis zadania został wprowadzony
        if (taskDescriptionField.getText() == null || taskDescriptionField.getText().trim().isEmpty()) {
            errorMessage.append("Opis zadania nie może być pusty!\n");
            animateFieldError(taskDescriptionField);
            isValid = false;
        }


        // Sprawdzenie czy wybrano datę
        if (taskDeadlineField.getValue() == null) {
            errorMessage.append("Należy wybrać termin realizacji zadania!\n");
            animateFieldError(taskDeadlineField);
            isValid = false;
        } else if (taskDeadlineField.getValue().isBefore(LocalDate.now())) {
            errorMessage.append("Termin realizacji nie może być wcześniejszy niż dzisiejsza data!\n");
            animateFieldError(taskDeadlineField);
            isValid = false;
        }

        // Jeśli wystąpiły błędy, wyświetl komunikat
        if (!isValid) {
            showAlert(Alert.AlertType.ERROR, "Błędne dane", errorMessage.toString());
        }

        return isValid;
    }

    private void animateFieldError(javafx.scene.Node field) {
        // Zapisujemy oryginalny styl
        String originalStyle = field.getStyle();

        // Tworzymy timeline dla animacji migania
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(field.styleProperty(), errorStyle)),
                new KeyFrame(Duration.millis(200), new KeyValue(field.styleProperty(), normalStyle)),
                new KeyFrame(Duration.millis(400), new KeyValue(field.styleProperty(), errorStyle)),
                new KeyFrame(Duration.millis(600), new KeyValue(field.styleProperty(), normalStyle)),
                new KeyFrame(Duration.millis(800), new KeyValue(field.styleProperty(), errorStyle))
        );

        // Po zakończeniu animacji ustawiamy styl błędu
        timeline.setOnFinished(event -> field.setStyle(errorStyle));

        // Odtwarzamy animację
        timeline.play();
    }
}
