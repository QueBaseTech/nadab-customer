package com.example.karokojnr.nadab.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class utils {

    /*
    * Check if the user is logged in
    * Return true or false
    * */
    public static final boolean isLoggedIn(Context context) {
        SharedPreferences sharedPreferences =   context.getSharedPreferences(Constants.M_SHARED_PREFERENCE, Context.MODE_PRIVATE);
        Log.d("PREFs", "Token:: "+sharedPreferences.getString(Constants.M_SHARED_PREFERENCE_LOGIN_TOKEN, null));
        Log.d("PREFs", "Token:: "+sharedPreferences.getString(Constants.M_SHARED_PREFERENCE_HOTEL_ID, null));
        return sharedPreferences.getString(Constants.M_SHARED_PREFERENCE_LOGIN_TOKEN, null) != null;// return false if it's null
    }

    public static final String getToken(Context context) {
        SharedPreferences sharedPreferences =   context.getSharedPreferences(Constants.M_SHARED_PREFERENCE, Context.MODE_PRIVATE);
        return sharedPreferences.getString(Constants.M_SHARED_PREFERENCE_LOGIN_TOKEN, null);
    }

}
