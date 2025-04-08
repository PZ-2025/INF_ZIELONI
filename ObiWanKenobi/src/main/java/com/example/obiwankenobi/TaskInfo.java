package com.example.obiwankenobi;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ChoiceBox;
import javafx.event.ActionEvent;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Klasa odpowiedzialna za wyświetlanie informacji o zadaniu w GUI.
 * Wczytuje dane zadania z bazy danych i wyświetla je na odpowiednich etykietach.
 */
public class TaskInfo {

    /** Identyfikator zadania */
    private int Id;

    /** Etykieta wyświetlająca termin zadania */
    @FXML
    private Label taskDeadline;

    /** Etykieta wyświetlająca opis zadania */
    @FXML
    private Label taskDescription;

    /** Etykieta wyświetlająca status zadania */
    @FXML
    private Label taskStatus;

    /** Etykieta wyświetlająca tytuł zadania */
    @FXML
    private Label taskTitle;

    @FXML
    private ChoiceBox<String> taskStatusChoiceBox;
    private UserController userController;

    public void setUserController(UserController userController) {
        this.userController = userController;
    }

    /**
     * Ustawia identyfikator zadania i wczytuje jego dane z bazy.
     *
     * @param Id identyfikator zadania
     */
    public void setTaskId(int Id) {
        this.Id = Id;
        loadDataFromDatabase();
    }

    /**
     * Wczytuje dane zadania z bazy danych na podstawie jego identyfikatora.
     * Ustawia odpowiednie etykiety w GUI.
     */
    private void loadDataFromDatabase() {
        try {
            Connection connection = DatabaseConnection.getConnection();

            String query = "SELECT id, title, deadline, status, description FROM tasks WHERE id = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, Id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String title = resultSet.getString("title");
                String deadline = resultSet.getString("deadline");
                String status = resultSet.getString("status");
                String description = resultSet.getString("description");

                taskTitle.setText(title);
                taskDeadline.setText(deadline != null ? deadline : "Brak terminu");
                taskStatusChoiceBox.setValue(status);
                taskDescription.setText(description != null ? description : "Brak opisu");
            }

            resultSet.close();
            preparedStatement.close();
            connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void saveTaskStatus(ActionEvent event) {

        String selectedStatus = taskStatusChoiceBox.getValue();
        if (selectedStatus == null || selectedStatus.isEmpty()) {
            return;
        }

        try {
            Connection connection = DatabaseConnection.getConnection();
            String updateQuery = "UPDATE tasks SET status = ? WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);
            preparedStatement.setString(1, selectedStatus);
            preparedStatement.setInt(2, Id);
            preparedStatement.executeUpdate();

            preparedStatement.close();
            connection.close();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Sukces");
            alert.setHeaderText(null);
            alert.setContentText("Status zadania został zaktualizowany.");
            alert.showAndWait();

            Stage stage = (Stage) taskStatusChoiceBox.getScene().getWindow();
            stage.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
