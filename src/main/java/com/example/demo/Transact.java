package com.example.demo;

import com.example.demo.data.Order;
import com.example.demo.database.MysqlCon;
import com.example.demo.print.PrintReceipt;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.print.PrinterJob;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;

public class Transact implements Initializable {

    @FXML
    private Label orderitdlable;

    @FXML
    private TextField homelaundryEdt, jacketBlazerEdt, curtainEdt, duvetEdt, carpetEdt, accumulatedTotalAmountEdt;
    private double totalAmount;

    @FXML
    private CheckBox checkboxhome, checkboxJB, checkboxCurtain, checkboxDuvet, checkboxCarpet;

    @FXML
    private ChoiceBox<String> choiceboxcurtain, choiceboxduvet;

    @FXML
    private Button btnViewReport, btnPrint;

    private final String[] choices = {"select", "medium", "large"};

    private List<Order> orderItemsList;

    public Transact(){

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        checkMysqlConnecion();
        clearFields();
        validateNumbers();
        getRandomString();
        fileChoiceBoxItems();
    }

    @FXML
    public void createTransaction(ActionEvent event) throws SQLException {

        Window owner = btnPrint.getScene().getWindow();

        if(checkboxhome.isSelected()){

        } else if(checkboxJB.isSelected()){

        } else if(checkboxCurtain.isSelected()){

        } else if(checkboxDuvet.isSelected()){

        } else if(checkboxCarpet.isSelected()){

        } else {
            showAlert(owner, "Please an option to Proceed");
            return;
        }

        boolean checkValue = checkEmptyValues(owner);

        if(checkValue){
            return;
        }

        String orderid = orderitdlable.getText();
        double amount = calculateTotal();

        MysqlCon conn = new MysqlCon();
        //create method in mysqlCon that accepts list of items
        boolean flag = conn.createTransaction(orderItemsList, orderid, amount);
        List<Order> receiptList = conn.getReceiptListData();

        if (!flag) {

            infoBox("Please enter correct Details to Create An order", "Form Error", "Failed");

        } else {

            printReceipt(receiptList, orderid);
            clearFields();
            getRandomString();
            infoBox("Payment was created Successful!", "Payment Creation", "Success");
        }
    }

    private void printReceipt(List<Order> list, String orderid) {
        double amount = getAmount();
        new PrintReceipt(list, amount, orderid);
    }

    @FXML
    private void viewReceipt(ActionEvent event){
        
        // use this method to calculate the total amount
        Window owner = btnPrint.getScene().getWindow();
        boolean checkValue = checkEmptyValues(owner);
        if(checkValue){
            return;
        }

        accumulatedTotalAmountEdt.setText("");
        double totalAmount = calculateTotal();
        accumulatedTotalAmountEdt.setText("ksh "+ totalAmount);
    }

    public boolean checkEmptyValues(Window owner){

        if (checkboxhome.isSelected() && ( homelaundryEdt.getText() == null || homelaundryEdt.getText().trim().isEmpty())) {
            showAlert(owner, "Please enter a Size of Home Laundry");
            return true;
        }

        if (checkboxJB.isSelected() && (jacketBlazerEdt.getText() == null || jacketBlazerEdt.getText().trim().isEmpty())) {
            showAlert(owner, "Please enter number of Blazer of Jacket");
            return true;
        }

        if (checkboxCurtain.isSelected() && (curtainEdt.getText() == null || curtainEdt.getText().trim().isEmpty() || choiceboxcurtain.getSelectionModel().isEmpty())) {


                showAlert(owner, "Please enter number of curtains or Size");
                return true;

        }

        if (checkboxDuvet.isSelected() && (duvetEdt.getText() == null || duvetEdt.getText().trim().isEmpty() || choiceboxduvet.getSelectionModel().isEmpty())) {
            showAlert(owner, "Please enter number of Duvet or Size");
            return true;
        }

        if (checkboxCarpet.isSelected() && (carpetEdt.getText() == null || carpetEdt.getText().trim().isEmpty())) {
            showAlert(owner, "Please enter number of Carpets");
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
        alert.setHeaderText("Input Error");
        alert.setContentText(message);
        alert.initOwner(owner);
        alert.show();
    }

    private void clearFields(){

        homelaundryEdt.setText("");
        jacketBlazerEdt.setText("");
        curtainEdt.setText("");
        duvetEdt.setText("");
        carpetEdt.setText("");
        accumulatedTotalAmountEdt.setText("");

        boolean state = false;

        choiceboxcurtain.setValue("Select");
        choiceboxduvet.setValue("Select");

        checkboxhome.setSelected(state);
        checkboxJB.setSelected(state);
        checkboxCurtain.setSelected(state);
        checkboxDuvet.setSelected(state);
        checkboxCarpet.setSelected(state);

    }

    private void getRandomString(){

        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 5) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();

        orderitdlable.setText(saltStr);
    }

    private void validateNumbers(){

        homelaundryEdt.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                homelaundryEdt.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });

        jacketBlazerEdt.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                jacketBlazerEdt.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });

        curtainEdt.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                curtainEdt.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });

        duvetEdt.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                duvetEdt.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });

        carpetEdt.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                carpetEdt.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });

    }

    private double calculateTotal(){

        return getAmount();
    }

    public double getAmount(){

        double total = 0.0;

        orderItemsList = new ArrayList<>();

        if (checkboxhome.isSelected()) {

            double hl = 50.00;
            double itemPrice = hl * (Integer.parseInt(homelaundryEdt.getText().trim().isEmpty() ? String.valueOf(1) : homelaundryEdt.getText().trim()));

            total += hl * Integer.parseInt(homelaundryEdt.getText());

            Order item1 = new Order("homelaundry", Integer.parseInt(homelaundryEdt.getText()), hl, itemPrice);

            orderItemsList.add(item1);
        }

        if (checkboxJB.isSelected()) {

            double jb = 150.00;
            double itemPrice = jb * (Integer.parseInt(jacketBlazerEdt.getText().trim().isEmpty() ? String.valueOf(1) : jacketBlazerEdt.getText().trim()));

            total += jb * Integer.parseInt(jacketBlazerEdt.getText());

            Order item2 = new Order("jacketBlazer", Integer.parseInt(jacketBlazerEdt.getText()), jb, itemPrice);

            orderItemsList.add(item2);
        }

        if (checkboxCurtain.isSelected()) {

            double itemPrice = 0.0;

            String value = choiceboxcurtain.getValue();

            Order item3;

            if(value.equalsIgnoreCase("medium")){

                double curtain = 150.00;
                itemPrice = curtain * (Integer.parseInt(curtainEdt.getText().trim().isEmpty() ? String.valueOf(1) : curtainEdt.getText().trim()));

                total += curtain * (Integer.parseInt(curtainEdt.getText().trim().isEmpty() ? String.valueOf(1) : curtainEdt.getText().trim()));

                item3 = new Order("curtain", Integer.parseInt(curtainEdt.getText()), curtain, itemPrice);

            } else {

                itemPrice = 250 * (Integer.parseInt(curtainEdt.getText().trim().isEmpty() ? String.valueOf(1) : curtainEdt.getText().trim()));

                total += 250 * (Integer.parseInt(curtainEdt.getText().trim().isEmpty() ? String.valueOf(1) : curtainEdt.getText().trim()));

                item3 = new Order("curtain", Integer.parseInt(curtainEdt.getText()), 250, itemPrice);

            }

            orderItemsList.add(item3);

        }

        if (checkboxDuvet.isSelected()) {

            double itemPrice = 0.0;

            String value = choiceboxduvet.getValue();

            Order item4;

            if(value.equalsIgnoreCase("medium")){

                double duvet = 400.00;
                itemPrice = duvet * (Integer.parseInt(duvetEdt.getText().trim().isEmpty() ? String.valueOf(1) : duvetEdt.getText().trim()));

                total += duvet * (Integer.parseInt(duvetEdt.getText().trim().isEmpty() ? String.valueOf(1) : duvetEdt.getText().trim()));

                item4 = new Order("duvet", ((Integer.parseInt(duvetEdt.getText().trim().isEmpty() ? String.valueOf(1) : duvetEdt.getText().trim()))), duvet, itemPrice);

            } else {

                itemPrice = 250 * (Integer.parseInt(duvetEdt.getText().trim().isEmpty() ? String.valueOf(1) : duvetEdt.getText().trim()));

                total += 450 * (Integer.parseInt(duvetEdt.getText().trim().isEmpty() ? String.valueOf(1) : duvetEdt.getText().trim()));

                item4 = new Order("duvet", (Integer.parseInt(duvetEdt.getText().trim().isEmpty() ? String.valueOf(1) : duvetEdt.getText().trim())), 450, itemPrice);

            }

            orderItemsList.add(item4);

        }

        if (checkboxCarpet.isSelected()) {

            double carpet = 500.00;
            double itemPrice = carpet * (Integer.parseInt(carpetEdt.getText().trim().isEmpty() ? String.valueOf(1) : carpetEdt.getText().trim()));

            total += carpet * (Integer.parseInt(carpetEdt.getText().trim().isEmpty() ? String.valueOf(1) : carpetEdt.getText().trim()));

            Order item5 = new Order("carpet", ((Integer.parseInt(carpetEdt.getText().trim().isEmpty() ? String.valueOf(1) : carpetEdt.getText().trim()))), carpet, itemPrice);

            orderItemsList.add(item5);
        }


        return total;
    }

    private void fileChoiceBoxItems(){

        choiceboxcurtain.getItems().addAll(choices);
        choiceboxduvet.getItems().addAll(choices);
    }

    private void pageSetup(List<Order> receiptList, Stage owner)    {
        // Create the PrinterJob
        PrinterJob job = PrinterJob.createPrinterJob();

        if (job == null)
        {
            return;
        }

        // Show the page setup dialog
        boolean proceed = job.showPageSetupDialog(owner);

        if (proceed)
        {
            //print(job, node);
            String orderid = " ";
            new PrintReceipt(receiptList, getAmount(), orderid);
        }
    }

    private void print(PrinterJob job, Node node) {
        // Set the Job Status Message
        //jobStatus.textProperty().bind(job.jobStatusProperty().asString());
        System.out.println(job.jobStatusProperty().asString());

        // Print the node
        boolean printed = job.printPage(node);

        if (printed)
        {
            job.endJob();
        }
    }

    public void checkMysqlConnecion(){
        boolean flag = new MysqlCon().getMysqlCon();

        if (!flag) {

            infoBox("Please ensure connection to database is established then Restart Application", "Database Connection", "Failed");
            System.exit(0);

        }
    }

}
