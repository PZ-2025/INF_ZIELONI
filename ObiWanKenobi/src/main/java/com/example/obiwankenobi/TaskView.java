package com.example.obiwankenobi;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.sql.Date;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Klasa odpowiedzialna za wyświetlanie szczegółów zadania w GUI.
 * Wczytuje dane zadania z bazy danych oraz umożliwia przejście do pełnego widoku zadania.
 */
public class TaskView {

    /** Etykieta wyświetlająca identyfikator zadania */
    @FXML
    private Label taskId;

    /** Etykieta wyświetlająca termin zadania */
    @FXML
    private Label taskDate;

    /** Etykieta wyświetlająca status zadania */
    @FXML
    private Label taskStatus;

    /** Identyfikator zadania */
    private int Id;

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

            String query = "SELECT id, deadline, title, status FROM tasks WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, Id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                taskId.setText(String.valueOf(resultSet.getInt("id")));

                Date deadline = resultSet.getDate("deadline");

                if (deadline != null) {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                    String formattedDate = dateFormat.format(deadline);
                    taskDate.setText(formattedDate);
                } else {
                    taskDate.setText("Brak terminu");
                }

                taskStatus.setText(resultSet.getString("status"));
            }

            resultSet.close();
            preparedStatement.close();
            connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Otwiera okno z pełnymi szczegółami zadania.
     * Ładuje widok zadania i ustawia odpowiednie dane.
     */
    @FXML
    private void more() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/obiwankenobi/views/task.fxml"));
            Parent parent = loader.load();

            TaskInfo taskInfoController = loader.getController();
            taskInfoController.setTaskId(Id);

            Stage stage = new Stage();
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.setScene(new Scene(parent));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
