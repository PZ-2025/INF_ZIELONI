package com.example.obiwankenobi;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.sql.SQLException;

import static com.example.obiwankenobi.DatabaseConnection.getConnection;

/**
 * Główna klasa uruchamiająca aplikację JavaFX.
 * Odpowiada za załadowanie początkowego widoku (ekranu logowania/powitalnego).
 */
public class Launcher extends Application {

    /**
     * Metoda uruchamiająca interfejs użytkownika.
     *
     * @param stage główna scena aplikacji
     * @throws Exception w przypadku problemów z ładowaniem pliku FXML
     */
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/com/example/obiwankenobi/views/pane.fxml"));
        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);
        stage.setScene(scene);
        stage.initStyle(StageStyle.TRANSPARENT); // Usuwa standardowy tytuł okna i obramowanie
        stage.show();
    }

    /**
     * Metoda main – punkt startowy aplikacji.
     *
     * @param args argumenty uruchomieniowe aplikacji
     * @throws SQLException może być rzucony przy próbie połączenia z bazą danych
     */
    public static void main(String[] args) throws SQLException {
        launch(args);
    }
}
