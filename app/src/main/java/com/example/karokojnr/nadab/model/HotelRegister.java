package com.example.karokojnr.nadab.model;


import com.google.gson.annotations.SerializedName;

public class HotelRegister {
    @SerializedName("hotel")
    private Hotel hotel;

    public Hotel getHotel() {
        return hotel;
    }

    public void setHotel(Hotel hotel) {
        this.hotel = hotel;
    }
}
