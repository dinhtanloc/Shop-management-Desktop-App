package com.example.app.controllers;

import com.example.app.Main;
import com.example.app.models.*;
import com.example.app.utils.Mediator;
import com.example.app.utils.MySQLConnector;
import com.opencsv.CSVWriter;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.chart.ScatterChart;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
//import org.w3c.dom.Node;
import javafx.scene.Node;
import javafx.util.Duration;

import java.io.File;
import java.io.FileWriter;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static javafx.fxml.FXMLLoader.loadType;

public class EditOrderWindowController implements Initializable {
    ObjectProperty<Category> categoryObjectProperty=new SimpleObjectProperty<>();
    ObjectProperty<Inventory>inventoryObjectProperty=new SimpleObjectProperty<>();
    Category category;
    Inventory inventory;

    ObservableList<Product> products=FXCollections.observableArrayList();;


    private StringProperty currentBoundProperty,currentBoundProperty_I;

    private String ImagePath_invent;

    @FXML
    private TextField Desinput_I;

    @FXML
    private TextField Desinput_P;

    @FXML
    private TextField IDinput_I;

    @FXML
    private TextField IDinput_P;

    @FXML
    private Tab InventoryTab;

    @FXML
    private TableView<Inventory> Inventview;

    @FXML
    private TableColumn<Inventory, String> Inventory_ID;

    @FXML
    private ImageView Inventory_imageview;
    @FXML
    private TableColumn<Inventory, String> Invent_unitcost;

    @FXML
    private TableColumn<Inventory, String> Inventory_qty;
    @FXML
    private TextField nameField;

    @FXML
    private TableColumn<Inventory, String> Inventory_type;

    @FXML
    private TableColumn<Inventory, String> Inventory_des;

    @FXML
    private Tab OrderlstTab;

    @FXML
    private TableView<Inventory> Orderview;

    @FXML
    private TableColumn<Inventory, String> Orderlst_ID;

    @FXML
    private TableColumn<Inventory, String> Orderlst_cost;

    @FXML
    private TableColumn<Inventory, String> Orderlst_des;

    @FXML
    private TableColumn<Category, String> Orderlst_invent;

    @FXML
    private TableColumn<Inventory, String> Orderlst_type;
    @FXML
    private ComboBox<String> Salercombobox;

    @FXML
    private TextField Unitinput_I;

    @FXML
    private Tab PriceTab;

    @FXML
    private TableView<Category> Priceview;

    @FXML
    private TableColumn<Category, String> Price_ProID;

    @FXML
    private TableColumn<Category, String> Price_des;

    @FXML
    private TableColumn<Category, Image> Price_image;

    @FXML
    private ImageView Price_imageview;

    @FXML
    private TableColumn<Category, String> Price_price;

    @FXML
    private TableColumn<Category, String> Price_type;

    @FXML
    private TableColumn<Category, Button> deleteCol;
    @FXML
    private TextField Priceinput_P;

    @FXML
    private TextField Qtyinput_I;

    @FXML
    private Button UpdateBtn_P;

    @FXML
    private ComboBox<String> TypecomboBox_P;
//    @FXML
//    private ComboBox<Category> TypecomboBox_P;

    @FXML
    private Button cancelBtn;

    @FXML
    private ComboBox<String> categorybox_I;
//    @FXML
//    private ComboBox<Inventory> categorybox_I;


    @FXML
    private ComboBox<?> caterorybox_P;

    @FXML
    private ListView<OrderDetail> lstView;

    @FXML
    private Button okBtn;

    @FXML
    private TabPane tab;

    @FXML
    private Button hideButton;

    @FXML
    private Button P_editBtn;

    @FXML
    private Button Update_Btn_I;
    @FXML
    private TextField phoneText;
    @FXML
    private Label dateTime;
    @FXML
    private Label totalPrice;
    @FXML
    private Label warningText;

    @FXML
    private TableColumn<Inventory, String> Orderlst_qty;

    @FXML
    private Button checkBtn;
    private boolean isHighlighting = false;

    @FXML
    private HBox box_up,box_down,exportOrderBox,exportBox;
    @FXML
    private VBox vbox_check;


    private TableWindowController tableWindowController;
    ObservableList<Category> categories = FXCollections.observableArrayList();
    ObservableList<Inventory> inventories = FXCollections.observableArrayList();
    ObservableList<OrderDetail> orderDetails = FXCollections.observableArrayList();
    ObservableList<Order> orders = FXCollections.observableArrayList();
    ObservableList<Order>orderslst=FXCollections.observableArrayList();

    Product product;

    Order order;
    private StringProperty selected;
    ObservableList<String> items = FXCollections.observableArrayList("Mục 1", "Mục 2", "Mục 3");



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //Reset danh sách order in ra;
        orderslst.clear();
        Rectangle clip = new Rectangle();
        clip.widthProperty().bind(vbox_check.widthProperty());
        clip.heightProperty().bind(vbox_check.heightProperty());
        vbox_check.setClip(clip);
        tab.getSelectionModel().selectedItemProperty().addListener((observable, oldTab, newTab) -> {
            if (newTab == OrderlstTab) {
                setupslideDatePickers(-600);
                PauseTransition pause = new PauseTransition(Duration.seconds(0.25));
                pause.setOnFinished(event -> {
                    slideDatePickers(0);
                });

// Bắt đầu độ trễ.
                pause.play();
            }
        });


        hideButton.setVisible(false);

        checkBtn.setOnAction(event -> {
            isHighlighting = !isHighlighting; // Toggle the state
            Inventview.refresh(); // Refresh the tableView to apply the changes
        });
        Mediator.getInstance().register(s -> {
            switch (s) {
                case "inventory":
                    tab.getSelectionModel().select(1);
                    break;

                case "order":
                    tab.getSelectionModel().select(2);
                    break;
                case "category":
                    tab.getSelectionModel().select(0);
                    break;
            }
        });


        //Inventory tab
        exportBox.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                FileChooser fileChooser = new FileChooser();

                // Đặt tiêu đề cho cửa sổ chọn tệp (tùy chọn)
                fileChooser.setTitle("Save File");

                // Hiển thị cửa sổ lưu tệp và lấy tệp được chọn
                File savedFile = fileChooser.showSaveDialog(null);

                if (savedFile != null) {
                    String filePath = savedFile.getAbsolutePath();
                    String absolutePath = savedFile.getAbsolutePath();
                    String parentDirectory = savedFile.getParent(); // Đây là thư mục chứa tệp
//                    System.out.println(parentDirectory + absolutePath + absolutePath + filePath);
                    exportFromMySQL(filePath, "dskiemke");
                }
            }
        });
        Inventory_imageview.setFitHeight(292);
        Inventory_imageview.setFitWidth(203);
        setTableView_Inventory();
        Update_Btn_I.setOnAction(event -> {
            String selectedValue = categorybox_I.getValue();
            inventory.setType(selectedValue);
            //them vao Database
            MySQLConnector connector = MySQLConnector.getInstance();
            String sql = "UPDATE dskiemke\n" +
                    "SET InventoryQty= " + inventory.getQuantity() +" WHERE ProductID= "+inventory.getProductID();
            System.out.println(sql);
            if (connector.queryUpdate(sql)) {
                if(tableWindowController!=null){
                tableWindowController.loadInventory();
                ((Node) (event.getSource())).getScene().getWindow().hide();}
                else{
                    loadInventory();
                    Inventview.setItems(inventories);
                }
            }

        });
        List<String> types = loadType();

        categorybox_I.getItems().addAll(types);

        //Chỉnh sửa ds kiểm kê
//        String ImagePath_invent="";
        Inventview.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) //Checking double click
            {
                //dong` double click
                Inventory inventory = Inventview.getSelectionModel().getSelectedItem();

                //Mo window nhap san phamStage stage = new Stage;
                if (inventory != null) {
                    int ID = inventory.getProductID(); // Lấy giá trị a1 từ đối tượng B

                    // Tiến hành đối chiếu với danh sách đối tượng A để lấy giá trị b1 tương ứng
                    for (Category category : categories) { // listOfA là danh sách các đối tượng A
                        if (category.getProductID() == ID) {
                            ImagePath_invent = category.getImagePath(); // Lấy giá trị b1 từ đối tượng A
                            // Bây giờ bạn có thể sử dụng giá trị b1Value cho mục đích của bạn
                            break; // Nếu bạn đã tìm thấy đối tượng A tương ứng, bạn có thể thoát khỏi vòng lặp
                        }
                    }
                }
                try {

//                    System.out.println("Image"+ImagePath_invent);
                    String Image=(String) ImagePath_invent;
                    initParams_Invent_CHANGE(this, products, inventory, Image);
                    bindData(inventory);
                } catch (Exception ex) {
                    System.out.println(ex);
                }
            }
        });

        //Category tab
        setTableView_Category();
        //Edit product
        P_editBtn.setOnAction(event -> {
            //update san pham trong Database
            String selectedValue = TypecomboBox_P.getValue();
            category.setType(selectedValue);
            MySQLConnector connector = MySQLConnector.getInstance();
            String sql = "UPDATE danhmucsp\n" +
                    "SET ProductID = " + category.getProductID() + ", ImagePath = '" + category.getImagePath() + "',  Type = '" + category.getType() + "', Description = '" + category.getDescription() + "', Unitcost = " + category.getPrice() + "\n" +
                    "WHERE productID = " + category.getProductID();
            System.out.println("cautruy van"+sql);
            if (connector.queryUpdate(sql)) {
                if(tableWindowController!=null){
                    tableWindowController.loadCategory();
                    tableWindowController.setTableViewCategory(tableWindowController.getProducts());

                    ((Node) (event.getSource())).getScene().getWindow().hide();}
                else{
//                    TableWindowController tableWindowController1=new TableWindowController();
                    loadCategory();
//                    ((Node) (event.getSource())).getScene().getWindow().hide();
                }

            }

        });

        TypecomboBox_P.getItems().addAll(types);

        //Them san pham vao danh muc
        hideButton.setOnAction(event -> {
            //them vao Database, khi thêm một sản phẩm mới vào danh sách sẽ tự động cập nhật danh sách kiểm kê
            MySQLConnector connector = MySQLConnector.getInstance();

            category.setProductID(Integer.parseInt(IDinput_P.getText()));
            category.setDescription(Desinput_P.getText());
            category.setPrice(Long.parseLong(Priceinput_P.getText()));
            String selectedValue = TypecomboBox_P.getValue();
            category.setType(selectedValue);
            if(category.getImagePath()==null){
                category.setImagePath("");
            }
            String sql = "INSERT INTO danhmucsp(ProductID,ImagePath, Type, Description, Unitcost)\n" +
                    "VALUES ('" + category.getProductID() + "','" + category.getImagePath() + "', '" + category.getType() + "', '" + category.getDescription() + "', " + category.getPrice() + ");";
            String sql1 = "INSERT INTO dskiemke(ProductID,Type, Description, Unitcost,InventoryQty)\n" +
                    "VALUES ('" + category.getProductID() + "', '" + category.getType() + "', '" + category.getDescription() + "', " + category.getPrice() + ",0);";
            System.out.println(sql);
            if (connector.queryUpdate(sql)) {
                if(tableWindowController!=null){
                tableWindowController.loadCategory();
                tableWindowController.setTableViewCategory(tableWindowController.getProducts());
                ((Node) (event.getSource())).getScene().getWindow().hide();}
                else{
                    loadCategory();
                }
            }
            if (connector.queryUpdate(sql1)) {
                if(tableWindowController!=null){
                    tableWindowController.loadInventory();
                    ((Node) (event.getSource())).getScene().getWindow().hide();}
                else{
                    loadInventory();
                }
            }


        });

        //Cap nhat hinh anh
        UpdateBtn_P.setOnAction(event -> {
//            System.out.println("okk");
            FileChooser fileChooser = new FileChooser();
            FileChooser.ExtensionFilter imageFilter
                    = new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.png");
            fileChooser.getExtensionFilters().add(imageFilter);
            fileChooser.setTitle("Select a File");

            // Show the file dialog to pick a file
            File selectedFile = fileChooser.showOpenDialog((Stage) ((Node) event.getSource()).getScene().getWindow());
            if (selectedFile != null) {
                String folder = Main.class.getResource("imgs/").getPath();
                folder = folder.substring(1);
                try {
                    String fileName = new SimpleDateFormat("yyyy-MM-dd hh-mm-ss").format(new Date());
                    fileName = fileName + selectedFile.getName().substring(selectedFile.getName().lastIndexOf("."));
                    Path destinationPath = Paths.get(folder, fileName);

                    // Copy the selected file to the destination path
                    Files.copy(selectedFile.toPath(), destinationPath, StandardCopyOption.REPLACE_EXISTING);
//                    System.out.println("File saved to: " + destinationPath.toString());
//                    category=new Category();
                    if(category ==null){
                        category=new Category();
                    }
                    category.setImagePath("imgs/" + fileName);
                    Price_imageview.setImage(new Image(destinationPath.toString()));

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        // double click
        //sua sp
        Priceview.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) //Checking double click
            {
                //dong` double click
                Category category = Priceview.getSelectionModel().getSelectedItem();
                //Mo window nhap san phamStage stage = new Stage;
                try {
//                    System.out.println("product"+products);
                    initParams_CHANGE(this, products, category);
                    bindData(category);

                } catch (Exception ex) {
                    System.out.println(ex);
                }
            }
        });
//
//


        //Order-tab
        //xuất file
        exportOrderBox.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                FileChooser fileChooser = new FileChooser();

                // Đặt tiêu đề cho cửa sổ chọn tệp (tùy chọn)
                fileChooser.setTitle("Save File");

                // Hiển thị cửa sổ lưu tệp và lấy tệp được chọn
                File savedFile = fileChooser.showSaveDialog(null);

                if (savedFile != null) {
                    String filePath = savedFile.getAbsolutePath();
                    String absolutePath = savedFile.getAbsolutePath();
                    String parentDirectory = savedFile.getParent(); // Đây là thư mục chứa tệp
//                    System.out.println(parentDirectory + absolutePath + absolutePath + filePath);
                    exportFromMySQL(filePath, "danhsachdonhang");
                }
            }
        });
        //hien len danh sach ton kho
        order = new Order();
        Orderview.setItems(inventories);
        ObservableList<String> saler=FXCollections.observableArrayList();
        saler.add("Loc Dinh");
        saler.add("Tuan Anh");
        Salercombobox.setItems(saler);
        Orderlst_type.setCellValueFactory(param -> param.getValue().typeProperty());
        Orderlst_ID.setCellValueFactory(param -> param.getValue().productIDProperty().asString());
        Orderlst_des.setCellValueFactory(param -> param.getValue().descriptionProperty());
        Orderlst_cost.setCellFactory(param -> {
            TableCell<Inventory, String> cell = new TableCell<Inventory, String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (!empty) {
                        int rowID = getIndex();
                        Category category = categories.get(rowID);
//                        Category category = getTableView().getItems().get(getIndex());


                        Locale locale = new Locale("vn", "VN");
                        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(locale);
                        setText(currencyFormatter.format(category.getPrice()));
                    } else {
                        setText(null);
                    }

                }
            };
            return cell;
        });
        Orderlst_qty.setCellValueFactory(param -> param.getValue().quantityProperty().asString());
        dateTime.textProperty().bind(Bindings.createStringBinding(() -> {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            return dtf.format(order.getDate());
        }, order.orderDateProperty()));

        totalPrice.textProperty().bind(Bindings.createStringBinding(() -> {
            Locale locale = new Locale("vn", "VN");
            NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(locale);
            return currencyFormatter.format(order.getTotalPice());
        }, order.totalCostProperty()));
        //orderDetails la mot list san pham o listview
        lstView.setItems(orderDetails);
        lstView.setCellFactory(param -> new ListViewItemController(orderDetails, order,inventory,categories));
        //Chon san pham
        Orderview.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) //Checking double click
            {
                //Lay dung doi tuong bi tro vao
                Inventory inventory1 = Orderview.getSelectionModel().getSelectedItem();
                if (inventory1.getQuantity() == 0)
                    return;//tu dong out neu san pham het hang
                //tim bien orderDetail dua tren inventory1, trả về đúng orderdetails trong ds orderdetails
                OrderDetail orderDetail = findProductInListView(inventory1, orderDetails);
                inventory1.setQuantity(inventory1.getQuantity() - 1);
                if (orderDetail == null) {
                    orderDetails.add(new OrderDetail(inventory1, 1));
                    //neu trong listview hien dang trong, thi se cap nhat vao listview
                }
                else {
                    orderDetail.increaseQuantity();
                    //neu hien dang co thi se tang len
                }
                order.UpdateOrderDetail(orderDetails);
            }

        });
        cancelBtn.setOnAction(event -> {
            if (tableWindowController!=null){
            Stage stage = (Stage) cancelBtn.getScene().getWindow();
            stage.close();}

        });
        okBtn.setOnAction(event -> {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String price=totalPrice.getText().substring(2).replace(",","");
        Order order1=new Order(0,orderDetails,LocalDateTime.parse(dateTime.getText(),formatter),Long.parseLong(price));
        orderslst.add(order1);
        if(orderDetails.isEmpty()||nameField.getText().isEmpty()||phoneText.getText().isEmpty()||Salercombobox.getValue().isEmpty()){
//            warningText.setText("Fill full information of this order");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.getButtonTypes().setAll(ButtonType.CANCEL);

            Label content = new Label("Remember to fill full information and choose your order !");
            content.setWrapText(true);
            content.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: red;");

            alert.getDialogPane().setContent(content);

            alert.showAndWait();

            if (alert.getResult() == ButtonType.CANCEL) {
                return;
            }
            return;
        }

            //B1 tao don hang moi trong database
            int OrderID= createnewOrderInDatabase();
            //B2 cap nhat lich su don hang trong database
            createOrderDetailInDatabase(OrderID);
            //load lai danh sach orders o Order Tab
//            tableWindowController.loadOrder();
            if(tableWindowController!=null){
                tableWindowController.loadOrder();
                tableWindowController.loadCategory();
                tableWindowController.setTableViewOrder(tableWindowController.getOrders());
                ((Node) (event.getSource())).getScene().getWindow().hide();}
            else{
                loadOrder();

            }

            //update danhsachdonhangmoi
//            Stage stage = (Stage) okBtn.getScene().getWindow();
//            stage.close();

        });
        loadCategory();


    }
//@Override
private static void exportFromMySQL(String filePath, String data) {
    if (!filePath.endsWith(".csv")) {
        filePath += ".csv";
    }
     MySQLConnector mySQLConnector=MySQLConnector.getInstance();
    try (CSVWriter writer = new CSVWriter(new FileWriter(filePath))) {

        String sql = "SELECT * FROM " + data +" WHERE OrderDate >= CURDATE() AND OrderDate <= NOW()";
        if(data.equals("danhsachdonhang")){
            String[] header={"ID","Order Details","OrderDate","Total"};
            writer.writeNext(header);
            System.out.println(sql);
            ResultSet resultSet = mySQLConnector.queryResults(sql);
            while (resultSet.next()) {
                String[] line = { resultSet.getString(1), resultSet.getString(2), resultSet.getString(3),
                        resultSet.getString(4) };
                writer.writeNext(line);
            }

        }
        else{
            String[] header={"Product ID","Type","Description","Unit cost", "Inventory Quantity"};
            writer.writeNext(header);
            sql = "SELECT * FROM " + data;
            ResultSet resultSet = mySQLConnector.queryResults(sql);
            System.out.println(sql);
            while (resultSet.next()) {
                String[] line = { resultSet.getString(1), resultSet.getString(2), resultSet.getString(3),
                        resultSet.getString(4), resultSet.getString(5) };
                writer.writeNext(line);
            }
        }

    } catch (Exception e) {
        e.printStackTrace();
    }
}

    private void setupslideDatePickers(double targetX) {
        KeyValue keyValue1 = new KeyValue(box_up.translateXProperty(), targetX);
        KeyFrame keyFrame1 = new KeyFrame(Duration.seconds(0.01), keyValue1);

        KeyValue keyValue2 = new KeyValue(box_down.translateXProperty(), targetX);
        KeyFrame keyFrame2 = new KeyFrame(Duration.seconds(0.01), keyValue2);

        Timeline timeline = new Timeline(keyFrame1, keyFrame2);
        timeline.play();
    }
    private void slideDatePickers(double targetX) {
        KeyValue keyValue1 = new KeyValue(box_up.translateXProperty(), targetX);
        KeyFrame keyFrame1 = new KeyFrame(Duration.seconds(0.5), keyValue1);

        KeyValue keyValue2 = new KeyValue(box_down.translateXProperty(), targetX);
        KeyFrame keyFrame2 = new KeyFrame(Duration.seconds(0.5), keyValue2);

        Timeline timeline = new Timeline(keyFrame1, keyFrame2);
        timeline.play();
    }

    void setTableView_Inventory() {
        Inventview.setItems(inventories);
        Inventory_ID.setCellValueFactory(param -> param.getValue().productIDProperty().asString());
        Inventory_qty.setCellValueFactory(param -> param.getValue().quantityProperty().asString());
        Inventory_des.setCellValueFactory(param -> param.getValue().descriptionProperty());
        //Chu y mai mot doi chieu lai
        Invent_unitcost.setCellValueFactory(param -> {
            Inventory inventory = param.getValue();
            Locale locale = new Locale("vn", "VN");
            NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(locale);
            return new SimpleStringProperty(currencyFormatter.format(inventory.getUnitcost()));

        });
        Inventory_type.setCellValueFactory(param -> param.getValue().typeProperty());

        Inventview.setRowFactory(tv -> {
            TableRow<Inventory> row = new TableRow<>();
            row.itemProperty().addListener((obs, previousProduct, currentProduct) -> {
                if (currentProduct == null) {
                    row.setStyle("");
                } else if (isHighlighting && currentProduct.getQuantity() <= 0) {
                    row.setStyle("-fx-background-color: #dc9292");
                } else {
                    row.setStyle("");
                }
            });
            return row;
        });
        loadInventory();
    }

    void setTableView_Category() {
        Priceview.setItems(categories);
        Price_ProID.setCellValueFactory(param -> param.getValue().productIDProperty().asString());
        Price_des.setCellValueFactory(param -> param.getValue().descriptionProperty());
        Price_price.setCellValueFactory(param -> {
            Category category = param.getValue();
            Locale locale = new Locale("vn", "VN");
            NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(locale);
            return new SimpleStringProperty(currencyFormatter.format(category.getPrice()));

        });
        Price_type.setCellValueFactory(param -> param.getValue().typeProperty());
        Price_image.setCellFactory(param -> {
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
                        }
                        try{
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
        // tao ra mot cot xoa sp o muc price
        deleteCol.setCellFactory(param -> {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("components/delete-btn-component.fxml"));
                final Button btn = fxmlLoader.load();
                TableCell<Category, Button> cell = new TableCell<Category, Button>() {
                    @Override
                    public void updateItem(Button item, boolean empty) {
                        super.updateItem(item, empty);
                        if (!empty) {
                            btn.setOnAction(event -> {
                                Category category1 = getTableView().getItems().get(getIndex());
                                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Delete " + category1.getDescription() + " ?", ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
                                alert.showAndWait();

                                if (alert.getResult() == ButtonType.YES) {
                                    deleteProductByID(category1.getProductID());
                                    if(tableWindowController!=null){
                                    tableWindowController.loadCategory();
                                    tableWindowController.setTableViewCategory(tableWindowController.getProducts());
                                    ((Node) (event.getSource())).getScene().getWindow().hide();}
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
        loadCategory();
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
                Inventory inventory = new Inventory(ProductID,Type, Description, price,quantity);
                inventories.add(inventory);
            }
        }catch (Exception ex){
            System.out.println(ex);
        }

    }
    void loadOrder(){
        orders.clear();
//        orderslst.clear();
        MySQLConnector mySQLConnector = MySQLConnector.getInstance();
        ObservableList<OrderDetail>orderDetails_lst=FXCollections.observableArrayList();
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
                Order order = new Order(OrderID,dateTime,Description,quantity,unitcost,price,name,phoneNumber,salePerson);
                orders.add(order);


            }
        }catch (Exception ex){
            System.out.println(ex);
        }
    }
    void deleteProductByID(int productID){
        MySQLConnector mySQLConnector = MySQLConnector.getInstance();
        try{
            String sql =  "DELETE FROM danhmucsp \n" +
                    "WHERE productID = " + productID;

            System.out.println(sql);
            mySQLConnector.queryUpdate(sql);
            sql= "DELETE FROM dskiemke \n" +
                    "WHERE productID = " + productID;
            mySQLConnector.queryUpdate(sql);
            loadInventory();
            loadCategory();
        }catch (Exception ex){
            System.out.println(ex);
        }
    }
    void loadCategory(){
        categories.clear();
        MySQLConnector mySQLConnector = MySQLConnector.getInstance();
        try{
            ResultSet resultSet = mySQLConnector.queryResults("select ProductID,ImagePath,Type,Description,UnitCost from danhmucsp " );
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

    // chọn này
    public void initParams(TableWindowController tableWindowController,ObservableList<Product> products,String check){
        if(check.equals("category")) {
            hideButton.setVisible(true);
            category = new Category();
            IDinput_P.setText(Integer.toString(categories.size()+1));
            Desinput_P.textProperty().bindBidirectional(category.descriptionProperty());
            Priceinput_P.textProperty().bindBidirectional(category.priceProperty(), NumberFormat.getNumberInstance());

            this.tableWindowController = tableWindowController;
            this.products = products;
        } else if (check.equals("inventory")) {
            inventory = new Inventory();
            category=new Category();
            IDinput_I.textProperty().bindBidirectional(inventory.productIDProperty(), NumberFormat.getNumberInstance());
            Desinput_I.textProperty().bindBidirectional(inventory.descriptionProperty());
            Unitinput_I.textProperty().bindBidirectional(inventory.unitcostProperty(),NumberFormat.getNumberInstance());
            Qtyinput_I.textProperty().bindBidirectional(inventory.quantityProperty(),NumberFormat.getNumberInstance());
            this.tableWindowController = tableWindowController;
            this.products = products;
//            System.out.println("1129" + this.products);
//
        }

    }

    void initParams_CHANGE(EditOrderWindowController tableWindowController, ObservableList<Product> products, Category oldProduct){
        category = oldProduct.clone();
        Desinput_P.textProperty().bindBidirectional(category.descriptionProperty());
        Priceinput_P.textProperty().bindBidirectional(category.priceProperty(), NumberFormat.getNumberInstance());
        IDinput_P.textProperty().bindBidirectional(category.productIDProperty(), NumberFormat.getNumberInstance());
        Class<?> dataType = category.getImagePath().getClass();

        // Lấy tên kiểu dữ liệu
        String dataTypeName = dataType.getName();

//        System.out.println("Kiểu dữ liệu của biến myInteger là: " + dataTypeName);
//        update hinh anh tren form
        if (!category.getImagePath().equals("")){
            String filePath = Main.class.getResource(category.getImagePath()).toExternalForm();
//            System.out.println();
//            System.out.print(filePath);
            Price_imageview.setImage(new Image(filePath));
        }
        else{
            String url = Main.class.getResource("imgs/default.jpg").toExternalForm();
            Price_imageview.setImage(new Image(url));
        }


    }
    void initParams_Invent_CHANGE(EditOrderWindowController tableWindowController, ObservableList<Product> products, Inventory oldProduct,String Image){
        inventory = oldProduct.clone();
        Desinput_I.textProperty().bindBidirectional(inventory.descriptionProperty());
        Unitinput_I.textProperty().bindBidirectional(inventory.unitcostProperty(), NumberFormat.getNumberInstance());
        IDinput_I.textProperty().bindBidirectional(inventory.productIDProperty(), NumberFormat.getNumberInstance());
        Qtyinput_I.textProperty().bindBidirectional(inventory.quantityProperty(),NumberFormat.getNumberInstance());
        //update hinh anh tren form

        if (!Image.equals("")){
            String filePath = Main.class.getResource(Image).toExternalForm();
            Inventory_imageview.setImage(new Image(filePath));
        }
        else{
            String url = Main.class.getResource("imgs/default.jpg").toExternalForm();
            Inventory_imageview.setImage(new Image(url));
        }


    }
    private void bindData(Category category) {
        if (currentBoundProperty != null) {
            TypecomboBox_P.valueProperty().unbindBidirectional(currentBoundProperty);
        }
        List<String> types = loadType();

        TypecomboBox_P.getItems().addAll(types);

        TypecomboBox_P.valueProperty().bindBidirectional(category.typeProperty());
        currentBoundProperty = category.typeProperty();

    }
    private void bindData(Inventory inventory) {
        if (currentBoundProperty_I != null) {
            categorybox_I.valueProperty().unbindBidirectional(currentBoundProperty_I);
        }
        List<String> types = loadType();

        categorybox_I.getItems().addAll(types);

        categorybox_I.valueProperty().bindBidirectional(inventory.typeProperty());
        currentBoundProperty_I = inventory.typeProperty();

    }
    List<String> loadType(){
        products.clear();
        List<String> types = new ArrayList<>();

        MySQLConnector mySQLConnector = MySQLConnector.getInstance();
        try{
            ResultSet resultSet = mySQLConnector.queryResults("select distinct Type from danhmucsp");
            while(resultSet.next()){
                String ProductType = resultSet.getString(1);
                Product product = new Product(ProductType);
                types.add(ProductType);
                products.add(product);
            }

        }catch (Exception ex){
            System.out.println(ex);
        }return types;
    }
    OrderDetail findProductInListView(Inventory category1, ObservableList<OrderDetail> list){
        for(int  i = 0; i < list.size(); i++)
        {
            if (category1.getProductID() == list.get(i).getProduct_detail().getProductID())
                return  list.get(i);
        }
        return null;
    }
    public void init(TableWindowController ordersTabController){
        this.tableWindowController = ordersTabController;
    }

    int createnewOrderInDatabase(){
        //them vao Database
        try{
            MySQLConnector connector = MySQLConnector.getInstance();
            String sql = "SELECT COUNT(*) FROM donhang";
            System.out.println(sql);
            ResultSet resultSet = connector.queryResults(sql);
            if(resultSet.next()){
                int num=resultSet.getInt(1);
                return num;
                }
                return 0;
                    //Lưu ý kiểm tra lại ProductID trong mục order


        }catch (Exception ex){
            System.out.println(ex);
        }

        return -1;
    }


    void insert(int OrderID) {
        try {
            MySQLConnector connector = MySQLConnector.getInstance();
            for (int i = 0; i < orderslst.size(); i++) {
                Order orderDetail = orderslst.get(i);
                String sql= String.format("""
                        INSERT INTO danhsachdonhang(ID, OrderDetails, OrderDate,Total) 
                        VALUES (%s,'%s','%s',%s)
                        """,OrderID,orderDetail.ProductsDisplay(),dateTime.getText(),totalPrice.getText().substring(2).replace(",",""));
                connector.queryUpdate(sql);
                System.out.println(sql);

            }

        } catch (Exception ex) {
            System.out.println(ex);
        }
    }
    void createOrderDetailInDatabase(int orderID){
        //them vao Database
        try{
            insert(orderID);
            MySQLConnector connector = MySQLConnector.getInstance();
            for(int i = 0; i < orderDetails.size(); i++){
                OrderDetail orderDetail = orderDetails.get(i);
                String sql= String.format("""
                        INSERT INTO donhang(OrderID, OrderDate, ProductID,Quantity,Unitcost,Totalcost,Name,phoneNumber,SalePerson) 
                        VALUES (%s,'%s',%s,%s,%s,%s,'%s','%s','%s')
                        """,orderID,dateTime.getText(),orderDetail.getProduct_detail().getProductID(),orderDetail.getQuantity(),orderDetail.getProduct_detail().getUnitcost(),orderDetail.getProduct_detail().getUnitcost()*orderDetail.getQuantity(),nameField.getText(),phoneText.getText(),Salercombobox.getValue());
                System.out.println(sql);
                connector.queryUpdate(sql);
                //cap nhat so luong ton kho cua product
                updateInStockOfProduct(orderDetail.getProduct_detail().getProductID(), orderDetail.getProduct_detail().getQuantity() - orderDetail.getQuantity());
            }

        }catch (Exception ex){
            System.out.println(ex);
        }
    }
    void updateInStockOfProduct(int productID, int amount){
        MySQLConnector connector = MySQLConnector.getInstance();
        String sql = "UPDATE dskiemke\n" +
                "SET InventoryQty = "+amount+"\n" +
                "WHERE productID = " + productID;
        connector.queryUpdate(sql);
    }

}

