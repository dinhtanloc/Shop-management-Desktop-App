package com.example.app.models;

import javafx.beans.property.*;

import java.text.MessageFormat;

public class Product {
    private StringProperty ProductType ;
    public Product(String ProductType){

        this.ProductType = new SimpleStringProperty(ProductType);
    }


    public String getProductType() {
        return ProductType.get();
    }



    @Override
    public String toString(){
        return getProductType();
    }
}
