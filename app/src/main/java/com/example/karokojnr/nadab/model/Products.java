package com.example.karokojnr.nadab.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Products {
    @SerializedName("products")
    private ArrayList<Product> productList;

    public ArrayList<Product> getProductArrayList() {
        return productList;
    }

    public void setProductArrayList(ArrayList<Product> productArrayList) {
        this.productList = productArrayList;
    }
}