package com.example.karokojnr.nadab_customer.model;

import com.google.gson.annotations.SerializedName;

public class Customer {

    @SerializedName("username")
    private String username;

    @SerializedName("profile")
    private String profile;

    @SerializedName("email")
    private String email;

    @SerializedName("fullName")
    private String fullName;

    @SerializedName("_id")
    private String id;

    @SerializedName("mobileNumber")
    private String mobileNumber;


    public Customer(String username, String email, String fullName, String id, String mobileNumber, String profile) {
        this.username = username;
        this.email = email;
        this.fullName = fullName;
        this.id = id;
        this.mobileNumber = mobileNumber;
        this.profile = profile;

    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getFullName() {
        return fullName;
    }

    public String getId() {
        return id;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "username='" + username + '\'' +
                ", profile='" + profile + '\'' +
                ", email='" + email + '\'' +
                ", fullName='" + fullName + '\'' +
                ", id='" + id + '\'' +
                ", mobileNumber='" + mobileNumber + '\'' +
                '}';
    }
}
