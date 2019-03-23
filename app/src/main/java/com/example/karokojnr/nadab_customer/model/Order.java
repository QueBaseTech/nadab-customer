package com.example.karokojnr.nadab_customer.model;

import com.google.gson.annotations.SerializedName;

import java.util.Arrays;

public class Order {
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
    private Hotel hotel;

    @SerializedName("hotelId")
    private String hotelId;

    @SerializedName("customerId")
    private String customerId;

    @SerializedName("updatedAt")
    private String updatedAt;

    public Order() {}

    public Order(String orderStatus, Double totalPrice, int totalItems, OrderItem[] orderItems, String hotel, String customerId) {
        this.orderStatus = orderStatus;
        this.totalPrice = totalPrice;
        this.totalItems = totalItems;
        this.orderItems = orderItems;
        this.hotelId = hotel;
        this.customerId = customerId;
    }

    public Order(String orderId, String orderStatus, Double totalPrice, int totalItems, OrderItem[] orderItems, OrderPayment[] orderPayments, String hotel, String customerId) {
        this.orderId = orderId;
        this.orderStatus = orderStatus;
        this.totalPrice = totalPrice;
        this.totalItems = totalItems;
        this.orderItems = orderItems;
        this.orderPayments = orderPayments;
        this.hotelId = hotel;
        this.customerId = customerId;
    }

    public String getUpdatedAt() {
        return updatedAt;
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

    public String getHotelId() {
        return hotelId;
    }

    public void setHotelId(String hotelId) {
        this.hotelId = hotelId;
    }

    public Hotel getHotel() {
        return hotel;
    }

    public void setHotel(Hotel hotel) {
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
                "_id='" + orderId + '\'' +
                "orderStatus='" + orderStatus + '\'' +
                ", totalPrice=" + totalPrice +
                ", totalItems=" + totalItems +
                ", orderItems=" + Arrays.toString(orderItems) +
                ", hotel='" + hotel.toString() + '\'' +
                ", customerId='" + customerId + '\'' +
                '}';
    }
}
