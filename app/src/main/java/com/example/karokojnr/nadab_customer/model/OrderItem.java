package com.example.karokojnr.nadab_customer.model;

import com.google.gson.annotations.SerializedName;

public class OrderItem {
    private String id;

    @SerializedName("name")
    private String name;

    @SerializedName("qty")
    private int qty;

    @SerializedName("price")
    private double price;

    public OrderItem() {}

    public OrderItem(String id, String name, int qty, double price) {
        this.id = id;
        this.name = name;
        this.qty = qty;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public int getQty() {
        return qty;
    }

    public double getPrice() {
        return price;
    }
}
