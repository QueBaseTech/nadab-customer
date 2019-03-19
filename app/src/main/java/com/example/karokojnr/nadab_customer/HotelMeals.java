package com.example.karokojnr.nadab_customer;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.karokojnr.nadab_customer.adapter.ItemsAdapter;
import com.example.karokojnr.nadab_customer.api.HotelService;
import com.example.karokojnr.nadab_customer.api.RetrofitInstance;
import com.example.karokojnr.nadab_customer.model.Product;
import com.example.karokojnr.nadab_customer.model.Products;
import com.example.karokojnr.nadab_customer.utils.Constants;
import com.example.karokojnr.nadab_customer.utils.CustomerSharedPreference;
import com.example.karokojnr.nadab_customer.utils.SharedPrefManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.karokojnr.nadab_customer.MainActivity.viewPager;

public class HotelMeals extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ItemsAdapter adapter;
    private RecyclerView recyclerView;
    private TextView mNoProductsNotice;
    private List<Product> productList = new ArrayList<> ();
    private Toolbar toolbar;
    private static final String TAG = "Items";
    private String hotelId;
    static ViewPager viewPager;
    static TabLayout tabLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_hotel_meals );


        toolbar = (Toolbar) findViewById ( R.id.toolbar );
        setSupportActionBar ( toolbar );

        /*getSupportActionBar ().setDisplayHomeAsUpEnabled ( true );
        getSupportActionBar ().setDisplayShowHomeEnabled ( true );

        //toolbar.setNavigationIcon(R.drawable.ic_arrow);
        toolbar.setNavigationOnClickListener ( new View.OnClickListener () {

            @Override
            public void onClick(View view) {

                // Your code
                finish ();
            }
        } );*/


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
             return super.onOptionsItemSelected ( item );
        }
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return true;

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
                Log.wtf ( "itemdetails ", product.getName ());
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

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        // Handle navigation view item clicks here.
        int id = menuItem.getItemId();

        if (id == R.id.nav_home) {
            viewPager.setCurrentItem(0);
        } else if (id == R.id.nav_profile) {
            startActivity(new Intent(HotelMeals.this, ProfileActivity.class));
        } else if (id == R.id.nav_orders) {
            viewPager.setCurrentItem(0);
        } else if (id == R.id.nav_sign_out) {
            // Log.wtf(TAG, "onOptionsItemSelected: Logout");
            SharedPrefManager.getInstance ( getApplicationContext () ).logout ();
            startActivity ( new Intent ( getApplicationContext (), LoginActivity.class ) );
        }else if (id == R.id.terms_conditions){
            startActivity(new Intent(HotelMeals.this, Terms.class));

        }
        this.finish();


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



}


