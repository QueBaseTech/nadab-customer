package com.example.karokojnr.nadab_customer;


import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.view.MenuItemCompat;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.karokojnr.nadab_customer.adapter.HotelAdapter;
import com.example.karokojnr.nadab_customer.api.HotelService;
import com.example.karokojnr.nadab_customer.api.RetrofitInstance;
import com.example.karokojnr.nadab_customer.model.Hotel;
import com.example.karokojnr.nadab_customer.model.HotelsList;
import com.example.karokojnr.nadab_customer.utils.Constants;
import com.example.karokojnr.nadab_customer.utils.CustomerSharedPreference;
import com.example.karokojnr.nadab_customer.utils.SharedPrefManager;
import com.example.karokojnr.nadab_customer.utils.utils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private HotelAdapter adapter;
    RecyclerView recyclerView;
    private List<Hotel> hotelList = new ArrayList<> ();
    private static final String TAG = "HotelAdapter";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(!SharedPrefManager.getInstance(this).isLoggedIn()) {
            startActivity(new Intent(this, LoginActivity.class));
            this.finish();
        } else {
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.setDrawerListener(toggle);
            toggle.syncState();

            NavigationView navigationView = (NavigationView) findViewById ( R.id.nav_view );
            View headerView = navigationView.getHeaderView ( 0 );
            TextView navUsername = (TextView) headerView.findViewById ( R.id.navTextview );
            ImageView navImageview = (ImageView) headerView.findViewById ( R.id.imageView );
            navigationView.setNavigationItemSelectedListener(this);

            CustomerSharedPreference customer = SharedPrefManager.getInstance ( this ).getUser ();
            navUsername.setText ( customer.getUser_fullname () );
            Glide.with ( this ).load ( RetrofitInstance.BASE_URL + "images/uploads/customers/" + String.valueOf ( customer.getIvImage () ) ).into ( navImageview );


            final String selectedHotel = utils.getSharedPrefsString(this, Constants.M_SHARED_PREFERENCE, Constants.M_ORDER_HOTEL);
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
                            Hotel hotel = response.body ().getHotelsArrayList ().get ( i);
                            if(hotel.getPaymentStatus().equals("PAID")) {
                                Log.wtf(TAG, "onResponse: Selected hotel"+ selectedHotel);
                                if(selectedHotel != null){
                                    if(hotel.getId().equals(selectedHotel))
                                        hotelList.add(hotel);
                                } else {
                                    hotelList.add(hotel);
                                }
                            }
                        }
                    }
                    generateHotelsList();
                }
                @Override
                public void onFailure(Call<HotelsList> call, Throwable t) {
                    Log.wtf(TAG, "onFailure: "+t.getMessage());
                    Toast.makeText ( MainActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT ).show ();
                }
            } );
        }
    }

    /*Method to generate List of hotel using RecyclerView with custom adapter*/
    private void generateHotelsList() {
        recyclerView = (RecyclerView) findViewById ( R.id.recycler_view);
        adapter = new HotelAdapter ((ArrayList<Hotel>) hotelList, this);
        recyclerView.setHasFixedSize(true);
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
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
                Intent intent = new Intent(MainActivity.this, HotelMeals.class);
                intent.putExtra(Constants.M_HOTEL_ID, hotel.getId());
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        } ) );
        return;
    }

    @Override
    protected void onResume() {
        super.onResume();
        invalidateOptionsMenu();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else

            new AlertDialog.Builder(this)
                    .setTitle("Really Exit?")
                    .setMessage("Are you sure you want to exit?")
                    .setNegativeButton(android.R.string.no, null)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener () {

                        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                        public void onClick(DialogInterface arg0, int arg1) {

                            finishAffinity();
                        }
                    }).create().show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);

/*
        MenuItem search = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(search);
        search(searchView);

*/

// Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService( Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search)
                .getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        // listening to search query text change
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted
                adapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed
                adapter.getFilter().filter(query);
                return false;
            }
        });



        MenuItem cart = menu.findItem ( R.id. action_cart);
        cart.setOnMenuItemClickListener ( new MenuItem.OnMenuItemClickListener () {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent i = new Intent ( getApplicationContext (), CartActivity.class );
                startActivity ( i );
                return true;
            }
        } );
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
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



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            startActivity(new Intent(MainActivity.this, MainActivity.class));
        } else if (id == R.id.nav_profile) {
            startActivity(new Intent(MainActivity.this, ProfileActivity.class));
        } else if (id == R.id.nav_orders) {
            startActivity(new Intent(MainActivity.this, OrdersActivity.class));
        } else if (id == R.id.nav_cart) {
            startActivity(new Intent(MainActivity.this, CartActivity.class));
        } else if (id == R.id.nav_sign_out) {
            // Log.wtf(TAG, "onOptionsItemSelected: Logout");
            SharedPrefManager.getInstance ( getApplicationContext () ).logout ();
            startActivity ( new Intent ( getApplicationContext (), LoginActivity.class ) );
            finish();
        }else if (id == R.id.terms_conditions){
            startActivity(new Intent(MainActivity.this, Terms.class));

        }
            this.finish();


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
