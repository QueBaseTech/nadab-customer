package com.example.karokojnr.nadab_customer.order;

import android.content.ContentResolver;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class OrderDbHelper extends SQLiteOpenHelper {
    private static final String TAG = OrderDbHelper.class.getSimpleName();

    public static final String DATABASE_NAME = "nadab.db";
    private static final int DATABASE_VERSION = 1;
    Context context;
    SQLiteDatabase db;
    ContentResolver mContentResolver;


    public OrderDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContentResolver = context.getContentResolver();
        db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_CART_TABLE = "CREATE TABLE " + OrderContract.OrderEntry.CART_TABLE + " (" +
                OrderContract.OrderEntry._CARTID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                OrderContract.OrderEntry.COLUMN_CART_NAME + " TEXT NOT NULL, " +
                OrderContract.OrderEntry.COLUMN_CART_IMAGE + " TEXT NOT NULL, " +
                OrderContract.OrderEntry.COLUMN_CART_QUANTITY + " INTEGER NOT NULL, " +
                OrderContract.OrderEntry.COLUMN_CART_ORDER_STATUS + " TEXT NOT NULL, " +
                OrderContract.OrderEntry.COLUMN_CART_ORDER_ID + " TEXT  NOT NULL, " +
                OrderContract.OrderEntry.COLUMN_CART_TOTAL_PRICE + " REAL NOT NULL " + " );";

        db.execSQL(SQL_CREATE_CART_TABLE);
        Log.d(TAG, "Database Created Successfully" );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + OrderContract.OrderEntry.CART_TABLE);
        onCreate(db);
    }
}
