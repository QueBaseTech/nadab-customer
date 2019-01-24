package com.example.karokojnr.nadab_customer;

import android.database.Cursor;


public class Order {

    public int id;

    public String name;
    public String description;
    public String imageUrl;
    public Double price;
    public int userRating;


    public Order(Cursor cursor) {
        this.name = cursor.getString(cursor.getColumnIndex(OrderContract.OrderEntry.COLUMN_NAME));
        this.description = cursor.getString(cursor.getColumnIndex(OrderContract.OrderEntry.COLUMN_DESCRIPTION));
        this.imageUrl = cursor.getString(cursor.getColumnIndex(OrderContract.OrderEntry.COLUMN_IMAGE));
        this.price = cursor.getDouble(cursor.getColumnIndex(OrderContract.OrderEntry.COLUMN_PRICE));
        this.userRating = cursor.getInt(cursor.getColumnIndex(OrderContract.OrderEntry.COLUMN_USERRATING));
    }

}
