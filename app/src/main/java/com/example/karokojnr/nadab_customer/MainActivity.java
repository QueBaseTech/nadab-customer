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
import android.widget.Toast;

import com.example.karokojnr.nadab_customer.adapter.HotelAdapter;
import com.example.karokojnr.nadab_customer.api.HotelService;
import com.example.karokojnr.nadab_customer.api.RetrofitInstance;
import com.example.karokojnr.nadab_customer.model.Hotel;
import com.example.karokojnr.nadab_customer.model.HotelsList;
import com.example.karokojnr.nadab_customer.utils.Constants;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(true) {
            startActivity(new Intent(this, LoginActivity.class));
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);

        if (viewPager != null) {
            setupViewPager(viewPager);
            tabLayout.setupWithViewPager(viewPager);
        }
        //RECYCLER VIEW
        HotelService service = RetrofitInstance.getRetrofitInstance ().create ( HotelService.class );
        Call<HotelsList> call = service.getHotels ();
        Log.wtf ( "URL Called", call.request ().url () + "" );
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
        //RECYCLERVIEW END
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

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // Get the notifications MenuItem and
        // its LayerDrawable (layer-list)
        MenuItem item = menu.findItem(R.id.action_cart);
        //Notifications

       // NotificationCountSetClass.setAddToCart(MainActivity.this, item,notificationCountCart);


        // force the ActionBar to relayout its MenuItems.
        // onCreateOptionsMenu(Menu) will be called again.
        invalidateOptionsMenu();
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //Option Selected

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {

            startActivity(new Intent (MainActivity.this, HotelsActivity.class));
            return true;
        }else if (id == R.id.action_cart) {

            /*NotificationCountSetClass.setAddToCart(MainActivity.this, item, notificationCount);
            invalidateOptionsMenu();*/
            startActivity(new Intent(MainActivity.this, CartActivity.class));

           // notificationCount=0;//clear notification count
            invalidateOptionsMenu();
            return true;
        }else {
           // startActivity(new Intent(MainActivity.this, EmptyActivity.class));

        }
        return super.onOptionsItemSelected(item);
    }

    private void setupViewPager(ViewPager viewPager) {

        //Imade selected
        /*Adapter adapter = new Adapter(getSupportFragmentManager());
        ImageListFragment fragment = new ImageListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("type", 1);
        fragment.setArguments(bundle);
        adapter.addFragment(fragment, getString(R.string.item_1));
        fragment = new ImageListFragment();
        bundle = new Bundle();
        bundle.putInt("type", 2);
        fragment.setArguments(bundle);
        adapter.addFragment(fragment, getString(R.string.item_2));
        fragment = new ImageListFragment();
        bundle = new Bundle();
        bundle.putInt("type", 3);
        fragment.setArguments(bundle);
        adapter.addFragment(fragment, getString(R.string.item_3));
        fragment = new ImageListFragment();
        bundle = new Bundle();
        bundle.putInt("type", 4);
        fragment.setArguments(bundle);
        adapter.addFragment(fragment, getString(R.string.item_4));
        fragment = new ImageListFragment();
        bundle = new Bundle();
        bundle.putInt("type", 5);
        fragment.setArguments(bundle);
        adapter.addFragment(fragment, getString(R.string.item_5));
        fragment = new ImageListFragment();
        bundle = new Bundle();
        bundle.putInt("type", 6);
        fragment.setArguments(bundle);
        adapter.addFragment(fragment, getString(R.string.item_6));
        viewPager.setAdapter(adapter);*/
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_item1) {
            viewPager.setCurrentItem(0);
        } else if (id == R.id.nav_item2) {
//            viewPager.setCurrentItem(1);
//        } else if (id == R.id.nav_item3) {
//            viewPager.setCurrentItem(2);
//        } else if (id == R.id.nav_item4) {
//            viewPager.setCurrentItem(3);
        } else if (id == R.id.nav_item5) {
            viewPager.setCurrentItem(4);
        }else if (id == R.id.nav_item6) {
            viewPager.setCurrentItem(5);
        }else if (id == R.id.my_wishlist) {
            //startActivity(new Intent(MainActivity.this, WishlistActivity.class));
        }else if (id == R.id.my_cart) {
            startActivity(new Intent(MainActivity.this, CartActivity.class));
        }else {
           // startActivity(new Intent(MainActivity.this, EmptyActivity.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
/*

    @Override
    public void onItemClick(int position) {

        Hotel hotel = hotelList.get ( position );
        Toast.makeText ( getApplicationContext (), hotel.getBusinessName () + " is selected!", Toast.LENGTH_SHORT ).show ();
       // Intent intent = new Intent ( this, SearchActivity.class );
        */
/*Hotel clickedHotel = dataList.get(position);

        intent.putExtra(EXTRA_URL, clickedHotel.getBusinessName ());
        intent.putExtra(EXTRA_CREATOR, clickedHotel.getAddress ());
        intent.putExtra(EXTRA_LIKES, clickedHotel.getPayBillNo ());*//*


       // startActivity ( intent );

    }
*/

    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragments = new ArrayList<>();
        private final List<String> mFragmentTitles = new ArrayList<>();

        public Adapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment, String title) {
            mFragments.add(fragment);
            mFragmentTitles.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitles.get(position);
        }
    }



    public void getProductList() {
        HotelService apiInterface = RetrofitInstance.getRetrofitInstance ().create ( HotelService.class );
        Call<HotelsList> call = apiInterface.getHotels  ();
        call.enqueue ( new Callback<HotelsList> () {
            @Override
            public void onResponse(Call<HotelsList> call, Response<HotelsList> response) {
                if (response == null) {
                    Toast.makeText ( getApplicationContext (), "Something Went Wrong...!!", Toast.LENGTH_SHORT ).show ();
                    //edited
                } else {
                    assert response.body () != null;
                    for (int i = 0; i < response.body ().getHotelsArrayList ().size (); i++) {
                        hotelList.add ( response.body ().getHotelsArrayList ().get ( i ) );
                    }
                    Log.i ( "RESPONSE: ", "" + response.toString () );
                }
                //adapter.notifyDataSetChanged ();
            }
            @Override
            public void onFailure(Call<HotelsList> call, Throwable t) {
                Toast.makeText ( getApplicationContext (), "Unable to fetch json: " + t.getMessage (), Toast.LENGTH_LONG ).show ();
                Log.e ( "ERROR: ", t.getMessage () );
            }
        } );
    }
}
