package com.example.demo;

import com.example.demo.database.MysqlCon;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.Objects;

public class HelloApplication extends Application {

    //helps move the window on click
    //double x, y;

    public static Stage anchorPane;

    private static Stage setStage;

    @Override
    public void start(Stage stage) throws IOException {

        checkMysqlConnecion();

        setStage = stage;
        Parent root = FXMLLoader.load((Objects.requireNonNull(getClass().getResource("/fpages/login.fxml"))));
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setResizable(false);
        Image iconImage = new Image("/expresslogo.jpg");
        setStage.getIcons().add(iconImage);
        setStage.setTitle("Express Laundry");
        stage.setScene(new Scene(root, Color.DARKGRAY));
        stage.show();
    }

    public void changeScene() throws IOException{

        Parent root = FXMLLoader.load((Objects.requireNonNull(HelloApplication.class.getResource("/fpages/main.fxml"))));
        Image iconImage = new Image("/expresslogo.jpg");
        setStage.getIcons().add(iconImage);
        anchorPane = setStage;

        setStage.setY(120);
        setStage.setX(200);
        setStage.setScene(new Scene(root));
        setStage.show();
    }

    public static void main(String[] args) {
        launch();
    }

    public void checkMysqlConnecion(){
        boolean flag = new MysqlCon().getMysqlCon();

        if (!flag) {

            infoBox("Please ensure connection to database is established and Restart Application", "Error", "Database Connection");
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