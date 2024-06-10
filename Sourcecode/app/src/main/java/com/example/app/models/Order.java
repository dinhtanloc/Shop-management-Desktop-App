package com.example.app.models;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javax.xml.parsers.SAXParser;
import java.time.LocalDateTime;
import java.util.Date;

public class Order {
    ObservableList<OrderDetail> list = FXCollections.observableArrayList();;
    private IntegerProperty orderID;
    private IntegerProperty top;
    private ObjectProperty<LocalDateTime> orderDate;
    private StringProperty description;
    private IntegerProperty quantity;
    private LongProperty unitCost;
    private LongProperty totalCost;
    private StringProperty name;
    private StringProperty phoneNumber;
    private StringProperty salePerson;
    private IntegerProperty countlst;
    private long quantity_hotlst;
    private long cost;
    private String name_product,saler;
    private double percent;

    int month,count;
    public Order(){
        orderID = new SimpleIntegerProperty();
        orderDate = new SimpleObjectProperty<>(LocalDateTime.now());
        description=new SimpleStringProperty();
        quantity=new SimpleIntegerProperty();
        unitCost=new SimpleLongProperty();
        totalCost = new SimpleLongProperty();
        name =new SimpleStringProperty();
        phoneNumber=new SimpleStringProperty();
        salePerson=new SimpleStringProperty();
    }

    public Order(double percent){
        this.percent=percent;
    }
    public Order(int top,String name,int count,long point){
        this.top=new SimpleIntegerProperty(top);
        this.name=new SimpleStringProperty(name);
        this.countlst=new SimpleIntegerProperty(count);
        this.totalCost=new SimpleLongProperty(point);
    }
    public Order(int top,String name){

        this.name=new SimpleStringProperty(name);
        this.top=new SimpleIntegerProperty(top);
    }
    public Order(String name_product,long quantity_hotlst){
        this.name_product=name_product;
        this.quantity_hotlst=quantity_hotlst;

    }
    public Order(int month,long cost){
        this.month=month;
        this.cost=cost;
    }
    public Order(String saler,int count){
        this.saler=saler;
        this.count=count;
    }

    public Order(int orderID, ObservableList<OrderDetail> list, LocalDateTime dateTime,long totalPrice ){

        this.list.addAll(list);
        this.orderDate = new SimpleObjectProperty<>(dateTime);
        this.totalCost = new SimpleLongProperty(totalPrice);
        this.orderID = new SimpleIntegerProperty(orderID);
    }
    public Order(int orderID, ObservableList<OrderDetail> list, LocalDateTime dateTime,int quantity,long unitCost,long totalPrice,String name,String phoneNumber,String salePerson ){

        this.list.addAll(list);
        this.orderID = new SimpleIntegerProperty(orderID);
        this.orderDate = new SimpleObjectProperty<>(dateTime);
        this.totalCost = new SimpleLongProperty(totalPrice);
        this.salePerson=new SimpleStringProperty(salePerson);
        this.unitCost=new SimpleLongProperty(unitCost);
        this.name=new SimpleStringProperty(name);
        this.phoneNumber=new SimpleStringProperty(phoneNumber);
        this.quantity=new SimpleIntegerProperty(quantity);
    }

    public Order(int orderID, LocalDateTime dateTime, String description, int quantity, long unitCost, long totalPrice, String name, String phoneNumber, String salePerson ){

        this.orderID = new SimpleIntegerProperty(orderID);
        this.orderDate = new SimpleObjectProperty<>(dateTime);
        this.description=new SimpleStringProperty(description);
        this.totalCost = new SimpleLongProperty(totalPrice);
        this.salePerson=new SimpleStringProperty(salePerson);
        this.unitCost=new SimpleLongProperty(unitCost);
        this.name=new SimpleStringProperty(name);
        this.phoneNumber=new SimpleStringProperty(phoneNumber);
        this.quantity=new SimpleIntegerProperty(quantity);
    }

    public double getPercent() {
        return percent;
    }

    public IntegerProperty countlstProperty() {
        return countlst;
    }

    public IntegerProperty topProperty() {
        return top;
    }

    public String getPhoneNumber() {
        return phoneNumber.get();
    }

    public long getCost() {
        return cost;
    }

    public int getMonth() {
        return month;
    }

    public String getDescription() {
        return description.get();
    }

    public long getQuantity_hotlst() {
        return quantity_hotlst;
    }

    public String getName_product() {
        return name_product;
    }

    public ObservableList<OrderDetail> getList() {
        return list;
    }

    public LocalDateTime getDate() {
        return orderDate.get();
    }

    public long getTotalPice() {
        return totalCost.get();
    }

    public int getOrderID() {
        return orderID.get();
    }

    public String getName() {
        return name.get();
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    public void setQuantity(int quantity) {
        this.quantity.set(quantity);
    }


    public void setTotalCost(long totalCost) {
        this.totalCost.set(totalCost);
    }




    public void setName(String name) {
        this.name.set(name);
    }
    public void setOrderID(int orderID) {
        this.orderID.set(orderID);
    }


    public StringProperty phoneNumberProperty() {
        return phoneNumber;
    }

    public StringProperty salePersonProperty() {
        return salePerson;
    }

    public ObjectProperty<LocalDateTime> orderDateProperty() {
        return orderDate;
    }


    public IntegerProperty orderIDProperty() {
        return orderID;
    }

    public LongProperty totalCostProperty() {
        return totalCost;
    }

    public IntegerProperty quantityProperty() {
        return quantity;
    }

    public StringProperty descriptionProperty() {
        return description;
    }

    public StringProperty nameProperty() {
        return name;
    }

    public long getUnitCost() {
        return unitCost.get();
    }



    public void UpdateOrderDetail(ObservableList<OrderDetail> list){
        this.list.addAll(list);

        //update lai tong gia cua don hang
        long newTotalPrice = 0;
        for(int i = 0; i < list.size(); i++){
            newTotalPrice += list.get(i).getProduct_detail().getUnitcost() * list.get(i).getQuantity();
        }
        setTotalCost(newTotalPrice);
    }
    public String ProductsDisplay(){
        String res = "";
        for(int i = 0; i < list.size();i++){
            res += list.get(i).toString() + ", ";
        }
        if (!res.equals("")){
            res =  res.substring(0, res.length() - 2);
        }
        return res;
    }
}