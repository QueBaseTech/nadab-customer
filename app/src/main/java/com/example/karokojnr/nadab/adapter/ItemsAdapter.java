package com.example.karokojnr.nadab.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.karokojnr.nadab.R;
import com.example.karokojnr.nadab.api.RetrofitInstance;
import com.example.karokojnr.nadab.model.Product;

import java.util.ArrayList;

public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.MyViewHolder>  {

    private ArrayList<Product> productList;
    Context context;


    public ItemsAdapter(ArrayList<Product> productList, Context context) {
        this.productList = productList;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.custom_list_row, parent, false);
        return new MyViewHolder (view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Product product = productList.get ( position );
        holder.name.setText(product.getName());
//        holder.unitMeasure.setText(product.getUnitMeasure());
        holder.price.setText(product.getPrice());
        //holder.hotel.setText(product.getHotel());
        Glide.with(context)
//                .load("https://ccc7835e.ngrok.io/images/uploads/thumbs/e4a27e9b74b1907706fa31f3dd519b36.jpg")
                .load(RetrofitInstance.BASE_URL+"images/uploads/thumbs/"+product.getImage())
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return productList.size ();
    }

     class MyViewHolder extends RecyclerView.ViewHolder {
         TextView name, unitMeasure, price, hotel, sellingStatus;
         ImageView imageView;

         MyViewHolder(View itemView) {
            super ( itemView );
            name = (TextView) itemView.findViewById ( R.id.tvName );
            //unitMeasure = (TextView) itemView.findViewById ( R.id.tvUnitMeasure );
            price = (TextView) itemView.findViewById ( R.id.tvPrice );
            //hotel = (TextView) itemView.findViewById ( R.id.tvHotel );
            imageView = (ImageView) itemView.findViewById ( R.id.imageView );
          //  sellingStatus = (TextView) view.findViewById ( R.id.sellingStatus );
        }
    }

}