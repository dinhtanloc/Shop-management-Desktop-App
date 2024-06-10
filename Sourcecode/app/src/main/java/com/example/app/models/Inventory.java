package com.example.app.models;

import javafx.beans.property.*;

public class Inventory {
    private IntegerProperty ProductID;
    private StringProperty Type;
    private StringProperty Description;
    private LongProperty Unitcost;
    private IntegerProperty quantity;
    ObjectProperty<Product> product=new SimpleObjectProperty<>();
    ObjectProperty<Category> category=new SimpleObjectProperty<>();

    public Inventory(){
        this.ProductID=new SimpleIntegerProperty();
        this.Type=new SimpleStringProperty();
        this.Description=new SimpleStringProperty();
        this.Unitcost=new SimpleLongProperty();
        this.quantity=new SimpleIntegerProperty();
        this.product=new SimpleObjectProperty<>();
        this.category=new SimpleObjectProperty<>();

    }

    public Inventory(String Type){
        this.Type=new SimpleStringProperty(Type);
    }
    public Inventory(int ProductID,String Type,String Description,long Unitcost,int quantity){
        this.ProductID=new SimpleIntegerProperty(ProductID);
        this.Type=new SimpleStringProperty(Type);
        this.Unitcost=new SimpleLongProperty(Unitcost);
        this.Description=new SimpleStringProperty(Description);
        this.quantity=new SimpleIntegerProperty(quantity);
    }

    public void setProductID(int productID) {
        this.ProductID.set(productID);
    }

    public void setDescription(String description) {
        this.Description.set(description);
    }

    public void setType(String type) {
        this.Type.set(type);
    }

    public void setQuantity(int quantity) {
        this.quantity.set(quantity);
    }

    public void setUnitcost(long unitcost) {
        this.Unitcost.set(unitcost);
    }

    public int getProductID() {
        return ProductID.get();
    }

    public int getQuantity() {
        return quantity.get();
    }

    public long getUnitcost() {
        return Unitcost.get();
    }

    public String getDescription() {
        return Description.get();
    }

    public String getType() {
        return Type.get();
    }

    public IntegerProperty productIDProperty() {
        return ProductID;
    }

    public LongProperty unitcostProperty() {
        return Unitcost;
    }

    public StringProperty descriptionProperty() {
        return Description;
    }

    public IntegerProperty quantityProperty() {
        return quantity;
    }

    public StringProperty typeProperty() {
        return Type;
    }


    @Override
    public String toString() {
        return "Inventory{" +
                "ProductID=" + ProductID +
                ", Type=" + Type +
                ", Description=" + Description +
                ", Unitcost=" + Unitcost +
                '}';
    }
    public Inventory clone(){
        return new Inventory(getProductID(), getType(), getDescription(), getUnitcost(),getQuantity());

    }
}
