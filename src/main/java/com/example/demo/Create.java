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
import java.sql.SQLException;
import java.util.ResourceBundle;

public class Create implements Initializable {

    @FXML
    private TextField firstnameEdt, lastnameEdt, emailEdt, numberEdt, usernameEdt, passwordEdt;

    @FXML
    private Button btnCreateUser;

    public Create(){

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        clearFields();
    }

    @FXML
    public void createUser(ActionEvent event) throws SQLException {

        Window owner = btnCreateUser.getScene().getWindow();
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

        MysqlCon conn = new MysqlCon();
        boolean flag = conn.create(fname, lname, email, number, username, password);

        if (!flag) {

            infoBox("Please enter correct Details to Create user", null, "Failed");

        } else {

            infoBox("User was created Successful!", null, "Success");
            clearFields();
            Account.createUserStage.close();
        }

    }

    private boolean checkEmptyValues(Window owner){

        if (firstnameEdt.getText() == null || firstnameEdt.getText().trim().isEmpty()) {
            showAlert(owner, "Please enter Firstname");
            return true;
        }

        if (lastnameEdt.getText() == null || lastnameEdt.getText().trim().isEmpty()) {
            showAlert(owner, "Please enter Lastname");
            return true;
        }

        if (emailEdt.getText() == null || emailEdt.getText().trim().isEmpty()) {
            showAlert(owner, "Please enter Email");
            return true;
        }

        if (numberEdt.getText() == null || numberEdt.getText().trim().isEmpty()) {
            showAlert(owner, "Please enter Number");
            return true;
        }

        if (usernameEdt.getText() == null || usernameEdt.getText().trim().isEmpty()) {
            showAlert(owner, "Please enter Username");
            return true;
        }

        if (passwordEdt.getText() == null || passwordEdt.getText().trim().isEmpty()) {
            showAlert(owner, "Please enter Password");
            return true;
        }

        return false;

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

    private void clearFields(){

        firstnameEdt.setText("");
        lastnameEdt.setText("");
        emailEdt.setText("");
        numberEdt.setText("");
        usernameEdt.setText("");
        passwordEdt.setText("");
    }
}
