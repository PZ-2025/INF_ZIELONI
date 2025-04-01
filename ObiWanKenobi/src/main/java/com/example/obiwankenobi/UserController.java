package com.example.obiwankenobi;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import java.io.IOException;
import java.sql.*;
import javafx.scene.control.Label;

public class UserController {

    @FXML
    private Label userName;
    @FXML
    private Label userId;
    @FXML
    private GridPane taskGrid;

    // Dodaj obiekt zalogowanego użytkownika
    private static User loggedInUser;

    // Metoda do ustawienia zalogowanego użytkownika
    public static void setLoggedInUser(User user) {
        loggedInUser = user;
    }

    // Metoda do pobrania zalogowanego użytkownika
    public static User getLoggedInUser() {
        return loggedInUser;
    }

    @FXML
    private void initialize() throws SQLException {
        loadUserInfo();
        loadContent();
    }

    private void loadUserInfo() {
        // Sprawdź czy mamy zalogowanego użytkownika
        if (loggedInUser != null) {
            int loggedInUserId = loggedInUser.getUserId();
            String loggedInUserEmail = loggedInUser.getEmail();

            if (loggedInUserId > 0) {
                userId.setText("ID:" + loggedInUserId);

                try (Connection connection = DatabaseConnection.getConnection();
                     PreparedStatement statement = connection.prepareStatement(
                             "SELECT first_name, last_name FROM users WHERE id = ?")) {

                    statement.setInt(1, loggedInUserId);
                    ResultSet resultSet = statement.executeQuery();

                    if (resultSet.next()) {
                        String firstName = resultSet.getString("first_name");
                        String lastName = resultSet.getString("last_name");

                        String shortLastName = lastName.substring(0, 1) + ".";

                        userName.setText(firstName + " " + shortLastName);
                    }

                } catch (SQLException e) {
                    e.printStackTrace();
                    userName.setText("Nieznany użytkownik");
                }
            } else {
                userId.setText("ID: Nieznane");
                userName.setText("Nieznany użytkownik");
            }
        } else {
            userId.setText("ID: Nieznane");
            userName.setText("Nieznany użytkownik");
        }
    }

    @FXML
    private void logout(ActionEvent event) {
        try {
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close();

            new Launcher().start(new Stage());

        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("blad");
            alert.setHeaderText(null);
            alert.setContentText("wystapil problem");
            alert.showAndWait();
        }
    }

    private void loadContent() {
        // Sprawdź czy mamy zalogowanego użytkownika
        if (loggedInUser == null) {
            return;
        }

        int loggedInUserId = loggedInUser.getUserId();
        if (loggedInUserId <= 0) {
            return;
        }

        int row = 1;
        try {
            taskGrid.getChildren().clear();

            Connection connection = DatabaseConnection.getConnection();
            String sql = "SELECT * FROM tasks where user_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, loggedInUserId);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/com/example/obiwankenobi/views/taskMain.fxml"));
                AnchorPane taskMain = fxmlLoader.load();

                TaskView taskViewController = fxmlLoader.getController();
                int taskId = resultSet.getInt("id");
                taskViewController.setTaskId(taskId);

                taskGrid.add(taskMain, 0, row++);
                GridPane.setMargin(taskMain, new Insets(5));
            }
            resultSet.close();
            preparedStatement.close();
            connection.close();

        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }
}