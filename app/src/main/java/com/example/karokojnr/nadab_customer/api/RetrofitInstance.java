package com.example.karokojnr.nadab_customer.api;


import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitInstance {
    private static Retrofit retrofit;
    //private static final String BASE_URL = "https://37d2ff15.ngrok.io";
<<<<<<< HEAD:app/src/main/java/com/example/karokojnr/nadab_customer/api/RetrofitInstance.java
    public static final String BASE_URL = "http://192.168.43.117:5000/";
=======
    public static final String BASE_URL = "http://192.168.43.185:5000/";
>>>>>>> 935f7615d5a021a25410c0df430c0d0098766035:app/src/main/java/com/example/karokojnr/nadab_customer/api/RetrofitInstance.java


    /**
     * Create an instance of Retrofit object
     * */
    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
