package com.example.karokojnr.nadab_customer.model;

import com.google.gson.annotations.SerializedName;

public class FCMToken {
    @SerializedName("token")
    private String token;

    @SerializedName("userID")
    public String userID;

    public FCMToken(String token, String userID) {
        this.token = token;
        this.userID = userID;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
}
