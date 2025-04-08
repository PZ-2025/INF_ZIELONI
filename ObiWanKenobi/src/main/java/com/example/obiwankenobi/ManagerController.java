package com.example.obiwankenobi;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

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
        // TODO: Zaimplementować odświeżanie listy zadań
        // Można tu dodać kod do pobrania zaktualizowanych danych z bazy
        // i odświeżenia tabeli/listy w interfejsie
        /*
    taskListContainer.getChildren().clear();

    String query = "SELECT title, date, status FROM tasks";

    try (Connection conn = DatabaseConnection.getConnection();
         Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery(query)) {

        while (rs.next()) {
            String title = rs.getString("title");
            Date date = rs.getDate("date");
            String status = rs.getString("status");

            HBox taskBox = new HBox();
            taskBox.setSpacing(10);
            taskBox.setStyle("-fx-background-color: #91ee91;");
            taskBox.setPrefHeight(78.0);
            taskBox.setPrefWidth(619.0);

            VBox textBox = new VBox();
            textBox.setPrefHeight(78.0);
            textBox.setPrefWidth(298.0);

            Label titleLabel = new Label(title + "  " + date.toString());
            titleLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #0C3105;");
            Label statusLabel = new Label(status);
            statusLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #0C3105;");

            textBox.getChildren().addAll(titleLabel, statusLabel);

            Button editBtn = new Button("Edytuj");
            editBtn.setStyle("-fx-background-color: #FFC849;");
            editBtn.setPrefSize(86, 44);

            Button confirmBtn = new Button("Zatwierdź");
            confirmBtn.setStyle("-fx-background-color: #0C5A18;");
            confirmBtn.setPrefSize(120, 44);

            Button deleteBtn = new Button("Usuń");
            deleteBtn.setStyle("-fx-background-color: #7D0A0A;");
            deleteBtn.setPrefSize(77, 44);

            taskBox.getChildren().addAll(textBox, editBtn, confirmBtn, deleteBtn);
            taskListContainer.getChildren().add(taskBox);
        }

    } catch (SQLException e) {
        e.printStackTrace();
        showErrorAlert("Nie udało się załadować listy zadań.");
    }
}
         */
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
        /*
    StringBuilder message = new StringBuilder();
    String query = "SELECT item_name, quantity FROM warehouse";

    try (Connection conn = DatabaseConnection.getConnection();
         Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery(query)) {

        while (rs.next()) {
            String item = rs.getString("item_name");
            int qty = rs.getInt("quantity");
            message.append(item).append(": ").append(qty).append(" szt.\n");
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Stan Magazynu");
        alert.setHeaderText("Aktualny stan magazynowy:");
        alert.setContentText(message.toString());
        alert.showAndWait();

    } catch (SQLException e) {
        e.printStackTrace();
        showErrorAlert("Nie udało się pobrać stanu magazynu.");
    }
}
         */
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