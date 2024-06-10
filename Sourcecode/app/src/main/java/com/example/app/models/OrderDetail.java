
package com.example.app.models;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;

public class OrderDetail {
    private ObjectProperty<Inventory> inventory;
    private IntegerProperty quantity;
    private IntegerProperty orderDetailID;
    public OrderDetail(){
        inventory = new SimpleObjectProperty<>();
        quantity = new SimpleIntegerProperty();
        orderDetailID = new SimpleIntegerProperty();
    }

    public OrderDetail(Inventory inventory, int quantity){
        this.inventory = new SimpleObjectProperty<>(inventory);
        this.quantity = new SimpleIntegerProperty(quantity);
    }
    public int getQuantity() {
        return quantity.get();
    }



    public Inventory getProduct_detail() {
        return inventory.get();
    }


    public void setQuantity(int quantity) {
        this.quantity.set(quantity);
    }


    public void setProduct(Inventory inventory) {
        this.inventory.set(inventory);
    }
    public IntegerProperty QuantityProperty(){
        return quantity;
    }
    @Override
    public String toString() {
        //iphone 15 x 2
        return getProduct_detail().getDescription() + " x " + getQuantity();
    }
    public void increaseQuantity(){
        setQuantity(getQuantity() + 1);
    }
    public void decreaseQuantity(){
        if (getQuantity() > 0)
            setQuantity(getQuantity() - 1);
    }
}
