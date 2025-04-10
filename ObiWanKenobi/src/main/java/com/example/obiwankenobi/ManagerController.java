package com.example.obiwankenobi;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.sql.*;

/**
 * Kontroler panelu kierownika (Managera).
 * Obsługuje akcje związane z dodawaniem zadań oraz wylogowaniem.
 */
public class ManagerController {

    @FXML
    private Button StatusWareHauseBtn;

    @FXML
    private Button addTaskBtn;

    @FXML
    private Button logoutBtn;

    @FXML
    private VBox taskListContainer;

    @FXML
    public void initialize() {
        refreshTaskList();
    }

    /**
     * Obsługuje akcję dodawania nowego zadania.
     * Otwiera nowe okno dialogowe z formularzem dodawania zadania.
     *
     * @param event kliknięcie przycisku "Dodaj zadanie"
     */
    @FXML
    void addTask(ActionEvent event) {
        try {
            // Ładowanie widoku formularza dodawania zadania
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/obiwankenobi/views/addTask.fxml"));
            Parent root = loader.load();

            // Tworzenie nowego okna
            Stage addTaskStage = new Stage();
            addTaskStage.setTitle("Dodaj nowe zadanie");
            addTaskStage.initModality(Modality.WINDOW_MODAL);
            addTaskStage.initOwner(((Node) event.getSource()).getScene().getWindow());

            // Ustawianie stylu okna na transparentny
            addTaskStage.initStyle(StageStyle.TRANSPARENT);

            // Ustawianie sceny i konfiguracja przezroczystości
            Scene scene = new Scene(root);
            scene.setFill(Color.TRANSPARENT);
            addTaskStage.setScene(scene);
            addTaskStage.showAndWait();

            // Po zamknięciu okna można odświeżyć listę zadań (jeśli istnieje)
            refreshTaskList();

        } catch (IOException e) {
            e.printStackTrace();
            showErrorAlert("Nie udało się otworzyć formularza dodawania zadania.");
        }
    }


    /**
     * Odświeża listę zadań po dodaniu nowego zadania.
     * Ta metoda powinna być wywołana po dodaniu nowego zadania,
     * aby zaktualizować widok z aktualną listą zadań.
     */
    private void refreshTaskList() {
        taskListContainer.getChildren().clear();

        String query = "SELECT t.id, t.title, t.description, t.status, t.deadline, t.user_id, t.created_at\n" +
                "FROM tasks t\n" +
                "JOIN users u ON t.user_id = u.id\n" +
                "JOIN departments d ON u.department_id = d.id\n" +
                "WHERE d.manager_id = ?\n";

        // Pobranie ID zalogowanego użytkownika
        int loggedInUserId = ManagerController.getLoggedInUserId();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, loggedInUserId);
            ResultSet rs = stmt.executeQuery();

            int taskNumber = 1;

            while (rs.next()) {
                String title = rs.getString("title");
                String status = rs.getString("status");
                Timestamp deadline = rs.getTimestamp("deadline");
                int id = rs.getInt("id");
                String description = rs.getString("description");
                int user_id = rs.getInt("user_id");

                HBox taskBox = new HBox();
                taskBox.setSpacing(10);
                taskBox.setStyle("-fx-background-color: #91ee91; -fx-padding: 10;");
                taskBox.setPrefHeight(78.0);
                taskBox.setPrefWidth(619.0);
                taskBox.setAlignment(Pos.CENTER_LEFT);

                VBox textBox = new VBox(5);  // Ustawienie odstępu między etykietami
                textBox.setPrefHeight(78.0);
                textBox.setPrefWidth(298.0);

                Label taskNumberLabel = new Label("Zadanie " + taskNumber++);
                taskNumberLabel.getStyleClass().add("textSmallDark");
                taskNumberLabel.setStyle("-fx-font-weight: bold;");

                // Tytuł zadania
                Label titleLabel = new Label(title);
                titleLabel.getStyleClass().add("textSmallDark");
                titleLabel.setPrefSize(254, 38);
                titleLabel.setWrapText(true);
                titleLabel.setMaxWidth(250);

                // Data deadline
                String deadlineText = (deadline != null ? deadline.toLocalDateTime().toLocalDate().toString() : "Brak terminu");
                Label deadlineLabel = new Label(deadlineText);
                deadlineLabel.getStyleClass().add("textSmallDark");
                deadlineLabel.setPrefSize(254, 38);
                deadlineLabel.setWrapText(true);
                deadlineLabel.setMaxWidth(250);

                textBox.getChildren().addAll(taskNumberLabel, titleLabel, deadlineLabel);

                // Status zadania
                Label statusLabel = new Label(status);
                statusLabel.getStyleClass().add("textSmallDark");
                statusLabel.setPrefHeight(38);
                statusLabel.setMaxWidth(150);

                // Przyciski
                Button editBtn = new Button("Edytuj");
                editBtn.setStyle("-fx-background-color: #FFC849; -fx-font-size: 12px;");
                editBtn.setPrefSize(60, 30);

                // edycja zadania
                editBtn.setOnAction(e -> {
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/obiwankenobi/views/editTask.fxml"));
                        Parent root = loader.load();

                        //kontroler edycji
                        EditTaskController controller = loader.getController();
                        controller.setTaskData(
                                id,
                                title,
                                description,
                                deadline.toLocalDateTime().toLocalDate(),
                                user_id,
                                getUserNameById(user_id)
                        );

                        Stage editStage = new Stage();
                        editStage.setTitle("Edytuj zadanie");
                        editStage.initModality(Modality.WINDOW_MODAL);
                        editStage.initOwner(((Node) e.getSource()).getScene().getWindow());
                        Scene scene = new Scene(root);
                        scene.setFill(Color.TRANSPARENT);
                        editStage.setScene(scene);
                        editStage.showAndWait();

                        refreshTaskList();

                    } catch (Exception ex) {
                        ex.printStackTrace();
                        showErrorAlert("Nie udało się otworzyć formularza edycji zadania.");
                    }
                });


                Button confirmBtn = new Button("Zatwierdź");
                confirmBtn.setStyle("-fx-background-color: #0C5A18; -fx-font-size: 12px;");
                confirmBtn.setPrefSize(80, 30);
                if ("Zakończone".equalsIgnoreCase(status)) {
                    confirmBtn.setDisable(true);
                }

                Button deleteBtn = new Button("Usuń");
                deleteBtn.setStyle("-fx-background-color: #7D0A0A; -fx-font-size: 12px;");
                deleteBtn.setPrefSize(60, 30);

                // Opakowanie przycisków w VBox i ustawienie na prawo
                VBox buttonBox = new VBox(5);
                buttonBox.setAlignment(Pos.CENTER_RIGHT);
                buttonBox.getChildren().addAll(editBtn, confirmBtn, deleteBtn);

                Region spacer = new Region(); // by wypchnąć buttony na prawo
                HBox.setHgrow(spacer, Priority.ALWAYS);

                // Dodanie do głównego kontenera
                taskBox.getChildren().addAll(textBox, statusLabel, spacer, buttonBox);
                taskListContainer.getChildren().add(taskBox);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            showErrorAlert("Nie udało się załadować listy zadań.");
        }
    }



    private String getUserNameById(int userId) {
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement("SELECT first_name, last_name FROM users WHERE id = ?")) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("first_name") + " " + rs.getString("last_name");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "";
    }


    /**
     * Zwraca ID zalogowanego użytkownika.
     */
    public static int getLoggedInUserId() {
        // Zwróć ID zalogowanego użytkownika
        return UserController.getLoggedInUser().getUserId(); // Dostęp do kontrolera użytkownika.
    }

    /**
     * Wyświetla stan magazynu.
     * (Aktualnie metoda niezaimplementowana.)
     *
     * @param event kliknięcie przycisku "Stan magazynu"
     */
    @FXML
    void StatusWareHause(ActionEvent event) {
        // TODO: implementacja wyświetlania stanu magazynu

    }

    /**
     * Obsługuje proces wylogowania użytkownika.
     * Zamyka bieżące okno i uruchamia ekran startowy.
     *
     * @param event kliknięcie przycisku "Wyloguj się"
     */
    @FXML
    private void logout(ActionEvent event) {
        try {
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close();

            new Launcher().start(new Stage());

        } catch (Exception e) {
            e.printStackTrace();
            showErrorAlert("Wystąpił problem podczas wylogowywania.");
        }
    }

    /**
     * Wyświetla alert z komunikatem o błędzie.
     *
     * @param message treść komunikatu o błędzie
     */
    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Błąd");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}