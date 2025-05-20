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
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.converter.FloatStringConverter;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Kontroler widoku panelu administratora.
 * Zarządza wyświetlaniem i edytowaniem listy użytkowników oraz ich danymi (rola, dział, hasło, email).
 * Umożliwia także dodawanie oraz usuwanie użytkowników.
 */
public class AdminController {

    /** Tabela wyświetlająca użytkowników */
    @FXML
    private TableView<User> tableView;

    @FXML
    private TableColumn<User, Void> actionColumn;

    /** Kolumna z nazwą działu */
    @FXML
    private TableColumn<User, String> departmentColumn;

    /** Kolumna z adresem e-mail */
    @FXML
    private TableColumn<User, String> emailColumn;

    /** Kolumna z imieniem */
    @FXML
    private TableColumn<User, String> nameColumn;

    @FXML
    private TableColumn<User, Float> salaryColumn;

    @FXML
    private TableColumn<User, String> cityColumn;

    /** Kolumna z hasłem */
    @FXML
    private TableColumn<User, String> passColumn;

    /** Kolumna z nazwą roli */
    @FXML
    private TableColumn<User, String> roleColumn;

    /** Kolumna z nazwiskiem */
    @FXML
    private TableColumn<User, String> scndNameColumn;
    ObservableList<User> usersList = FXCollections.observableArrayList();

    private ObservableList<String> roles = FXCollections.observableArrayList();
    private ObservableList<String> departments = FXCollections.observableArrayList();


    /**
     * Metoda inicjalizacyjna FXML. Ustawia mapowanie kolumn i ładuje użytkowników.
     */
    @FXML
    public void initialize() {
        loadRolesAndDepartments();
        loadUsers();
        enableEditing();
        addDeleteButtonToTable();
    }

    /**
     * Ładuje użytkowników z bazy danych i ustawia je w tabeli.
     */
    private void loadUsers() {
        usersList.clear();

        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        scndNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        passColumn.setCellValueFactory(new PropertyValueFactory<>("password"));
        salaryColumn.setCellValueFactory(new PropertyValueFactory<>("salary"));
        cityColumn.setCellValueFactory(new PropertyValueFactory<>("city"));
        departmentColumn.setCellValueFactory(new PropertyValueFactory<>("department"));
        roleColumn.setCellValueFactory(new PropertyValueFactory<>("role"));

        String query = "SELECT users.id, users.first_name, users.last_name, users.city, users.email, users.password, " +
                "roles.name AS role, departments.name AS department, users.salary " +
                "FROM users " +
                "LEFT JOIN roles ON users.role_id = roles.id " +
                "LEFT JOIN departments ON users.department_id = departments.id " +
                "WHERE users.id > 1";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                usersList.add(new User(
                        rs.getInt("id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getFloat("salary"),
                        rs.getString("city"),
                        rs.getString("role"),
                        rs.getString("department")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        tableView.setItems(usersList);
    }

    /**
     * Ustawia kolumny jako edytowalne i definiuje sposób obsługi zdarzeń edycji.
     */
    private void enableEditing(){
        tableView.setEditable(true);

        nameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        scndNameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        emailColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        cityColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        passColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        salaryColumn.setCellFactory(TextFieldTableCell.forTableColumn(new FloatStringConverter()));
        departmentColumn.setCellFactory(ComboBoxTableCell.forTableColumn(departments));
        roleColumn.setCellFactory(ComboBoxTableCell.forTableColumn(roles));

        nameColumn.setOnEditCommit(event -> updateUserField(event, "first_name", event.getNewValue()));
        scndNameColumn.setOnEditCommit(event -> updateUserField(event, "last_name", event.getNewValue()));
        emailColumn.setOnEditCommit(event -> updateUserField(event, "email", event.getNewValue()));
        cityColumn.setOnEditCommit(event -> updateUserField(event, "city", event.getNewValue()));
        passColumn.setOnEditCommit(event -> updateUserField(event, "password", event.getNewValue()));
        salaryColumn.setOnEditCommit(event -> updateUserField(event, "salary", event.getNewValue()));
        departmentColumn.setOnEditCommit(event -> updateUserDepartment(event.getRowValue(), event.getNewValue()));
        roleColumn.setOnEditCommit(event -> updateUserRole(event.getRowValue(), event.getNewValue()));
    }

    /**
     * Aktualizuje wybrane pole użytkownika w bazie danych.
     *
     * @param event      zdarzenie edycji komórki
     * @param fieldName  nazwa pola w bazie danych
     * @param newValue   nowa wartość wpisana przez użytkownika
     */
    private void updateUserField(TableColumn.CellEditEvent<User, ?> event, String fieldName, Object newValue) {
        User user = event.getRowValue();
        String query = "UPDATE users SET " + fieldName + " = ? WHERE id = ?";

        if (newValue == null || newValue.toString().trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Błąd", "Pole nie może być puste!");
            return;
        }

        // Walidacja e-maila
        if (fieldName.equals("email")) {
            String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
            if (!((String) newValue).matches(emailRegex)) {
                showAlert(Alert.AlertType.ERROR, "Niepoprawny e-mail", "Podaj poprawny adres e-mail.");
                return;
            }

            if (isValueExistsInColumn(fieldName, newValue)) {
                showAlert(Alert.AlertType.ERROR, "Błąd", "Email '" + newValue + "' już istnieje.");
                return;
            }
        }

        if (fieldName.equals("salary")) {
            try {
                String strValue = newValue.toString();

                if (!strValue.matches("\\d+(\\.\\d+)?")) {
                    showAlert(Alert.AlertType.ERROR, "Błąd", "Pensja musi być dodatnią liczbą i zawierać tylko cyfry.");
                    return;
                }

                float salaryValue = Float.parseFloat(strValue);

                if (salaryValue <= 0) {
                    showAlert(Alert.AlertType.ERROR, "Błąd", "Pensja musi być liczbą większą od zera.");
                    return;
                }

                newValue = salaryValue;

            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.ERROR, "Błąd", "Pensja musi być liczbą.");
                return;
            }
        }



        // Walidacja hasła
        if (fieldName.equals("password")) {
            String password = (String) newValue;
            String passwordRegex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$";
            if (!password.matches(passwordRegex)) {
                showAlert(Alert.AlertType.ERROR, "Niepoprawne hasło",
                        "Hasło musi mieć min. 8 znaków, zawierać literę i cyfrę.");
                return;
            }
            newValue = password;
        }

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setObject(1, newValue);
            stmt.setInt(2, user.getUserId());

            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                switch (fieldName) {
                    case "first_name":
                        user.setFirstName((String) newValue);
                        break;
                    case "last_name":
                        user.setLastName((String) newValue);
                        break;
                    case "email":
                        user.setEmail((String) newValue);
                        break;
                    case "city":
                        user.setCity((String) newValue);
                        break;
                    case "salary":
                        user.setSalary((Float) newValue);
                        break;
                    case "password":
                       user.setPassword((String) newValue);
                        break;
                }
                tableView.refresh();
                showAlert(Alert.AlertType.INFORMATION, "Sukces", "Dane zostały zaktualizowane.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Błąd", "Błąd podczas aktualizacji danych.");
        }
    }

    /**
     * Dodaje kolumnę z przyciskiem "Usuń" do tabeli użytkowników.
     * Kliknięcie usuwa danego użytkownika z bazy danych.
     */
    private void addDeleteButtonToTable() {
        actionColumn.setCellFactory(param -> new javafx.scene.control.TableCell<>() {
            private final Button deleteButton = new Button("Usuń");

            {
                deleteButton.setStyle("-fx-background-color: #c0392b; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8; -fx-font-size: 14px;");
                deleteButton.setOnAction(event -> {
                    User user = getTableView().getItems().get(getIndex());
                    deleteUser(user);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(deleteButton);
                }
            }
        });
    }

    /**
     * Usuwa użytkownika z bazy danych oraz z listy wyświetlanej w tabeli.
     *
     * @param user użytkownik do usunięcia
     */
    private void deleteUser(User user) {
        String query = "DELETE FROM users WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, user.getUserId());
            int rowsDeleted = stmt.executeUpdate();

            if (rowsDeleted > 0) {
                usersList.remove(user);
                showAlert(Alert.AlertType.INFORMATION, "Sukces", "Użytkownik został usunięty.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Błąd", "Nie udało się usunąć użytkownika.");
        }
    }

    /**
     * Wczytuje dostępne role i działy z bazy danych i zapisuje je do list.
     * Role są pobierane z tabeli `roles`, działy z tabeli `departments`.
     */
    private void loadRolesAndDepartments() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String roleQuery = "SELECT name FROM roles where id > 1";
            try (PreparedStatement stmt = conn.prepareStatement(roleQuery);
                 ResultSet rs = stmt.executeQuery()) {
                roles.clear();
                while (rs.next()) {
                    roles.add(rs.getString("name"));
                }
            }

            String deptQuery = "SELECT name FROM departments";
            try (PreparedStatement stmt = conn.prepareStatement(deptQuery);
                 ResultSet rs = stmt.executeQuery()) {
                departments.clear();
                while (rs.next()) {
                    departments.add(rs.getString("name"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Aktualizuje rolę użytkownika w bazie danych.
     * Dodatkowo przypisuje lub usuwa kierownika w danym dziale.
     * Jeśli rola to "Dyrektor", usuwa przypisanie do działu.
     *
     * @param user        użytkownik do zaktualizowania
     * @param newRoleName nowa nazwa roli
     */
    private void updateUserRole(User user, String newRoleName) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            int roleId = getRoleIdByName(newRoleName, conn);

            if ("Kierownik".equalsIgnoreCase(newRoleName)) {
                String query = "SELECT COUNT(*) FROM users WHERE role_id = ? AND department_id = ?";
                try (PreparedStatement stmt = conn.prepareStatement(query)) {
                    stmt.setInt(1, roleId);
                    stmt.setInt(2, getDepartmentIdByName(user.getDepartment(), conn));

                    try (ResultSet rs = stmt.executeQuery()) {
                        rs.next();
                        int count = rs.getInt(1);

                        if (count > 0) {
                            showAlert(Alert.AlertType.ERROR, "Błąd", "Ten dział ma już przydzielonego Kierownika!");
                            return;
                        }
                    }
                }
            }

            String updateQuery = "UPDATE users SET role_id = ? WHERE id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(updateQuery)) {
                stmt.setInt(1, roleId);
                stmt.setInt(2, user.getUserId());
                int rowsUpdated = stmt.executeUpdate();

                if (rowsUpdated > 0) {
                    if ("Kierownik".equalsIgnoreCase(newRoleName)) {
                        String updateDeptQuery = "UPDATE departments SET manager_id = ? WHERE id = ?";
                        try (PreparedStatement deptStmt = conn.prepareStatement(updateDeptQuery)) {
                            deptStmt.setInt(1, user.getUserId());
                            deptStmt.setInt(2, getDepartmentIdByName(user.getDepartment(), conn));
                            deptStmt.executeUpdate();
                        }
                    } else {
                        String clearManagerQuery = "UPDATE departments SET manager_id = NULL WHERE manager_id = ?";
                        try (PreparedStatement clearStmt = conn.prepareStatement(clearManagerQuery)) {
                            clearStmt.setInt(1, user.getUserId());
                            clearStmt.executeUpdate();
                        }
                    }

                    user.setRole(newRoleName);

                    if ("Dyrektor".equalsIgnoreCase(newRoleName)) {
                        clearUserDepartment(user, conn);
                    }

                    tableView.refresh();
                    showAlert(Alert.AlertType.INFORMATION, "Sukces", "Rola została zaktualizowana.");
                }
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Błąd", "Nie udało się zaktualizować roli: " + e.getMessage());
        }
    }

    /**
     * Czyści przypisanie użytkownika do działu (ustawia `department_id` na NULL).
     * Używane, gdy użytkownik staje się "Dyrektorem".
     *
     * @param user użytkownik
     * @param conn aktywne połączenie z bazą danych
     * @throws SQLException w przypadku błędu SQL
     */
    private void clearUserDepartment(User user, Connection conn) throws SQLException {
        String query = "UPDATE users SET department_id = NULL WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, user.getUserId());
            stmt.executeUpdate();
            user.setDepartment(null);
        }
    }

    /**
     * Aktualizuje dział użytkownika w bazie danych.
     *
     * @param user              użytkownik do zaktualizowania
     * @param newDepartmentName nowy dział
     */
    private void updateUserDepartment(User user, String newDepartmentName) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            int departmentId = getDepartmentIdByName(newDepartmentName, conn);

            String query = "UPDATE users SET department_id = ? WHERE id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setInt(1, departmentId);
                stmt.setInt(2, user.getUserId());

                int rowsUpdated = stmt.executeUpdate();
                if (rowsUpdated > 0) {
                    user.setDepartment(newDepartmentName);
                    tableView.refresh();
                    showAlert(Alert.AlertType.INFORMATION, "Sukces", "Dział został zaktualizowany.");
                }
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Błąd", "Nie udało się zaktualizować działu: " + e.getMessage());
        }
    }

    /**
     * Zwraca identyfikator roli na podstawie jej nazwy.
     *
     * @param roleName nazwa roli
     * @param conn     aktywne połączenie
     * @return identyfikator roli
     * @throws SQLException jeśli rola nie została znaleziona
     */
    private int getRoleIdByName(String roleName, Connection conn) throws SQLException {
        String query = "SELECT id FROM roles WHERE name = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, roleName);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                } else {
                    throw new SQLException("Nie znaleziono roli: " + roleName);
                }
            }
        }
    }

    /**
     * Zwraca identyfikator działu na podstawie jego nazwy.
     *
     * @param deptName nazwa działu
     * @param conn     aktywne połączenie
     * @return identyfikator działu
     * @throws SQLException jeśli dział nie został znaleziony
     */
    private int getDepartmentIdByName(String deptName, Connection conn) throws SQLException {
        String query = "SELECT id FROM departments WHERE name = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, deptName);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                } else {
                    throw new SQLException("Nie znaleziono działu: " + deptName);
                }
            }
        }
    }

    /**
     * Sprawdza, czy wartość już istnieje w danej kolumnie tabeli `users`.
     *
     * @param column nazwa kolumny
     * @param value  wartość do sprawdzenia
     * @return true, jeśli wartość istnieje, false w przeciwnym razie
     */
    private boolean isValueExistsInColumn(String column, Object value) {
        String query = "SELECT COUNT(*) FROM users WHERE " + column + " = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setObject(1, value);
            try (ResultSet rs = stmt.executeQuery()) {
                rs.next();
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
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
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setScene(new Scene(parent));
        stage.show();
    }

    private int getRoleIdByName(String roleName) throws SQLException {
        String query = "SELECT id FROM roles WHERE name = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, roleName);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                } else {
                    throw new SQLException("Rola nie znaleziona: " + roleName);
                }
            }
        }
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

    /**
     * Wyświetla alert (komunikat) dla użytkownika.
     *
     * @param alertType typ alertu (np. ERROR, INFORMATION)
     * @param title     tytuł okna alertu
     * @param content   treść komunikatu
     */
    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
