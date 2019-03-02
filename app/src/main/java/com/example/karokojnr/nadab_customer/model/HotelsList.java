package com.example.karokojnr.nadab_customer.model;



import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.stream.Stream;
/*
* List of hotels model
* Used to read data from API
* @hotels is used to read hotels list from api
* */

public class HotelsList {
    @SerializedName("hotels")
    private ArrayList<Hotel> hotelsList;

    public ArrayList<Hotel> getHotelsArrayList() {
        return hotelsList;
    }

    public void setHotelsArrayList(ArrayList<Hotel> hotelsArrayList) {
        this.hotelsList = hotelsArrayList;
    }


}
