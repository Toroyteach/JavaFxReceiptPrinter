package com.example.demo;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ResourceBundle;

public class About implements Initializable {

    @FXML
    private ImageView aboutImageView;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        Image image = new Image("/expresslogo.jpg");
        aboutImageView.setImage(image);
    }
}
