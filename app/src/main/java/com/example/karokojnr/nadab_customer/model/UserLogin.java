package com.example.karokojnr.nadab_customer.model;

import com.google.gson.annotations.SerializedName;

public class UserLogin {

    @SerializedName("success")
    private boolean success;

    @SerializedName("message")
    private String message;

    @SerializedName("token")
    private String token;

    @SerializedName("customer")
    private Customer customer;

    public UserLogin() { }

    public Customer getCustomer() {
        return customer;
    }

    public String getToken() {
        return token;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "UserLogin{" +
                "success=" + success +
                ", message='" + message + '\'' +
                ", token='" + token + '\'' +
                ", customer='" + customer.toString() + '\'' +
                '}';
    }
}
