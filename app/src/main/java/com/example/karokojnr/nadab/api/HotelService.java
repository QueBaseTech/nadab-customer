package com.example.karokojnr.nadab.api;



import com.example.karokojnr.nadab.model.Hotel;
import com.example.karokojnr.nadab.model.HotelRegister;
import com.example.karokojnr.nadab.model.HotelsList;
import com.example.karokojnr.nadab.model.Login;
import com.example.karokojnr.nadab.model.Products;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;


public interface HotelService {

    /*
    * Pull all the hotels
//    * */
//    @GET("hotels/")
//    Call<HotelsList> getHotels(int token);

    @GET("hotels/")
    Call<HotelsList> getHotels();

    /*
    * Post a new hotel
    * */
    @POST("register/")
    Call<HotelRegister> addHotel(@Body Hotel hotel);
//
//    @POST("products/add")
//    Call<Products> addProduct(@Body Product product);

    @FormUrlEncoded
    @POST("login/")
    Call<Login> login(@Field("email") String email, @Field("password") String password);

    @GET("products/")
    Call<Products> getProducts(@Header("x-token") String token);
/*

    *//**
     * URL MANIPULATION
     * @since Not used, Just to know how to use @query to get JSONObject
     * *//*
    @GET("bins/path/")
    Call<NoticeList> getNoticeDataData(@Query("company_no") int companyNo);



    *//**
     * URL MANIPULATION
     * A request URL can be updated dynamically using replacement blocks and parameters on the method.
     * A replacement block is an alphanumeric string surrounded by { and }
     * A corresponding parameter must be annotated with @Path using the same string.
     * *//*
    @GET("group/{id}/users")
    Call<List<Notice>> groupList(@Path("id") int groupId);



    *//**
     * URL MANIPULATION
     * Using Query parameters.
     * *//*
    @GET("group/{id}/users")
    Call<List<Notice>> groupList(@Path("id") int groupId, @Query("sort") String sort);




    *//**
     * URL MANIPULATION
     * complex query parameter combinations a Map can be used
     * *//*
    @GET("group/{id}/noticelist")
    Call<List<Notice>> groupList(@Path("id") int groupId, @QueryMap Map<String, String> options);




    *//**
     * URL MANIPULATION
     * HTTP request body with the @Body annotation
     *//*
    @POST("notice/new")
    Call<Notice> createNotice(@Body Notice notice);




    *//**
     * FORM ENCODED AND MULTIPART
     * Form-encoded data is sent when @FormUrlEncoded is present on the method.
     * Each key-value pair is annotated with @Field containing the name and the object providing the value
     * *//*
    @FormUrlEncoded
    @POST("notice/edit")
    Call<Notice> updateNotice(@Field("id") String id, @Field("title") String title);




    *//**
     * FORM ENCODED AND MULTIPART
     * Multipart requests are used when @Multipart is present on the method.
     * Parts are declared using the @Part annotation.
     * *//*
    @Multipart
    @PUT("notice/photo")
    Call<Notice> updateNotice(@Part("photo") RequestBody photo, @Part("description") RequestBody description);




    *//**
     * HEADER MANIPULATION
     * Set static headers for a method using the @Headers annotation.
     * *//*
    @Headers("Cache-Control: max-age=640000")
    @GET("notice/list")
    Call<List<Notice>> NoticeList();



    *//**
     * HEADER MANIPULATION
     * *//*
    @Headers({
            "Accept: application/vnd.github.v3.full+json",
            "User-Agent: Retrofit-Sample-App"
    })
    @GET("noticelist/{title}")
    Call<Notice> getNotice(@Path("title") String title);




    *//**
     * HEADER MANIPULATION
     * A request Header can be updated dynamically using the @Header annotation.
     * A corresponding parameter must be provided to the @Header.
     * If the value is null, the header will be omitted. Otherwise, toString will be called on the value, and the result used.
     * *//*
    @GET("notice")
    Call<Notice> getNoticeUsingHeader(@Header("Authorization") String authorization);
    */
}
