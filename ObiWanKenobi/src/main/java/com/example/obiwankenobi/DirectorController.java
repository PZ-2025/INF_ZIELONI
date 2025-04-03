package com.example.obiwankenobi;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
    public void initialize() {
        // Mapowanie kolumn na pola klasy User
        departmentName.setCellValueFactory(new PropertyValueFactory<>("depName"));
        itemName.setCellValueFactory(new PropertyValueFactory<>("itemName"));
        warehouseAmount.setCellValueFactory(new PropertyValueFactory<>("itemAmount"));

        loadData();
    }

    public static ObservableList<Warehouse> getItems() {
        ObservableList<Warehouse> items = FXCollections.observableArrayList();
        String query = "SELECT " +
                "d.name AS department_name," +
                " w.name AS warehouse_name," +
                " i.name AS item_name," +
                " i.quantity " +
                "FROM" +
                " warehouses w " +
                "JOIN" +
                " departments d ON w.department_id = d.id " +
                "JOIN" +
                " items i ON w.id = i.warehouse_id;";

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
            e.printStackTrace();
        }

        return items;
    }

    private void loadData() {
        ObservableList<Warehouse> items = getItems();
        tableView.setItems(items    );
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

    @FXML
    void raports(ActionEvent event) {

    }

}