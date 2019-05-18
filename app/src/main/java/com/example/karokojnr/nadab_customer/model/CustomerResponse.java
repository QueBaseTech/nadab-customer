package com.example.karokojnr.nadab_customer.model;

import com.google.gson.annotations.SerializedName;

public class CustomerResponse {
    @SerializedName("success")
    private boolean success;

    @SerializedName("message")
    private String message;

    @SerializedName("customer")
    private Customer customer;

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public Customer getCustomer() {
        return customer;
    }

    public boolean isSuccessful() {
        return success;
    }
}
