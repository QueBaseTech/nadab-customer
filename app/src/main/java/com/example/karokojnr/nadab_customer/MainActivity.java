package com.example.karokojnr.nadab_customer;


import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.karokojnr.nadab_customer.adapter.HotelAdapter;
import com.example.karokojnr.nadab_customer.api.HotelService;
import com.example.karokojnr.nadab_customer.api.RetrofitInstance;
import com.example.karokojnr.nadab_customer.model.Customer;
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


    public static final String EXTRA_URL = "imageUrl";
    public static final String EXTRA_CREATOR = "creatorName";
    public static final String EXTRA_LIKES = "likeCount";

    private static final String TAG = "HotelAdapter";

    public static int notificationCountCart = 0;
    static ViewPager viewPager;
    static TabLayout tabLayout;
    private ActionBar actionBar;
    private LinearLayout nav_header;
    private ImageView ivImage;
    private TextView user_name, user_email;


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

            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            View headerView = navigationView.getHeaderView ( 0 );

            navigationView.setNavigationItemSelectedListener(this);

            viewPager = (ViewPager) findViewById(R.id.viewpager);
            tabLayout = (TabLayout) findViewById(R.id.tabs);


            ivImage = (ImageView) headerView.findViewById ( R.id.ivImage );
            //user_name = (TextView) headerView.findViewById ( R.id.tv_user_name ) ;
            user_email = (TextView) headerView.findViewById ( R.id.tv_user_email ) ;



            //getting the current user
            CustomerSharedPreference customer = SharedPrefManager.getInstance(this).getUser ();
            //user_name.setText(String.valueOf(customer.getFullName ()));
            user_email.setText(String.valueOf ( customer.getEmail ()));
            //tv_user_mobile.setText(String.valueOf ( customer.getMobileNumber ()));
            Glide.with(this)
                    .load(RetrofitInstance.BASE_URL+"images/uploads/customers/"+ String.valueOf ( customer.getIvImage ()))
                    .into(ivImage);


//            nav_header = (LinearLayout) findViewById ( R.id.nav_view)
            navigationView.setOnClickListener ( new View.OnClickListener () {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(MainActivity.this, ProfileActivity.class));

                }
            } );

            if (viewPager != null) {
                setupViewPager(viewPager);
                tabLayout.setupWithViewPager(viewPager);
            }
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
                    Log.wtf(TAG, "onFailure: "+t.getMessage());
                    Toast.makeText ( MainActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT ).show ();
                }
            } );
        }
    }

    /*Method to generate List of hotel using RecyclerView with custom adapter*/
    private void generateHotelsList(ArrayList<Hotel> empDataList) {
        recyclerView = (RecyclerView) findViewById ( R.id.recycler_view );
        adapter = new HotelAdapter ( empDataList, this );
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
    //Styling for double press back button
    private static long back_pressed;
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
        //
        if (back_pressed + 2000 > System.currentTimeMillis()){
            super.onBackPressed();
        }
        else{
            Toast.makeText(this, "Press once again to exit", Toast.LENGTH_SHORT).show();
            back_pressed = System.currentTimeMillis();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.action_cart);
        invalidateOptionsMenu();
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_search) {
            startActivity(new Intent (MainActivity.this, SearchActivity.class));
            return true;
        }else if (id == R.id.action_cart) {

            startActivity(new Intent(MainActivity.this, CartActivity.class));

             notificationCountCart=0;
            invalidateOptionsMenu();
            return true;

        }
        return super.onOptionsItemSelected(item);
    }

    private void setupViewPager(ViewPager viewPager) {

    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.home) {
            viewPager.setCurrentItem(0);
        } else if (id == R.id.my_profile) {
            startActivity(new Intent(MainActivity.this, ProfileActivity.class));
        }else if (id == R.id.sign_out) {
            Log.wtf(TAG, "onOptionsItemSelected: Logout");
            SharedPrefManager.getInstance(getApplicationContext()).logout();
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            this.finish();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}
