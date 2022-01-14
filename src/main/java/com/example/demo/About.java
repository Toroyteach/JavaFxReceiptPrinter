package com.example.demo;

import com.example.demo.database.MysqlCon;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ResourceBundle;

public class About implements Initializable {

    @FXML
    private ImageView aboutImageView;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        checkMysqlConnecion();

        Image image = new Image("/expresslogo.jpg");
        aboutImageView.setImage(image);
    }

    public void checkMysqlConnecion(){
        boolean flag = new MysqlCon().getMysqlCon();

        if (!flag) {

            infoBox("Please ensure connection to database is established and Restart Application", "Database Connection", "Error");
            System.exit(0);

        }
    }

    public static void infoBox(String infoMessage, String headerText, String title) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(infoMessage);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.showAndWait();
    }
}
