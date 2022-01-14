package com.example.demo.database;

import com.example.demo.data.*;
import java.sql.*;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;

public class MysqlCon{

    private static final String DATABASE_URL = "jdbc:mysql://localhost:3306/loundramat_db?useSSL=false";
    private static final String DATABASE_USERNAME = "root";
    private static final String DATABASE_PASSWORD = "";
    private static final String SELECT_QUERY = "SELECT * FROM users WHERE username = ? and password = ? and status = ?";
    private static final String INSERT_QUERY = "INSERT INTO users (firstname, lastname, password, email, number, username) VALUES ( ?, ?, ?, ?, ?, ? )";
    private static final String INSERT_TRANSACTION = " INSERT INTO laundryorder (orderid, userid, amount) VALUES ( ?,?,?)";
    private static final String INSERT_TRANSACTION_ITEMS = " INSERT INTO order_items (orderitemid, name, quantity, price, total) VALUES ( ?, ?, ?, ?, ?)";
    private static final String SEARCH_QUERY = "SELECT * FROM order_items WHERE orderitemid = ?";
    private static final String SELECT_USERS = "SELECT * FROM users";
    private static final String DELETE_USER = "UPDATE TABLE users SET status = ? WHERE id = ?";
    private static final String UPDATE_USER = "UPDATE TABLE users SET firstname = ?, lastname = ?, password = ?, email = ?, number = ?, username = ? WHERE id = ?";
    private static final String GET_LINE_CHART_DATA = "SELECT SUM(amount) AS total_amount from laundryorder WHERE created_at = ?"; //daily
    private static final String GET_BAR_CHART_DATA = "SELECT SUM(amount) AS total_amount from laundryorder WHERE created_at BETWEEN ? AND ?"; //monthly
    private static final String GET_PIE_CHART_DATA = "SELECT name, SUM(total) AS total_sales from order_items GROUP BY name";
    private static final String GET_TOTAL_SALES = "SELECT SUM(amount) AS total_sales from laundryorder";
    private static final String GET_TODAY_SALES = "SELECT SUM(amount) AS today_sales from laundryorder WHERE created_at = ?";
    private static final String GET_TOP_ITEM = "SELECT name, SUM(total) AS total_sales FROM order_items GROUP BY name ORDER BY total_sales DESC";

    private List<Order> orderReceiptList;

    public boolean getMysqlCon() {
        final String CHECK_SQL_QUERY = "SELECT 1";
        boolean isConnected = false;

        try (Connection connection = DriverManager
                .getConnection(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD);

             PreparedStatement preparedStatement = connection.prepareStatement(CHECK_SQL_QUERY)) {

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return true;
            }

        } catch (SQLException e) {

        }
        return false;
    }

    public boolean getCon(){

        try{

            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con=DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/loundramat_db","root","");
            Statement stmt=con.createStatement();
            ResultSet rs=stmt.executeQuery("select * from users");

            if (!rs.next()) {
                System.out.println("No Conn");
                return true;
            }

            con.close();

        }catch(Exception e){

            System.out.println(e);

        }

        return false;

    }

    public static void printSQLException(SQLException ex) {
        for (Throwable e: ex) {
            if (e instanceof SQLException) {
                e.printStackTrace(System.err);
                System.err.println("SQLState: " + ((SQLException) e).getSQLState());
                System.err.println("Error Code: " + ((SQLException) e).getErrorCode());
                System.err.println("Message: " + e.getMessage());
                Throwable t = ex.getCause();
                while (t != null) {
                    System.out.println("Cause: " + t);
                    t = t.getCause();
                }
            }
        }
    }

    public boolean validate(String emailId, String password) throws SQLException {

        try (Connection connection = DriverManager
                .getConnection(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD);

             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_QUERY)) {
            preparedStatement.setString(1, emailId);
            preparedStatement.setString(2, password);
            preparedStatement.setInt(3, 1);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return true;
            }

        } catch (SQLException e) {

            printSQLException(e);
        }

        return false;
    }

    public boolean create(String fname, String lname, String email, String number, String username, String password) throws SQLException{

        try (Connection connection = DriverManager
                .getConnection(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD);

             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_QUERY)) {
            preparedStatement.setString(1, fname);
            preparedStatement.setString(2, lname);
            preparedStatement.setString(3, password);
            preparedStatement.setString(4, email);
            preparedStatement.setString(5, number);
            preparedStatement.setString(6, username);

            //System.out.println(preparedStatement);

            int resultSet = preparedStatement.executeUpdate();
            //System.out.println(resultSet);

            if (resultSet == 1) {

                return true;
            }

        } catch (SQLException e) {
            // print SQL exception information
            printSQLException(e);
        }

        return false;
    }

    public boolean createTransaction(List<Order> ordersList, String orderId, Double amount) throws SQLException{

        int step1 = 0;
        int step2 = 0;

        this.orderReceiptList = ordersList;

        try (Connection connection = DriverManager
                .getConnection(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD);

             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_TRANSACTION)) {
            preparedStatement.setString(1, orderId);
            preparedStatement.setInt(2, 1);
            preparedStatement.setDouble(3, amount);

            //System.out.println(preparedStatement);

            int resultSet = preparedStatement.executeUpdate();

            if (resultSet == 1) {
                step1 = 1;
            }

        } catch (SQLException e) {
            // print SQL exception information
            printSQLException(e);
        }

        if( step1 == 1) {

            for (Order model : ordersList) {

                try (Connection connection = DriverManager
                        .getConnection(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD);

                     PreparedStatement preparedStatement = connection.prepareStatement(INSERT_TRANSACTION_ITEMS)) {
                    preparedStatement.setString(1, orderId);
                    preparedStatement.setString(2, model.getName());
                    preparedStatement.setInt(3, model.getQuantity());
                    preparedStatement.setDouble(4, model.getPrice());
                    preparedStatement.setDouble(5, model.getTotal());

                    int resultSet = preparedStatement.executeUpdate();

                    if (resultSet == 1) {
                        step2++;
                    }

                } catch (SQLException e) {
                    // print SQL exception information
                    printSQLException(e);
                }
            }
        }

        return step1 == 1 && (step2 == ordersList.size());

    }

    public List<Order> search(String orderId){

        List<Order> resultList = new ArrayList<>();

        Order searchOrder;

        try (Connection connection = DriverManager
                .getConnection(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD);

             PreparedStatement preparedStatement = connection.prepareStatement(SEARCH_QUERY)) {
            preparedStatement.setString(1, orderId);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {

                searchOrder = new Order(resultSet.getString("name"), resultSet.getInt("quantity"), resultSet.getDouble("price"), resultSet.getDouble("total"));
                resultList.add(searchOrder);

            }

            return resultList;

        } catch (SQLException e) {
            // print SQL exception information
            printSQLException(e);
        }

        return null;
    }

    public ResultSet getUsers(){

        try (Connection connection = DriverManager
                .getConnection(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD);

             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_USERS)) {

            System.out.println(preparedStatement);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet;
            }

        } catch (SQLException e) {
            // print SQL exception information
            printSQLException(e);
        }

        return null;
    }

    public boolean updateUser(String fname, String lname, String email, String number, String username, String password, int id){

//        try (Connection connection = DriverManager
//                .getConnection(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD);
//
//             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_USER)) {
//            preparedStatement.setString(1, fname);
//            preparedStatement.setString(2, lname);
//            preparedStatement.setString(3, email);
//            preparedStatement.setString(4, number);
//            preparedStatement.setString(5, username);
//            preparedStatement.setString(6, password);
//            preparedStatement.setInt(7, id);
//
//            //System.out.println(preparedStatement);
//
//            int resultSet = preparedStatement.executeUpdate();
//
//            if (resultSet == 1) {
//                return true;
//            }
//
//        } catch (SQLException e) {
//            // print SQL exception information
//            printSQLException(e);
//        }

        return false;
    }

    public boolean deleteUser(int id){

//        try (Connection connection = DriverManager
//                .getConnection(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD);
//
//             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_USER)) {
//            preparedStatement.setInt(1, 0);
//            preparedStatement.setInt(2, id);
//
//            //System.out.println(preparedStatement);
//
//            int resultSet = preparedStatement.executeUpdate();
//
//            if (resultSet == 1) {
//                return true;
//            }
//
//        } catch (SQLException e) {
//            // print SQL exception information
//            printSQLException(e);
//        }

        return false;
    }

    public ArrayList<BarChartData> getBarChartData(){
        //get total monthly sales and display in monthly basis
        //total amount per month
        String[] months = new DateFormatSymbols().getMonths();
        ArrayList<LastDaysOfMonth> monthsList = getLastDaysOfMonth(2022);
        ArrayList<BarChartData> barChartData = new ArrayList<>();

         for (int i = 0; i < monthsList.size(); i++) {

             try (Connection connection = DriverManager
                     .getConnection(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD);

                  PreparedStatement preparedStatement = connection.prepareStatement(GET_BAR_CHART_DATA)) {
                 preparedStatement.setString(1, monthsList.get(i).getFirstDay());
                 preparedStatement.setString(2, monthsList.get(i).getLastDay());

                 ResultSet resultSet = preparedStatement.executeQuery();
                 if (resultSet.next()) {
                     BarChartData dataObject = new BarChartData(months[i], resultSet.getDouble("total_amount"));
                     barChartData.add(dataObject);
                 }

             } catch (SQLException e) {
                 // print SQL exception information
                 printSQLException(e);
             }
         }

        return barChartData;
    }

    public ArrayList<PieChartData> getPieChartData(){

        //get individual items sales in total and show against its name
        //get total indvidual amount
        ArrayList<PieChartData> pieListData = new ArrayList<>();

            try (Connection connection = DriverManager
                    .getConnection(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD);

                 PreparedStatement preparedStatement = connection.prepareStatement(GET_PIE_CHART_DATA)) {

                ResultSet resultSet = preparedStatement.executeQuery();

                while(resultSet.next()){
                    PieChartData dataListObject = new PieChartData(resultSet.getString("name"), resultSet.getDouble("total_sales"));
                    pieListData.add(dataListObject);
                }

            } catch (SQLException e) {
                // print SQL exception information
                printSQLException(e);
            }


        return pieListData;
    }

    public ArrayList<LineChartData> getLineChartData(){

        //get daily sales and total amount and represent on line graph
        //get total daily daily sales
        Calendar now = Calendar.getInstance();

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        String[] days = new String[7];

        int delta = -now.get(GregorianCalendar.DAY_OF_WEEK) + 1; //add 2 if your week start on monday

        now.add(Calendar.DAY_OF_MONTH, delta );

        ArrayList<LineChartData> lineList = new ArrayList<>();

        for (int i = 0; i < 7; i++)
        {
            days[i] = format.format(now.getTime());
            now.add(Calendar.DAY_OF_MONTH, 1);

            try (Connection connection = DriverManager
                    .getConnection(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD);

                 PreparedStatement preparedStatement = connection.prepareStatement(GET_LINE_CHART_DATA)) {
                preparedStatement.setString(1, days[i]);

                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    LineChartData lineDataObject = new LineChartData(days[i], resultSet.getDouble("total_amount"));
                    lineList.add(lineDataObject);
                    //System.out.println(resultSet.getDouble("total_amount")+" "+days[i]);
                }

            } catch (SQLException e) {
                // print SQL exception information
                printSQLException(e);
            }
        }

        return lineList;

    }

    public ArrayList<LastDaysOfMonth> getLastDaysOfMonth(int year){

        ArrayList<LastDaysOfMonth> monthsList = new ArrayList<LastDaysOfMonth>();

        for (int i = 1; i <= 12; i++){

            YearMonth yearMonth = YearMonth.of( year, i );
            LocalDate firstOfMonth = yearMonth.atDay( 1 );
            LocalDate lastOfMonth = yearMonth.atEndOfMonth();
            LastDaysOfMonth days = new LastDaysOfMonth(firstOfMonth, lastOfMonth);
            monthsList.add(days);

        }

        return monthsList;
    }

    public Map<String, String> getRowValues(){

        Map<String, String> map = new HashMap<>();

        map.put("totalsales", getTotalSales());
        map.put("todaysales", getTodaySales());
        map.put("topitem", getTopItem());

        return map;
    }

    public String getTotalSales(){

        try (Connection connection = DriverManager
                .getConnection(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD);

             PreparedStatement preparedStatement = connection.prepareStatement(GET_TOTAL_SALES)) {

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("total_sales");
            }

        } catch (SQLException e) {
            // print SQL exception information
            printSQLException(e);
        }

        return "0.0";
    }

    public String getTodaySales(){

        LocalDate todaysDate = LocalDate.now();

        try (Connection connection = DriverManager
                .getConnection(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD);

             PreparedStatement preparedStatement = connection.prepareStatement(GET_TODAY_SALES)) {
            preparedStatement.setString(1, String.valueOf(todaysDate));

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("today_sales");
            }

        } catch (SQLException e) {
            // print SQL exception information
            printSQLException(e);
        }

        return "0.0";
    }

    public String getTopItem(){

        try (Connection connection = DriverManager
                .getConnection(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD);

             PreparedStatement preparedStatement = connection.prepareStatement(GET_TOP_ITEM)) {

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("name");
            }

        } catch (SQLException e) {
            // print SQL exception information
            printSQLException(e);
        }
        return "null";
    }

    public List<Order> getReceiptListData(){
        return orderReceiptList;
    }
}
