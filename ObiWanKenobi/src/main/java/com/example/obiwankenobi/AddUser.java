package com.example.obiwankenobi;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Kontroler odpowiedzialny za dodawanie nowych użytkowników do systemu.
 */
public class AddUser {

    /** Pole wyboru działu */
    @FXML
    private ChoiceBox<String> addDep;

    /** Pole wyboru roli */
    @FXML
    private ChoiceBox<String> addRole;

    /** Pole tekstowe na adres e-mail */
    @FXML
    private TextField addEmail;

    /** Pole tekstowe na imię */
    @FXML
    private TextField addName;

    /** Pole tekstowe na hasło */
    @FXML
    private TextField addPass;

    /** Pole tekstowe na nazwisko */
    @FXML
    private TextField addScndName;

    /** Referencja do kontrolera administratora w celu odświeżenia danych */
    @FXML
    private AdminController adminController;

    /**
     * Metoda wywoływana automatycznie podczas inicjalizacji kontrolera.
     * Pobiera listy dostępnych ról i działów z bazy danych.
     *
     * @throws SQLException jeśli wystąpi błąd podczas komunikacji z bazą danych
     */
    @FXML
    public void initialize() throws SQLException {
        fetchInfo();
    }

    /**
     * Ustawia referencję do kontrolera administratora.
     *
     * @param adminController kontroler administratora
     */
    public void setAdminController(AdminController adminController) {
        this.adminController = adminController;
    }

    /**
     * Pobiera z bazy danych listy ról i działów i ustawia je w odpowiednich ChoiceBoxach.
     *
     * @throws SQLException jeśli wystąpi błąd podczas pobierania danych
     */
    public void fetchInfo() throws SQLException {
        Connection con = DatabaseConnection.getConnection();

        // Pobierz role
        String sql = "SELECT * FROM roles ORDER BY id ASC";
        PreparedStatement statement = con.prepareStatement(sql);
        ResultSet resultSet = statement.executeQuery();

        ObservableList<String> roles = FXCollections.observableArrayList();
        while (resultSet.next()) {
            roles.add(resultSet.getInt("id") + ": " + resultSet.getString("name"));
        }
        addRole.setItems(roles);

        // Pobierz działy
        String sql2 = "SELECT id, name FROM departments ORDER BY id ASC";
        PreparedStatement statement2 = con.prepareStatement(sql2);
        ResultSet resultSet2 = statement2.executeQuery();

        ObservableList<String> deps = FXCollections.observableArrayList();
        while (resultSet2.next()) {
            deps.add(resultSet2.getInt("id") + ": " + resultSet2.getString("name"));
        }
        addDep.setItems(deps);
    }

    /**
     * Zapisuje nowego użytkownika do bazy danych.
     *
     * @param name     imię
     * @param scndName nazwisko
     * @param email    adres e-mail
     * @param password hasło
     * @param depId    ID działu
     * @param roleId   ID roli
     * @throws SQLException jeśli wystąpi błąd SQL
     * @throws IOException  jeśli wystąpi błąd wejścia/wyjścia
     */
    private void saveUserToDB(String name, String scndName, String email, String password, int depId, int roleId)
            throws SQLException, IOException {
        Connection con = DatabaseConnection.getConnection();

        String query = "INSERT INTO users (email, password, first_name, last_name, department_id, role_id) VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement statement = con.prepareStatement(query);

        statement.setString(1, email);
        statement.setString(2, password);
        statement.setString(3, name);
        statement.setString(4, scndName);
        statement.setInt(5, depId);
        statement.setInt(6, roleId);

        int rowsInserted = statement.executeUpdate();
        if (rowsInserted > 0) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Sukces");
            alert.setHeaderText(null);
            alert.setContentText("Użytkownik został dodany.");
            alert.showAndWait();

            // Zamknij okno po dodaniu użytkownika
            Stage stage = (Stage) addName.getScene().getWindow();
            stage.close();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Błąd");
            alert.setHeaderText(null);
            alert.setContentText("Nie udało się dodać użytkownika");
            alert.showAndWait();
        }
    }

    /**
     * Obsługuje przycisk zapisu użytkownika.
     * Sprawdza poprawność danych i wywołuje metodę zapisującą użytkownika.
     *
     * @param event zdarzenie kliknięcia
     * @throws SQLException jeśli wystąpi błąd SQL
     * @throws IOException  jeśli wystąpi błąd wejścia/wyjścia
     */
    @FXML
    public void saveUser(ActionEvent event) throws SQLException, IOException {
        String name = addName.getText();
        String scndName = addScndName.getText();
        String email = addEmail.getText();
        String password = addPass.getText();

        String roleSelect = addRole.getValue();
        String depSelect = addDep.getValue();

        // Walidacja pól
        if (name.isEmpty() || scndName.isEmpty() || email.isEmpty() || password.isEmpty() ||
                roleSelect == null || depSelect == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Błąd");
            alert.setHeaderText(null);
            alert.setContentText("Wypełnij wszystkie pola");
            alert.showAndWait();
            return;
        }

        int roleId = Integer.parseInt(roleSelect.split(":")[0].trim());
        int depId = Integer.parseInt(depSelect.split(":")[0].trim());

        saveUserToDB(name, scndName, email, password, depId, roleId);

        // Odśwież tabelę użytkowników po dodaniu nowego
        if (adminController != null) {
            adminController.refreshTable();
        }
    }
}
