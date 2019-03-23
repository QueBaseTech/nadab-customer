package com.example.karokojnr.nadab_customer.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Orders {
    @SerializedName("success")
    private boolean success;

    @SerializedName("message")
    private String message;

    @SerializedName("orders")
    private ArrayList<Order> orders;

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public ArrayList<Order> getOrders() {
        return orders;
    }

    public void setOrders(ArrayList<Order> orders) {
        this.orders = orders;
    }
}