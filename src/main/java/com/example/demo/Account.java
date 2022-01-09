package com.example.demo;

import com.example.demo.data.UserDetails;
import com.example.demo.database.MysqlCon;
import javafx.collections.ObservableSet;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.print.Printer;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.stage.*;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Account implements Initializable {

    @FXML
    private Button btncreateuserview, btndeleteuser, btnchangepassword;

    public static Stage changePasswordStage, createUserStage;

    @FXML
    private BorderPane createUserPane, updateUserPane;

    @FXML
    private TableView tblUsers;

    @FXML
    private TextArea viewPrinters;

    public Account(){

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //getUsersDetails();
    }

    private void getUsersDetails() {

        TableColumn<UserDetails, String> column1 = new TableColumn<>("First Name");
        column1.setCellValueFactory(new PropertyValueFactory<>("firstname"));


        TableColumn<UserDetails, String> column2 = new TableColumn<>("Last Name");
        column2.setCellValueFactory(new PropertyValueFactory<>("lastname"));


        tblUsers.getColumns().add(column1);
        tblUsers.getColumns().add(column2);

        tblUsers.getItems().add(new UserDetails("Anthony", "Toroyteach", "tony@gmail.com", "0710516288", "toroyteach", "password"));
        tblUsers.getItems().add(new UserDetails("Anthony", "Toroyteach", "tony@gmail.com", "0710516288", "toroyteach", "password"));
    }

    @FXML
    private void createUserAction(ActionEvent event) throws IOException {

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fpages/create.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            createUserStage = new Stage();
            createUserStage.initModality(Modality.APPLICATION_MODAL);
            createUserStage.setTitle("Create User");
            createUserStage.setScene(new Scene(root1));
            createUserStage.show();
        } catch (IOException e) {

            e.printStackTrace();

        }

    }

    @FXML
    private void changePassword(ActionEvent event) throws IOException {

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fpages/updateuser.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            changePasswordStage = new Stage();
            changePasswordStage.initModality(Modality.APPLICATION_MODAL);
            changePasswordStage.setTitle("Update User");
            changePasswordStage.setScene(new Scene(root1));
            changePasswordStage.show();
        } catch (IOException e) {

            e.printStackTrace();

        }
    }

    @FXML
    private void deleteUserAction(ActionEvent event){

//        Window owner = btndeleteuser.getScene().getWindow();
//
//        int userid = 1;
//
//        MysqlCon conn = new MysqlCon();
//        boolean flag = conn.deleteUser(userid);
//
//        if (!flag) {
//
//            infoBox("Error occurred Deactivating user", null, "Failed");
//
//        } else {
//
//            infoBox("User was Deactivated Succesfully", null, "Success");
//        }

        //check printers
        viewPrinters.setText("");
        ObservableSet<Printer> printers = Printer.getAllPrinters();

        Printer defaultprinter = Printer.getDefaultPrinter();

        if (defaultprinter != null)
        {
            String name = defaultprinter.getName();
            viewPrinters.appendText("Default printer is: " + name);
        }
        else
        {
            viewPrinters.appendText("No printers installed.");
        }
    }

    public static void infoBox(String infoMessage, String headerText, String title) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText(infoMessage);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.showAndWait();
    }

    private static void showAlert(Window owner, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Form Error!");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.initOwner(owner);
        alert.show();
    }

}
