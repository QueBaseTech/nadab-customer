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

    @SerializedName("customerId")
    private String customerId;

    @SerializedName("hotelId")
    private String hotelId;

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

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getHotelId() {
        return hotelId;
    }

    public void setHotelId(String hotelId) {
        this.hotelId = hotelId;
    }

    public int getQty() {
        return qty;
    }

    public double getPrice() {
        return price;
    }
}
