package com.example.karokojnr.nadab_customer;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.karokojnr.nadab_customer.adapter.ItemsAdapter;
import com.example.karokojnr.nadab_customer.api.HotelService;
import com.example.karokojnr.nadab_customer.api.RetrofitInstance;
import com.example.karokojnr.nadab_customer.model.Product;
import com.example.karokojnr.nadab_customer.model.Products;
import com.example.karokojnr.nadab_customer.utils.Constants;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HotelMeals extends AppCompatActivity {

    private ItemsAdapter adapter;
    private RecyclerView recyclerView;
    private TextView mNoProductsNotice;
    private List<Product> productList = new ArrayList<> ();
    Toolbar toolbar;

    private static final String TAG = "Items";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_hotel_meals );
        toolbar = (Toolbar) findViewById ( R.id.toolbar );
        setSupportActionBar ( toolbar );



        String hotelId = getIntent().getStringExtra(Constants.M_HOTEL_ID);

        HotelService service = RetrofitInstance.getRetrofitInstance ().create ( HotelService.class );
        Call<Products> call = service.getHotelProducts(hotelId);
        Log.wtf ( "URL Called", call.request ().url () + "" );
        call.enqueue ( new Callback<Products>() {
            @Override
            public void onResponse(Call<Products> call, Response<Products> response) {
                for (int i = 0; i < response.body ().getProductArrayList ().size (); i++) {
                    productList.add ( response.body ().getProductArrayList ().get ( i ) );
                }
                generateProductsList ( response.body ().getProductArrayList () );
            }

            @Override
            public void onFailure(Call<Products> call, Throwable t) {
                Toast.makeText ( getApplicationContext (), "Something went wrong...Please try later!", Toast.LENGTH_SHORT ).show ();
            }
        } );
    }


    private void generateProductsList(ArrayList<Product> empDataList) {
        recyclerView = (RecyclerView) findViewById ( R.id.recycler_view );


        //
        recyclerView.setHasFixedSize(true);
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager (this, 4));
        }



        // Show no products notice
        mNoProductsNotice = (TextView) findViewById(R.id.notProducts);
        if (productList.size() == 0)
            mNoProductsNotice.setVisibility(View.VISIBLE);
        else
            mNoProductsNotice.setVisibility(View.GONE);


        adapter = new ItemsAdapter ( empDataList, this);

        recyclerView.setAdapter ( adapter );
        recyclerView.setHasFixedSize ( true );
        recyclerView.addOnItemTouchListener ( new RecyclerTouchListener ( this, recyclerView, new RecyclerTouchListener.ClickListener () {
            @Override
            public void onClick(View view, int position) {
                Product product = productList.get ( position );
                Toast.makeText ( getApplicationContext (), product.getName () + " is selected!", Toast.LENGTH_SHORT ).show ();

                Intent intent = new Intent(HotelMeals.this, ItemDetails.class);
                intent.putExtra(Constants.M_NAME, product.getName ());
                intent.putExtra(Constants.M_IMAGE, product.getImage ());
                intent.putExtra(Constants.M_UNITMEASURE, product.getUnitMeasure ());
                intent.putExtra(Constants.M_PRICE, product.getPrice ());

                startActivity(intent);

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        } ));
    }
}


