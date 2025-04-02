package com.example.obiwankenobi;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AddUser {

    @FXML
    private ChoiceBox<String> addDep;

    @FXML
    private ChoiceBox<String> addRole;

    @FXML
    private TextField addEmail;

    @FXML
    private TextField addName;

    @FXML
    private TextField addPass;

    @FXML
    private TextField addScndName;


    @FXML
    public void initialize() throws SQLException {
        fetchInfo();
    }

    public void fetchInfo() throws SQLException {
        Connection con = DatabaseConnection.getConnection();

        String sql = "SELECT * FROM roles order by id ASC";

        PreparedStatement statement = con.prepareStatement(sql);

        ResultSet resultSet = statement.executeQuery();

        ObservableList<String> roles = FXCollections.observableArrayList();

        while (resultSet.next()) {
            roles.add(resultSet.getInt("id") + ": " + resultSet.getString("name"));
        }

        addRole.setItems(roles);

        // ==================================== //

        String sql2 = "SELECT id, name FROM departments ORDER BY id ASC";

        PreparedStatement statement2 = con.prepareStatement(sql2);

        ResultSet resultSet2 = statement2.executeQuery();

        ObservableList<String> deps = FXCollections.observableArrayList();

        while (resultSet2.next()) {
            deps.add(resultSet2.getInt("id") + ": " + resultSet2.getString("name"));
        }

        addDep.setItems(deps);
    }

    private void saveUserToDB(String name, String scndName, String email, String password, int depId, int roleId) throws SQLException, IOException {
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

            addEmail.clear();
            addName.clear();
            addPass.clear();
            addScndName.clear();
            addRole.setValue(null);
            addDep.setValue(null);



            alert.showAndWait();

        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Błąd");
            alert.setHeaderText(null);
            alert.setContentText("Nie udało się dodać użytkownika");
            alert.showAndWait();
        }
    }

    @FXML
    public void saveUser(ActionEvent event) throws SQLException, IOException {
        String name = addName.getText();
        String scndName = addScndName.getText();
        String email = addEmail.getText();
        String password = addPass.getText();

        String roleSelect = addRole.getValue();
        String depSelect = addDep.getValue();

//        isInputEmpty();

//
//        if (nationSelection == null  ammoSelection == null  museumSelection == null) {
//            return;
//        }

        String[] roleParts = roleSelect.split(":");
        int roleId = Integer.parseInt(roleParts[0].trim());

        String[] depParts = depSelect.split(":");
        int depId = Integer.parseInt(depParts[0].trim());

        saveUserToDB(name, scndName, email, password,  depId,  roleId);

    }
}
