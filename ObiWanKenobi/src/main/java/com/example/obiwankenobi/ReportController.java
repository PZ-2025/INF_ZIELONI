package com.example.obiwankenobi;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Kontroler odpowiedzialny za generowanie raportów w formacie PDF w aplikacji.
 * Obsługuje generowanie raportów na podstawie danych zadania, użytkowników i działów.
 */
public class ReportController implements Initializable {

    /**
     * Inicjalizuje kontroler. Wczytuje listę działów do rozwijanej listy (ChoiceBox).
     *
     * @param location Adres URL kontrolera
     * @param resources Zasoby kontrolera
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    /**
     * Obsługuje zdarzenie kliknięcia przycisku wyświetlania raportu użytkowników.
     * Otwiera nowe okno z widokiem {@code report1.fxml} zawierającym opcje filtrowania i generowania raportu użytkowników.
     *
     * @param event zdarzenie kliknięcia przycisku (np. z widoku GUI)
     * @throws IOException jeśli wystąpi błąd podczas ładowania pliku FXML
     */
    @FXML
    void userReportShow(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/obiwankenobi/views/report1.fxml"));
        Parent root = loader.load();

        Stage reports = new Stage();
        reports.initModality(Modality.WINDOW_MODAL);
        reports.initOwner(((Node) event.getSource()).getScene().getWindow());

        reports.initStyle(StageStyle.TRANSPARENT);

        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);
        reports.setScene(scene);
        reports.showAndWait();
    }


    /**
     * Obsługuje zdarzenie kliknięcia przycisku wyświetlania raportu zadań.
     * Otwiera nowe okno z widokiem {@code report2.fxml} zawierającym opcje filtrowania i generowania raportu zadań.
     *
     * @param event zdarzenie kliknięcia przycisku (np. z widoku GUI)
     * @throws IOException jeśli wystąpi błąd podczas ładowania pliku FXML
     */
    @FXML
    void taskReportShow(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/obiwankenobi/views/report2.fxml"));
        Parent root = loader.load();

        Stage reports = new Stage();
        reports.initModality(Modality.WINDOW_MODAL);
        reports.initOwner(((Node) event.getSource()).getScene().getWindow());

        reports.initStyle(StageStyle.TRANSPARENT);

        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);
        reports.setScene(scene);
        reports.showAndWait();
    }

    /**
     * Obsługuje zdarzenie kliknięcia przycisku wyświetlania raportu magazynowego.
     * Otwiera nowe okno z widokiem {@code report3.fxml} zawierającym opcje filtrowania i generowania raportu magazynowego.
     *
     * @param event zdarzenie kliknięcia przycisku (np. z widoku GUI)
     * @throws IOException jeśli wystąpi błąd podczas ładowania pliku FXML
     */
    @FXML
    void warehouseReportShow(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/obiwankenobi/views/report3.fxml"));
        Parent root = loader.load();

        Stage reports = new Stage();
        reports.initModality(Modality.WINDOW_MODAL);
        reports.initOwner(((Node) event.getSource()).getScene().getWindow());

        reports.initStyle(StageStyle.TRANSPARENT);

        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);
        reports.setScene(scene);
        reports.showAndWait();
    }

    /**
     * Zamknięcie okna raportów.
     *
     * @param event Zdarzenie wywołujące zamknięcie okna
     */
    @FXML
    void reportsClose(ActionEvent event) {
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.close();
    }
}
