package com.example.obiwankenobi;

import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;

public class MainController implements Initializable{

    @FXML
    private VBox vbox;

    private Parent fxml;

    @FXML
    private Button backButton;


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if (vbox.getChildren().isEmpty()) {
            vbox.setTranslateX(450);

            try {
                fxml = FXMLLoader.load(getClass().getResource("/com/example/obiwankenobi/views/start.fxml"));
                vbox.getChildren().setAll(fxml);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }


    @FXML
    private void goBack(ActionEvent event) {
        System.out.println("xdxdadad");
        TranslateTransition t = new TranslateTransition(Duration.seconds(1), vbox);
        t.setToX(450);
        t.play();
        t.setOnFinished((e) ->{
            try{
                fxml = FXMLLoader.load(getClass().getResource("/com/example/obiwankenobi/views/start.fxml"));
                vbox.getChildren().clear();
                vbox.getChildren().setAll(fxml);
            }catch(IOException ex){

            }
        });
    }


    @FXML
    private void login(ActionEvent event) {
        try {
            Stage currentStage = (Stage) vbox.getScene().getWindow();
            currentStage.close();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/obiwankenobi/views/mainView.fxml"));
            Parent root = loader.load();

            Stage newStage = new Stage();
            newStage.setScene(new Scene(root));
            newStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void openLogin(ActionEvent event){
        TranslateTransition t = new TranslateTransition(Duration.seconds(1), vbox);
        t.setToX(-450);
        t.play();
        t.setOnFinished((e) ->{
            try{
                fxml = FXMLLoader.load(getClass().getResource("/com/example/obiwankenobi/views/login.fxml"));
                vbox.getChildren().clear();
                vbox.getChildren().setAll(fxml);
            }catch(IOException _){

            }
        });
    }

    @FXML
    private void forgotPass(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/obiwankenobi/views/forgotPass.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root);
            scene.setFill(Color.TRANSPARENT);
            Stage newStage = new Stage();
            newStage.setScene(scene);
            newStage.initStyle(StageStyle.TRANSPARENT);
            newStage.initModality(Modality.APPLICATION_MODAL);
            newStage.setTitle("Przypomnij hasło");
            newStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}