package com.example.app.controllers;

import com.example.app.Main;
import com.example.app.models.Category;
import com.example.app.models.Inventory;
import com.example.app.models.OrderDetail;
import com.example.app.models.Order;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import java.text.NumberFormat;

public class ListViewItemController extends ListCell<OrderDetail> {
    @FXML
    private Button decreaseBtn;

    @FXML
    private HBox hbox;

    @FXML
    private ImageView imageView;

    @FXML
    private Button increaseBtn;

    @FXML
    private Label name;

    @FXML
    private Label quantity;
    ObservableList<OrderDetail> list;
    ObservableList<Category> categories;

    Order order;
    Inventory inventory;

    public ListViewItemController(ObservableList<OrderDetail> list, Order order,Inventory inventory,ObservableList<Category> categories) {
        this.list = list;
        this.order = order;
        this.categories=categories;
        this.inventory=inventory;
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("components/listview-item-component.fxml"));
            fxmlLoader.setController(this);
            fxmlLoader.load();

        } catch (Exception ex) {

        }
    }

    @Override
    protected void updateItem(OrderDetail item, boolean empty) {
        super.updateItem(item, empty);
        if (empty || item == null) {
            setGraphic(null);
        } else {
            String filePath = printImage(item.getProduct_detail(),categories);
            if (filePath.equals("")) {
                filePath = "imgs/default.jpg";
                System.out.println("image here" + filePath);
            }
            String url = Main.class.getResource(filePath).toExternalForm();
            imageView.setImage(new Image(url));
            name.textProperty().bindBidirectional(item.getProduct_detail().descriptionProperty());
            quantity.textProperty().bindBidirectional(item.QuantityProperty(), NumberFormat.getNumberInstance());
            increaseBtn.setOnAction(event -> {
                if (item.getProduct_detail().getQuantity() != 0) {
                    item.increaseQuantity();
                    order.UpdateOrderDetail(list);
                    item.getProduct_detail().setQuantity(item.getProduct_detail().getQuantity() - 1);
                }

            });
            decreaseBtn.setOnAction(event -> {
                item.decreaseQuantity();
                if (item.getQuantity() == 0) {
                    list.remove(item);
                }
                item.getProduct_detail().setQuantity(item.getProduct_detail().getQuantity() + 1);
                order.UpdateOrderDetail(list);
            });

            //set graphic cho cell
            setGraphic(hbox);
        }
    }

    String printImage(Inventory inventory,ObservableList<Category> categories) {
        if (inventory != null) {
            int ID = inventory.getProductID(); // Lấy giá trị a1 từ đối tượng B

            // Tiến hành đối chiếu với danh sách đối tượng A để lấy giá trị b1 tương ứng
            for (Category category : categories) { // listOfA là danh sách các đối tượng A
                if (category.getProductID() == ID) {
                    System.out.println(category.getImagePath());
                    return category.getImagePath();
                }
            }
        }return "";
    }
}