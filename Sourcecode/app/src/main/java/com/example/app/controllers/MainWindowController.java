package com.example.app.controllers;

import com.example.app.Main;
import com.example.app.utils.Storing;
//import String;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
//import org.w3c.dom.Node;
//
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class MainWindowController implements Initializable {
    @FXML
    private Label chargepro;

    @FXML
    private ToggleButton dashboard;

    @FXML
    private ToggleButton edit;

    @FXML
    private HBox hbox;

    @FXML
    private ToggleButton logoutBtn;

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

    @FXML
    private ToggleButton statistic;
    @FXML
    private ImageView imgview;

    @FXML
    private ImageView imageAcc;
    @FXML
    private Label nameAcc;

    @FXML
    private Label storage;

    @FXML
    private ToggleButton table;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (Storing.getValueToPreferences("username").equals("locdinh.myshop@gmail.com")){
           nameAcc.setText("Dinh Tan Loc");
            imageAcc.setImage(new Image(Main.class.getResource("imgs/LocDinh.png").toExternalForm()));

        }else{
            nameAcc.setText("Pham Tuan Anh");
            imageAcc.setImage(new Image(Main.class.getResource("imgs/TuanAnh.jpg").toExternalForm()));

        }
        logoutBtn.setOnAction(event -> {
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("login-window.fxml"));
            Scene scene = null;
            try {
                scene = new Scene(fxmlLoader.load(), 1080, 680);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            stage.setTitle("Login!");
            stage.setScene(scene);
            stage.show();
            ((Node) (event.getSource())).getScene().getWindow().hide();
            //xoa username + password
            Storing.putValueToPreferences("username","");
            Storing.putValueToPreferences("password", "");
        });
        dashboard.setOnAction(event -> {
            ChangeTab(1);
        });
        table.setOnAction(event -> {
            ChangeTab(2);
        });
        edit.setOnAction(event -> {
            ChangeTab(3);
        });
        statistic.setOnAction(event -> {
            ChangeTab(4);
        });
        ChangeTab(1);

    }

    void ChangeTab(int idTab){
        //idTab
        //1: dashboard,2: products, 3: orders, 4:stats
        //Remove thang thu 2 di
        if (hbox.getChildren().size() >= 2){
            hbox.getChildren().remove(1);
        }
        ChangeSelectedMenu(idTab);

        if (idTab == 1){
            Node node = loadTab("dashboard-window.fxml");
            hbox.getChildren().add(node);
        }
        else if  (idTab == 2){
            Node node = loadTab("table-window.fxml");
            hbox.getChildren().add(node);
        }
        else if  (idTab == 3){
            Node node = loadTab("editorder-window.fxml");
            hbox.getChildren().add(node);
        }
        else if  (idTab == 4){
            Node node = loadTab("satistic-window.fxml");
            hbox.getChildren().add(node);
        }

    }
    Node loadTab(String fxmlPath){
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource(fxmlPath));
            Node node = fxmlLoader.load();
            System.out.println("load thanh cong");
            return node;
        }
        catch (Exception ex){
            System.out.println(ex);
        }
        return null;
    }
    void ChangeSelectedMenu(int idTab){
        dashboard.setSelected(false);
        table.setSelected(false);
        edit.setSelected(false);
        statistic.setSelected(false);
        if (idTab == 1){
            dashboard.setSelected(true);
        }
        else if  (idTab == 2){
            table.setSelected(true);
        }
        else if  (idTab == 3){
            edit.setSelected(true);
        }
        else if  (idTab == 4){
            statistic.setSelected(true);
        }

    }
}
