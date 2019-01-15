package com.example.karokojnr.nadab.adapter;


import android.annotation.SuppressLint;
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

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Product product = productList.get ( position );
        holder.name.setText(product.getName());
        holder.unitMeasure.setText(product.getUnitMeasure());
        holder.price.setText("Ksh " + product.getPrice());
        Glide.with(context)
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
            unitMeasure = (TextView) itemView.findViewById ( R.id.tvUnitMeasure );
            price = (TextView) itemView.findViewById ( R.id.tvPrice);
            imageView = (ImageView) itemView.findViewById ( R.id.imageView );
        }
    }

}