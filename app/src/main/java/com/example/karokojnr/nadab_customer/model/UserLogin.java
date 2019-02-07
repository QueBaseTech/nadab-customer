package com.example.karokojnr.nadab_customer.model;

import com.google.gson.annotations.SerializedName;

public class UserLogin {

    @SerializedName("success")
    private boolean success;

    @SerializedName("message")
    private String message;

    @SerializedName("username")
    private String username;

    @SerializedName("email")
    private String email;

    @SerializedName("fullName")
    private String fullName;

    @SerializedName("token")
    private String token;

    @SerializedName("_id")
    private String id;

    @SerializedName("mobileNumber")
    private String mobileNumber;

    public UserLogin() { }

    public UserLogin(String username, String email, String fullName, String token, String id, String mobileNumber) {
        this.username = username;
        this.email = email;
        this.fullName = fullName;
        this.token = token;
        this.id = id;
        this.mobileNumber = mobileNumber;
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

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getFullName() {
        return fullName;
    }

    public String getToken() {
        return token;
    }

    public String getId() {
        return id;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }
}
