package com.example.obiwankenobi;

import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.example.ReportGenerator;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

/**
 * Kontroler raportu magazynowego.
 * Odpowiada za zarządzanie filtrami raportowania oraz generowanie raportów magazynu.
 */
public class WarehouseReportController implements Initializable {

    /** Przycisk zamykający okno raportu. */
    public Button closeButton;

    /** Pole wyboru kategorii magazynowej. */
    public ChoiceBox<String> categoryChoiceBox;

    /** Pole wyboru menedżera magazynu. */
    public ChoiceBox<String> menagerChoiceBox;

    /** Pola tekstowe do określenia ilości produktów. */
    public TextField minQuantityField;
    public TextField maxQuantityField;

    /** Przycisk generujący raport. */
    public Button generateButton;

    /** Przycisk czyszczący formularz. */
    public Button clearButton;

    /**
     * Inicjalizuje kontroler, ładuje dostępne kategorie magazynowe oraz menedżerów.
     * @param url URL dla pliku FXML
     * @param resourceBundle Zasoby lokalizacyjne
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadDeps();
        loadMenagers();
    }

    /**
     * Ładuje dostępne działy magazynowe z bazy danych do pola wyboru.
     */
    private void loadDeps() {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String sql = "SELECT name FROM departments ORDER BY name";
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            categoryChoiceBox.getItems().add("wszystkie");

            while (rs.next()) {
                String departmentName = rs.getString("name");
                categoryChoiceBox.getItems().add(departmentName);

            }

            categoryChoiceBox.setValue("wszystkie");



        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Ładuje listę menedżerów magazynu z bazy danych do pola wyboru.
     */
    private void loadMenagers(){
        try (Connection connection = DatabaseConnection.getConnection()){
            String sql = "SELECT first_name, last_name FROM users WHERE role_id = 3";
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet rs= stmt.executeQuery();

            menagerChoiceBox.getItems().add("wszyscy");

            while (rs.next()){
                String first = rs.getString("first_name");
                String last = rs.getString("last_name");
                String fullName = first + " " + last;
                menagerChoiceBox.getItems().add(fullName);

            }

            menagerChoiceBox.setValue("wszyscy");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Zamyka okno raportu.
     * @param event Zdarzenie zamknięcia okna
     */
    @FXML
    void reportClose(ActionEvent event) {
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.close();
    }

    /**
     * Czyści wszystkie pola formularza raportu.
     * @param event Zdarzenie kliknięcia przycisku czyszczenia
     */
    @FXML
    void handleClearTask(ActionEvent event) {
        categoryChoiceBox.setValue("wszystkie");
        minQuantityField.clear();
        maxQuantityField.clear();
        menagerChoiceBox.setValue("wszyscy");

    }

    /**
     * Generuje raport magazynowy na podstawie wybranych filtrów.
     * @throws SQLException W przypadku błędów bazy danych
     */
    @FXML
    public void generateWarehouseButton() throws SQLException {
        String selectedDepartment = categoryChoiceBox.getValue();
        String selectedManager = menagerChoiceBox.getValue();
        String minQtyText = minQuantityField.getText();
        String maxQtyText = maxQuantityField.getText();

        Integer minQty = null;
        Integer maxQty = null;

        if ("wszyscy".equals(selectedDepartment)) {
            selectedDepartment = null;
        }

        if ("wszyscy".equals(selectedManager)) {
            selectedManager = null;
        }

        if (minQtyText != null && !minQtyText.isEmpty()) {
            try {
                minQty = Integer.parseInt(minQtyText);
                if (minQty < 0) {
                    shakeNode(minQuantityField);
                    System.err.println("Minimalna ilość nie może być mniejsza niż 0.");
                    return;
                }
            } catch (NumberFormatException e) {
                shakeNode(minQuantityField);
                System.err.println("Nieprawidłowa minimalna ilość.");
                return;
            }
        }

        if (maxQtyText != null && !maxQtyText.isEmpty()) {
            try {
                maxQty = Integer.parseInt(maxQtyText);
                if (maxQty < 0) {
                    shakeNode(maxQuantityField);
                    System.err.println("Maksymalna ilość nie może być mniejsza niż 0.");
                    return;
                }
            } catch (NumberFormatException e) {
                shakeNode(maxQuantityField);
                System.err.println("Nieprawidłowa maksymalna ilość.");
                return;
            }
        }

        if (minQty != null && maxQty != null && minQty > maxQty) {
            shakeNode(minQuantityField);
            shakeNode(maxQuantityField);
            System.err.println("Minimalna ilość nie może być większa niż maksymalna.");
            return;
        }

        try {
            ReportGenerator.generateWarehouse(selectedDepartment, selectedManager, minQty, maxQty);

            javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
            alert.setTitle("Raport wygenerowany");
            alert.setHeaderText(null);
            alert.setContentText("Raport magazynowy zostal pomyslnie wygenerowany");
            alert.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();

            javafx.scene.control.Alert errorAlert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
            errorAlert.setTitle("Blad generowania raportu");
            errorAlert.setHeaderText("Nie udalo sie wygenerowac raportu");
            errorAlert.setContentText("Wystapil blad: " + e.getMessage());
            errorAlert.showAndWait();
        }
    }

    /**
     * Tworzy efekt drżenia dla podanego węzła, wizualnie wyróżniając go.
     * @param node Węzeł do potrząśnięcia
     */
    private void shakeNode(Node node) {
        TranslateTransition tt = new TranslateTransition(Duration.millis(50), node);
        node.setStyle("-fx-border-color: red; -fx-border-width: 2;");
        tt.setFromX(0);
        tt.setByX(10);
        tt.setCycleCount(20);
        tt.setAutoReverse(true);
        tt.play();
    }
}
