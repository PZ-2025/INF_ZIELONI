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