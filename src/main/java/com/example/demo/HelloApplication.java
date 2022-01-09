package com.example.demo;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.Objects;

public class HelloApplication extends Application {

    //helps move the window on click
    double x, y;

    @FXML
    private ImageView testImage;

    public static Stage anchorPane;

    @Override
    public void start(Stage stage) throws IOException {

        Parent root = FXMLLoader.load((Objects.requireNonNull(getClass().getResource("/fpages/main.fxml"))));
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(new Scene(root));
        anchorPane = stage;
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}