package com.example.app.controllers.tabs;

import com.example.app.controllers.SatisticWindowController;
import com.example.app.models.Order;
import com.example.app.utils.MySQLConnector;
import javafx.animation.PauseTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.util.Duration;

import java.net.URL;
import java.sql.ResultSet;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.ResourceBundle;

public class RevenueMonthController implements Initializable {
    @FXML
    private Label Recus;

    @FXML
    private Label Reor;

    @FXML
    private Label Repro;

    @FXML
    private Label Rerev;

    @FXML
    private TableView<Order> RevTable;

    @FXML
    private TableColumn<Order, String> Revcus;

    @FXML
    private TableColumn<Order, String> Revtop;

    @FXML
    private PieChart pieChart;
    private LocalDate fromDate;
    private LocalDate toDate;

    ObservableList<Order> top_customer= FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setTableView();

    }
    public void pieChart(long revenue) {
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        pieChartData.add(new PieChart.Data("Profit", revenue*0.4));
        pieChartData.add(new PieChart.Data("Cost", revenue*0.6));

        pieChart.setData(pieChartData);
    }

    public void receiveDates(LocalDate FromDate, LocalDate ToDate) {
        this.fromDate = FromDate;
        this.toDate = ToDate;
        System.out.println(fromDate+"  okk  "+toDate);
        Locale locale = new Locale("vn", "VN");
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(locale);
        if (fromDate ==null && toDate==null){
            System.out.println("no bi null");
            Rerev.setText(currencyFormatter.format(revenue()));
            Recus.setText(Integer.toString(Customer()));
            Reor.setText(Integer.toString(Order()));
            Repro.setText(Integer.toString(Quantity()));
            loadTable();
            PauseTransition pause = new PauseTransition(Duration.seconds(0.25));

// Đặt hành động sẽ được thực hiện sau khi độ trễ kết thúc
            pause.setOnFinished(event -> {
                // Các hành động bạn muốn thực hiện sau 5 giây
                pieChart(revenue());
                System.out.println("Độ trễ đã hoàn thành!");
            });

// Bắt đầu độ trễ
            pause.play();
        }
        else{
            System.out.println("no ko bi null");

//            currencyFormatter.format(revenue(fromDate,toDate));
            Rerev.setText(currencyFormatter.format(revenue(fromDate,toDate)));
//            Re.setText(Long.toString(revenue(fromDate,toDate)));
            Recus.setText(Integer.toString(Customer(fromDate,toDate)));
            Reor.setText(Integer.toString(Order(fromDate,toDate)));
            Repro.setText(Integer.toString(Quantity(fromDate,toDate)));
            loadTable(fromDate,toDate);
            PauseTransition pause = new PauseTransition(Duration.seconds(0.25));

// Đặt hành động sẽ được thực hiện sau khi độ trễ kết thúc
            pause.setOnFinished(event -> {
                // Các hành động bạn muốn thực hiện sau 5 giây
                pieChart(revenue(fromDate,toDate));
                System.out.println("Độ trễ đã hoàn thành!");
            });

// Bắt đầu độ trễ
            pause.play();

        }

    }

    void setTableView(){
        RevTable.setItems(top_customer);
        Revtop.setCellValueFactory(param -> param.getValue().topProperty().asString());
        Revcus.setCellValueFactory(param -> param.getValue().nameProperty());
        //Chu y mai mot doi chieu lai

    }
    void loadTable(LocalDate fromDate, LocalDate toDate){
        top_customer.clear();
        MySQLConnector mySQLConnector = MySQLConnector.getInstance();
        try{
            String sql= String.format("""
                    WITH cte AS(
                        SELECT DISTINCT Name,SUM(Totalcost) OVER(PARTITION BY Name ) AS point
                        FROM donhang
                        WHERE OrderDate BETWEEN '%s' AND '%s'
                        ORDER BY point DESC
                                        
                        )
                    SELECT  RANK() OVER ( ORDER BY point DESC) ,Name
                    FROM cte
                    LIMIT 10
                    """,fromDate,toDate);
            ResultSet resultSet = mySQLConnector.queryResults(sql);
            while(resultSet.next()){
                int top = resultSet.getInt(1);
                String Description = resultSet.getString(2);
                Order check = new Order(top, Description);
                top_customer.add(check);
            }
        }catch (Exception ex){
            System.out.println(ex);
        }

    }
    void loadTable(){
        top_customer.clear();
        MySQLConnector mySQLConnector = MySQLConnector.getInstance();
        try{
            String sql= """
                    WITH cte AS(
                        SELECT DISTINCT Name,SUM(Totalcost) OVER(PARTITION BY Name ) AS point
                        FROM donhang
                        ORDER BY point DESC
                                        
                        )
                    SELECT  RANK() OVER ( ORDER BY point DESC) ,Name
                    FROM cte
                    LIMIT 10
                    """;
            System.out.println("bibi");
            System.out.println(sql);
            ResultSet resultSet = mySQLConnector.queryResults(sql);
            while(resultSet.next()){
                int top = resultSet.getInt(1);
                String Description = resultSet.getString(2);
                Order check = new Order(top, Description);
                top_customer.add(check);
            }
        }catch (Exception ex){
            System.out.println(ex);
        }

    }
    Long revenue(){
        MySQLConnector mySQLConnector = MySQLConnector.getInstance();
        long rev=0;
        try{
            String sql= """
                    SELECT Sum(Totalcost)
                    FROM donhang
                    """;
            System.out.println(sql);
            ResultSet resultSet = mySQLConnector.queryResults(sql);
            if(resultSet.next()){
                rev = resultSet.getLong(1);
                return rev;
            }

        }catch (Exception ex){
            System.out.println(ex);
        }
        return rev;
    }
    Long revenue(LocalDate fromDate, LocalDate toDate){
        MySQLConnector mySQLConnector = MySQLConnector.getInstance();
        long rev=0;

        // Chuyển đổi LocalDate thành chuỗi

        try{
            String sql= String.format("""
                    SELECT Sum(Totalcost)
                    FROM donhang
                    WHERE OrderDate BETWEEN '%s' AND '%s'
                    """,fromDate,toDate);
            ResultSet resultSet = mySQLConnector.queryResults(sql);
            if(resultSet.next()){
                rev = resultSet.getLong(1);
                return rev;
            }

        }catch (Exception ex){
            System.out.println(ex);
        }
        return rev;
    }
    int Customer(){
        MySQLConnector mySQLConnector = MySQLConnector.getInstance();
        int amount=0;
        try{
            String sql= """
                    SELECT COUNT(DISTINCT Name)
                    FROM donhang
                    """;
            System.out.println(sql);

            ResultSet resultSet = mySQLConnector.queryResults(sql);
            while(resultSet.next()){
                amount = resultSet.getInt(1);
                return amount;
            }

        }catch (Exception ex){
            System.out.println(ex);
        }
        return amount;

    }
    int Customer(LocalDate fromDate,LocalDate toDate){
        MySQLConnector mySQLConnector = MySQLConnector.getInstance();
        int amount=0;
        try{
            String sql= String.format("""
                    SELECT COUNT(DISTINCT OrderDate, Name)
                    FROM donhang
                    WHERE OrderDate BETWEEN '%s' AND '%s'
                    """,fromDate,toDate);
            ResultSet resultSet = mySQLConnector.queryResults(sql);
            while(resultSet.next()){
                amount = resultSet.getInt(1);
                return amount;
            }

        }catch (Exception ex){
            System.out.println(ex);
        }
        return amount;

    }
    int Order(){
        MySQLConnector mySQLConnector = MySQLConnector.getInstance();
        int amount=0;
        try{
            String sql= """
                    SELECT COUNT(*)
                    FROM donhang
                    """;
            System.out.println(sql);
            ResultSet resultSet = mySQLConnector.queryResults(sql);
            while(resultSet.next()){
                amount = resultSet.getInt(1);
                return amount;
            }

        }catch (Exception ex){
            System.out.println(ex);
        }
        return amount;

    }
    int Order(LocalDate fromDate,LocalDate toDate){
        MySQLConnector mySQLConnector = MySQLConnector.getInstance();
        int amount=0;
        try{
            String sql= String.format("""
                    SELECT COUNT(*)
                    FROM donhang
                    WHERE OrderDate BETWEEN '%s' AND '%s'
                    """,fromDate,toDate);
            ResultSet resultSet = mySQLConnector.queryResults(sql);
            while(resultSet.next()){
                amount = resultSet.getInt(1);
                return amount;
            }

        }catch (Exception ex){
            System.out.println(ex);
        }
        return amount;

    }
    int Quantity(){
        MySQLConnector mySQLConnector = MySQLConnector.getInstance();
        int amount=0;
        try{
            String sql= """
                    SELECT SUM(Quantity)
                    FROM donhang
                    """;
            ResultSet resultSet = mySQLConnector.queryResults(sql);
            while(resultSet.next()){
                amount = resultSet.getInt(1);
                return amount;
            }

        }catch (Exception ex){
            System.out.println(ex);
        }
        return amount;

    }
    int Quantity(LocalDate fromDate,LocalDate toDate){
        MySQLConnector mySQLConnector = MySQLConnector.getInstance();
        int amount=0;
        try{
            String sql= String.format("""
                    SELECT Sum(Quantity)
                    FROM donhang
                    WHERE OrderDate BETWEEN '%s' AND '%s'
                    """,fromDate,toDate);
            System.out.println(sql);
            ResultSet resultSet = mySQLConnector.queryResults(sql);
            if(resultSet.next()){
                amount = resultSet.getInt(1);
                return amount;
            }

        }catch (Exception ex){
            System.out.println(ex);
        }
        return amount;

    }

    public LocalDate getFromDate() {
        return fromDate;
    }
}
