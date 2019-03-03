package com.example.karokojnr.nadab_customer;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Filterable;
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
    private Toolbar toolbar;
    private static final String TAG = "Items";
    private String hotelId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_hotel_meals );
        toolbar = (Toolbar) findViewById ( R.id.toolbar );
        setSupportActionBar ( toolbar );

        getSupportActionBar ().setDisplayHomeAsUpEnabled ( true );
        getSupportActionBar ().setDisplayShowHomeEnabled ( true );

        //toolbar.setNavigationIcon(R.drawable.ic_arrow);
        toolbar.setNavigationOnClickListener ( new View.OnClickListener () {

            @Override
            public void onClick(View view) {

                // Your code
                finish ();
            }
        } );


        hotelId = getIntent().getStringExtra(Constants.M_HOTEL_ID);

        HotelService service = RetrofitInstance.getRetrofitInstance ().create ( HotelService.class );
        Call<Products> call = service.getHotelProducts ( hotelId );
        Log.wtf ( "URL Called", call.request ().url () + "" );
        call.enqueue ( new Callback<Products> () {
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

//menu
        @Override
        public boolean onCreateOptionsMenu (Menu menu){
            getMenuInflater().inflate(R.menu.main, menu);

            MenuItem search = menu.findItem(R.id.action_search);
            SearchView searchView = (SearchView) MenuItemCompat.getActionView(search);
            search(searchView);
            return true;

        }

        @Override
        public boolean onOptionsItemSelected (MenuItem item){
            // Handle action bar item clicks here. The action bar will
            // automatically handle clicks on the Home/Up button, so long
            // as you specify a parent activity in AndroidManifest.xml.
            int id = item.getItemId ();

            //noinspection SimplifiableIfStatement
            if (id == R.id.action_cart) {
                startActivity(new Intent(HotelMeals.this, CartActivity.class));

                // notificationCount=0;//clear notification count
                invalidateOptionsMenu();

                //Toast.makeText ( HotelMeals.this, "Action clicked", Toast.LENGTH_LONG ).show ();
                return true;
            }

            return super.onOptionsItemSelected ( item );
        }

    private void search(SearchView searchView) {

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

//                adapter.getFilter().filter(newText);
                return true;
            }
        });
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
                intent.putExtra(Constants.M_HOTEL_ID, hotelId);

                startActivity(intent);

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        } ));
    }
}


