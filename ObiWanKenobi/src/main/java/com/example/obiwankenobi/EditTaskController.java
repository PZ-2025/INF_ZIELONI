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
 * Obsługuje formularz edycji zadań, weryfikację danych i aktualizację w bazie.
 */
public class EditTaskController implements Initializable {

    @FXML public Button addTaskButton;
    @FXML public Button clearTaskButton;
    @FXML public Button closeButton;
    @FXML public ChoiceBox<String> employeeChoiceBox;
    @FXML public DatePicker taskDeadlineField;
    @FXML public TextField taskPriorityField;
    @FXML public TextArea taskDescriptionField;
    @FXML public TextField taskTitleField;

    // Style CSS dla elementów z błędem
    private final String errorStyle = "-fx-border-color: red; -fx-border-width: 2px; -fx-border-radius: 6; -fx-background-radius: 6";
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
            //showAlert(Alert.AlertType.ERROR, "Błąd połączenia", "Nie udało się nawiązać połączenia z bazą danych: " + e.getMessage());
        }

        // Resetowanie stanu błędów przy zmianie wartości pól
        taskTitleField.textProperty().addListener((observable, oldValue, newValue) -> {
            resetFieldStyle(taskTitleField);
        });

        taskDescriptionField.textProperty().addListener((observable, oldValue, newValue) -> {
            resetFieldStyle(taskDescriptionField);
        });

        taskPriorityField.textProperty().addListener((observable, oldValue, newValue) -> {
            resetFieldStyle(taskPriorityField);
        });

        employeeChoiceBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            resetFieldStyle(employeeChoiceBox);
        });

        taskDeadlineField.valueProperty().addListener((observable, oldValue, newValue) -> {
            resetFieldStyle(taskDeadlineField);
        });
    }

    /**
     * Resetuje styl wskazanego pola do stylu domyślnego.
     *
     * @param field pole formularza
     */
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
    public void setTaskData(int taskId, String title, String description, String priority, LocalDate deadline, int userId, String userName) {
        this.taskId = taskId;
        taskTitleField.setText(title);
        taskDescriptionField.setText(description);
        taskPriorityField.setText(priority);
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
        taskPriorityField.clear();
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
    private void saveTask() {
        if (!validateInputData()) {
            return;
        }

        String title = taskTitleField.getText();
        String description = taskDescriptionField.getText();
        LocalDate deadline = taskDeadlineField.getValue();
        String priority = taskPriorityField.getText();
        String selectedUser = employeeChoiceBox.getValue();

        if (selectedUser == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Brak pracownika");
            alert.setHeaderText(null);
            alert.setContentText("Wybierz pracownika.");
            alert.showAndWait();
            return;
        }

        int userId = Integer.parseInt(selectedUser.split(":")[0].trim());

        Connection con = null;
        PreparedStatement stmt = null;

        try {
            con = DatabaseConnection.getConnection();
            String sql = "UPDATE tasks SET title = ?, description = ?, user_id = ?, deadline = ?, priority = ? WHERE id = ?";
            stmt = con.prepareStatement(sql);

            stmt.setString(1, title);
            stmt.setString(2, description);
            stmt.setInt(3, userId);
            stmt.setDate(4, Date.valueOf(deadline));
            stmt.setString(5, priority);
            stmt.setInt(6, taskId);

            int updated = stmt.executeUpdate();

            if (updated > 0) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Sukces");
                alert.setHeaderText(null);
                alert.setContentText("Zadanie zostało zaktualizowane.");
                alert.showAndWait();
                handleClose();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Błąd");
                alert.setHeaderText(null);
                alert.setContentText("Nie udało się zaktualizować zadania.");
                alert.showAndWait();
            }

        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Błąd bazy danych");
            alert.setHeaderText(null);
            alert.setContentText("Wystąpił problem podczas aktualizacji zadania:\n" + e.getMessage());
            alert.showAndWait();
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    System.err.println("Błąd podczas zamykania statement: " + e.getMessage());
                }
            }
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
    /*
    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
    */

    /**
     * Sprawdza poprawność danych wprowadzonych do formularza.
     *
     * @return true jeśli dane są poprawne, false w przeciwnym razie
     */
    public boolean validateInputData() {
        boolean isValid = true;
        StringBuilder errorMessage = new StringBuilder();

        // Resetowanie stylów wszystkich pól
        resetFieldStyle(taskDeadlineField);
        resetFieldStyle(taskDescriptionField);
        resetFieldStyle(taskTitleField);
        resetFieldStyle(taskPriorityField);
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

        if (taskPriorityField.getText() == null || taskPriorityField.getText().trim().isEmpty()) {
            errorMessage.append("priorytet nie może być pusty!\n");
            animateFieldError(taskPriorityField);
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
          //  showAlert(Alert.AlertType.ERROR, "Błędne dane", errorMessage.toString());
        }

        return isValid;
    }

    /**
     * Animuje pole formularza w przypadku błędu.
     *
     * @param field pole do wyróżnienia
     */
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
