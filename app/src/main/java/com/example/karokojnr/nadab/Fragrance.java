package com.example.karokojnr.nadab;

import android.database.Cursor;


public class Fragrance {

    public int id;

    public String tvName;
    public String tvUnitMeasure;
    public String imageView;
    public Double tvPrice;
    public int userRating;


    public Fragrance(Cursor cursor) {
        this.tvName = cursor.getString(cursor.getColumnIndex(FragranceContract.FragranceEntry.COLUMN_ITEM_NAME));
        this.tvUnitMeasure = cursor.getString(cursor.getColumnIndex(FragranceContract.FragranceEntry.COLUMN_UNIT_MEASURE));
        this.imageView = cursor.getString(cursor.getColumnIndex(FragranceContract.FragranceEntry.COLUMN_ITEM_IMAGE));
        this.tvPrice = cursor.getDouble(cursor.getColumnIndex(FragranceContract.FragranceEntry.COLUMN_ITEM_PRICE));
        this.userRating = cursor.getInt(cursor.getColumnIndex(FragranceContract.FragranceEntry.COLUMN_USERRATING));
    }

}
