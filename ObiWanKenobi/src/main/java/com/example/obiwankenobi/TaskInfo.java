package com.example.obiwankenobi;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TaskInfo {
    private int Id;
    @FXML
    private Label taskDeadline;
    @FXML
    private Label taskDescription;
    @FXML
    private Label taskStatus;
    @FXML
    private Label taskTitle;

    public void setTaskId(int Id) {
        this.Id = Id;
        loadDataFromDatabase();
    }

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
                taskStatus.setText(status);
                taskDescription.setText(description != null ? description : "Brak opisu");
            }

            resultSet.close();
            preparedStatement.close();
            connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
