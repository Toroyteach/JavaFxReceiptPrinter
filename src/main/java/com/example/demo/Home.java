package com.example.demo;

import com.example.demo.data.BarChartData;
import com.example.demo.data.DashboardRowsData;
import com.example.demo.data.LineChartData;
import com.example.demo.data.PieChartData;
import com.example.demo.database.MysqlCon;
import javafx.beans.value.ObservableListValue;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.*;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ArrayList;
import java.util.Map;
import java.util.ResourceBundle;

public class Home implements Initializable {

    @FXML
    private Label totalSales, todaysSales, topItem;

    @FXML
    private LineChart<?, ?> weeklySalesLineGraph;
    private ObservableList<PieChart.Data> weeklyLineList = FXCollections.observableArrayList();;

    @FXML
    private NumberAxis weeklySalesLineGraph_x;

    @FXML
    private CategoryAxis weeklySalesLineGraph_y;

    @FXML
    private PieChart individualItemsPieChart;

    @FXML
    private BarChart<?, ?> monthlyBarGraphSales;

    @FXML
    private CategoryAxis monthlySalesBarChart_x;

    @FXML
    private NumberAxis monthlySalesBarChart_y;

    @FXML
    private ImageView cartImageView, calendarImageView, peopleImageView;


    public Home() {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        checkMysqlConnecion();
        loadDashboardValues();
        loadImages();
    }

    private void loadImages() {

        Image image1 = new Image("/cart.png");
        cartImageView.setImage(image1);

        Image image2 = new Image("/date.png");
        calendarImageView.setImage(image2);

        Image image3 = new Image("/customers.png");
        peopleImageView.setImage(image3);
    }

    public void loadDashboardValues(){
        loadLineGraphDailyValues();
        loadBarGraphMonthlyValues();
        loadPieChartItemValues();
        loadRowValues();
    }

    private void loadRowValues() {
        MysqlCon rowValues = new MysqlCon();
        Map<String, String> rowsValues = rowValues.getRowValues();
        totalSales.setText(rowsValues.get("totalsales"));
        todaysSales.setText(rowsValues.get("todaysales"));
        topItem.setText(rowsValues.get("topitem"));

    }

    private void loadPieChartItemValues() {


        MysqlCon pieChartConn = new MysqlCon();
        ArrayList<PieChartData>  pieList = pieChartConn.getPieChartData();

        for(PieChartData data: pieList){
            weeklyLineList.add(new PieChart.Data(data.getName(), data.getAmount()));
        }

        individualItemsPieChart.setData(weeklyLineList);
    }

    private void loadBarGraphMonthlyValues() {
        MysqlCon newCon = new MysqlCon();
        ArrayList<BarChartData> testList = newCon.getBarChartData();

        monthlySalesBarChart_y.setLabel("Total Amount");
        monthlySalesBarChart_x.setLabel("Months");

        XYChart.Series dataPoints = new XYChart.Series();
        dataPoints.setName("Yearly Sales");

        for(BarChartData student: testList) {
            dataPoints.getData().add(new XYChart.Data(student.getName(), student.getAmount()));
        }


        monthlyBarGraphSales.getData().add(dataPoints);
    }

    private void loadLineGraphDailyValues() {

        XYChart.Series series = new XYChart.Series();

        MysqlCon lineDataConn = new MysqlCon();
        ArrayList<LineChartData> lineData = lineDataConn.getLineChartData();
        for(int i = 0; i < lineData.size(); i++){
            series.getData().add(new XYChart.Data(lineData.get(i).getName(), lineData.get(i).getAmount()));
        }
        weeklySalesLineGraph.getData().addAll(series);
    }

    public void checkMysqlConnecion(){
        boolean flag = new MysqlCon().getMysqlCon();

        if (!flag) {

            infoBox("Please ensure connection to database is established and Restart Application", "Database Connection", "Failed");
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
