package com.example.app.controllers;

import com.example.app.Main;
import com.example.app.controllers.tabs.RevenueMonthController;
import com.example.app.controllers.tabs.SalaryPersonController;
import com.example.app.controllers.tabs.TopProductController;
import com.example.app.models.Order;
import com.example.app.models.Product;
import com.example.app.utils.Mediator;
import com.example.app.utils.MySQLConnector;
import javafx.animation.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class SatisticWindowController implements Initializable {
    @FXML
    private BarChart<String, Long> barChart;

    @FXML
    private HBox rev_info,hotpro_info,sale_info;

    @FXML
    private Label from;

    @FXML
    private DatePicker fromPicker;


    @FXML
    private LineChart<String,Number> lineChart;
    @FXML
    private void lineChart(ObservableList<Order> rev_month) {
        XYChart.Series<String,Number> series = new XYChart.Series<>();
        for(int i=0;i<rev_month.size();i++){
            Order order1=rev_month.get(i);
            for(int m=1;m<=12;m++){
                if(m== order1.getMonth()) {
                    month=month_arr[m-1];
                }
            }
            XYChart.Data<String, Number> dataPoint = new XYChart.Data<>(month, order1.getCost());
//            series.getData().add(new XYChart.Data<>(month, order1.getCost()));
            series.getData().add(dataPoint);



        }

        lineChart.getData().add(series);
        lineChart.lookup(".chart-plot-background").setStyle("-fx-background-color: transparent");
//        series.getNode().setStyle("-fx-stroke: #d01e1e");



    }
//    @FXML
//    private void lineChart(ObservableList<Order> rev_month) {
//        XYChart.Series<String, Number> series = new XYChart.Series<>();
//        Timeline timeline = new Timeline();
//
//        for (int i = 0; i < rev_month.size(); i++) {
//            Order order1 = rev_month.get(i);
//            String month = ""; // Khởi tạo biến month
//            for (int m = 1; m <= 12; m++) {
//                if (m == order1.getMonth()) {
//                    month = month_arr[m - 1];
//                }
//            }
//            XYChart.Data<String, Number> dataPoint = new XYChart.Data<>(month, order1.getCost());
//            series.getData().add(dataPoint);
//        }
//
//        lineChart.getData().add(series);
//
//        for (int i = 0; i < series.getData().size(); i++) {
//            XYChart.Data<String, Number> dataPoint = series.getData().get(i);
//            Node node = dataPoint.getNode();
//            node.setVisible(false);
//
//            timeline.getKeyFrames().add(new KeyFrame(Duration.millis(i * 300), e -> {
//                node.setVisible(true);
//            }));
//        }
//
//        timeline.play();
//
//        lineChart.lookup(".chart-plot-background").setStyle("-fx-background-color: transparent");
//    }

//    @FXML
//    private void animateChart(LineChart<Number, Number> chart,ObservableList<Order> rev_month) {
//        XYChart.Series<Number, Number> series = new XYChart.Series<>();
//        series.setName("Sample Data");
//
//
//
//        chart.getData().add(series);
//
//        Timeline timeline = new Timeline();
//        for (int i = 0; i < rev_month.size(); i++) {
//            Order order1 = rev_month.get(i);
//            XYChart.Data<Number, Number> dataPoint = new XYChart.Data<>(point[0], point[1]);
//            series.getData().add(dataPoint);
//
//            Node node = dataPoint.getNode();
//            node.setVisible(false);
//
//            timeline.getKeyFrames().add(new KeyFrame(Duration.millis(i * 300), e -> {
//                node.setVisible(true);
//            }));
//        }
//
//        timeline.play();
//    }

    @FXML
    private HBox iconClock;

    @FXML
    private PieChart pieChart;

    @FXML
    private Label to;

    @FXML
    private DatePicker toPicker;

    @FXML
    private VBox vBar;

    @FXML
    private VBox vLine;

    @FXML
    private VBox vPie;
    @FXML
    private Button updateBtn;
    @FXML
    private HBox Hbox_date,box_from,box_to;
    String[] month_arr = {"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};
    String month;

    ObservableList<Order> hot_lst=FXCollections.observableArrayList();
    ObservableList<Order> rev_month=FXCollections.observableArrayList();
    ObservableList<Order> best_saler=FXCollections.observableArrayList();
    Order order;
    private boolean isExpanded = false; // Biến trạng thái
    private boolean isStartDatePicked = false;
    private boolean isEndDatePicked = false;
    private LocalDate fromDate;
    private LocalDate toDate;
    private RevenueMonthController revenueMonthController;
    private SalaryPersonController salaryPersonController;
    private TopProductController topProductController;
    private Boolean checkScreen=false;
//
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupslideDatePickers(-600); // Giả sử -100 là vị trí bạn muốn dịch chuyển đến
        Axis xAxis = lineChart.getXAxis();
        xAxis.setAutoRanging(true);
        Rectangle clip = new Rectangle();
        clip.widthProperty().bind(Hbox_date.widthProperty());
        clip.heightProperty().bind(Hbox_date.heightProperty());
        Hbox_date.setClip(clip);
        iconClock.setOnMouseClicked(event -> {
            if (isExpanded) {
                // Nếu hiện tại đang mở rộng, thì thu gọn lại
                slideDatePickers(-600); // Giả sử -100 là vị trí bạn muốn dịch chuyển đến
            } else {
                // Nếu hiện tại đang thu gọn, thì mở rộng ra
                slideDatePickers(0); // 0 là vị trí ban đầu
            }
            isExpanded = !isExpanded; // Đảo trạng thái
        });
        toPicker.setOnAction(event -> updateChart());

        loadtotalCost_month();
        topproduct();
        best_saler();
//        lineChart.getData().clear();

        barChart(best_saler);
        PauseTransition pause=new PauseTransition(Duration.millis(0.1));
        pause.setOnFinished(event -> {
            lineChart(rev_month);
            pieChart(hot_lst);
            barChart.getData().clear();
            barChart(best_saler);


        });
        pause.play();
//        drawChart();
        lineChart(rev_month);
////        pieChart(hot_lst);

//        rev_info.setOnMouseClicked(event -> {
//            if (event.getClickCount() == 2) {  // Kiểm tra xem có phải là double-click không
//                Stage stage = new Stage();
//                FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("tabs/rev-month.fxml"));
//                Scene scene = null;
//                try {
//                    scene = new Scene(fxmlLoader.load(), 600, 550);
//                } catch (IOException e) {
//                    throw new RuntimeException(e);
//                }
////            stage.setTitle("Login!");
//                stage.setScene(scene);
//                stage.show();
////                ((Node) (event.getSource())).getScene().getWindow().hide();
//            }
//        });
//        hotpro_info.setOnMouseClicked(event -> {
//            if (event.getClickCount() == 2) {  // Kiểm tra xem có phải là double-click không
//                Stage stage = new Stage();
//                FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("tabs/top-product.fxml"));
//                Scene scene = null;
//                try {
//                    scene = new Scene(fxmlLoader.load(), 600, 550);
//                } catch (IOException e) {
//                    throw new RuntimeException(e);
//                }
////            stage.setTitle("Login!");
//                stage.setScene(scene);
//                stage.show();
//                ((Node) (event.getSource())).getScene().getWindow().hide();
//            }
//        });
//
//        sale_info.setOnMouseClicked(event -> {
//            if (event.getClickCount() == 2) {  // Kiểm tra xem có phải là double-click không
//                Stage stage = new Stage();
//                FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("tabs/salary-person.fxml"));
//                Scene scene = null;
//                try {
//                    scene = new Scene(fxmlLoader.load(), 600, 550);
//                } catch (IOException e) {
//                    throw new RuntimeException(e);
//                }
////            stage.setTitle("Login!");
//                stage.setScene(scene);
//                stage.show();
//                ((Node) (event.getSource())).getScene().getWindow().hide();
//            }
//        });

        //lia chuột vào hbox, hiện thông tin sau đó, click đôi để hiện kết quả
        show_hidein4(rev_info,"dashtabs/dashrev-month.fxml","tabs/rev-month.fxml","rev");
        show_hidein4(sale_info,"dashtabs/dashsalary-person.fxml","tabs/salary-person.fxml","sala");
        show_hidein4(hotpro_info,"dashtabs/dashtop-product.fxml","tabs/top-product.fxml","top");


    }
    void drawChart(){
        loadtotalCost_month();
        topproduct();
        best_saler();
//        lineChart.getData().clear();

        lineChart(rev_month);
        pieChart(hot_lst);
        barChart(best_saler);
    }
    public void setRevenueMonthController(RevenueMonthController revenueMonthController) {
        this.revenueMonthController = revenueMonthController;
    }

    public void setSalaryPersonController(SalaryPersonController salaryPersonController) {
        this.salaryPersonController = salaryPersonController;
    }

    public void setTopProductController(TopProductController topProductController) {
        this.topProductController = topProductController;
    }
    public void sendDataToRevcontroller() {
        if (revenueMonthController != null) {
            revenueMonthController.receiveDates(fromDate, toDate);
        }
    }
    public void sendDataToSalcontroller() {
        if (salaryPersonController != null) {
            salaryPersonController.receiveDates(fromDate, toDate);
        }
    }
    public void sendDataToTopcontroller() {
        if (topProductController != null) {
            topProductController.receiveDate1s(fromDate, toDate);
        }
    }









    public LocalDate getFromDate() {
        return fromDate;
    }

    public LocalDate getToDate() {
        return toDate;
    }

    private void show_hidein4(HBox hbox, String link1, String link2,String check){
        Popup popup = new Popup();
        try {
            Pane popupContent = FXMLLoader.load(Main.class.getResource(link1));
            popupContent.setPrefSize(200, 300);  // Thiết lập kích thước mong muốn
            popup.getContent().add(popupContent);
            hbox.setOnMouseExited(event -> {
//                System.out.println(getFromDate()+"    "+getToDate());

                    Point2D mousePoint = new Point2D(event.getScreenX(), event.getScreenY());
                    Node popupContentNode = (Node) popup.getContent().get(0); // Lấy nội dung đầu tiên trong Popup
                    Point2D localPointForPopup = popupContentNode.screenToLocal(mousePoint);
                if (popupContentNode.contains(localPointForPopup)) {
                    popupContent.setOnMouseClicked(ev -> {
                        if (ev.getClickCount() == 2) {  // Kiểm tra xem có phải là double-click không
                            Stage stage = new Stage();
                            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource(link2));
//                            if(stage.isShowing()){
//                                setCheckScreen(true);
//                                System.out.println("conbo cuoi");
//                            }
//                            else {
//                                setCheckScreen(false);
//
//                            }
                            stage.setOnShown(e -> {
                                setCheckScreen(true);
                                System.out.println(checkScreen);
                            });
                            stage.setOnHidden(e -> {
                                setCheckScreen(false);
                            });
                            Scene scene = null;
                            try {
                                scene = new Scene(fxmlLoader.load(), 600, 550);
                                if(check.equals("rev")){
                                    System.out.println("okk");
                                    RevenueMonthController revenueMonthController1=fxmlLoader.getController();
                                    setRevenueMonthController(revenueMonthController1);
                                    sendDataToRevcontroller();
                                }else if(check.equals("top")){
                                    TopProductController topProductController1=fxmlLoader.getController();
                                    setTopProductController(topProductController1);
                                    sendDataToTopcontroller();
                                } else if (check.equals("sala")) {
                                    SalaryPersonController salaryPersonController1=fxmlLoader.getController();
                                    setSalaryPersonController(salaryPersonController1);
                                    sendDataToSalcontroller();
                                }



                                stage.setScene(scene);
                                stage.show();
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
//                            stage.setScene(scene);
//                            stage.show();
//                            ((Node) (event.getSource())).getScene().getWindow().hide();
                        }
                    });
                    popupContent.setOnMouseExited(event1 -> {
                        popup.hide();
                    });
                } else {
                    popup.hide();
                }

            });

        } catch (Exception e) {
            e.printStackTrace();
        }

        hbox.setOnMouseEntered(event -> {
            if(checkScreen){
                return;
            }
            Point2D hboxScreenPosition = hbox.localToScreen(hbox.getBoundsInLocal().getMinX(), hbox.getBoundsInLocal().getMinY());

            // Hiển thị Popup sát bên phải của HBox
            double popupX = hboxScreenPosition.getX() + hbox.getWidth()*0.85;
            double popupY = hboxScreenPosition.getY()-100;

            popup.show(hbox, popupX, popupY);
//            popup.show(hbox, event.getScreenX() + hbox.getWidth(), event.getScreenY());
        });
    }

    private void updateChart(){
//        lineChart.layout();
//        lineChart.getData().clear();
//        pieChart.layout();
//        pieChart.getData().clear();
//        barChart.layout();
        barChart.getData().clear();
//         fromDate = fromPicker.getValue();
//         toDate=toPicker.getValue();
        setFromDate(fromPicker.getValue());
        setToDate(toPicker.getValue());

        if(fromDate==null||toDate==null){
            drawChart();
        }
//        best_saler.clear();
//        hot_lst.clear();
//        rev_month.clear();
        else {
            loadtotalCost_month(fromDate, toDate);
            topproduct(fromDate, toDate);
            best_saler(fromDate, toDate);

            pieChart(hot_lst);
            lineChart(rev_month);
            barChart(best_saler);
        }

    }

    private void setupslideDatePickers(double targetX) {
        KeyValue keyValue1 = new KeyValue(box_from.translateXProperty(), targetX);
        KeyFrame keyFrame1 = new KeyFrame(Duration.seconds(0.05), keyValue1);

        KeyValue keyValue2 = new KeyValue(box_to.translateXProperty(), targetX);
        KeyFrame keyFrame2 = new KeyFrame(Duration.seconds(0.05), keyValue2);

        Timeline timeline = new Timeline(keyFrame1, keyFrame2);
        timeline.play();
    }
    private void slideDatePickers(double targetX) {
        KeyValue keyValue1 = new KeyValue(box_from.translateXProperty(), targetX);
        KeyFrame keyFrame1 = new KeyFrame(Duration.seconds(0.5), keyValue1);

        KeyValue keyValue2 = new KeyValue(box_to.translateXProperty(), targetX);
        KeyFrame keyFrame2 = new KeyFrame(Duration.seconds(0.5), keyValue2);

        Timeline timeline = new Timeline(keyFrame1, keyFrame2);
        timeline.play();
    }

    private void hideDatePickers() {
        TranslateTransition slideOut1 = new TranslateTransition(Duration.seconds(0.5), fromPicker);
        slideOut1.setFromX(0);
        slideOut1.setToX(-100);
        slideOut1.setOnFinished(e -> fromPicker.setVisible(false));

        TranslateTransition slideOut2 = new TranslateTransition(Duration.seconds(0.5), toPicker);
        slideOut2.setFromX(0);
        slideOut2.setToX(-100);
        slideOut2.setOnFinished(e -> toPicker.setVisible(false));

        ParallelTransition parallelTransition = new ParallelTransition(slideOut1, slideOut2);
        parallelTransition.play();
    }

    private void showDatePickers() {
        fromPicker.setVisible(true);
        toPicker.setVisible(true);

        TranslateTransition slideIn1 = new TranslateTransition(Duration.seconds(0.5), toPicker);
        slideIn1.setFromX(-100);
        slideIn1.setToX(0);

        TranslateTransition slideIn2 = new TranslateTransition(Duration.seconds(0.5), fromPicker);
        slideIn2.setFromX(-100);
        slideIn2.setToX(0);

        ParallelTransition parallelTransition = new ParallelTransition(slideIn1, slideIn2);
        parallelTransition.play();
    }
//    @FXML
//    private void barChart(long X,long Y) {
//        final CategoryAxis xAxis = new CategoryAxis();
//        final NumberAxis yAxis = new NumberAxis();
//        final BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
//
//        xAxis.setLabel("Category");
//        yAxis.setLabel("Value");
//
//        XYChart.Series series = new XYChart.Series();
//        series.setName("My Data");
//
//        series.getData().add(new XYChart.Data("Loc Dinh", X));
//        series.getData().add(new XYChart.Data("Tuan Anh", Y));
//
//        barChart.getData().add(series);
//    }

    private void barChart(ObservableList<Order> best_saler) {
        XYChart.Series<String, Long> series = new XYChart.Series<>();
        for(int i=0;i<best_saler.size();i++){
            Order order1=best_saler.get(i);
            series.getData().add(new XYChart.Data<>(order1.getName_product(), order1.getQuantity_hotlst()));

        }
        barChart.getData().add(series);
        barChart.lookup(".chart-plot-background").setStyle("-fx-background-color: transparent");


    }

    public void pieChart(ObservableList<Order> hot_lst) {
            ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        for(int i=0;i<hot_lst.size();i++) {
            Order order = hot_lst.get(i);
            pieChartData.add(new PieChart.Data(order.getName_product(), order.getQuantity_hotlst()));
        }
        pieChart.setData(pieChartData);
    }


    void loadtotalCost_month(LocalDate timef,LocalDate timet){
        rev_month.clear();
        MySQLConnector mySQLConnector = MySQLConnector.getInstance();

        String sql= String.format("""
                SELECT DISTINCT EXTRACT(MONTH FROM OrderDate),
                        SUM(Totalcost) OVER (PARTITION BY EXTRACT(MONTH FROM OrderDate) ORDER BY EXTRACT(MONTH FROM OrderDate) ASC ) AS revenue
                FROM donhang
                WHERE OrderDate Between '%s' AND '%s'
                """,timef,timet);
        System.out.println(sql);
        try{
            ResultSet resultSet = mySQLConnector.queryResults(sql);

            while(resultSet.next()){
                int month = resultSet.getInt(1);
                long total=resultSet.getLong(2);
                Order order=new Order(month,total);
                rev_month.add(order);
            }

        }catch (Exception ex){
            System.out.println(ex);
        }
    }
    void loadtotalCost_month(){
        rev_month.clear();
        MySQLConnector mySQLConnector = MySQLConnector.getInstance();
        String sql="SELECT DISTINCT EXTRACT(MONTH FROM OrderDate),SUM(Totalcost) OVER (PARTITION BY EXTRACT(MONTH FROM OrderDate) ORDER BY EXTRACT(MONTH FROM OrderDate) ) AS revenue FROM donhang ";
        System.out.println(sql);
        try{
            ResultSet resultSet = mySQLConnector.queryResults(sql);
            while(resultSet.next()){
                int month = resultSet.getInt(1);
                long total=resultSet.getLong(2);
                Order order=new Order(month,total);
                rev_month.add(order);
            }


        }catch (Exception ex){
            System.out.println(ex);
        }
    }

    void topproduct(){
        hot_lst.clear();
        MySQLConnector mySQLConnector = MySQLConnector.getInstance();
        String sql= """
                WITH cte AS (
                SELECT DISTINCT Description,Sum(Totalcost) OVER(PARTITION BY Description) AS Totalcost
                FROM donhang d JOIN danhmucsp d2 on d.ProductID = d2.ProductID
                )

                SELECT DISTINCT RANK() OVER(ORDER BY Totalcost DESC ) AS Top , Description, Totalcost FROM cte
                LIMIT 3;
                """;
        System.out.println(sql);
        try{
            ResultSet resultSet = mySQLConnector.queryResults(sql);
            while(resultSet.next()){
                int rank = resultSet.getInt(1);
                String description=resultSet.getString(2);
                long total=resultSet.getLong(3);
                Order order=new Order(description,total);
                hot_lst.add(order);
            }

        }catch (Exception ex){
            System.out.println(ex);
        }

    }
    void topproduct(LocalDate startDate,LocalDate endDate){
        hot_lst.clear();
        MySQLConnector mySQLConnector = MySQLConnector.getInstance();
        String sql= String.format("""
                WITH cte AS (
                SELECT DISTINCT Description,Sum(Totalcost) OVER(PARTITION BY Description) AS Totalcost
                FROM donhang d JOIN danhmucsp d2 on d.ProductID = d2.ProductID
                WHERE OrderDate BETWEEN  '%s' AND  '%s'
                )

                SELECT DISTINCT RANK() OVER(ORDER BY Totalcost DESC ) AS Top , Description, Totalcost FROM cte
                LIMIT 3;
                """,startDate,endDate);
        System.out.println(sql);
        try{
            ResultSet resultSet = mySQLConnector.queryResults(sql);
            while(resultSet.next()){
                int rank = resultSet.getInt(1);
                String description=resultSet.getString(2);
                long total=resultSet.getLong(3);
                Order order=new Order(description,total);
                hot_lst.add(order);
            }

        }catch (Exception ex){
            System.out.println(ex);
        }

    }

    long rbest_saler(String name){
        MySQLConnector mySQLConnector = MySQLConnector.getInstance();
        String sql= String.format("""
               SELECT Saleperson, COUNT(*) FROM donhang
               WHERE Saleperson='%s'
               GROUP BY Saleperson
                """,name);
        System.out.println(sql);
        try{
            ResultSet resultSet = mySQLConnector.queryResults(sql);
            if(resultSet.next()){
                long total=resultSet.getLong(2);
                return total;
            }

        }catch (Exception ex){
            System.out.println(ex);
        } return  0;



    }
    void best_saler(){
        best_saler.clear();
        MySQLConnector mySQLConnector = MySQLConnector.getInstance();
        String sql= """
               SELECT Saleperson, COUNT(*) FROM donhang
               GROUP BY Saleperson
                """;
        System.out.println(sql);
        try{
            ResultSet resultSet = mySQLConnector.queryResults(sql);
            while(resultSet.next()){
                String name = resultSet.getString(1);
                long total=resultSet.getLong(2);
                Order order2=new Order(name,total);
                best_saler.add(order2);

            }

        }catch (Exception ex){
            System.out.println(ex);
        }



    }
    void best_saler(LocalDate startDate,LocalDate endDate){
        best_saler.clear();
        MySQLConnector mySQLConnector = MySQLConnector.getInstance();
        String sql= String.format("""
               SELECT Saleperson, COUNT(*) FROM donhang
               WHERE OrderDate BETWEEN '%s' AND '%s'
               GROUP BY Saleperson
                """,startDate,endDate);
        System.out.println(sql);
        try{
            ResultSet resultSet = mySQLConnector.queryResults(sql);
            while(resultSet.next()){
                String name = resultSet.getString(1);
                long total=resultSet.getInt(2);
                Order order=new Order(name,total);
                best_saler.add(order);
            }

        }catch (Exception ex){
            System.out.println(ex);
        }

    }

    public void setFromDate(LocalDate fromDate) {
        this.fromDate = fromDate;
    }

    public void setToDate(LocalDate toDate) {
        this.toDate = toDate;
    }
    public void setCheckScreen(boolean check){
        this.checkScreen=check;
    }
}
