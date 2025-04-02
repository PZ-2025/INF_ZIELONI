package com.example.obiwankenobi;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.awt.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class AdminController {


    @FXML
    private TableView<User> tableView;
    @FXML
    private TableColumn<User, String> departmentColumn;
    @FXML
    private TableColumn<User, String> emailColumn;
    @FXML
    private TableColumn<User, String> nameColumn;
    @FXML
    private TableColumn<User, String> passColumn;
    @FXML
    private TableColumn<User, String> roleColumn;
    @FXML
    private TableColumn<User, String> scndNameColumn;

    @FXML
    public void initialize() {
        // Mapowanie kolumn na pola klasy User
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        scndNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        passColumn.setCellValueFactory(new PropertyValueFactory<>("password"));
        departmentColumn.setCellValueFactory(new PropertyValueFactory<>("department"));
        roleColumn.setCellValueFactory(new PropertyValueFactory<>("role"));

        loadUsers();
    }

    private void loadUsers() {
        ObservableList<User> users = getUsers();
        tableView.setItems(users);
    }

    public static ObservableList<User> getUsers() {
        ObservableList<User> users = FXCollections.observableArrayList();
        String query = "SELECT users.id, users.first_name, users.last_name, users.email, users.password, " +
                "roles.name AS role, departments.name AS department " +
                "FROM users " +
                "LEFT JOIN roles ON users.role_id = roles.id " +
                "LEFT JOIN departments ON users.department_id = departments.id " +
                "WHERE users.id > 1";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                users.add(new User(
                        rs.getInt("id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("role"),
                        rs.getString("department")
                        ));

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return users;
    }

    @FXML
    void addUser(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/obiwankenobi/views/addUser.fxml"));
        Parent parent = loader.load();

        AddUser addUser = loader.getController();

        Stage stage = new Stage();
        stage.setScene(new Scene(parent));
        stage.initStyle(StageStyle.UTILITY);
        stage.show();
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
}
