package com.example.karokojnr.nadab_customer;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;

import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.karokojnr.nadab_customer.adapter.HotelAdapter;
import com.example.karokojnr.nadab_customer.adapter.SearchAdapter;
import com.example.karokojnr.nadab_customer.api.HotelService;
import com.example.karokojnr.nadab_customer.api.RetrofitInstance;
import com.example.karokojnr.nadab_customer.model.Hotel;
import com.example.karokojnr.nadab_customer.model.HotelsList;
import com.example.karokojnr.nadab_customer.utils.Constants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SearchActivity extends AppCompatActivity {

//    public static final String BASE_URL = "https://api.learn2crack.com";
    private SearchAdapter adapter;
    RecyclerView recyclerView;
    private List<Hotel> hotelList = new ArrayList<> ();
    private Menu menu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initViews();
        loadJSON();
    }

    private void initViews(){
        recyclerView = (RecyclerView)findViewById(R.id.card_recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
    }
    private void loadJSON(){
        //RECYCLER VIEW
        HotelService service = RetrofitInstance.getRetrofitInstance ().create ( HotelService.class );
        Call<HotelsList> call = service.getHotels ();
        call.enqueue ( new Callback<HotelsList> () {
            @Override
            public void onResponse(Call<HotelsList> call, Response<HotelsList> response) {
                assert response.body () != null;
                if (response == null) {
                    Toast.makeText ( getApplicationContext (), "Something Went Wrong...!!", Toast.LENGTH_SHORT ).show ();
                } else {
                    assert response.body () != null;
                    for (int i = 0; i < response.body ().getHotelsArrayList ().size (); i++) {
                        hotelList.add ( response.body ().getHotelsArrayList ().get ( i ) );
                    }
                    Log.i ( "RESPONSE: ", "" + response.toString () );
                }
                generateHotelsList ( response.body ().getHotelsArrayList () );
            }
            @Override
            public void onFailure(Call<HotelsList> call, Throwable t) {
                Log.d("Error",t.getMessage());

                //Log.wtf(TAG, "onFailure: "+t.getMessage());
                Toast.makeText ( SearchActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT ).show ();
            }
        } );
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main, menu);

        MenuItem search = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(search);
        search(searchView);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }

    private void search(SearchView searchView) {

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                adapter.getFilter().filter(newText);
                return true;
            }
        });
    }
    /*Method to generate List of hotel using RecyclerView with custom adapter*/
    private void generateHotelsList(ArrayList<Hotel> empDataList) {
        recyclerView = (RecyclerView) findViewById ( R.id.card_recycler_view );
        adapter = new SearchAdapter ( empDataList, this );
        recyclerView.setHasFixedSize(true);
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            recyclerView.setLayoutManager(new GridLayoutManager (this, 2));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager (this, 4));
        }

        // recyclerView.setLayoutManager ( layoutManager );

        recyclerView.setAdapter ( adapter );
        recyclerView.addOnItemTouchListener ( new RecyclerTouchListener ( getApplicationContext (), recyclerView, new RecyclerTouchListener.ClickListener () {
            @Override
            public void onClick(View view, int position) {
                Hotel hotel = hotelList.get ( position );
                Toast.makeText ( getApplicationContext (), hotel.getBusinessName () + " is selected!", Toast.LENGTH_SHORT ).show ();
                Intent intent = new Intent(SearchActivity.this, HotelMeals.class);
                intent.putExtra(Constants.M_HOTEL_ID, hotel.getId());
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        } ) );
        return;
    }

}