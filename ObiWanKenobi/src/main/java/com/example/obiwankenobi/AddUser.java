package com.example.obiwankenobi;

import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Kontroler odpowiedzialny za dodawanie nowych użytkowników do systemu.
 */
public class AddUser {

    public Button closeButton;
    /**
     * Pole wyboru działu
     */
    @FXML
    protected ChoiceBox<String> addDep;

    /**
     * Pole wyboru roli
     */
    @FXML
    protected ChoiceBox<String> addRole;

    /**
     * Pole tekstowe na adres e-mail
     */
    @FXML
    protected TextField addEmail;

    @FXML
    private TextField addSalary;

    /**
     * Pole tekstowe na imię
     */
    @FXML
    protected TextField addName;

    /**
     * Pole tekstowe na hasło
     */
    @FXML
    protected TextField addPass;

    /**
     * Pole tekstowe na nazwisko
     */
    @FXML
    protected TextField addScndName;

    @FXML
    protected TextField addCity;
    /**
     * Referencja do kontrolera administratora w celu odświeżenia danych
     */
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

    @FXML
    private void handleClose() {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
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
    private void saveUserToDB(String name, String scndName, String email, String password, String city, Float salary, int depId, int roleId)
            throws SQLException, IOException {
        Connection con = DatabaseConnection.getConnection();

        String query = "INSERT INTO users (email, password, first_name, last_name, city, salary, department_id, role_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement statement = con.prepareStatement(query);

        statement.setString(1, email);
        statement.setString(2, password);
        statement.setString(3, name);
        statement.setString(4, scndName);
        statement.setString(5, city);
        statement.setFloat(6, salary);
        statement.setInt(7, depId);
        statement.setInt(8, roleId);

        int rowsInserted = statement.executeUpdate();
        if (rowsInserted > 0) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Sukces");
            alert.setHeaderText(null);
            alert.setContentText("Użytkownik został dodany.");
            alert.showAndWait();

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
        String city = addCity.getText();
        String roleSelect = addRole.getValue();
        String depSelect = addDep.getValue();

        boolean hasError = false;

        if (!validateNameAndSurname()) {
            shakeNode(addName);
            shakeNode(addScndName);
            hasError = true;
        }

        // Walidacja emaila
        if (!isEmailValid()) {
            shakeNode(addEmail);
            hasError = true;
        }

        if (isEmailExists(email)) {
            shakeNode(addEmail);
            hasError = true;
        }

        // Walidacja hasła
        if (!validatePassword()) {
            shakeNode(addPass);
            hasError = true;
        }

        if (!validateCity()) {
            hasError = true;
        }

        // Walidacja wyborów z ComboBoxów
        if (!validateSelections(roleSelect, depSelect)) {
            shakeNode(addRole);
            shakeNode(addDep);
            hasError = true;
        }

        Float salary = validateSalary();
        if (salary == null) {
            hasError = true;
        }

        if (hasError) {
            return;
        }

        int roleId = Integer.parseInt(roleSelect.split(":")[0].trim());
        int depId = Integer.parseInt(depSelect.split(":")[0].trim());

        saveUserToDB(name, scndName, email, password, city, salary, depId, roleId);

        if (adminController != null) {
            adminController.refreshTable();
        }
    }

    /**
     * Sprawdza poprawność wprowadzonej pensji.
     * Jeśli jest błędna, ustawia czerwony obrys i animację.
     *
     * @return Float wartość pensji jeśli poprawna, null w przeciwnym wypadku
     */
    private Float validateSalary() {
        String salaryText = addSalary.getText();
        if (salaryText == null || salaryText.trim().isEmpty()) {
            shakeNode(addSalary);
            return null;
        }

        try {
            Float salary = Float.parseFloat(salaryText);
            if (salary < 0) {
                shakeNode(addSalary);
                return null;
            }
            addSalary.setStyle(null);
            return salary;
        } catch (NumberFormatException e) {
            shakeNode(addSalary);
            return null;
        }
    }

    /**
     * Sprawdza, czy pole miasto nie jest puste.
     * Jeśli jest puste, ustawia czerwony obrys i animację.
     *
     * @return true jeśli miasto jest poprawnie wprowadzone, false w przeciwnym wypadku
     */
    private boolean validateCity() {
        String city = addCity.getText().trim();
        if (city.isEmpty()) {
            shakeNode(addCity);
            return false;
        }
        addCity.setStyle(null);
        return true;
    }



    /**
     * Sprawdza, czy podany adres e-mail istnieje już w bazie danych.
     *
     * @param email adres e-mail do sprawdzenia
     * @return true jeśli e-mail już istnieje, false w przeciwnym wypadku
     * @throws SQLException w przypadku błędów zapytania
     */
    private boolean isEmailExists(String email) throws SQLException {
        String query = "SELECT COUNT(*) FROM users WHERE email = ?";
        PreparedStatement statement = DatabaseConnection.getConnection().prepareStatement(query);
        statement.setString(1, email);
        ResultSet resultSet = statement.executeQuery();
        resultSet.next();
        if (resultSet.getInt(1) > 0) {
            return true;
        }
        return false;
    }

    /**
     * Sprawdza poprawność formatu adresu e-mail.
     *
     * @return true jeśli e-mail jest poprawny, false w przeciwnym wypadku
     */
    @FXML
    boolean isEmailValid() {
        String email = addEmail.getText();
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";


        if (!email.matches(emailRegex)) {
            return false;
        } else {
            addEmail.setStyle(null);
            return true;
        }
    }

    /**
     * Sprawdza bezpieczeństwo hasła użytkownika.
     *
     * @param password hasło do sprawdzenia
     * @return true jeśli hasło spełnia wymagania bezpieczeństwa
     */
    private boolean isPasswordSecure(String password) {
        String pattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$";
        return password.matches(pattern);
    }

    /**
     * Sprawdza poprawność hasła wpisanego przez użytkownika,
     * wykorzystując wzorzec bezpieczeństwa.
     *
     * @return true jeśli hasło jest poprawne, false w przeciwnym wypadku
     */
    @FXML
    boolean validatePassword() {
        String password = addPass.getText();
        addPass.setStyle(null);

        if (!isPasswordSecure(password)) {
            return false;
        }

        addPass.setStyle(null);
        return true;
    }

    /**
     * Sprawdza, czy pola imienia i nazwiska są wypełnione.
     * Jeśli nie, ustawia czerwony obrys i uruchamia animację Shake.
     *
     * @return true jeśli oba pola są wypełnione, false w przeciwnym razie
     */
    boolean validateNameAndSurname() {
        String name = addName.getText().trim();
        String scndName = addScndName.getText().trim();

        boolean valid = true;

        if (name.isEmpty()) {
            valid = false;
        } else {
            addName.setStyle(null);
        }

        if (scndName.isEmpty()) {
            valid = false;
        } else {
            addScndName.setStyle(null);
        }

        return valid;
    }

    /**
     * Sprawdza, czy użytkownik wybrał dział i rolę z list wyboru.
     * Jeśli nie, oznacza odpowiednie pola jako niepoprawne.
     *
     * @param roleSelect wartość wybrana z listy ról
     * @param depSelect  wartość wybrana z listy działów
     * @return true jeśli oba wybory zostały dokonane, false w przeciwnym wypadku
     */
    private boolean validateSelections(String roleSelect, String depSelect) {
        boolean valid = true;

        if (roleSelect == null) {
            valid = false;
        } else {
            addRole.setStyle(null);
        }

        if (depSelect == null) {
            valid = false;
        } else {
            addDep.setStyle(null);
        }


        return valid;
    }

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
