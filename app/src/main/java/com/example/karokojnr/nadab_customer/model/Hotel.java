package com.example.karokojnr.nadab_customer.model;

import android.widget.EditText;

import com.google.gson.annotations.SerializedName;


/*
* Hotel model
* Contains the hotel fields from API
* Consumed by
* */
public class Hotel {
    @SerializedName("_id")
    private String id;

    @SerializedName("businessName")
    private String businessName;

    @SerializedName("applicantName")
    private String applicantName;

    @SerializedName("payBillNo")
    private String payBillNo;

    @SerializedName("mobileNumber")
    private int mobileNumber;

    @SerializedName("city")
    private String city;

    @SerializedName("address")
    private String address;

    @SerializedName("businessEmail")
    private String businessEmail;

    @SerializedName("createdAt")
    private String createdAt;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @SerializedName("password")
    private String password;

    @SerializedName("image")
    private String profile;

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public Hotel(String businessName, String address, String payBillNo) {
        this.businessName = businessName;
        this.address = address;
        this.payBillNo = payBillNo;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPayBillNo() {
        return payBillNo;
    }

    public void setPayBillNo(String payBillNo) {
        this.payBillNo = payBillNo;
    }
}