package com.example.app.controllers.tabs;

import com.example.app.controllers.SatisticWindowController;
import com.example.app.models.Order;
import com.example.app.utils.MySQLConnector;
import javafx.animation.PauseTransition;
import javafx.beans.property.SimpleStringProperty;
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
import java.util.Locale;
import java.util.ResourceBundle;

public class SalaryPersonController implements Initializable {
    @FXML
    private Label Anhinfo;

    @FXML
    private Label Locinfo;

    @FXML
    private TableColumn<Order, String> nameCol;

    @FXML
    private TableColumn<Order, String> orderCol;

    @FXML
    private PieChart pieChart;

    @FXML
    private TableColumn<Order, String> profitCol;

    @FXML
    private Label quantityEmployee;

    @FXML
    private TableView<Order> staffTableViiew;

    @FXML
    private TableColumn<Order, String> topCol;



    ObservableList<Order> saler_lst= FXCollections.observableArrayList();
    ObservableList<Order> saler_percent= FXCollections.observableArrayList();

    SatisticWindowController stata=new SatisticWindowController();

    private LocalDate fromDate;
    private LocalDate toDate;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setTableView();
    }
    public void pieChart(double Locdinhcheck,double TuanAnhcheck) {
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        pieChartData.add(new PieChart.Data("Loc Dinh", Locdinhcheck));
        pieChartData.add(new PieChart.Data("Tuan Anh", TuanAnhcheck));

        pieChart.setData(pieChartData);
    }
    public void receiveDates(LocalDate FromDate, LocalDate ToDate) {
        this.fromDate = FromDate;
        this.toDate = ToDate;
        if (fromDate ==null && toDate==null){
            quantityEmployee.setText(Integer.toString(Saler()));
            Orderlst();
            loadTable();
        }
        else {
            quantityEmployee.setText(Integer.toString(Saler(fromDate, toDate)));
            Orderlst(fromDate, toDate);
            loadTable(fromDate, toDate);


        }
        if (!saler_percent.isEmpty()) {
            System.out.println(saler_percent.get(0).getPercent());
            Locinfo.setText(Double.toString(saler_percent.get(0).getPercent()));
            Anhinfo.setText(Double.toString(saler_percent.get(1).getPercent()));
            PauseTransition pause = new PauseTransition(Duration.seconds(0.25));

// Đặt hành động sẽ được thực hiện sau khi độ trễ kết thúc
            pause.setOnFinished(event -> {
                // Các hành động bạn muốn thực hiện sau 5 giây
                pieChart(saler_percent.get(0).getPercent(), saler_percent.get(1).getPercent());
                System.out.println("Độ trễ đã hoàn thành!");
            });

// Bắt đầu độ trễ
            pause.play();
        }

    }


    void setTableView(){
        staffTableViiew.setItems(saler_lst);
        topCol.setCellValueFactory(param -> param.getValue().topProperty().asString());
        nameCol.setCellValueFactory(param -> param.getValue().nameProperty());
        orderCol.setCellValueFactory(param -> param.getValue().countlstProperty().asString());
//        profitCol.setCellValueFactory(param -> param.getValue().totalCostProperty().asString());
        profitCol.setCellValueFactory(param ->
        {
            Order order = param.getValue();
            Locale locale = new Locale("vn", "VN");
            NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(locale);
            return new SimpleStringProperty(currencyFormatter.format(order.getTotalPice()));
        });
        //Chu y mai mot doi chieu lai

    }

    void loadTable(LocalDate fromDate, LocalDate toDate){
        saler_lst.clear();
        MySQLConnector mySQLConnector = MySQLConnector.getInstance();
        try{
            String sql= String.format("""
                     WITH CTE AS(
                        SELECT DISTINCT Saleperson, SUM(Totalcost) OVER(PARTITION BY Saleperson) AS Point,COUNT(*) OVER (PARTITION BY Saleperson) AS counting
                        FROM donhang
                        WHERE OrderDate BETWEEN '%s' AND '%s'

                    )
                    SELECT RANK() OVER(ORDER BY Point DESC),Saleperson,counting,Point FROM CTE
                    """,fromDate,toDate);
            System.out.println(sql);
            ResultSet resultSet = mySQLConnector.queryResults(sql);
            while(resultSet.next()){
                int top = resultSet.getInt(1);
                String name = resultSet.getString(2);
                int count=resultSet.getInt(3);
                long point= resultSet.getLong(4);
                Order check = new Order(top, name,count,point);
                saler_lst.add(check);
            }
        }catch (Exception ex){
            System.out.println(ex);
        }

    }
    void loadTable(){
        saler_lst.clear();
        MySQLConnector mySQLConnector = MySQLConnector.getInstance();
        try{
            String sql= """
                    WITH CTE AS(
                        SELECT DISTINCT Saleperson, SUM(Totalcost) OVER(PARTITION BY Saleperson) AS Point,COUNT(*) OVER (PARTITION BY Saleperson) AS counting
                        FROM donhang
                    )
                    SELECT RANK() OVER(ORDER BY Point DESC),Saleperson,counting,Point FROM CTE
                    """;
            System.out.println("bibi");
            System.out.println(sql);
            ResultSet resultSet = mySQLConnector.queryResults(sql);
            while(resultSet.next()){
                int top = resultSet.getInt(1);
                String name = resultSet.getString(2);
                int count=resultSet.getInt(3);
                long point= resultSet.getLong(4);
                Order check = new Order(top, name,count,point);
                saler_lst.add(check);

            }
        }catch (Exception ex){
            System.out.println(ex);
        }

    }

    int Saler(){
        MySQLConnector mySQLConnector = MySQLConnector.getInstance();
        int amount=0;
        try{
            String sql= """
                    SELECT COUNT(DISTINCT Saleperson)
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


    int Saler(LocalDate fromDate,LocalDate toDate){
        MySQLConnector mySQLConnector = MySQLConnector.getInstance();
        int amount=0;
        try{
            String sql= String.format("""
                    SELECT COUNT(DISTINCT Saleperson)
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
        }return amount;

    }
    void Orderlst(){
        saler_percent.clear();
        MySQLConnector mySQLConnector = MySQLConnector.getInstance();
        try{
            String sql= """
                    SELECT Saleperson, COUNT(*)/(SELECT COUNT(*) FROM donhang)*100
                    FROM donhang
                    # WHERE OrderDate BETWEEN '2023-01-01' AND '2023-01-01'
                    GROUP BY Saleperson
                    ORDER BY 1;
                    """;
            System.out.println(sql);

            ResultSet resultSet = mySQLConnector.queryResults(sql);
            while(resultSet.next()){
                double amount = resultSet.getInt(2);
                System.out.println(amount);
                saler_percent.add(new Order(amount));

            }

        }catch (Exception ex){
            System.out.println(ex);
        }

    }

    void Orderlst(LocalDate fromDate,LocalDate toDate){
        saler_percent.clear();
        MySQLConnector mySQLConnector = MySQLConnector.getInstance();
        try{
            String sql= String.format("""
                    SELECT Saleperson, COUNT(*)/(SELECT COUNT(*) FROM donhang)*100
                    FROM donhang
                    WHERE OrderDate BETWEEN '%s' AND '%s'
                    GROUP BY Saleperson
                    ORDER BY 1;
                    """,fromDate,toDate);
            System.out.println(sql);
            System.out.println("check");
            ResultSet resultSet = mySQLConnector.queryResults(sql);
            while(resultSet.next()){
                double amount = resultSet.getDouble(2);
                saler_percent.add(new Order(amount));
            }

        }catch (Exception ex){
            System.out.println(ex);
        }
        System.out.println("check");


    }

}
