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

/**
 * Kontroler dla widoku użytkownika.
 * Odpowiada za wyświetlanie informacji o użytkowniku oraz zarządzanie zadaniami przypisanymi do użytkownika.
 */
public class UserController {

    /** Etykieta wyświetlająca nazwisko użytkownika */
    @FXML
    private Label userName;

    /** Etykieta wyświetlająca ID użytkownika */
    @FXML
    private Label userId;

    /** Siatka z zadaniami przypisanymi do użytkownika */
    @FXML
    private GridPane taskGrid;

    /** Obiekt zalogowanego użytkownika */
    private static User loggedInUser;

    /**
     * Ustawia zalogowanego użytkownika.
     *
     * @param user obiekt użytkownika, który ma zostać ustawiony jako zalogowany
     */
    public static void setLoggedInUser(User user) {
        loggedInUser = user;
    }

    /**
     * Zwraca zalogowanego użytkownika.
     *
     * @return obiekt zalogowanego użytkownika
     */
    public static User getLoggedInUser() {
        return loggedInUser;
    }

    /**
     * Inicjalizuje dane użytkownika oraz ładuje zadania przypisane do użytkownika.
     *
     * @throws SQLException jeśli wystąpi błąd przy połączeniu z bazą danych
     */
    @FXML
    private void initialize() throws SQLException {
        loadUserInfo();
        loadContent();
    }

    /**
     * Ładuje dane użytkownika (imię, nazwisko, ID).
     * Sprawdza, czy użytkownik jest zalogowany i wyświetla odpowiednie dane w widoku.
     */
    private void loadUserInfo() {
        if (loggedInUser != null) {
            int loggedInUserId = loggedInUser.getUserId();

            if (loggedInUserId > 0) {
                userId.setText("" + loggedInUserId);

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

    /**
     * Obsługuje akcję wylogowania użytkownika.
     * Zamyka bieżące okno i otwiera okno logowania.
     *
     * @param event wydarzenie kliknięcia przycisku
     */
    @FXML
    private void logout(ActionEvent event) {
        try {
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close();

            new Launcher().start(new Stage());

        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Błąd");
            alert.setHeaderText(null);
            alert.setContentText("Wystąpił problem");
            alert.showAndWait();
        }
    }

    @FXML
    private void refreshTasks(ActionEvent event) {
        loadContent();
    }

    /**
     * Ładuje zadania przypisane do zalogowanego użytkownika i wyświetla je w widoku.
     * Dla każdego zadania ładowany jest osobny widok.
     */
    private void loadContent() {
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
            String sql = "SELECT * FROM tasks WHERE user_id = ?";
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
