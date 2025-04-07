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
    private Button closeButton;

    @FXML
    private ChoiceBox<String> employeeChoiceBox;

    @FXML
    private DatePicker taskDeadlineField;

    @FXML
    private TextArea taskDescriptionField;

    @FXML
    private TextField taskTitleField;

    // Lista przechowująca pracowników
    private ObservableList<String> employeeList;

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
        try {
            fetchInfo();
        } catch (SQLException e) {
            throw new RuntimeException(e);
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


    @FXML
    private void saveTask (ActionEvent event) throws SQLException, IOException {
       String title = taskTitleField.getText();
       String description = taskDescriptionField.getText();

       LocalDate deadline = taskDeadlineField.getValue();
       String choiceBox = employeeChoiceBox.getValue();

       int userId = Integer.parseInt(choiceBox.split(":")[0].trim());

       saveTaskToDb(title, description, userId, deadline);

    }
    private void saveTaskToDb(String title, String description, int user_id, LocalDate deadline)
            throws SQLException, IOException {
        Connection con = DatabaseConnection.getConnection();

        String sql = "INSERT INTO tasks (title, description, user_id, status, deadline) VALUES (?, ?, ?, 'pending', ?)";
        PreparedStatement statement = con.prepareStatement(sql);

        statement.setString(1, title);
        statement.setString(2, description);
        statement.setInt(3, user_id);
        statement.setDate(4, Date.valueOf(deadline));


        int rowsInserted = statement.executeUpdate();
        if (rowsInserted > 0) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Sukces");
            alert.setHeaderText(null);
            alert.setContentText("Użytkownik został dodany.");
            alert.showAndWait();
            

        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Błąd");
            alert.setHeaderText(null);
            alert.setContentText("Nie udało się dodać zadania");
            alert.showAndWait();
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



    public void fetchInfo() throws SQLException {
        Connection con = DatabaseConnection.getConnection();
        int idRole = UserController.getLoggedInUser().getUserId();


        String sql = "SELECT * FROM users u JOIN departments d ON u.department_id = d.id WHERE u.role_id = 4 AND d.manager_id = ? ORDER BY u.id ASC";
        PreparedStatement statement = con.prepareStatement(sql);

        statement.setInt(1, idRole);
        ResultSet resultSet = statement.executeQuery();

        ObservableList<String> users = FXCollections.observableArrayList();
        while (resultSet.next()) {
            users.add(resultSet.getInt("id") + ": " + resultSet.getString("first_name") + " " + resultSet.getString("last_name"));
        }
        employeeChoiceBox.setItems(users);
    }

}