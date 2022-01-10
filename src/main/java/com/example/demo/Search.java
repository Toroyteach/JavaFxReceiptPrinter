package com.example.demo;

import com.example.demo.data.Order;
import com.example.demo.database.MysqlCon;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Window;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class Search implements Initializable {

    @FXML
    private Button btnsearch;

    @FXML
    private TextField searchItemEdt;

    @FXML
    private Label txtHomeLaundry, txtJackerBlazer, txtCurtain, txtDuvet, txtCarpet;

    public Search(){

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        checkMysqlConnecion();
        clearFields();
        //validateText();
    }

    @FXML
    public void searchOrder(ActionEvent event) throws SQLException{

        Window owner = btnsearch.getScene().getWindow();
        if (searchItemEdt.getText() == null || searchItemEdt.getText().trim().isEmpty()) {
            showAlert(owner, "Please Fill in the Order Id to search");
            return;
        }

        String orderId = searchItemEdt.getText();

        MysqlCon conn = new MysqlCon();
        List<Order> flag = conn.search(orderId);

        if (flag.isEmpty()) {

            showAlert(owner, "No order was Found");

        } else {

            for(int i = 0; i < flag.size(); i++){

                if(Objects.equals(flag.get(i).getName(), "homelaundry")){
                    txtHomeLaundry.setText(String.valueOf(flag.get(i).getQuantity()));
                }

                if(Objects.equals(flag.get(i).getName(), "jacketBlazer")){
                    txtJackerBlazer.setText(String.valueOf(flag.get(i).getQuantity()));
                }

                if(Objects.equals(flag.get(i).getName(), "curtain")){
                    txtCurtain.setText(String.valueOf(flag.get(i).getQuantity()));
                }

                if(Objects.equals(flag.get(i).getName(), "duvet")){
                    txtDuvet.setText(String.valueOf(flag.get(i).getQuantity()));
                }

                if(Objects.equals(flag.get(i).getName(), "carpet")){
                    txtCarpet.setText(String.valueOf(flag.get(i).getQuantity()));
                }

            }
        }
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
        searchItemEdt.setText("");
    }

    private void validateText(){
        searchItemEdt.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                searchItemEdt.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
    }

    public void checkMysqlConnecion(){
        boolean flag = new MysqlCon().getMysqlCon();

        if (!flag) {

            infoBox("Please ensure connection to database is established and Restart Application", null, "Failed");
            System.exit(0);

        }
    }

    public static void infoBox(String infoMessage, String headerText, String title) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText(infoMessage);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.showAndWait();
    }
}
