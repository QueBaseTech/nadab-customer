package com.example.karokojnr.nadab.model;

import com.google.gson.annotations.SerializedName;

import java.util.Arrays;

public class Order {
    @SerializedName("success")
    private boolean success;

    @SerializedName("message")
    private String message;

    @SerializedName("_id")
    private String orderId;

    @SerializedName("status")
    private String orderStatus;

    @SerializedName("totalPrice")
    private Double totalPrice;

    @SerializedName("totalItems")
    private int totalItems;

    @SerializedName("items")
    private OrderItem[] orderItems;

    @SerializedName("payments")
    private OrderPayment[] orderPayments;

    @SerializedName("hotel")
    private String hotel;

    @SerializedName("customerId")
    private String customerId;

    public Order() {}

    public Order(String orderStatus, Double totalPrice, int totalItems, OrderItem[] orderItems, String hotel, String customerId) {
        this.orderStatus = orderStatus;
        this.totalPrice = totalPrice;
        this.totalItems = totalItems;
        this.orderItems = orderItems;
        this.hotel = hotel;
        this.customerId = customerId;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public int getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(int totalItems) {
        this.totalItems = totalItems;
    }

    public OrderItem[] getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(OrderItem[] orderItems) {
        this.orderItems = orderItems;
    }

    public OrderPayment[] getOrderPayments() {
        return orderPayments;
    }

    public void setOrderPayments(OrderPayment[] orderPayments) {
        this.orderPayments = orderPayments;
    }

    public String getHotel() {
        return hotel;
    }

    public void setHotel(String hotel) {
        this.hotel = hotel;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderStatus='" + orderStatus + '\'' +
                ", totalPrice=" + totalPrice +
                ", totalItems=" + totalItems +
                ", orderItems=" + Arrays.toString(orderItems) +
                ", hotel='" + hotel + '\'' +
                ", customerId='" + customerId + '\'' +
                '}';
    }
}
