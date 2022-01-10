package com.example.demo;

import com.example.demo.database.MysqlCon;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Window;

import java.net.URL;
import java.util.ResourceBundle;

public class UpdateUser implements Initializable {

    @FXML
    public Button btnUpdateUser;

    @FXML
    private TextField firstnameEdt, lastnameEdt, emailEdt, numberEdt, usernameEdt, passwordEdt;

    public UpdateUser(){

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        checkMysqlConnecion();
        getSelectedUser();
    }

    private void getSelectedUser() {

    }

    public void updateUser(ActionEvent actionEvent) {

        Window owner = btnUpdateUser.getScene().getWindow();
        boolean check = checkEmptyValues(owner);

        if(check){
            return;
        }

        String fname = firstnameEdt.getText();
        String lname = lastnameEdt.getText();
        String email = emailEdt.getText();
        String number = numberEdt.getText();
        String username = usernameEdt.getText();
        String password = passwordEdt.getText();
        int id = 1;

        MysqlCon conn = new MysqlCon();
        boolean flag = conn.updateUser(fname, lname, email, number, username, password, id);

        if (!flag) {

            infoBox("Failed to update user details", null, "Failed");

        } else {

            infoBox("User was updated Successful!", null, "Success");
            Account.changePasswordStage.close();
        }

    }

    private boolean checkEmptyValues(Window owner){

        if (firstnameEdt.getText() == null || firstnameEdt.getText().trim().isEmpty()) {
            showAlert(owner, "Please enter a Firstname");
            return true;
        }

        if (lastnameEdt.getText() == null || lastnameEdt.getText().trim().isEmpty()) {
            showAlert(owner, "Please enter a Lastname");
            return true;
        }

        if (emailEdt.getText() == null || emailEdt.getText().trim().isEmpty()) {
            showAlert(owner, "Please enter a Email");
            return true;
        }

        if (numberEdt.getText() == null || numberEdt.getText().trim().isEmpty()) {
            showAlert(owner, "Please enter a Number");
            return true;
        }

        if (usernameEdt.getText() == null || usernameEdt.getText().trim().isEmpty()) {
            showAlert(owner, "Please enter a Username");
            return true;
        }

        if (passwordEdt.getText() == null || passwordEdt.getText().trim().isEmpty()) {
            showAlert(owner, "Please enter a Password");
            return true;
        }

        return true;

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

    public void checkMysqlConnecion(){
        boolean flag = new MysqlCon().getMysqlCon();

        if (!flag) {

            infoBox("Please ensure connection to database is established then Restart Application", null, "Failed");
            System.exit(0);

        }
    }
}
