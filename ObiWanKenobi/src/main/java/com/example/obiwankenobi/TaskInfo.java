package com.example.obiwankenobi;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

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

            // Zapytanie do bazy danych, aby pobrać szczegóły zadania
            String query = "SELECT id, title, deadline, status, description FROM tasks WHERE id = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, Id);
            ResultSet resultSet = preparedStatement.executeQuery();

            // Jeśli dane zadania zostały znalezione, ustawiamy je w etykietach
            if (resultSet.next()) {
                String title = resultSet.getString("title");
                String deadline = resultSet.getString("deadline");
                String status = resultSet.getString("status");
                String description = resultSet.getString("description");

                // Ustawianie tekstu w etykietach
                taskTitle.setText(title);
                taskDeadline.setText(deadline != null ? deadline : "Brak terminu");
                taskStatus.setText(status);
                taskDescription.setText(description != null ? description : "Brak opisu");
            }

            // Zamknięcie połączenia i wyników
            resultSet.close();
            preparedStatement.close();
            connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
