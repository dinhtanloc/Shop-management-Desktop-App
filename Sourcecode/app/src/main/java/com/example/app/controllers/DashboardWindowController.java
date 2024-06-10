package com.example.app.controllers;

import com.example.app.models.Order;
import com.example.app.utils.MySQLConnector;
import com.example.app.utils.Storing;
import javafx.animation.PauseTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.*;
import javafx.scene.control.Label;
import javafx.util.Duration;

import java.net.URL;
import java.sql.ResultSet;
import java.text.NumberFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;

public class DashboardWindowController implements Initializable {

    @FXML
    private Label chargepro;

    @FXML
    private Label netrev;

    @FXML
    private Label ore;

    @FXML
    private Label qtyorder;

    @FXML
    private Label qtyproduct;

    @FXML
    private Label refund;

    @FXML
    private Label rev;

    @FXML
    private Label shortage;

    @FXML
    private Label soon;
    private  CategoryAxis xAxis = new CategoryAxis();
    private  NumberAxis yAxis = new NumberAxis();
    @FXML
    private Label storage;
    @FXML
    private Label totalproduct;
    @FXML
    private Label LocDinh;
    @FXML
    private Label TuanAnh;
    @FXML
    private PieChart pieChart;
    @FXML
    private LineChart<String,Number> chartLine=new LineChart<>(xAxis, yAxis);
    ;
    long rev_variable;
    Order order=new Order();
    ObservableList<Order> revinday = FXCollections.observableArrayList();
    ObservableList<Order> topproductinday=FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        PauseTransition pause=new PauseTransition(Duration.millis(0.1));
        pause.setOnFinished(event -> {
            iniLineChart();
            topproduct();
            pieChart(topproductinday);

        });
        pause.play();
        Locale locale = new Locale("vn", "VN");
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(locale);

        qtyorder.setText(Integer.toString(totalorder()));
        qtyproduct.setText(Integer.toString(product_invent()));
        chargepro.setText(Integer.toString(productoff_invent()));
        totalproduct.setText(Integer.toString(producton_invent()));
        storage.setText(Integer.toString(productin_invent()));
        iniLineChart();
        netrev.setText(currencyFormatter.format(catulate_netrev()));
        rev.setText(currencyFormatter.format(catulate_netrev()));
        ore.setText(Integer.toString(amountOrder()));
        refund.setText("0");
        storage.setText(Integer.toString(quantityProduct()));
        shortage.setText((Integer.toString(countShortageproduct())));
        soon.setText(Integer.toString(countShortageproduct()*3));
        if (Storing.getValueToPreferences("username").equals("locdinh.myshop@gmail.com")){
            LocDinh.setText("ON");
            LocDinh.setStyle("-fx-text-fill: green");
            TuanAnh.setText("OFF");
            TuanAnh.setStyle("-fx-text-fill: red");

        }else{
            LocDinh.setText("OFF");
            LocDinh.setStyle("-fx-text-fill: red");
            TuanAnh.setText("ON");
            TuanAnh.setStyle("-fx-text-fill: green");
        }
//        topproduct();
//        pieChart(topproductinday);

    }
    public void pieChart(ObservableList<Order> hot_lst) {
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        for(int i=0;i<hot_lst.size();i++) {
            Order order = hot_lst.get(i);
            pieChartData.add(new PieChart.Data(order.getName_product(), order.getQuantity_hotlst()));
        }
        pieChart.setData(pieChartData);
    }
    void topproduct(){
        topproductinday.clear();
        MySQLConnector mySQLConnector = MySQLConnector.getInstance();
        String sql= """
                SELECT Type,SUM(InventoryQty)
                         FROM dskiemke
                         GROUP BY  Type
                """;
        System.out.println(sql);
        try{
            ResultSet resultSet = mySQLConnector.queryResults(sql);
            while(resultSet.next()){
                String description=resultSet.getString(1);
                long total=resultSet.getLong(2);
                Order order=new Order(description,total);
                topproductinday.add(order);
            }

        }catch (Exception ex){
            System.out.println(ex);
        }

    }

    void iniLineChart(){
        caculateMonth();

        xAxis.setLabel("Day of Week");
        xAxis.setTickLabelRotation(90);
        yAxis.setLabel("Value");

        // Lấy ngày hiện tại và ngày trong tuần
        LocalDate today = LocalDate.now();
        DayOfWeek dayOfWeek = today.getDayOfWeek();
//
//        // Khởi tạo dữ liệu cho LineChart
        XYChart.Series<String, Number> series = new XYChart.Series<>();
//
//        // Thêm dữ liệu cho từng ngày từ đầu tuần cho đến ngày hiện tại
        for (int i = 1; i <= dayOfWeek.getValue(); i++) {
            DayOfWeek currentDay = DayOfWeek.of(i);
//            System.out.println(revinday.get(0).getName_product()+"  "+revinday.get(0).getQuantity_hotlst());
//            if(revinday.isEmpty()){
//                series.getData().add(new XYChart.Data<>(currentDay.name().substring(0, 3), 0));
//
//            }
//            else{
//                Order abc = revinday.get(i - 1);
//                // Giả sử dữ liệu là một số ngẫu nhiên từ 1 đến 100
//                series.getData().add(new XYChart.Data<>(currentDay.name().substring(0, 3), abc.getQuantity_hotlst()));
//            }
            try {
                Order abc = revinday.get(i - 1);
                // Giả sử dữ liệu là một số ngẫu nhiên từ 1 đến 100
                series.getData().add(new XYChart.Data<>(currentDay.name().substring(0, 3), abc.getQuantity_hotlst()));
            }catch (Exception e) {
                series.getData().add(new XYChart.Data<>(currentDay.name().substring(0, 3), 0));
            }
        }
        chartLine.getData().add(series);
        chartLine.lookup(".chart-plot-background").setStyle("-fx-background-color: transparent");
        series.getNode().setStyle("-fx-stroke: #b98080");


    }

    void caculateMonth(){
        revinday.clear();
        MySQLConnector mySQLConnector = MySQLConnector.getInstance();
        String sql= """
                 SELECT Sum(Totalcost)
                         FROM donhang
                         WHERE
                             CASE
                                 WHEN DAYOFWEEK(CURDATE()) = 1 THEN OrderDate BETWEEN CONCAT(DATE_SUB(CURDATE(), INTERVAL 6 DAY), ' 00:00:00') AND NOW() -- Chủ Nhật
                                 WHEN DAYOFWEEK(CURDATE()) = 2 THEN OrderDate BETWEEN CONCAT(CURDATE(), ' 00:00:00') AND NOW() -- Thứ Hai
                                 WHEN DAYOFWEEK(CURDATE()) = 3 THEN OrderDate BETWEEN CONCAT(DATE_SUB(CURDATE(), INTERVAL 1 DAY), ' 00:00:00') AND NOW() -- Thứ Ba
                                 WHEN DAYOFWEEK(CURDATE()) = 4 THEN OrderDate BETWEEN CONCAT(DATE_SUB(CURDATE(), INTERVAL 2 DAY), ' 00:00:00') AND NOW() -- Thứ Tư
                                 WHEN DAYOFWEEK(CURDATE()) = 5 THEN OrderDate BETWEEN CONCAT(DATE_SUB(CURDATE(), INTERVAL 3 DAY), ' 00:00:00') AND NOW() -- Thứ Năm
                                 WHEN DAYOFWEEK(CURDATE()) = 6 THEN OrderDate BETWEEN CONCAT(DATE_SUB(CURDATE(), INTERVAL 4 DAY), ' 00:00:00') AND NOW() -- Thứ Sáu
                                 WHEN DAYOFWEEK(CURDATE()) = 7 THEN OrderDate BETWEEN CONCAT(DATE_SUB(CURDATE(), INTERVAL 5 DAY), ' 00:00:00') AND NOW() -- Thứ Bảy
                                 ELSE 1=0
                                 END
                         GROUP BY EXTRACT(DAY FROM OrderDate)
                 
                                     
                """;
        System.out.println(sql);
        try{
            ResultSet resultSet = mySQLConnector.queryResults(sql);
            while(resultSet.next()){
                long rev=resultSet.getLong(1);
                Order order1=new Order("null",rev);
                System.out.println(rev);
                revinday.add(Objects.requireNonNullElseGet(order1, () -> new Order("null",0)));
               }

        }catch (Exception ex){
            System.out.println(ex);
        }

    }

    int productin_invent(){
        MySQLConnector mySQLConnector = MySQLConnector.getInstance();
        String sql= """
               SELECT COUNT(*)
               FROM dskiemke
               WHERE InventoryQty =0
                """;
        System.out.println(sql);
        try{
            ResultSet resultSet = mySQLConnector.queryResults(sql);
            if(resultSet.next()){

                if(resultSet.wasNull()){
                    return 0;
                }
                int total=resultSet.getInt(1);
                return total;

            } else{return 0;}

        }catch (Exception ex){
            System.out.println(ex);
        }

        return 0;

    }

    int productoff_invent(){
        MySQLConnector mySQLConnector = MySQLConnector.getInstance();
        String sql= """
               SELECT COUNT(*)
               FROM dskiemke
               WHERE InventoryQty !=0
                """;
        System.out.println(sql);
        try{
            ResultSet resultSet = mySQLConnector.queryResults(sql);
            if(resultSet.next()){

                if(resultSet.wasNull()){
                    return 0;
                }
                int total=resultSet.getInt(1);
                return total;

            } else{return 0;}

        }catch (Exception ex){
            System.out.println(ex);
        }

        return 0;

    }
    int producton_invent(){
        MySQLConnector mySQLConnector = MySQLConnector.getInstance();
        String sql= """
               SELECT COUNT(*)
               FROM danhmucsp
               
                """;
        System.out.println(sql);
        try{
            ResultSet resultSet = mySQLConnector.queryResults(sql);
            if(resultSet.next()){

                if(resultSet.wasNull()){
                    return 0;
                }
                int total=resultSet.getInt(1);
                return total;

            } else{return 0;}

        }catch (Exception ex){
            System.out.println(ex);
        }

        return 0;

    }

    int product_invent(){
        MySQLConnector mySQLConnector = MySQLConnector.getInstance();
        String sql= String.format("""
               SELECT SUM(Quantity)
               FROM donhang
               WHERE OrderDate BETWEEN '%s 00:00' AND NOW()
                """,getDate());
        System.out.println(sql);
        try{
            ResultSet resultSet = mySQLConnector.queryResults(sql);
            if(resultSet.next()){

                if(resultSet.wasNull()){
                    return 0;
                }
                int total=resultSet.getInt(1);
                return total;

            } else{return 0;}

        }catch (Exception ex){
            System.out.println(ex);
        }

        return 0;

    }
    int totalorder(){
        MySQLConnector mySQLConnector = MySQLConnector.getInstance();
        String sql= String.format("""
               SELECT DISTINCT COUNT(*)
               FROM danhsachdonhang
               WHERE OrderDate BETWEEN '%s 00:00' AND NOW()
                """,getDate());
        System.out.println(sql);
        try{
            ResultSet resultSet = mySQLConnector.queryResults(sql);
            if(resultSet.next()){

                if(resultSet.wasNull()){
                    return 0;
                }
                int total=resultSet.getInt(1);
                return total;

            } else{return 0;}

        }catch (Exception ex){
            System.out.println(ex);
        }

        return 0;
    }
    long catulate_netrev(){
        MySQLConnector mySQLConnector = MySQLConnector.getInstance();
        String sql= String.format("""
               SELECT SUM(Totalcost)
               FROM donhang
               WHERE OrderDate BETWEEN '%s 00:00' AND NOW()
                """,getDate());
        System.out.println(sql);
        try{
            ResultSet resultSet = mySQLConnector.queryResults(sql);
            if(resultSet.next()){

                if(resultSet.wasNull()){
                    return 0;
                }
                long total=resultSet.getLong(1);
                return total;

            } else{return 0;}

        }catch (Exception ex){
            System.out.println(ex);
        }

        return 0;

    }

    int amountOrder(){
        MySQLConnector mySQLConnector = MySQLConnector.getInstance();
        String sql= String.format("""
               SELECT COUNT(*)
               FROM donhang
               WHERE OrderDate BETWEEN '%s 00:00' AND NOW()
                """,getDate());
        System.out.println(sql);
        try{
            ResultSet resultSet = mySQLConnector.queryResults(sql);
            if(resultSet.next()){

                if(resultSet.wasNull()){
                    return 0;
                }
                int total=resultSet.getInt(1);
                return total;

            } else{return 0;}

        }catch (Exception ex){
            System.out.println(ex);
        }

        return 0;

    }

    int refund(){
        return 0;
    }

    int quantityProduct(){
        MySQLConnector mySQLConnector = MySQLConnector.getInstance();
        String sql= """
               SELECT SUM(InventoryQty)
               FROM dskiemke
                """;
        System.out.println(sql);
        try{
            ResultSet resultSet = mySQLConnector.queryResults(sql);
            if(resultSet.next()){

                if(resultSet.wasNull()){
                    return 0;
                }
                int total=resultSet.getInt(1);
                return total;

            } else{return 0;}

        }catch (Exception ex){
            System.out.println(ex);
        }

        return 0;
    }

    int countShortageproduct(){
        MySQLConnector mySQLConnector = MySQLConnector.getInstance();
        String sql= """
               SELECT COUNT(*)
               FROM dskiemke
               WHERE InventoryQty=0
                """;
        System.out.println(sql);
        try{
            ResultSet resultSet = mySQLConnector.queryResults(sql);
            if(resultSet.next()){

                if(resultSet.wasNull()){
                    return 0;
                }
                int total=resultSet.getInt(1);
                return total;

            } else{return 0;}

        }catch (Exception ex){
            System.out.println(ex);
        }

        return 0;
    }

    int soonProduct(){
        return countShortageproduct()*5;
    }

    String getDate(){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return dtf.format(order.getDate());
    }


//    void topproduct(){
//        MySQLConnector mySQLConnector = MySQLConnector.getInstance();
//        String sql= """
//                WITH cte AS (
//                SELECT DISTINCT Description,Sum(Totalcost) OVER(PARTITION BY Description) AS Totalcost
//                FROM donhang d JOIN danhmucsp d2 on d.ProductID = d2.ProductID
//                )
//
//                SELECT DISTINCT RANK() OVER(ORDER BY Totalcost DESC ) AS Top , Description, Totalcost FROM cte
//                LIMIT 3;
//                """;
//        System.out.println(sql);
//        try{
//            ResultSet resultSet = mySQLConnector.queryResults(sql);
//            while(resultSet.next()){
//                int rank = resultSet.getInt(1);
//                String description=resultSet.getString(2);
//                long total=resultSet.getLong(3);
//                Order order=new Order(description,total);
//                hot_lst.add(order);
//            }
//
//        }catch (Exception ex){
//            System.out.println(ex);
//        }
//
//    }
}
