package com.example.obiwankenobi;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import java.awt.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class UserController {
    @FXML
    private Button logoutBtn;
    @FXML
    private GridPane taskGrid;

    @FXML
    private void initialize() throws SQLException {
        loadContent();

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


//    private void loadContent(String museumId, String searchQuery, String nationId) {
//        int row = 1;
//        try {
//            taskGrid.getChildren().clear();
//
//            Connection connection = DatabaseConnection.getConnection();
//            Statement statement = connection.createStatement();
//            ResultSet resultSet;
//
//            String sql = "SELECT * FROM tasks";
//
//            if (museumId != null && !museumId.isEmpty()) {
//                sql += " JOIN museum_tanks ON tanks.tank_id = museum_tanks.tank_id WHERE museum_tanks.museum_id = " + museumId;
//                if (nationId != null && !nationId.isEmpty()) {
//                    sql += " AND tanks.nation_id = " + nationId;
//                }
//                if (searchQuery != null && !searchQuery.isEmpty()) {
//                    sql += " AND tanks.name LIKE '%" + searchQuery + "%'";
//                }
//            } else if (nationId != null && !nationId.isEmpty()) {
//                sql += " WHERE tanks.nation_id = " + nationId;
//                if (searchQuery != null && !searchQuery.isEmpty()) {
//                    sql += " AND tanks.name LIKE '%" + searchQuery + "%'";
//                }
//            } else if (searchQuery != null && !searchQuery.isEmpty()) {
//                sql += " WHERE tanks.name LIKE '%" + searchQuery + "%'";
//            }
//
//            resultSet = statement.executeQuery(sql);
//
//            while (resultSet.next()) {
//                FXMLLoader fxmlLoader = new FXMLLoader();
//                fxmlLoader.setLocation(getClass().getResource("/com/example/obiwankenobi/views/taskMain.fxml"));
//                AnchorPane taskMain = fxmlLoader.load();
//
//                TaskView taskViewController = fxmlLoader.getController();
//                taskViewController.setTaskId(resultSet.getInt("taskId"));
//
//                if (column == 2) {
//                    column = 0;
//                    ++row;
//                }
//                taskGrid.add(taskMain, column++, row);
//                GridPane.setMargin(taskMain, new Insets(5));
//            }
//            resultSet.close();
//            statement.close();
//            connection.close();
//
//        } catch (SQLException | IOException e) {
//            e.printStackTrace();
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//    }

    private void loadContent() {
        int row = 1;

        try {
            taskGrid.getChildren().clear();

            for (int i = 0; i < 5; i++) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/com/example/obiwankenobi/views/taskMain.fxml"));
                AnchorPane taskMain = fxmlLoader.load();

                taskGrid.add(taskMain, 0, row++);
                GridPane.setMargin(taskMain, new Insets(5));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
