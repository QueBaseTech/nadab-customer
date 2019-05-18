package com.example.karokojnr.nadab_customer.api;



import com.example.karokojnr.nadab_customer.model.Customer;
import com.example.karokojnr.nadab_customer.model.CustomerResponse;
import com.example.karokojnr.nadab_customer.model.FCMToken;
import com.example.karokojnr.nadab_customer.model.Hotel;
import com.example.karokojnr.nadab_customer.model.HotelRegister;
import com.example.karokojnr.nadab_customer.model.HotelsList;
import com.example.karokojnr.nadab_customer.model.Order;
import com.example.karokojnr.nadab_customer.model.OrderItem;
import com.example.karokojnr.nadab_customer.model.OrderResponse;
import com.example.karokojnr.nadab_customer.model.Orders;
import com.example.karokojnr.nadab_customer.model.Products;
import com.example.karokojnr.nadab_customer.model.UserLogin;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;


public interface HotelService {

    @GET("hotels/{id}/products")
    Call<Products> getHotelProducts(@Path("id") String hotelId);

    @GET("hotels/")
    Call<HotelsList> getHotels();
    /*
    * Post a new hotel
    * */
    @POST("register/")
    Call<HotelRegister> addHotel(@Body Hotel hotel);

    @POST("orders/add")
    Call<OrderResponse> placeOrder(@Body Order order);

    @POST("orders/{id}/addItem")
    Call<OrderResponse> addItemToOrder(@Path("id") String currentOrderId, @Body OrderItem orderItem);

    @FormUrlEncoded
    @POST("customer/login")
    Call<UserLogin> login(@Field("email") String email, @Field("password") String password);

    @GET("products/")
    Call<Products> getProducts(@Header("x-token") String token);

    // Send app token to server everytime it changes
    @FormUrlEncoded
    @PUT("customer/token")
    Call<FCMToken> sendTokenToServer(@Header("x-token") String authToken, @Field("token") String token);

    @FormUrlEncoded
    @PUT("customers/edit/{id}")
    Call<CustomerResponse> profileEdit(@Header("x-token") String token, @Path ( "id" )String customerId, @Field("fullName") String fullName, @Field("email") String email, @Field("mobileNumber") String mobileNumber);


    @Multipart
    @POST("customer/register")
    Call<UserLogin> addCustomer(@Part MultipartBody.Part fileToUpload, @Part("filename") RequestBody filename, @Part("fullName") RequestBody userName, @Part("mobileNumber") RequestBody mobileNumber, @Part("email") RequestBody userEmail, @Part("password") RequestBody password);

    @GET("customer/orders")
    Call<Orders> getOrders(@Header("x-token") String token);

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
