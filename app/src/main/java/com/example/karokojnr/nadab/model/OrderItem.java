package com.example.karokojnr.nadab.model;

import com.google.gson.annotations.SerializedName;

public class OrderItem {
    @SerializedName("name")
    private String name;

    @SerializedName("qty")
    private int qty;

    @SerializedName("price")
    private double price;

    public OrderItem() {}

    public OrderItem(String name, int qty, double price) {
        this.name = name;
        this.qty = qty;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public int getQty() {
        return qty;
    }

    public double getPrice() {
        return price;
    }
}
