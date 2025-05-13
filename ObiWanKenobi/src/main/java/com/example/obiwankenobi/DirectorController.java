package com.example.obiwankenobi;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * Kontroler odpowiedzialny za interakcję dyrektora z GUI.
 * Wyświetla dane magazynowe przypisane do działów oraz umożliwia wylogowanie
 * i przejście do widoku raportów.
 */
public class DirectorController {

    @FXML
    private Button logoutBtn;

    @FXML
    private Button raportBtn;

    @FXML
    private TableView<Warehouse> tableView;

    @FXML
    private TableColumn<Warehouse, String> departmentName;

    @FXML
    private TableColumn<Warehouse, String> itemName;

    @FXML
    private TableColumn<Warehouse, Integer> warehouseAmount;

    @FXML
    private Label helloName;

    /**
     * Zwraca identyfikator aktualnie zalogowanego użytkownika.
     *
     * @return identyfikator użytkownika
     */
    public static int getLoggedInUserId() {
        // Zwróć ID zalogowanego użytkownika
        return UserController.getLoggedInUser().getUserId(); // Dostęp do kontrolera użytkownika.
    }

    private static User loggedInUser;


    /**
     * Inicjalizuje kontroler.
     * Mapuje kolumny tabeli na odpowiednie pola modelu oraz ładuje dane
     * z bazy danych i informacje o użytkowniku.
     */
    @FXML
    public void initialize() {
        departmentName.setCellValueFactory(new PropertyValueFactory<>("depName"));
        itemName.setCellValueFactory(new PropertyValueFactory<>("itemName"));
        warehouseAmount.setCellValueFactory(new PropertyValueFactory<>("itemAmount"));
        loadUserInfo();

        loadData();
    }

    /**
     * Pobiera dane z bazy danych i zwraca listę obiektów Warehouse.
     *
     * @return lista elementów magazynowych
     */
    public static ObservableList<Warehouse> getItems() {
        ObservableList<Warehouse> items = FXCollections.observableArrayList();
        String query = "SELECT " +
                "d.name AS department_name," +
                " w.name AS warehouse_name," +
                " i.name AS item_name," +
                " i.quantity " +
                "FROM warehouses w " +
                "JOIN departments d ON w.department_id = d.id " +
                "JOIN items i ON w.id = i.warehouse_id;";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                items.add(new Warehouse(
                        rs.getString("department_name"),
                        rs.getString("item_name"),
                        rs.getInt("quantity")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Możesz to później zastąpić loggerem
        }

        return items;
    }

    /**
     * Ładuje dane do widoku tabeli z bazy danych.
     */
    private void loadData() {
        ObservableList<Warehouse> items = getItems();
        tableView.setItems(items);
    }

    /**
     * Obsługuje akcję wylogowania użytkownika.
     * Zamyka bieżące okno i uruchamia ekran startowy.
     *
     * @param event zdarzenie kliknięcia przycisku
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

    /**
     * Obsługuje przejście do widoku raportów.
     * Otwiera nowe okno z widokiem raportów (reports.fxml).
     *
     * @param event zdarzenie kliknięcia przycisku
     * @throws IOException jeśli wystąpi problem z załadowaniem pliku FXML
     */
    @FXML
    void raports(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/obiwankenobi/views/reports.fxml"));
        Parent root = loader.load();

        Stage reports = new Stage();
        reports.initModality(Modality.WINDOW_MODAL);
        reports.initOwner(((Node) event.getSource()).getScene().getWindow());

        reports.initStyle(StageStyle.TRANSPARENT);

        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);
        reports.setScene(scene);
        reports.showAndWait();
    }

    /**
     * Wczytuje imię aktualnie zalogowanego użytkownika i wyświetla je w etykiecie powitalnej.
     */
    private void loadUserInfo() {
        int loggedInUserId = DirectorController.getLoggedInUserId();
        String query = "SELECT first_name FROM users WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();

             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, loggedInUserId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String firstName = rs.getString("first_name");
                helloName.setText(firstName);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}


