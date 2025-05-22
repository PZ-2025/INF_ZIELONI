package com.example.obiwankenobi;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.sql.*;

/**
 * Kontroler panelu kierownika (Managera).
 * Obsługuje akcje związane z dodawaniem, edycją, usuwaniem, zatwierdzaniem zadań,
 * wylogowaniem, wyświetlaniem zadań oraz stanem magazynu.
 */
public class ManagerController {

    @FXML
    private Button StatusWareHauseBtn;

    @FXML
    private Button addTaskBtn;

    @FXML
    private Button logoutBtn;

    @FXML
    private VBox taskListContainer;

    @FXML
    public void initialize() {
        refreshTaskList();
    }

    /**
     * Obsługuje akcję dodawania nowego zadania.
     * Otwiera nowe okno dialogowe z formularzem dodawania zadania.
     *
     * @param event kliknięcie przycisku "Dodaj zadanie"
     */
    @FXML
    void addTask(ActionEvent event) {
        try {
            // Ładowanie widoku formularza dodawania zadania
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/obiwankenobi/views/addTask.fxml"));
            Parent root = loader.load();

            // Tworzenie nowego okna
            Stage addTaskStage = new Stage();
            addTaskStage.setTitle("Dodaj nowe zadanie");
            addTaskStage.initModality(Modality.WINDOW_MODAL);
            addTaskStage.initOwner(((Node) event.getSource()).getScene().getWindow());

            // Ustawianie stylu okna na transparentny
            addTaskStage.initStyle(StageStyle.TRANSPARENT);

            // Ustawianie sceny i konfiguracja przezroczystości
            Scene scene = new Scene(root);
            scene.setFill(Color.TRANSPARENT);
            addTaskStage.setScene(scene);
            addTaskStage.showAndWait();

            // Po zamknięciu okna można odświeżyć listę zadań (jeśli istnieje)
            refreshTaskList();

        } catch (IOException e) {
            e.printStackTrace();
            showErrorAlert("Nie udało się otworzyć formularza dodawania zadania.");
        }
    }

    /**
     * Odświeża listę zadań po dodaniu, edycji, usunięciu i zatwierdzeniu zadania.
     * Ta metoda powinna być wywołana po dodaniu, edycji, usunięciu i zatwierdzeniu zadania,
     * aby zaktualizować widok z aktualną listą zadań.
     * Zawiera również metody obsługi przycisków dodawania, edycji, usuwania i zatwierdzania zadania.
     */
    private void refreshTaskList() {
        taskListContainer.getChildren().clear();

        String query = "SELECT t.id, t.title, t.description, t.status, t.deadline, t.priority, t.user_id, t.created_at\n" +
                "FROM tasks t\n" +
                "JOIN users u ON t.user_id = u.id\n" +
                "JOIN departments d ON u.department_id = d.id\n" +
                "WHERE d.manager_id = ?\n";

        // Pobranie ID zalogowanego użytkownika
        int loggedInUserId = ManagerController.getLoggedInUserId();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, loggedInUserId);
            ResultSet rs = stmt.executeQuery();

            int taskNumber = 1;

            while (rs.next()) {
                String title = rs.getString("title");
                String status = rs.getString("status");
                Timestamp deadline = rs.getTimestamp("deadline");
                String priority = rs.getString("priority");
                int id = rs.getInt("id");
                String description = rs.getString("description");
                int user_id = rs.getInt("user_id");

                HBox taskBox = new HBox();
                taskBox.setSpacing(10);
                taskBox.setStyle("-fx-background-color: #91ee91; -fx-padding: 10;");
                taskBox.setPrefHeight(78.0);
                taskBox.setPrefWidth(619.0);
                taskBox.setAlignment(Pos.CENTER_LEFT);

                VBox textBox = new VBox(5);  // Ustawienie odstępu między etykietami
                textBox.setPrefHeight(78.0);
                textBox.setPrefWidth(298.0);

                Label taskNumberLabel = new Label("Zadanie id: " + id);
                taskNumberLabel.getStyleClass().add("textSmallDark");
                taskNumberLabel.setStyle("-fx-font-weight: bold;");

                // Tytuł zadania
                Label titleLabel = new Label(title);
                titleLabel.getStyleClass().add("textSmallDark");
                titleLabel.setPrefSize(254, 38);
                titleLabel.setWrapText(true);
                titleLabel.setMaxWidth(250);

                // Data deadline
                String deadlineText = (deadline != null ? deadline.toLocalDateTime().toLocalDate().toString() : "Brak terminu");
                Label deadlineLabel = new Label(deadlineText);
                deadlineLabel.getStyleClass().add("textSmallDark");
                deadlineLabel.setPrefSize(254, 38);
                deadlineLabel.setWrapText(true);
                deadlineLabel.setMaxWidth(250);

                textBox.getChildren().addAll(taskNumberLabel, titleLabel, deadlineLabel);

                // Status zadania
                Label statusLabel = new Label(status);
                statusLabel.getStyleClass().add("textSmallDark");
                statusLabel.setPrefHeight(38);
                statusLabel.setMaxWidth(150);

                // Przycisk edycji zadania
                Button editBtn = new Button("Edytuj");
                editBtn.setStyle("-fx-background-color: #FFC849; -fx-font-size: 12px;");
                editBtn.setPrefSize(60, 30);

                // edycja zadania
                editBtn.setOnAction(e -> {
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/obiwankenobi/views/editTask.fxml"));
                        Parent root = loader.load();

                        //kontroler edycji
                        EditTaskController controller = loader.getController();
                        controller.setTaskData(
                                id,
                                title,
                                description,
                                priority,
                                deadline.toLocalDateTime().toLocalDate(),
                                user_id,
                                getUserNameById(user_id)
                        );

                        Stage editStage = new Stage();
                        editStage.setTitle("Edytuj zadanie");
                        editStage.initModality(Modality.WINDOW_MODAL);
                        editStage.initOwner(((Node) e.getSource()).getScene().getWindow());
                        Scene scene = new Scene(root);
                        editStage.initStyle(StageStyle.TRANSPARENT);
                        editStage.setScene(scene);
                        editStage.showAndWait();

                        refreshTaskList();

                    } catch (Exception ex) {
                        ex.printStackTrace();
                        showErrorAlert("Nie udało się otworzyć formularza edycji zadania.");
                    }
                });

                // Przycisk zatwierdzania zadania
                Button confirmBtn = new Button("Zatwierdź");
                confirmBtn.setStyle("-fx-background-color: #0C5A18; -fx-font-size: 12px;");
                confirmBtn.setPrefSize(80, 30);

                if ("Zakonczone".equalsIgnoreCase(status)) {
                    confirmBtn.setDisable(true);
                    editBtn.setDisable(true);
                }

                if (!"Oczekujace".equalsIgnoreCase(status)) {
                    confirmBtn.setVisible(false);
                }

                confirmBtn.setOnAction(e -> {
                    if (!"Oczekujace".equalsIgnoreCase(status)) {
                        showInfoAlert("Tylko zadania ze statusem 'Oczekujace' mogą być zatwierdzane.");
                        return;
                    }

                    try (Connection conn1 = DatabaseConnection.getConnection();
                         PreparedStatement stmt1 = conn1.prepareStatement("UPDATE tasks SET status = ? WHERE id = ?")) {

                        stmt1.setString(1, "Zakonczone");
                        stmt1.setInt(2, id);
                        int rowsAffected = stmt1.executeUpdate();

                        if (rowsAffected > 0) {
                            showInfoAlert("Zadanie zostało zatwierdzone.");
                            refreshTaskList();
                        } else {
                            showErrorAlert("Nie udało się zatwierdzić zadania.");
                        }

                    } catch (SQLException ex) {
                        ex.printStackTrace();
                        showErrorAlert("Błąd podczas zatwierdzania zadania.");
                    }
                });

                // Przycisk usuwania zadania
                Button deleteBtn = new Button("Usuń");
                deleteBtn.setStyle("-fx-background-color: #7D0A0A; -fx-font-size: 12px;");
                deleteBtn.setPrefSize(60, 30);

                deleteBtn.setOnAction(e -> {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Potwierdzenie usunięcia");
                    alert.setHeaderText("Czy na pewno chcesz usunąć to zadanie?");
                    alert.setContentText("Zadanie ID: " + id + "\nTytuł: " + title);

                    alert.showAndWait().ifPresent(response -> {
                        if (response == ButtonType.OK) {
                            try (Connection conn2 = DatabaseConnection.getConnection();
                                 PreparedStatement stmt2 = conn2.prepareStatement("DELETE FROM tasks WHERE id = ?")) {

                                stmt2.setInt(1, id);
                                int rowsAffected = stmt2.executeUpdate();

                                if (rowsAffected > 0) {
                                    showInfoAlert("Zadanie zostało usunięte.");
                                    refreshTaskList();
                                } else {
                                    showErrorAlert("Nie udało się usunąć zadania.");
                                }

                            } catch (SQLException ex) {
                                ex.printStackTrace();
                                showErrorAlert("Błąd podczas usuwania zadania.");
                            }
                        }
                    });
                });

                // Opakowanie przycisków w VBox i ustawienie na prawo
                VBox buttonBox = new VBox(5);
                buttonBox.setAlignment(Pos.CENTER_RIGHT);
                buttonBox.getChildren().addAll(editBtn, confirmBtn, deleteBtn);

                Region spacer = new Region(); // by wypchnąć buttony na prawo
                HBox.setHgrow(spacer, Priority.ALWAYS);

                // Dodanie do głównego kontenera
                taskBox.getChildren().addAll(textBox, statusLabel, spacer, buttonBox);
                taskListContainer.getChildren().add(taskBox);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            showErrorAlert("Nie udało się załadować listy zadań.");
        }
    }

    /**
     * Pobiera imię i nazwisko użytkownika po id.
     * @param userId id użytkownika
     */
    private String getUserNameById(int userId) {
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement("SELECT first_name, last_name FROM users WHERE id = ?")) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("first_name") + " " + rs.getString("last_name");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * Zwraca ID zalogowanego użytkownika.
     */
    public static int getLoggedInUserId() {
        // Zwróć ID zalogowanego użytkownika
        return UserController.getLoggedInUser().getUserId(); // Dostęp do kontrolera użytkownika.
    }

    /**
     * Wyświetla stan magazynu.
     * @param event kliknięcie przycisku "Stan magazynu"
     */
    @FXML
    void StatusWareHause(ActionEvent event) {
        taskListContainer.getChildren().clear();

        // Ukrycie przycisków
        addTaskBtn.setVisible(false);
        StatusWareHauseBtn.setVisible(false);

        try {
            // Tabela
            javafx.scene.control.TableView<Warehouse> tableView = new javafx.scene.control.TableView<>();

            // Kolumny
            javafx.scene.control.TableColumn<Warehouse, String> depCol = new javafx.scene.control.TableColumn<>("Dzial");
            depCol.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("depName"));
            depCol.setPrefWidth(200);

            javafx.scene.control.TableColumn<Warehouse, String> itemCol = new javafx.scene.control.TableColumn<>("Przedmiot");
            itemCol.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("itemName"));
            itemCol.setPrefWidth(300);

            javafx.scene.control.TableColumn<Warehouse, Integer> amountCol = new javafx.scene.control.TableColumn<>("Ilosc");
            amountCol.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("itemAmount"));
            amountCol.setPrefWidth(100);

            tableView.getColumns().addAll(depCol, itemCol, amountCol);
            tableView.setItems(fetchWarehouseData());

            tableView.setPrefHeight(400);
            tableView.setPrefWidth(600);
            tableView.setStyle("-fx-background-color: white;");

            taskListContainer.getChildren().add(tableView);

            Button backBtn = new Button("Pokaż zadania");
            backBtn.setStyle("-fx-background-color: #FFC849; -fx-text-fill: white;");
            backBtn.setOnAction(e -> {
                addTaskBtn.setVisible(true);
                StatusWareHauseBtn.setVisible(true);
                refreshTaskList();
            });

            taskListContainer.getChildren().add(0, backBtn);

        } catch (Exception e) {
            e.printStackTrace();
            showErrorAlert("Nie udało się załadować stanu magazynu.");
        }
    }

    /**
     * Pobiera dane magazynu z bazy danych.
     *
     * @return lista magazynów z przedmiotami i ilościami
     */
    private javafx.collections.ObservableList<Warehouse> fetchWarehouseData() {
        javafx.collections.ObservableList<Warehouse> list = javafx.collections.FXCollections.observableArrayList();

        String query = "SELECT w.id, d.name AS department_name, i.name AS item_name, i.quantity " +
                "FROM warehouses w " +
                "JOIN departments d ON w.department_id = d.id " +
                "JOIN items i ON w.id = i.warehouse_id " +
                "WHERE d.manager_id = ?";

        int loggedInUserId = getLoggedInUserId();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, loggedInUserId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int warehouseId = rs.getInt("id");
                String dep = rs.getString("department_name");
                String item = rs.getString("item_name");
                int quantity = rs.getInt("quantity");

                Warehouse warehouse = new Warehouse(dep, item, quantity);
                warehouse.setId(warehouseId);

                list.add(warehouse);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            showErrorAlert("Błąd podczas pobierania danych magazynu.");
        }

        return list;
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
            showErrorAlert("Wystąpił problem podczas wylogowywania.");
        }
    }

    /**
     * Wyświetla alert z komunikatem o błędzie.
     *
     * @param message treść komunikatu o błędzie
     */
    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Błąd");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Wyświetla alert z komunikatem o informacji.
     *
     * @param message treść komunikatu o informacji
     */
    private void showInfoAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Informacja");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}