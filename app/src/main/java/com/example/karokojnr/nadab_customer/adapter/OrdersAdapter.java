package com.example.karokojnr.nadab_customer.adapter;


import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.karokojnr.nadab_customer.R;
import com.example.karokojnr.nadab_customer.api.RetrofitInstance;
import com.example.karokojnr.nadab_customer.model.Order;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.MyViewHolder> {

    private ArrayList<Order> ordersList;

    Context context;


    public OrdersAdapter(ArrayList<Order> ordersList, Context context) {
        this.ordersList = ordersList;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.order_item, parent, false);
        return new MyViewHolder (view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Order order = ordersList.get ( position );

        String sdf;
        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse(order.getUpdatedAt());
            sdf = new SimpleDateFormat("E dd.MM.yyyy HH:mm:ss").format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            sdf = new Date().toString();
        }
        holder.hotelName.setText(order.getHotel().getBusinessName());
        holder.qty.setText(Integer.toString(order.getTotalItems()));
        holder.bill.setText("Ksh " + order.getTotalPrice());
        holder.time.setText("Date: "+sdf);
        holder.orderStatus.setText(order.getOrderStatus());
    }

    @Override
    public int getItemCount() {
        return ordersList.size ();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
         TextView hotelName, qty, bill, time, orderStatus;

         MyViewHolder(View itemView) {
            super ( itemView );
            hotelName = (TextView) itemView.findViewById ( R.id.hotel_name );
            qty = (TextView) itemView.findViewById ( R.id.items_qty );
            bill = (TextView) itemView.findViewById ( R.id.total_bill);
            time = itemView.findViewById ( R.id.time_ordered );
            orderStatus = itemView.findViewById ( R.id.order_status );
        }
    }

}