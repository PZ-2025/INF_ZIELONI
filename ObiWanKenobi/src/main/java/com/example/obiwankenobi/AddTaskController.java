package com.example.obiwankenobi;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
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
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
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
    private ChoiceBox<String> priorityChoiceBox;

    @FXML
    public Button closeButton;

    @FXML
    public ChoiceBox<String> employeeChoiceBox;

    @FXML
    public DatePicker taskDeadlineField;

    @FXML
    public TextArea taskDescriptionField;

    @FXML
    public TextField taskTitleField;

    // Lista przechowująca pracowników
    private ObservableList<String> employeeList;

    // Referencja do połączenia z bazą danych
    private Connection connection;

    // Style CSS dla elementów z błędem
    private final String errorStyle = "-fx-border-color: red; -fx-border-width: 2px; -fx-border-radius: 6; -fx-background-radius: 6";
    private final String normalStyle = "";

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
           // showAlert(Alert.AlertType.ERROR, "Błąd połączenia", "Nie udało się nawiązać połączenia z bazą danych: " + e.getMessage());
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

        priorityChoiceBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            resetFieldStyle(priorityChoiceBox);
        });
    }

    /**
     * Resetuje styl pola do normalnego (bez błędu).
     *
     * @param field pole, którego styl ma zostać zresetowany
     */
    private void resetFieldStyle(javafx.scene.Node field) {
        field.setStyle(normalStyle);
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
        priorityChoiceBox.setValue(null);

        // Resetowanie stylów wszystkich pól
        resetFieldStyle(taskTitleField);
        resetFieldStyle(taskDescriptionField);
        resetFieldStyle(employeeChoiceBox);
        resetFieldStyle(taskDeadlineField);
        resetFieldStyle(priorityChoiceBox);
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
     * Animuje pole formularza, aby zasygnalizować błąd.
     * Pole będzie migać na czerwono.
     *
     * @param field pole, które ma być animowane
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

    /**
     * Sprawdza poprawność danych wprowadzonych przez użytkownika.
     *
     * @return true jeśli dane są poprawne, false w przeciwnym przypadku
     */


    public boolean validateInputData() {
        boolean isValid = true;
        StringBuilder errorMessage = new StringBuilder();


        resetFieldStyle(taskTitleField);
        resetFieldStyle(taskDescriptionField);
        resetFieldStyle(employeeChoiceBox);
        resetFieldStyle(taskDeadlineField);
        resetFieldStyle(priorityChoiceBox);


        if (taskTitleField.getText() == null || taskTitleField.getText().trim().isEmpty()) {
            errorMessage.append("Tytuł zadania nie może być pusty!\n");
            animateFieldError(taskTitleField);
            isValid = false;
        }


        if (taskDescriptionField.getText() == null || taskDescriptionField.getText().trim().isEmpty()) {
            errorMessage.append("Opis zadania nie może być pusty!\n");
            animateFieldError(taskDescriptionField);
            isValid = false;
        }


        if (employeeChoiceBox.getValue() == null) {
            errorMessage.append("Należy wybrać pracownika!\n");
            animateFieldError(employeeChoiceBox);
            isValid = false;
        }

        if (priorityChoiceBox.getValue() == null) {
            errorMessage.append("Należy wybrać priorytet!\n");
            animateFieldError(priorityChoiceBox);
            isValid = false;
        }


        if (taskDeadlineField.getValue() == null) {
            errorMessage.append("Należy wybrać termin realizacji zadania!\n");
            animateFieldError(taskDeadlineField);
            isValid = false;
        } else if (taskDeadlineField.getValue().isBefore(LocalDate.now())) {
            errorMessage.append("Termin realizacji nie może być wcześniejszy niż dzisiejsza data!\n");
            animateFieldError(taskDeadlineField);
            isValid = false;
        }

        return isValid;
    }

    /**
     * Obsługuje akcję zapisu zadania do bazy danych.
     *
     * @param event zdarzenie kliknięcia przycisku
     */
    @FXML
    private void saveTask(ActionEvent event) {
        try {

            if (!validateInputData()) {
                return;
            }

            String title = taskTitleField.getText().trim();
            String description = taskDescriptionField.getText().trim();
            LocalDate deadline = taskDeadlineField.getValue();
            String choiceBox = employeeChoiceBox.getValue();
            String priority = priorityChoiceBox.getValue();

            try {
                int userId = Integer.parseInt(choiceBox.split(":")[0].trim());
                saveTaskToDb(title, description, userId, deadline, priority);

                handleClearTask(null);
            } catch (NumberFormatException e) {
              //  showAlert(Alert.AlertType.ERROR, "Błąd formatu", "Nieprawidłowy format identyfikatora pracownika.");
                animateFieldError(employeeChoiceBox);
            }

        } catch (SQLException e) {
           // showAlert(Alert.AlertType.ERROR, "Błąd bazy danych", "Wystąpił błąd podczas zapisywania zadania: " + e.getMessage());
        } catch (IOException e) {
           // showAlert(Alert.AlertType.ERROR, "Błąd I/O", "Wystąpił błąd wejścia/wyjścia: " + e.getMessage());
        } catch (Exception e) {
         //   showAlert(Alert.AlertType.ERROR, "Nieoczekiwany błąd", "Wystąpił nieoczekiwany błąd: " + e.getMessage());
        }
    }

    /**
     * Zapisuje zadanie do bazy danych.
     *
     * @param title       tytuł zadania
     * @param description opis zadania
     * @param user_id     ID przypisanego pracownika
     * @param deadline    termin realizacji
     * @throws SQLException w przypadku błędów SQL
     * @throws IOException  w przypadku błędów I/O
     */
    private void saveTaskToDb(String title, String description, int user_id, LocalDate deadline, String priority)
            throws SQLException, IOException {
        Connection con = null;
        PreparedStatement statement = null;

        try {
            con = DatabaseConnection.getConnection();

            String sql = "INSERT INTO tasks (title, description, user_id, status, deadline, priority) VALUES (?, ?, ?, 'Nie rozpoczete', ?, ?)";
            statement = con.prepareStatement(sql);

            statement.setString(1, title);
            statement.setString(2, description);
            statement.setInt(3, user_id);
            statement.setDate(4, Date.valueOf(deadline));
            statement.setString(5, priority);

            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Sukces");
                alert.setHeaderText(null);
                alert.setContentText("Zadanie zostało pomyślnie dodane.");
                alert.showAndWait();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Błąd");
                alert.setHeaderText(null);
                alert.setContentText("Nie udało się dodać zadania");
                alert.showAndWait();
            }

        } catch (SQLException e) {
            throw e;
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    System.err.println("Błąd podczas zamykania statement: " + e.getMessage());
                }
            }
        }
    }


    /**
     * Wyświetla alert z określonym typem, tytułem i treścią.
     *
     * @param type typ alertu
     * @param title tytuł alertu
     * @param content treść alertu
     */

    /**
     * Pobiera listę pracowników z bazy danych do wyświetlenia w ChoiceBox.
     *
     * @throws SQLException w przypadku błędów zapytania SQL
     */
    public void fetchInfo() throws SQLException {
        Connection con = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            con = DatabaseConnection.getConnection();
            int idRole = UserController.getLoggedInUser().getUserId();

            String sql = "SELECT * FROM users u JOIN departments d ON u.department_id = d.id WHERE u.role_id = 4 AND d.manager_id = ? ORDER BY u.id ASC";
            statement = con.prepareStatement(sql);

            statement.setInt(1, idRole);
            resultSet = statement.executeQuery();

            ObservableList<String> users = FXCollections.observableArrayList();
            while (resultSet.next()) {
                users.add(resultSet.getInt("id") + ": " + resultSet.getString("first_name") + " " + resultSet.getString("last_name"));
            }

            if (users.isEmpty()) {
               // showAlert(Alert.AlertType.WARNING, "Brak pracowników", "Nie znaleziono pracowników do przypisania zadania.");
            }

            employeeChoiceBox.setItems(users);
        } catch (SQLException e) {
            throw e;
        } finally {
            // Zamknięcie zasobów
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    System.err.println("Błąd podczas zamykania resultSet: " + e.getMessage());
                }
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    System.err.println("Błąd podczas zamykania statement: " + e.getMessage());
                }
            }
        }
    }

}


