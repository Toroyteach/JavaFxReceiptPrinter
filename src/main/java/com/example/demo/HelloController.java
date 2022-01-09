package com.example.demo;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HelloController implements Initializable {

    double x,y = 0;

    @FXML
    private Label exit;

    @FXML
    private Label minimize;

    @FXML
    private StackPane containerArea;

    @FXML
    private AnchorPane topBar;

    @FXML
    private Label dateTime;

    @FXML
    private ImageView logoImage, minimizeImageView, closeImageView;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        try{

            Parent fxml = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fpages/home.fxml")));
            containerArea.getChildren().removeAll();
            containerArea.getChildren().setAll(fxml);

            topBar.setOnMousePressed(mouseEvent -> {
                x = mouseEvent.getSceneX();
                y = mouseEvent.getSceneY();
            });

            topBar.setOnMouseDragged(mouseEvent -> {
                HelloApplication.anchorPane.setX(mouseEvent.getScreenX() - x);
                HelloApplication.anchorPane.setY(mouseEvent.getScreenY() - y);
            });

            showTime();
            loadImages();
            setWindowFunction();

        } catch (IOException ex){

            Logger.getLogger(HelloController.class.getName()).log(Level.SEVERE, ex.getCause().getMessage(), ex);
        }
    }

    private void setWindowFunction() {

        closeImageView.setOnMouseClicked(mouseEvent -> {
            System.exit(0);
        });
    }

    private void loadImages() {
        Image image = new Image("/expresslogo.jpg");
        logoImage.setImage(image);

        Image minize = new Image("/minimize-window-50.png");
        minimizeImageView.setImage(minize);

        Image close = new Image("/close-window-50.png");
        closeImageView.setImage(close);
    }

    public void home(javafx.event.ActionEvent actionEvent) throws IOException{
        Parent fxml = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fpages/home.fxml")));
        containerArea.getChildren().removeAll();
        containerArea.getChildren().setAll(fxml);
    }

    public void account(javafx.event.ActionEvent actionEvent) throws IOException{
        Parent fxml = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fpages/account.fxml")));
        containerArea.getChildren().removeAll();
        containerArea.getChildren().setAll(fxml);
    }

    public void transact(javafx.event.ActionEvent actionEvent) throws IOException{
        Parent fxml = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fpages/transact.fxml")));
        containerArea.getChildren().removeAll();
        containerArea.getChildren().setAll(fxml);
    }

    public void about(javafx.event.ActionEvent actionEvent) throws IOException{
        Parent fxml = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fpages/about.fxml")));
        containerArea.getChildren().removeAll();
        containerArea.getChildren().setAll(fxml);
    }

    public void search(javafx.event.ActionEvent actionEvent) throws IOException{
        Parent fxml = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fpages/search.fxml")));
        containerArea.getChildren().removeAll();
        containerArea.getChildren().setAll(fxml);
    }

    public void showTime(){

        Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            LocalTime currentTime = LocalTime.now();
            LocalDate today = LocalDate.now();
            Calendar calendar = Calendar.getInstance();
            String leo = LocalDate.now().getMonth().name() +" "+ LocalDate.now().getDayOfWeek().name();
            dateTime.setText(leo+ " - " +currentTime.getHour() + ":" + currentTime.getMinute() + ":" + currentTime.getSecond());
        }),
                new KeyFrame(Duration.seconds(1))
        );
        clock.setCycleCount(Animation.INDEFINITE);
        clock.play();
    }

}