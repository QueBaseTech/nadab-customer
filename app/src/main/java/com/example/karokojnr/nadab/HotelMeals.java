package com.example.karokojnr.nadab;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.karokojnr.nadab.adapter.ItemsAdapter;
import com.example.karokojnr.nadab.api.HotelService;
import com.example.karokojnr.nadab.api.RetrofitInstance;
import com.example.karokojnr.nadab.model.Product;
import com.example.karokojnr.nadab.model.Products;
import com.example.karokojnr.nadab.utils.utils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static java.security.AccessController.getContext;

public class HotelMeals extends AppCompatActivity {

    private ItemsAdapter adapter;
    RecyclerView recyclerView;

    private List<Product> productList = new ArrayList<> ();
    private ActionBar toolbar;
    FloatingActionButton fab;

    private static final String TAG = "Items";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_hotel_meals );



    /*
    DIPLAY RECYCLER VIEW
    */
    /*Create handle for the RetrofitInstance interface*/
    HotelService service = RetrofitInstance.getRetrofitInstance ().create ( HotelService.class );
    /*Call the method with parameter in the interface to get the employee data*/
    Call<Products> call = service.getProducts ( utils.getToken ( getApplicationContext () ) );
    /*Log the URL called*/
        Log.wtf ( "URL Called", call.request ().url () + "" );

        call.enqueue ( new Callback<Products>() {
        @Override
        public void onResponse(Call<Products> call, Response<Products> response) {
            generateProductsList ( response.body ().getProductArrayList () );
        }

        @Override
        public void onFailure(Call<Products> call, Throwable t) {
            Toast.makeText ( getApplicationContext (), "Something went wrong...Please try later!", Toast.LENGTH_SHORT ).show ();
        }
    } );


    recyclerView = (RecyclerView) findViewById ( R.id.recycler_view );
//        adapter = new ItemsAdapter ( productList, (Items) getApplicationContext () () );
        recyclerView.setHasFixedSize ( true );
    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager ( getApplicationContext () );
        recyclerView.setLayoutManager ( mLayoutManager );
    // adding inbuilt divider line
        recyclerView.addItemDecoration ( new DividerItemDecoration ( this, LinearLayoutManager.VERTICAL ) );
    // adding custom divider line with padding 16dp
    // recyclerView.addItemDecoration(new MyDividerItemDecoration(this, LinearLayoutManager.VERTICAL, 16));
        recyclerView.setItemAnimator ( new DefaultItemAnimator () );
        recyclerView.setAdapter ( adapter );
    // row click listener
        recyclerView.addOnItemTouchListener ( new RecyclerTouchListener ( this, recyclerView, new RecyclerTouchListener.ClickListener () {
        @Override
        public void onClick(View view, int position) {
            Product product = productList.get ( position );
            Toast.makeText ( getApplicationContext (), product.getName () + " is selected!", Toast.LENGTH_SHORT ).show ();
        }

        @Override
        public void onLongClick(View view, int position) {

        }
    } ));

    getProductList ();


       // return view;


}


    /*Method to generate List of employees using RecyclerView with custom adapter*/
    private void generateProductsList(ArrayList<Product> empDataList) {
        //  recyclerView = (RecyclerView) view.findViewById ( R.id.recycler_view );

        adapter = new ItemsAdapter ( empDataList, this);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager ( getApplicationContext () );

        recyclerView.setLayoutManager ( layoutManager );

        recyclerView.setAdapter ( adapter );
    }

    public void getProductList() {
        HotelService apiInterface = RetrofitInstance.getRetrofitInstance ().create ( HotelService.class );
        Call<Products> call = apiInterface.getProducts ( utils.getToken ( getApplicationContext () ) );
        call.enqueue ( new Callback<Products> () {
            @Override
            public void onResponse(Call<Products> call, Response<Products> response) {
                if (response == null) {
                    Toast.makeText ( getApplicationContext (), "Something Went Wrong...!!", Toast.LENGTH_SHORT ).show ();
                    //edited
                } else {
                    assert response.body () != null;
                    for (int i = 0; i < response.body ().getProductArrayList ().size (); i++) {
                        productList.add ( response.body ().getProductArrayList ().get ( i ) );
                    }
                    Log.i ( "RESPONSE: ", "" + response.toString () );
                }
                //adapter.notifyDataSetChanged ();
            }

            @Override
            public void onFailure(Call<Products> call, Throwable t) {
                Toast.makeText ( getApplicationContext (), "Unable to fetch json: " + t.getMessage (), Toast.LENGTH_LONG ).show ();
                Log.e ( "ERROR: ", t.getMessage () );
            }
        } );
    }
}


