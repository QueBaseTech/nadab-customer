package com.example.karokojnr.nadab_customer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.example.karokojnr.nadab_customer.api.HotelService;
import com.example.karokojnr.nadab_customer.api.RetrofitInstance;
import com.example.karokojnr.nadab_customer.model.Hotel;
import com.example.karokojnr.nadab_customer.model.HotelsList;
import com.example.karokojnr.nadab_customer.adapter.HotelAdapter;


import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HotelsActivity extends AppCompatActivity {

    private HotelAdapter adapter;
    RecyclerView recyclerView;


    private static final String TAG = "HotelAdapter";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_hotels );



        /*Create handle for the RetrofitInstance interface*/
        HotelService service = RetrofitInstance.getRetrofitInstance ().create ( HotelService.class );
        /*Call the method with parameter in the interface to get the employee data*/

        Call<HotelsList> call = service.getHotels ();


        //Call<HotelsList> call = service.getHotels (utils.getToken(getApplicationContext()));
        /*Log the URL called*/
        Log.wtf ( "URL Called", call.request ().url () + "" );

        call.enqueue ( new Callback<HotelsList> () {
            @Override
            public void onResponse(Call<HotelsList> call, Response<HotelsList> response) {
                assert response.body () != null;
                generateHotelsList ( response.body ().getHotelsArrayList () );
                Log.d ( TAG, "onResponse: fetched okay" );
            }

            @Override
            public void onFailure(Call<HotelsList> call, Throwable t) {
                Log.wtf(TAG, "onFailure: "+t.getMessage() );
                Toast.makeText ( HotelsActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT ).show ();
            }
        } );


    }


    /*Method to generate List of hotel using RecyclerView with custom adapter*/
    private void generateHotelsList(ArrayList<Hotel> empDataList) {
        recyclerView = (RecyclerView) findViewById ( R.id.recycler_view );

        adapter = new HotelAdapter ( empDataList, this );

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager ( HotelsActivity.this );

        recyclerView.setLayoutManager ( layoutManager );

        recyclerView.setAdapter ( adapter );
        return;




    }




}
