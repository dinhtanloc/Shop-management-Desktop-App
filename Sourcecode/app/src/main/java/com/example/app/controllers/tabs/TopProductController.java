package com.example.app.controllers.tabs;

import com.example.app.controllers.SatisticWindowController;
import com.example.app.models.Category;
import com.example.app.models.Order;
import com.example.app.utils.MySQLConnector;
import javafx.animation.PauseTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.util.Duration;

import java.net.URL;
import java.sql.ResultSet;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;

public class TopProductController implements Initializable {
    @FXML
    private BarChart<String, Integer> barChart;

    @FXML
    private Label firstDep;


    @FXML
    private TableColumn<Order, String> proCol;

    @FXML
    private TableView<Order> proView;




    @FXML
    private TableColumn<Order, String> topCol;

    @FXML
    private Label typeDes;

    @FXML
    private Label revProduct;
    ObservableList<Order> toplst= FXCollections.observableArrayList();
    ObservableList<Category> categories=FXCollections.observableArrayList();
    SatisticWindowController sta=new SatisticWindowController();
    ArrayList<Integer> propoint = new ArrayList<>();


    private LocalDate fromDate;
    private LocalDate toDate;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setTableView();



    }

    private void barChart(ArrayList<Integer> point) {
            System.out.println("size point"+point.size());
            System.out.println("size top"+toplst.size());
        System.out.println("---");
        XYChart.Series<String, Integer> series = new XYChart.Series<>();
        series.getData().add(new XYChart.Data<>(toplst.get(0).getName(), point.get(0)));
        series.getData().add(new XYChart.Data<>(toplst.get(1).getName(), point.get(1)));
        series.getData().add(new XYChart.Data<>(toplst.get(2).getName(), point.get(2)));
        series.getData().add(new XYChart.Data<>(toplst.get(3).getName(), point.get(3)));
        series.getData().add(new XYChart.Data<>(toplst.get(4).getName(), point.get(4)));


        barChart.getData().add(series);
        barChart.lookup(".chart-plot-background").setStyle("-fx-background-color: transparent");


    }

    public void receiveDate1s(LocalDate FromDate, LocalDate ToDate) {
        this.fromDate = FromDate;
        this.toDate = ToDate;
            System.out.println("heheheh");

        if (fromDate ==null && toDate==null){
//            barChart(propoint);
            loadTable();
        }
        else{
            loadTable(fromDate,toDate);

        }
        barChart(propoint);

        loadCategory();
        System.out.println(fromDate+"dsahcfsodcjds"+toDate);

        for(int i=0;i<categories.size();i++){
            Category categoryname=categories.get(i);
            if(toplst.isEmpty()){
                System.out.println("okk");
            }

            if(categoryname.getDescription().equals(toplst.get(0).getName())){
                typeDes.setText(categoryname.getType());
                firstDep.setText(categoryname.getDescription());
            }
        }
        Locale locale = new Locale("vn", "VN");
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(locale);
        revProduct.setText(currencyFormatter.format(Revenue(firstDep.getText())));
//        PauseTransition pause = new PauseTransition(Duration.seconds(0.25));
//
//// Đặt hành động sẽ được thực hiện sau khi độ trễ kết thúc
//        pause.setOnFinished(event -> {
//            // Các hành động bạn muốn thực hiện sau 5 giây
//            barChart(propoint,toplst);
////            pieChart(saler_percent.get(0).getPercent(),saler_percent.get(1).getPercent());
//            System.out.println("Độ trễ đã hoàn thành!");
//        });
//
//// Bắt đầu độ trễ
//



    }
    void setTableView(){
        proView.setItems(toplst);
        topCol.setCellValueFactory(param -> param.getValue().topProperty().asString());
        proCol.setCellValueFactory(param -> param.getValue().nameProperty());

        //Chu y mai mot doi chieu lai

    }
    void loadTable(LocalDate fromDate, LocalDate toDate){
        toplst.clear();
        MySQLConnector mySQLConnector = MySQLConnector.getInstance();
        try{
            String sql= String.format("""
                        WITH cte AS(
                                SELECT DISTINCT Description,COUNT(*) OVER(PARTITION BY Description ) AS point
                                FROM donhang
                                JOIN danhmucsp d on donhang.ProductID = d.ProductID    
                                WHERE OrderDate BETWEEN '%s' AND '%s'                        
                                ORDER BY point DESC
                            
                            )
                            SELECT  RANK() OVER ( ORDER BY point DESC) ,Description,point
                            FROM cte
                            LIMIT 5                                               
                    """,fromDate,toDate);
            ResultSet resultSet = mySQLConnector.queryResults(sql);
            while(resultSet.next()){
                int top = resultSet.getInt(1);
                String Description = resultSet.getString(2);
                Order check = new Order(top, Description);
                propoint.add(resultSet.getInt(3));
                toplst.add(check);
            }
        }catch (Exception ex){
            System.out.println(ex);
        }

    }
    void loadTable(){
        toplst.clear();
        MySQLConnector mySQLConnector = MySQLConnector.getInstance();
        try{
            String sql= """
                    WITH cte AS(
                                SELECT DISTINCT Description,COUNT(*) OVER(PARTITION BY Description ) AS point
                                FROM donhang
                                JOIN danhmucsp d on donhang.ProductID = d.ProductID                            
                                ORDER BY point DESC
                            
                            )
                            SELECT  RANK() OVER ( ORDER BY point DESC) ,Description,point
                            FROM cte
                            LIMIT 5
                    """;
            System.out.println("bibi");
            System.out.println(sql);
            ResultSet resultSet = mySQLConnector.queryResults(sql);
            while(resultSet.next()){
                int top = resultSet.getInt(1);
                String Description = resultSet.getString(2);
                System.out.println("okdaynaty"+top+Description);
                Order check = new Order(top, Description);
                propoint.add(resultSet.getInt(3));
                toplst.add(check);
            }
        }catch (Exception ex){
            System.out.println(ex);
        }

    }
    long Revenue(String Description){
        MySQLConnector mySQLConnector = MySQLConnector.getInstance();
        try{
            String sql= String.format("""
                    SELECT SUM(Totalcost) FROM donhang
                    JOIN danhmucsp d on donhang.ProductID = d.ProductID
                    WHERE d.Description='%s'
                    """,Description);
            System.out.println("bibi");
            System.out.println(sql);
            ResultSet resultSet = mySQLConnector.queryResults(sql);
            if(resultSet.next()){
                return resultSet.getLong(1);
            }
        }catch (Exception ex){
            System.out.println(ex);
        }
        return 0;
    }
    void loadCategory(){
        categories.clear();
        MySQLConnector mySQLConnector = MySQLConnector.getInstance();
        try{
            ResultSet resultSet = mySQLConnector.queryResults("select ProductID,Type,Description,UnitCost from danhmucsp  " );
            while(resultSet.next()){
                int ProductID = resultSet.getInt(1);
                String Type = resultSet.getString(2);
                String Description = resultSet.getString(3);
                long price = resultSet.getLong(4);
                Category category = new Category(ProductID,"null",Type, Description, price);
                categories.add(category);
            }

        }catch (Exception ex){
            System.out.println(ex);
        }
    }

    public LocalDate getFromDate() {
        return fromDate;
    }

    public LocalDate getToDate() {
        return toDate;
    }
}
