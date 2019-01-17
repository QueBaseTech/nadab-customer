package com.example.karokojnr.nadab;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;



import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;




public class OrderDbHelper extends SQLiteOpenHelper {
    private static final String TAG = OrderDbHelper.class.getSimpleName();

    public static final String DATABASE_NAME = "nadab.db";
    private static final int DATABASE_VERSION = 1;
    Context context;
    SQLiteDatabase db;
    ContentResolver mContentResolver;

    //Used to read data from res/ and assets/
    private Resources mResources;



    public OrderDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

        mResources = context.getResources();
        mContentResolver = context.getContentResolver();

        db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_ORDERS_TABLE = "CREATE TABLE " + OrderContract.OrderEntry.CART_TABLE + " (" +
                OrderContract.OrderEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                OrderContract.OrderEntry.COLUMN_NAME + " TEXT UNIQUE NOT NULL, " +
                OrderContract.OrderEntry.COLUMN_DESCRIPTION + " TEXT NOT NULL, " +
                OrderContract.OrderEntry.COLUMN_IMAGE + " TEXT NOT NULL, " +
                OrderContract.OrderEntry.COLUMN_PRICE + " REAL NOT NULL, " +
                OrderContract.OrderEntry.COLUMN_USERRATING + " INTEGER NOT NULL " + " );";

        final String SQL_CREATE_CART_TABLE = "CREATE TABLE " + OrderContract.OrderEntry.CART_TABLE + " (" +
                OrderContract.OrderEntry._CARTID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                OrderContract.OrderEntry.COLUMN_CART_NAME + " TEXT UNIQUE NOT NULL, " +
                OrderContract.OrderEntry.COLUMN_CART_IMAGE + " TEXT NOT NULL, " +
                OrderContract.OrderEntry.COLUMN_CART_QUANTITY + " INTEGER NOT NULL, " +
                OrderContract.OrderEntry.COLUMN_CART_TOTAL_PRICE + " REAL NOT NULL " + " );";


        db.execSQL(SQL_CREATE_CART_TABLE);
//        db.execSQL(SQL_CREATE_ORDERS_TABLE);
        Log.d(TAG, "Database Created Successfully" );


        try {
            readFragrancesFromResources(db);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //TODO: Handle database version upgrades
        db.execSQL("DROP TABLE IF EXISTS " + OrderContract.OrderEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + OrderContract.OrderEntry.CART_TABLE);
        onCreate(db);
    }


    private void readFragrancesFromResources(SQLiteDatabase db) throws IOException, JSONException {
        //db = this.getWritableDatabase();
        StringBuilder builder = new StringBuilder();
        InputStream in = mResources.openRawResource(R.raw.fragrance);
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));

        String line;
        while ((line = reader.readLine()) != null) {
            builder.append(line + "\n");
        }

        //Parse resource into key/values
        final String rawJson = builder.toString();

        final String BGS_FRAGRANCES = "orders";

        final String BGS_NAME = "name";
        final String BGS_UNITMEASURE = "unitmeasure";
        final String BGS_IMAGEURL = "imageUrl";
        final String BGS_PRICE = "price";
        final String BGS_USERRATING = "userrating";

        try {
            JSONObject fragranceJson = new JSONObject(rawJson);
            JSONArray fragranceArray = fragranceJson.getJSONArray(BGS_FRAGRANCES);


            for (int i = 0; i < fragranceArray.length(); i++) {

                String name;
                String unitmeasure;
                String imageUrl;
                Double price;
                int userRating;

                JSONObject fragranceDetails = fragranceArray.getJSONObject(i);

                name = fragranceDetails.getString(BGS_NAME);
                unitmeasure = fragranceDetails.getString(BGS_UNITMEASURE);
                imageUrl = fragranceDetails.getString(BGS_IMAGEURL);
                price = fragranceDetails.getDouble(BGS_PRICE);
                userRating = fragranceDetails.getInt(BGS_USERRATING);


                ContentValues fragranceValues = new ContentValues();

                fragranceValues.put(OrderContract.OrderEntry.COLUMN_NAME, name);
                fragranceValues.put(OrderContract.OrderEntry.COLUMN_DESCRIPTION, unitmeasure);
                fragranceValues.put(OrderContract.OrderEntry.COLUMN_IMAGE, imageUrl);
                fragranceValues.put(OrderContract.OrderEntry.COLUMN_PRICE, price);
                fragranceValues.put(OrderContract.OrderEntry.COLUMN_USERRATING, userRating);

                 db.insert(OrderContract.OrderEntry.TABLE_NAME, null, fragranceValues);

                Log.d(TAG, "Inserted Successfully " + fragranceValues );
            }

            Log.d(TAG, "Inserted Successfully " + fragranceArray.length() );

        } catch (JSONException e) {
            Log.e(TAG, e.getMessage(), e);
            e.printStackTrace();
        }
    }


}
