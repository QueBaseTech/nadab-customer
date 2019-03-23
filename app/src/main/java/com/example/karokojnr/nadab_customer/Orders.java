package com.example.karokojnr.nadab_customer;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.karokojnr.nadab_customer.adapter.ItemsAdapter;
import com.example.karokojnr.nadab_customer.adapter.OrdersAdapter;
import com.example.karokojnr.nadab_customer.api.HotelService;
import com.example.karokojnr.nadab_customer.api.RetrofitInstance;
import com.example.karokojnr.nadab_customer.model.Order;
import com.example.karokojnr.nadab_customer.model.OrderItem;
import com.example.karokojnr.nadab_customer.model.Product;
import com.example.karokojnr.nadab_customer.model.Products;
import com.example.karokojnr.nadab_customer.utils.Constants;
import com.example.karokojnr.nadab_customer.utils.CustomerSharedPreference;
import com.example.karokojnr.nadab_customer.utils.SharedPrefManager;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Orders extends AppCompatActivity {
    private ArrayList<Order> orders = new ArrayList<>();
    private OrdersAdapter adapter;
    private RecyclerView recyclerView;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        context = this;

        HotelService service = RetrofitInstance.getRetrofitInstance ().create ( HotelService.class );
        Call<com.example.karokojnr.nadab_customer.model.Orders> call = service.getOrders( SharedPrefManager.getInstance(this).getToken() );
        call.enqueue ( new Callback<com.example.karokojnr.nadab_customer.model.Orders>() {
            @Override
            public void onResponse(Call<com.example.karokojnr.nadab_customer.model.Orders> call, Response<com.example.karokojnr.nadab_customer.model.Orders> response) {
                if(response.body().isSuccess()){
                    for (int i = 0; i < response.body().getOrders().size(); i++) {
                        orders.add ( response.body ().getOrders ().get ( i ) );
                    }
                }
                generateOrders (orders);
            }

            @Override
            public void onFailure(Call<com.example.karokojnr.nadab_customer.model.Orders> call, Throwable t) {
                Toast.makeText ( getApplicationContext (), "Something went wrong...Please try later!", Toast.LENGTH_SHORT ).show ();
            }
        } );
    }

    private void generateOrders(ArrayList<Order> empDataList) {
        recyclerView = (RecyclerView) findViewById ( R.id.orders_recyler_view );
        adapter = new OrdersAdapter( empDataList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter ( adapter );
        recyclerView.addOnItemTouchListener ( new RecyclerTouchListener ( this, recyclerView, new RecyclerTouchListener.ClickListener () {
            @Override
            public void onClick(View view, int position) {
                Order order = orders.get( position );
                OrderItem[] orderItems = order.getOrderItems();
                String[] items = new String[orderItems.length];
                for(int i=0;i<orderItems.length;i++) {
                    items[i] = orderItems[i].getQty() + " "+ orderItems[i].getName() + " @ Kshs." + orderItems[i].getPrice();
                }
                final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Items");
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                builder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
                builder.show();
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        } ));
    }
}
