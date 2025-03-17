package com.example.obiwankenobi;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static com.example.obiwankenobi.DatabaseConnection.getConnection;


public class Launcher extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/com/example/obiwankenobi/views/pane.fxml"));
        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);
        stage.setScene(scene);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.show();

    }

    public static void main(String[] args) throws SQLException {
//        Connection conn = DatabaseConnection.getConnection();
//
//        if (conn != null) {
//            System.out.println("✅ Połączenie do MySQL działa poprawnie!");
//            addUser("maciek", "maciek@gmail");
//            conn.close();
//        } else {
//            System.out.println("❌ Błąd połączenia z bazą danych.");
//        }

        launch(args);

    }


    //TO NA TESTA JEST INO
//    public static void addUser(String username, String email) {
//        String sql = "INSERT INTO users (name, email) VALUES (?, ?)";
//
//        try (Connection conn = getConnection();
//             PreparedStatement pstmt = conn.prepareStatement(sql)) {
//
//            pstmt.setString(1, username);
//            pstmt.setString(2, email);
//
//            int rowsAffected = pstmt.executeUpdate();
//            if (rowsAffected > 0) {
//                System.out.println("✅ Użytkownik został dodany pomyślnie!");
//            } else {
//                System.out.println("⚠️ Nie udało się dodać użytkownika.");
//            }
//
//        } catch (SQLException e) {
//            System.err.println("❌ Błąd podczas dodawania użytkownika.");
//            e.printStackTrace();
//        }
//    }

}