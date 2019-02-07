package com.example.karokojnr.nadab_customer.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.karokojnr.nadab_customer.model.UserLogin;


//here for this class we are using a singleton pattern

public class SharedPrefManager {

    private static SharedPrefManager mInstance;
    private static Context mCtx;

    private SharedPrefManager(Context context) {
        mCtx = context;
    }

    public static synchronized SharedPrefManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SharedPrefManager(context);
        }
        return mInstance;
    }

    //method to let the hotel login
    //this method will store the hotel data in shared preferences
    public void userLogin(UserLogin user) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(Constants.M_SHARED_PREFERENCE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString (Constants.M_USER_ID, user.getId ());
        editor.putString (Constants.M_USER_EMAIL, user.getEmail());
        editor.putString (Constants.M_USERNAME, user.getUsername());
        editor.putString (Constants.M_USERFULLNAME, user.getFullName());
        editor.putString (Constants.M_USER_MOBILE, user.getMobileNumber());
        editor.putString (Constants.M_SHARED_PREFERENCE_LOGIN_TOKEN, user.getToken());
        editor.apply();
        editor.commit();
    }

    //this method will checker whether hotel is already logged in or not
    public boolean isLoggedIn() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(Constants.M_SHARED_PREFERENCE, Context.MODE_PRIVATE);
        return sharedPreferences.getString(Constants.M_SHARED_PREFERENCE_LOGIN_TOKEN, null) != null;
    }

    public String getToken() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(Constants.M_SHARED_PREFERENCE, Context.MODE_PRIVATE);
        return sharedPreferences.getString(Constants.M_SHARED_PREFERENCE_LOGIN_TOKEN, null);
    }

    //this method will give the logged in user
    public UserLogin getUser() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(Constants.M_SHARED_PREFERENCE, Context.MODE_PRIVATE);
        return new UserLogin (
                sharedPreferences.getString(Constants.M_USERNAME, null),
                sharedPreferences.getString(Constants.M_USER_EMAIL, null),
                sharedPreferences.getString(Constants.M_USERFULLNAME, null),
                sharedPreferences.getString(Constants.M_SHARED_PREFERENCE_LOGIN_TOKEN, null),
                sharedPreferences.getString(Constants.M_USER_ID, null),
                sharedPreferences.getString(Constants.M_USER_MOBILE, null)
        );
    }


    //this method will logout the user
    public void logout() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(Constants.M_SHARED_PREFERENCE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
}