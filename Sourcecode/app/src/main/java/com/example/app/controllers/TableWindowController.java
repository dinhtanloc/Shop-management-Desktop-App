package com.example.app.controllers;

import com.example.app.Main;
import com.example.app.models.Inventory;
import com.example.app.models.Order;
import com.example.app.models.Category;
import com.example.app.models.Product;
import com.example.app.utils.Mediator;
import com.example.app.utils.MySQLConnector;
import com.example.app.utils.Storing;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;

public class TableWindowController implements Initializable {
    @FXML
    private Tab Category;

    @FXML
    private TableColumn<Inventory, String> InventDes;

    @FXML
    private TableColumn<Inventory, String> InventID;

    @FXML
    private TableColumn<Inventory, String> InventType;

    @FXML
    private TableColumn<Inventory, String> Inventcost;

    @FXML
    private TableColumn<Inventory, String> InventQty;

    @FXML
    private ToggleButton InventoryBtn;

    @FXML
    private Tab Inventorylst;

    @FXML
    private ToggleButton OrderDetailBtn;

    @FXML
    private TableColumn<Order, LocalDateTime> Order_Date;

    @FXML
    private TableColumn<Order,String> Order_ID;
    @FXML
    private TableColumn<Order,Button> delcol;

    @FXML
    private TableColumn<Order,String> Order_Qty;

    @FXML
    private TableColumn<Order,String> Order_des;

    @FXML
    private TableColumn<Order,String> Order_number;

    @FXML
    private TableColumn<Order,String> Order_sale;

    @FXML
    private TableColumn<Order,String> Order_total;
    @FXML
    private TableColumn<Order,String> Order_name;

    @FXML
    private TableColumn<Order,String> Order_unit;
    @FXML
    private TableView<Order> OrderTableView;
    @FXML
    private TableView<Inventory> InventoryTableView;
    @FXML TableView<Category> CategoryTableView;

    @FXML
    private Tab Orderlst;

    @FXML
    private TableColumn<Category, String> ProID;

    @FXML
    private TableColumn<Category, Image> ProImage;

    @FXML
    private TableColumn<Category, String> ProType;

    @FXML
    private TableColumn<Category, String> Prodes;

    @FXML
    private TableColumn<Category, String> Proprice;

    @FXML
    private ToggleButton categoryBtn;

    @FXML
    private TabPane tab_table;

    @FXML
    private TextField searchOrder;

    @FXML
    private TextField searchCategory;
    @FXML
    private TextField searchInventory;

    @FXML
    private TabPane tab;

    @FXML
    private Pagination pagination_O;

    @FXML
    private Pagination pagination_I;

    @FXML
    private Pagination pagination_C;
    @FXML
    private ComboBox<String> typeCheck;
    private ObservableList<String> danhsach=FXCollections.observableArrayList();


    private static final int ITEMS_PER_PAGE = 7;
    private static final int ITEMS_PER_PAGEI = 10;
    private static final int ITEMS_PER_PAGEO = 9;



    ObservableList<Inventory> inventories = FXCollections.observableArrayList();


    ObservableList<Category> categories = FXCollections.observableArrayList();


    ObservableList<Product> products = FXCollections.observableArrayList();

    ObservableList<Order> orders=FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        categoryBtn.setOnAction(event -> {
            ChangeSelectedMenu(1);
            tab_table.getSelectionModel().select(0);
        });
        OrderDetailBtn.setOnAction(event -> {
            ChangeSelectedMenu(3);
            tab_table.getSelectionModel().select(2);
        });
        InventoryBtn.setOnAction(event -> {
            ChangeSelectedMenu(2);
            tab_table.getSelectionModel().select(1);});




        setTableView_OrderDetail();
        setTableView_Inventory();
        setTableView_Category();
        loadType();
        OrderTableView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) //Checking double click
            {
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("editorder-window.fxml"));
            Scene scene = null;
            try {
                scene = new Scene(fxmlLoader.load(), 881, 618);
                EditOrderWindowController controller = fxmlLoader.getController();
                controller.init(this);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
//            stage.setTitle("Login!");
            stage.setTitle("Thêm Sản Phẩm!");
            stage.setScene(scene);
            stage.show();
            Mediator.getInstance().fireEvent("order");
//            ((Node) (event.getSource())).getScene().getWindow().hide();
            }
        });
        CategoryTableView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) //Checking double click
            {
            try{
                Stage stage = new Stage();
                FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("editorder-window.fxml"));
                Scene scene = new Scene(fxmlLoader.load(), 881, 618);
                EditOrderWindowController controller = fxmlLoader.getController();
                controller.initParams(this,products,"category");
                stage.setTitle("Thêm Sản Phẩm!");
                stage.setScene(scene);
                stage.show();
                Mediator.getInstance().fireEvent("category");
            }catch (Exception ex){
                System.out.println(ex);
            }}

        });

    }
    void ChangeSelectedMenu(int idTab){
        categoryBtn.setSelected(false);
        OrderDetailBtn.setSelected(false);
        InventoryBtn.setSelected(false);
        if (idTab == 1){
            categoryBtn.setSelected(true);
        }
        else if  (idTab == 3){
            OrderDetailBtn.setSelected(true);
        }
        else if  (idTab == 2){
            InventoryBtn.setSelected(true);
        }else{
            categoryBtn.setSelected(false);
            OrderDetailBtn.setSelected(false);
            InventoryBtn.setSelected(false);
        }


    }
    private void typeFilter() {

        //của Lộc
        //loadProducts();
        //của Lộc

        FilteredList<Category> filterData = new FilteredList<>(categories, p -> true);

        typeCheck.valueProperty().addListener((observable, oldValue, newValue) -> {
            filterData.setPredicate(category -> {
                if (newValue == null || newValue.isEmpty()|| newValue.equals("All")) {
                    return true;
                }

                String toLowerCaseFilter = newValue.toLowerCase();
                if (category.getType().toLowerCase().contains(toLowerCaseFilter)) {
                    return true;
                }
                return false;
            });
            updatePaginationC(filterData, CategoryTableView, pagination_C);
        });
        updatePaginationC(filterData, CategoryTableView, pagination_C);

    }

    void setTableView_Category() {
        typeCheck.setItems(danhsach);
        CategoryTableView.setItems(categories);
        ProID.setCellValueFactory(param -> param.getValue().productIDProperty().asString());
        Prodes.setCellValueFactory(param -> param.getValue().descriptionProperty());
        Proprice.setCellValueFactory(param -> {
            Category category = param.getValue();
            Locale locale = new Locale("vn", "VN");
            NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(locale);
            return new SimpleStringProperty(currencyFormatter.format(category.getPrice()));

        });
        ProType.setCellValueFactory(param -> param.getValue().typeProperty());
        ProImage.setCellFactory(param -> {
            final ImageView imageView = new ImageView();
            imageView.setFitHeight(30);
            imageView.setFitWidth(30);
            TableCell<Category, Image> cell = new TableCell<Category, Image>() {
                @Override
                public void updateItem(Image item, boolean empty) {
                    super.updateItem(item, empty);
                    if (!empty) {
                        String filePath = getTableView().getItems().get(getIndex()).getImagePath();
                        if (filePath.equals("")) {
                            filePath = "imgs/default.jpg";
                        }try{
                        String url = Main.class.getResource(filePath).toExternalForm();
                        imageView.setImage(new Image(url));
                        setGraphic(imageView);}
                        catch (Exception e){
                            System.out.println(e);
                        }
                    } else setGraphic(null);

                }
            };

            return cell;
        });
        loadCategory();
        typeFilter();
        FilteredList<Category> filteredData = new FilteredList<>(categories, p -> true);

        searchCategory.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate( category -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                if (category.getDescription().toLowerCase().contains(lowerCaseFilter) ) {
                    return true;
                }

                if (Integer.toString(category.getProductID()).contains(lowerCaseFilter)) {
                    return true;
                }
                if(Long.toString(category.getPrice()).contains(lowerCaseFilter)){
                    return true;
                }


                return false;
            });
            updatePaginationC(filteredData, CategoryTableView, pagination_C);

        });
        updatePaginationC(filteredData, CategoryTableView, pagination_C);
    }
    void deleteProductByID(int productID){
        MySQLConnector mySQLConnector = MySQLConnector.getInstance();
        try{
            String sql =  "DELETE FROM donhang \n" +
                    "WHERE OrderID = " + productID;

            System.out.println(sql);
            mySQLConnector.queryUpdate(sql);
            sql =  "DELETE FROM danhsachdonhang \n" +
                    "WHERE OrderID = " + productID;
            mySQLConnector.queryUpdate(sql);
            loadOrder();
            int pageIndex=pagination_O.getCurrentPageIndex();
            int fromIndex = pageIndex * ITEMS_PER_PAGEO;
            int toIndex = Math.min(fromIndex + ITEMS_PER_PAGEO, orders.size());
            OrderTableView.setItems(FXCollections.observableArrayList(orders.subList(fromIndex, toIndex)));

        }catch (Exception ex){
            System.out.println(ex);
        }
    }

        void setTableView_Inventory(){
            InventID.setCellValueFactory(param -> param.getValue().productIDProperty().asString());
            InventQty.setCellValueFactory(param -> param.getValue().quantityProperty().asString());
            InventDes.setCellValueFactory(param -> param.getValue().descriptionProperty());
            //Chu y mai mot doi chieu lai
            Inventcost.setCellValueFactory(param -> {
                Inventory inventory = param.getValue();
                Locale locale = new Locale("vn", "VN");
                NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(locale);
                return new SimpleStringProperty(currencyFormatter.format(inventory.getUnitcost()));

            });

            InventType.setCellValueFactory(param -> param.getValue().typeProperty());
            loadInventory();
            FilteredList<Inventory> filteredData = new FilteredList<>(inventories, p -> true);

            searchInventory.textProperty().addListener((observable, oldValue, newValue) -> {
                filteredData.setPredicate( inventory -> {
                    if (newValue == null || newValue.isEmpty()) {
                        return true;
                    }

                    String lowerCaseFilter = newValue.toLowerCase();

                    if (inventory.getDescription().toLowerCase().contains(lowerCaseFilter) ) {
                        return true;
                    }


                    if (inventory.getType().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    }
                    if (Integer.toString(inventory.getProductID()).contains(lowerCaseFilter)) {
                        return true;
                    }


                    return false;
                });
                updatePaginationI(filteredData, InventoryTableView, pagination_I);

            });
            updatePaginationI(filteredData, InventoryTableView, pagination_I);
        }
        void setTableView_OrderDetail(){
            Order_ID.setCellValueFactory(param -> param.getValue().orderIDProperty().asString());
            Order_Date.setCellFactory(column -> {
                TableCell<Order, LocalDateTime> cell = new TableCell<Order, LocalDateTime>() {

                    DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

                    @Override
                    protected void updateItem(LocalDateTime item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setText(null);
                        } else {
                            setText(format.format(getTableView().getItems().get(getIndex()).getDate()));
                        }
                    }
                };

                return cell;
            });
            Order_des.setCellValueFactory(param -> param.getValue().descriptionProperty());
            Order_Qty.setCellValueFactory(param -> param.getValue().quantityProperty().asString());
            Order_unit.setCellValueFactory(param -> {
                Order order = param.getValue();
                Locale locale = new Locale("vn", "VN");
                NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(locale);
                return new SimpleStringProperty(currencyFormatter.format(order.getUnitCost()));
            });
            Order_total.setCellValueFactory(param ->
            {
                Order order = param.getValue();
                Locale locale = new Locale("vn", "VN");
                NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(locale);
                return new SimpleStringProperty(currencyFormatter.format(order.getTotalPice()));
            });
            Order_name.setCellValueFactory(param -> param.getValue().nameProperty());
            Order_number.setCellValueFactory(param -> param.getValue().phoneNumberProperty());
            Order_sale.setCellValueFactory(param -> param.getValue().salePersonProperty());
            delcol.setCellFactory(param -> {
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("components/delete-btn-component.fxml"));
                    final Button btn = fxmlLoader.load();
                    TableCell<Order, Button> cell = new TableCell<Order, Button>() {
                        @Override
                        public void updateItem(Button item, boolean empty) {
                            super.updateItem(item, empty);
                            if (!empty) {
                                btn.setOnAction(event -> {
                                    Order order = getTableView().getItems().get(getIndex());
                                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Delete " + order.getOrderID() + " ?", ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
                                    alert.showAndWait();

                                    if (alert.getResult() == ButtonType.YES) {
                                        deleteProductByID(order.getOrderID());
                                    }
                                });

                                setGraphic(btn);
                            } else {
                                setGraphic(null);
                            }

                        }
                    };
                    return cell;
                } catch (Exception ex) {
                    System.out.println(ex);
                }
                return null;
            });
            loadOrder();
            FilteredList<Order> filteredData = new FilteredList<>(orders, p -> true);

            searchOrder.textProperty().addListener((observable, oldValue, newValue) -> {
                filteredData.setPredicate(order -> {
                    if (newValue == null || newValue.isEmpty()) {
                        return true;
                    }
                    String lowerCaseFilter = newValue.toLowerCase();
                    if (order.getDescription().toLowerCase().contains(lowerCaseFilter) ) {
                        return true;
                    }
                    if (order.getName().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    }
                    if (order.getPhoneNumber().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    }
                    if (Integer.toString(order.getOrderID()).contains(lowerCaseFilter)) {
                        return true;
                    }
                    return false;
                });
                updatePagination(filteredData, OrderTableView, pagination_O);

            });
            updatePagination(filteredData, OrderTableView, pagination_O);
        }


    int countAllProducts(){
        MySQLConnector mySQLConnector = MySQLConnector.getInstance();
        try{
            ResultSet resultSet = mySQLConnector.queryResults("select count(*) from danhmucsp");
            if(resultSet.next()){
                return resultSet.getInt(1);
            }

        }catch (Exception ex){
            System.out.println(ex);
        }
        return 0;
    }

    int countAllOrders(){
        MySQLConnector mySQLConnector = MySQLConnector.getInstance();
        try{
            ResultSet resultSet = mySQLConnector.queryResults("select count(*) from donhang");
            if(resultSet.next()){
                return resultSet.getInt(1);
            }

        }catch (Exception ex){
            System.out.println(ex);
        }
        return 0;
    }

    int countAllInventory_lst(){
        MySQLConnector mySQLConnector = MySQLConnector.getInstance();
        try{
            ResultSet resultSet = mySQLConnector.queryResults("select count(*) from dskiemke");
            if(resultSet.next()){
                return resultSet.getInt(1);
            }

        }catch (Exception ex){
            System.out.println(ex);
        }
        return 0;
    }

    void loadType(){
        products.clear();
        danhsach.add("All");
        MySQLConnector mySQLConnector = MySQLConnector.getInstance();
        try{
            ResultSet resultSet = mySQLConnector.queryResults("select distinct Type from danhmucsp");
            while(resultSet.next()){
                String ProductType = resultSet.getString(1);
                Product product = new Product(ProductType);
                products.add(product);
                danhsach.add(ProductType);

            }

        }catch (Exception ex){
            System.out.println(ex);
        }
    }


    void loadCategory(){
        categories.clear();
        MySQLConnector mySQLConnector = MySQLConnector.getInstance();
        try{
            ResultSet resultSet = mySQLConnector.queryResults("select ProductID,ImagePath,Type,Description,UnitCost from danhmucsp  " );
            while(resultSet.next()){
                int ProductID = resultSet.getInt(1);
                String imagePath = resultSet.getString(2);
                String Type = resultSet.getString(3);
                String Description = resultSet.getString(4);
                long price = resultSet.getLong(5);
                Category category = new Category(ProductID,imagePath,Type, Description, price);
                categories.add(category);
            }

        }catch (Exception ex){
            System.out.println(ex);
        }
    }
    void loadInventory(){
        inventories.clear();
        MySQLConnector mySQLConnector = MySQLConnector.getInstance();
        try{
            ResultSet resultSet = mySQLConnector.queryResults("select * from dskiemke" );
            while(resultSet.next()){
                int ProductID = resultSet.getInt(1);
                String Type = resultSet.getString(2);
                String Description = resultSet.getString(3);
                long price = resultSet.getLong(4);
                int quantity = resultSet.getInt(5);
                Inventory inventory = new Inventory(ProductID,Type, Description, price*quantity,quantity);
                inventories.add(inventory);
            }

        }catch (Exception ex){
            System.out.println(ex);
        }

    }
    void loadOrder(){
        orders.clear();
        MySQLConnector mySQLConnector = MySQLConnector.getInstance();
        try{
            ResultSet resultSet = mySQLConnector.queryResults("select OrderID,OrderDate,Description,Quantity,h.Unitcost,Totalcost,Name,PhoneNumber,Saleperson from donhang h join danhmucsp s on h.ProductID=s.ProductID " );
            while(resultSet.next()){
                int OrderID = resultSet.getInt(1);
                LocalDateTime dateTime =  resultSet.getTimestamp(2).toLocalDateTime();
                String Description = resultSet.getString(3);
                int quantity = resultSet.getInt(4);
                long unitcost = resultSet.getLong(5);
                long price = resultSet.getLong(6);
                String name=resultSet.getString(7);
                String phoneNumber=resultSet.getString(8);
                String salePerson=resultSet.getString(9);
                Order order12 = new Order(OrderID,dateTime,Description,quantity,unitcost,price,name,phoneNumber,salePerson);
                orders.add(order12);
//                System.out.println(order12);

            }

        }catch (Exception ex){
            ex.printStackTrace();

        }
    }

    private void updatePagination(ObservableList<Order> data, TableView<Order> tableView, Pagination pagination) {
        int totalPage = (int) Math.ceil(data.size() * 1.0 / ITEMS_PER_PAGEO);
        pagination.setPageCount(totalPage);
        pagination.setPageFactory(pageIndex -> {
            createPageO(pageIndex, tableView, data);
            return new BorderPane();
                });
    }

    private void createPageO(int pageIndex, TableView<Order> table, ObservableList<Order> data) {
        int fromIndex = pageIndex * ITEMS_PER_PAGEO;
        int toIndex = Math.min(fromIndex + ITEMS_PER_PAGEO, data.size());
        table.setItems(FXCollections.observableArrayList(data.subList(fromIndex, toIndex)));

    }

    private void updatePaginationI(ObservableList<Inventory> data, TableView<Inventory> tableView, Pagination pagination) {
        int totalPage = (int) Math.ceil(data.size() * 1.0 / ITEMS_PER_PAGEI);
        pagination.setPageCount(totalPage);
        pagination.setPageFactory(pageIndex -> {
            createPageI(pageIndex, tableView, data);
            return new BorderPane();
        });
    }

    private void createPageI(int pageIndex, TableView<Inventory> table, ObservableList<Inventory> data) {
        int fromIndex = pageIndex * ITEMS_PER_PAGEI;
        int toIndex = Math.min(fromIndex + ITEMS_PER_PAGEI, data.size());
        table.setItems(FXCollections.observableArrayList(data.subList(fromIndex, toIndex)));

    }
    private void updatePaginationC(ObservableList<Category> data, TableView<Category> tableView, Pagination pagination) {
        int totalPage = (int) Math.ceil(data.size() * 1.0 / ITEMS_PER_PAGE);
        pagination.setPageCount(totalPage);
        pagination.setPageFactory(pageIndex -> {
            createPageC(pageIndex, tableView, data);
            return new BorderPane();
        });
    }

    private void createPageC(int pageIndex, TableView<Category> table, ObservableList<Category> data) {
        int fromIndex = pageIndex * ITEMS_PER_PAGE;
        int toIndex = Math.min(fromIndex + ITEMS_PER_PAGE, data.size());
        table.setItems(FXCollections.observableArrayList(data.subList(fromIndex, toIndex)));

    }

    public Pagination getPagination_C() {
        return pagination_C;
    }

    public TableView<Category> getTableViewCategory(){
        return CategoryTableView;
    }
    public void setTableViewCategory(ObservableList<Category> categories){
        int pageIndex = pagination_C.getCurrentPageIndex();
        int fromIndex = pageIndex * ITEMS_PER_PAGE;
        int toIndex = Math.min(fromIndex + ITEMS_PER_PAGE, categories.size());
        CategoryTableView.setItems(FXCollections.observableArrayList(categories.subList(fromIndex, toIndex)));
    }
    public ObservableList<Category> getProducts(){
        return categories;
    }

    public TableView<Order> getTableViewOrder(){
        return OrderTableView;
    }
    public void setTableViewOrder(ObservableList<Order> orders){
        int pageIndex = pagination_O.getCurrentPageIndex();
        int fromIndex = pageIndex * ITEMS_PER_PAGEO;
        int toIndex = Math.min(fromIndex + ITEMS_PER_PAGEO, orders.size());
        OrderTableView.setItems(FXCollections.observableArrayList(orders.subList(fromIndex, toIndex)));
    }
    public ObservableList<Order> getOrders(){
        return orders;
    }
}
