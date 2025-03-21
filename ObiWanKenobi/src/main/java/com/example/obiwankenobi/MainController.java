package com.example.obiwankenobi;

import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
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
    private TextField loginMail;

    @FXML
    private PasswordField loginPassword;

    @FXML
    private Button closeButton;

//    @FXML
//    private Button logoutBtn;




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
    private void login(ActionEvent event) {
        try {
            String login = loginMail.getText();
            String password = loginPassword.getText();

            if(authenticateUser(login, password) == true) {

                Stage currentStage = (Stage) vbox.getScene().getWindow();
                currentStage.close();

                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/obiwankenobi/views/mainPanel.fxml"));
                Parent root = loader.load();

                Stage newStage = new Stage();
                newStage.setScene(new Scene(root));
                newStage.initStyle(StageStyle.TRANSPARENT);
                newStage.setResizable(false); // 🚀 Zablokowanie powiększania
                newStage.show();
            }else {
                System.out.printf("nie ma takiego uzytkownika");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




    @FXML
    private void openLogin(ActionEvent event){
        TranslateTransition t = new TranslateTransition(Duration.seconds(1), vbox);
        t.setToX(-450);
        t.play();
        t.setOnFinished((e) -> {
            try {
                fxml = FXMLLoader.load(getClass().getResource("/com/example/obiwankenobi/views/login.fxml"));
                vbox.getChildren().clear();
                vbox.getChildren().setAll(fxml);

            } catch(IOException ex) {

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

    private boolean authenticateUser(String login, String password) {
        String query = "SELECT * FROM users WHERE email = ? AND password = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, login);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();

            return rs.next(); // Jeśli znajdzie użytkownika, zwróci true
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    @FXML
    private void handleClose() {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }


    //to jeszcze nie smiga
//    @FXML
//    void logout(ActionEvent event) {
//        try {
//            // Załaduj ekran logowania
//            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/views/login.fxml"));
//            Parent root = loader.load();
//
//            // Pobierz aktualną scenę
//            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
//            stage.setScene(new Scene(root));
//            stage.show();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }


}