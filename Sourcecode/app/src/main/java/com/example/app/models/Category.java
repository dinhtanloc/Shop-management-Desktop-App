package com.example.app.models;


import javafx.beans.property.*;

public class Category {
    private IntegerProperty ProductID;
    private StringProperty ImagePath;
    private StringProperty Type;
    private StringProperty Description;
    private LongProperty Price;
    ObjectProperty<Product> product=new SimpleObjectProperty<>();

    ObjectProperty<Inventory> inventory=new SimpleObjectProperty<>();

    public Category(){
        this.Description=new SimpleStringProperty();
        this.ImagePath=new SimpleStringProperty();
        this.Price=new SimpleLongProperty();
        this.ProductID=new SimpleIntegerProperty();
        this.Type=new SimpleStringProperty();
        this.product=new SimpleObjectProperty<>();
        this.inventory=new SimpleObjectProperty<>();
    }

    public Category(String Type){
        this.Type=new SimpleStringProperty(Type);
    }
//    public Category(int ProductID){
//
//    }
    public Category(int ProductID, String ImagePath,String Type,String Description,long Price ){
        this.ProductID=new SimpleIntegerProperty(ProductID);
        this.ImagePath=new SimpleStringProperty(ImagePath);
        this.Type=new SimpleStringProperty(Type);
        this.Description=new SimpleStringProperty(Description);
        this.Price=new SimpleLongProperty(Price);

    }

    public String getType() {
        return Type.get();
    }

    public long getPrice() {
        return Price.get();
    }

    public String getDescription() {
        return Description.get();
    }

    public int getProductID() {
        return ProductID.get();
    }

    public String getImagePath() {
        return ImagePath.get();
    }

    public void setDescription(String description) {
        this.Description.set(description);
    }

    public void setProductID(int productID) {
        this.ProductID.set(productID);
    }

    public void setType(String type) {
        this.Type.set(type);
    }
    public void setPrice(long price) {
        this.Price.set(price);
    }

    public void setImagePath(String imagePath) {
        this.ImagePath.set(imagePath);
    }

    public StringProperty typeProperty() {
        return Type;
    }

    public StringProperty descriptionProperty() {
        return Description;
    }

    public IntegerProperty productIDProperty() {
        return ProductID;
    }




    public LongProperty priceProperty() {
        return Price;
    }

    public Product getProduct() {
        return product.get();
    }



    @Override
    public String toString() {
        return "Category{" +
                "ProductID=" + ProductID +
                ", ImagePath=" + ImagePath +
                ", Type=" + Type +
                ", Description=" + Description +
                ", Price=" + Price +
                '}';
    }
    public Category clone(){
        return new Category(getProductID(), getImagePath(), getType(), getDescription(), getPrice());

    }
}
