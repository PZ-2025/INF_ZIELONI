package com.example.obiwankenobi;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Główna klasa uruchamiająca aplikację JavaFX.
 * Odpowiada za załadowanie początkowego widoku (ekranu logowania/powitalnego).
 */
public class Launcher extends Application {

    private static boolean mysqlStarted = false;
    private static boolean mysqlStopped = false;

    /**
     * Uruchamia MySQL z uprawnieniami administratora po uzyskaniu zgody użytkownika
     */
    private void startMySQL() {
        if (mysqlStarted) {
            System.out.println("MySQL już uruchomiony, pomijam start.");
            return;
        }

        try {

            String batFilePath = findStartBatFile();
            if (batFilePath == null) {
                showErrorAndExit("Nie znaleziono pliku start_mysql.bat");
                return;
            }

            System.out.println("Znaleziono plik: " + batFilePath);
            System.out.println("Uruchamianie MySQL jako administrator...");


            ProcessBuilder pb = new ProcessBuilder(
                    "powershell",
                    "-Command",
                    "Start-Process",
                    "-FilePath", "cmd",
                    "-ArgumentList", "/c,\"" + batFilePath + "\"",
                    "-Verb", "RunAs",
                    "-Wait"
            );


            pb.directory(new java.io.File(batFilePath).getParentFile());

            Process process = pb.start();
            int exitCode = process.waitFor();

            if (exitCode == 0) {

                if (waitForMySQLConnection(10, 2)) {
                    mysqlStarted = true;
                    System.out.println("MySQL uruchomiony pomyślnie jako administrator.");
                } else {
                    showErrorAndExit("MySQL nie odpowiada po uruchomieniu.");
                }
            } else {
                showErrorAndExit("Błąd podczas uruchamiania MySQL. Kod błędu: " + exitCode);
            }

        } catch (IOException | InterruptedException e) {
            System.err.println("Błąd podczas uruchamiania MySQL: " + e.getMessage());
            e.printStackTrace();
            showErrorAndExit("Nie udało się uruchomić MySQL: " + e.getMessage());
        }
    }

    /**
     * Szuka pliku start_mysql.bat w różnych lokalizacjach
     */
    private String findStartBatFile() {
        String[] possiblePaths = {

                System.getProperty("user.dir") + "\\start_mysql.bat",

                "start_mysql.bat",

                "src\\main\\resources\\start_mysql.bat",

                "target\\classes\\start_mysql.bat",

                "D:\\PZ\\INF_ZIELONI\\ObiWanKenobi\\start_mysql.bat"
        };

        return findBatFile(possiblePaths, "start_mysql.bat");
    }

    /**
     * Szuka pliku stop_mysql.bat w różnych lokalizacjach
     */
    private String findStopBatFile() {
        String[] possiblePaths = {

                System.getProperty("user.dir") + "\\stop_mysql.bat",

                "stop_mysql.bat",

                "src\\main\\resources\\stop_mysql.bat",

                "target\\classes\\stop_mysql.bat",

                "D:\\PZ\\INF_ZIELONI\\ObiWanKenobi\\stop_mysql.bat"
        };

        return findBatFile(possiblePaths, "stop_mysql.bat");
    }

    /**
     * Uniwersalna metoda do wyszukiwania plików .bat
     */
    private String findBatFile(String[] possiblePaths, String fileName) {
        for (String path : possiblePaths) {
            java.io.File file = new java.io.File(path);
            System.out.println("Sprawdzam " + fileName + ": " + file.getAbsolutePath());
            if (file.exists() && file.canRead()) {
                System.out.println("Znaleziono plik: " + file.getAbsolutePath());
                return file.getAbsolutePath();
            }
        }
        return null;
    }

    /**
     * Sprawdza połączenie z MySQL przez określoną liczbę prób
     */
    private boolean waitForMySQLConnection(int maxAttempts, int delaySeconds) {
        for (int i = 0; i < maxAttempts; i++) {
            try {
                System.out.println("Sprawdzanie połączenia z MySQL (" + (i + 1) + "/" + maxAttempts + ")...");
                DatabaseConnection.getConnection().close();
                System.out.println("MySQL gotowy do pracy!");
                return true;
            } catch (Exception e) {
                System.out.println("MySQL jeszcze nie gotowy, czekam " + delaySeconds + " sekund...");
                try {
                    Thread.sleep(delaySeconds * 1000);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    return false;
                }
            }
        }
        return false;
    }

    /**
     * Wyświetla błąd i zamyka aplikację
     */
    private void showErrorAndExit(String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Błąd aplikacji");
            alert.setHeaderText("Nie można uruchomić aplikacji");
            alert.setContentText(message);
            alert.showAndWait();
            Platform.exit();
        });
    }

    /**
     * Zatrzymuje MySQL używając pliku stop_mysql.bat
     */
    private void stopMySQL() {
        if (mysqlStopped || !mysqlStarted) {
            return;
        }

        try {

            String stopBatFilePath = findStopBatFile();
            if (stopBatFilePath == null) {
                System.err.println("Nie znaleziono pliku stop_mysql.bat - próbuję alternatywne metody zatrzymania");

                tryAlternativeStop();
                return;
            }

            System.out.println("Zatrzymywanie MySQL używając: " + stopBatFilePath);

            ProcessBuilder pb = new ProcessBuilder(
                    "powershell",
                    "-Command",
                    "Start-Process",
                    "-FilePath", "cmd",
                    "-ArgumentList", "/c,\"" + stopBatFilePath + "\"",
                    "-Verb", "RunAs",
                    "-Wait"
            );


            pb.directory(new java.io.File(stopBatFilePath).getParentFile());

            Process process = pb.start();
            boolean finished = process.waitFor(15, java.util.concurrent.TimeUnit.SECONDS);

            if (finished) {
                int exitCode = process.exitValue();
                if (exitCode == 0) {
                    System.out.println("MySQL zatrzymany pomyślnie.");
                } else {
                    System.err.println("MySQL zatrzymany z kodem błędu: " + exitCode);
                }
            } else {
                System.err.println("Timeout podczas zatrzymywania MySQL - wymuszam zakończenie procesu");
                process.destroyForcibly();
            }

            mysqlStopped = true;

        } catch (IOException | InterruptedException e) {
            System.err.println("Błąd podczas zatrzymywania MySQL: " + e.getMessage());
            e.printStackTrace();

            tryAlternativeStop();
        }
    }

    /**
     * Próbuje zatrzymać MySQL alternatywnymi metodami
     */
    private void tryAlternativeStop() {
        try {
            System.out.println("Próbuję zatrzymać MySQL przez mysqladmin...");
            ProcessBuilder pb = new ProcessBuilder("mysqladmin", "-u", "root", "shutdown");
            Process process = pb.start();

            boolean finished = process.waitFor(10, java.util.concurrent.TimeUnit.SECONDS);
            if (finished && process.exitValue() == 0) {
                System.out.println("MySQL zatrzymany przez mysqladmin.");
                mysqlStopped = true;
                return;
            }
        } catch (Exception e) {
            System.err.println("Nie udało się zatrzymać przez mysqladmin: " + e.getMessage());
        }

        try {
            System.out.println("Próbuję zatrzymać usługę MySQL...");
            ProcessBuilder pb = new ProcessBuilder("net", "stop", "mysql");
            Process process = pb.start();

            boolean finished = process.waitFor(10, java.util.concurrent.TimeUnit.SECONDS);
            if (finished && process.exitValue() == 0) {
                System.out.println("MySQL zatrzymany przez net stop.");
                mysqlStopped = true;
            }
        } catch (Exception e) {
            System.err.println("Nie udało się zatrzymać usługi: " + e.getMessage());
        }
    }

    @Override
    public void start(Stage stage) throws Exception {

        startMySQL();


        Parent root = FXMLLoader.load(getClass().getResource("/com/example/obiwankenobi/views/pane.fxml"));
        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);
        stage.setScene(scene);
        stage.initStyle(StageStyle.TRANSPARENT);

        stage.setOnCloseRequest(event -> {
            System.out.println("Zamykanie aplikacji...");
            stopMySQL();
            Platform.exit();
        });

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (!mysqlStopped) {
                System.out.println("Nieoczekiwane zamknięcie - zatrzymywanie MySQL...");
                stopMySQL();
            }
        }));

        stage.show();
    }

    @Override
    public void stop() throws Exception {
        System.out.println("Zatrzymywanie aplikacji JavaFX...");
        stopMySQL();
        super.stop();
    }

    public static void main(String[] args) throws SQLException {
        launch(args);
    }
}