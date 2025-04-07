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

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Kontroler dla panelu administratora. Obsługuje wyświetlanie, dodawanie i wylogowywanie użytkowników.
 */
public class AdminController {

    /** Tabela wyświetlająca użytkowników */
    @FXML
    private TableView<User> tableView;

    /** Kolumna z nazwą działu */
    @FXML
    private TableColumn<User, String> departmentColumn;

    /** Kolumna z adresem e-mail */
    @FXML
    private TableColumn<User, String> emailColumn;

    /** Kolumna z imieniem */
    @FXML
    private TableColumn<User, String> nameColumn;

    /** Kolumna z hasłem */
    @FXML
    private TableColumn<User, String> passColumn;

    /** Kolumna z nazwą roli */
    @FXML
    private TableColumn<User, String> roleColumn;

    /** Kolumna z nazwiskiem */
    @FXML
    private TableColumn<User, String> scndNameColumn;

    /**
     * Metoda inicjalizacyjna FXML. Ustawia mapowanie kolumn i ładuje użytkowników.
     */
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

    /**
     * Ładuje użytkowników z bazy danych i ustawia je w tabeli.
     */
    private void loadUsers() {
        ObservableList<User> users = getUsers();
        tableView.setItems(users);
    }

    /**
     * Pobiera listę użytkowników z bazy danych.
     *
     * @return lista użytkowników jako ObservableList
     */
    public static ObservableList<User> getUsers() {
        ObservableList<User> users = FXCollections.observableArrayList();

        String query = "SELECT users.id, users.first_name, users.last_name, users.email, users.password, " +
                "roles.name AS role, departments.name AS department " +
                "FROM users " +
                "LEFT JOIN roles ON users.role_id = roles.id " +
                "LEFT JOIN departments ON users.department_id = departments.id " +
                "WHERE users.id > 1"; // Pomija użytkownika z ID 1 (np. superadmina)

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

    /**
     * Otwiera nowe okno umożliwiające dodanie użytkownika.
     * Przekazuje referencję do obecnego kontrolera w celu odświeżenia tabeli po dodaniu użytkownika.
     *
     * @param event zdarzenie kliknięcia przycisku
     * @throws IOException jeśli wystąpi problem z załadowaniem FXML
     */
    @FXML
    void addUser(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/obiwankenobi/views/addUser.fxml"));
        Parent parent = loader.load();

        AddUser addUserController = loader.getController();
        addUserController.setAdminController(this);  // Przekaż referencję

        Stage stage = new Stage();
        stage.setScene(new Scene(parent));
        stage.initStyle(StageStyle.UTILITY);
        stage.show();
    }

    /**
     * Odświeża dane w tabeli użytkowników.
     */
    public void refreshTable() {
        loadUsers();
    }

    /**
     * Wylogowuje użytkownika i otwiera ekran logowania.
     *
     * @param event zdarzenie kliknięcia przycisku wylogowania
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
