package com.example.obiwankenobi;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Stage;

/**
 * Kontroler panelu kierownika (Managera).
 * Obsługuje akcje związane z dodawaniem zadań oraz wylogowaniem.
 */
public class ManagerController {

    @FXML
    private Button addTaskBtn;

    @FXML
    private Button logoutBtn;

    /**
     * Obsługuje akcję dodawania nowego zadania.
     * (Aktualnie metoda niezaimplementowana.)
     *
     * @param event kliknięcie przycisku "Dodaj zadanie"
     */
    @FXML
    void addTask(ActionEvent event) {
        // TODO: implementacja dodawania zadania
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
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Błąd");
            alert.setHeaderText(null);
            alert.setContentText("Wystąpił problem podczas wylogowywania.");
            alert.showAndWait();
        }
    }

}
