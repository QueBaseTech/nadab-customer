package com.example.karokojnr.nadab_customer.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.karokojnr.nadab_customer.CartActivity;
import com.example.karokojnr.nadab_customer.api.HotelService;
import com.example.karokojnr.nadab_customer.model.OrderItem;
import com.example.karokojnr.nadab_customer.model.OrderResponse;
import com.example.karokojnr.nadab_customer.order.OrderContract;
import com.example.karokojnr.nadab_customer.R;
import com.example.karokojnr.nadab_customer.api.RetrofitInstance;
import com.example.karokojnr.nadab_customer.utils.utils;

import java.text.DecimalFormat;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    // Class variables for the Cursor that holds task data and the Context
    private Cursor mCursor;
    private Context mContext;

    public static final String TAG = CartAdapter.class.getSimpleName();

    /**
     * Constructor for the CustomCursorAdapter that initializes the Context.
     *
     * @param mContext the current Context
     */
    public CartAdapter(Context mContext) {
        this.mContext = mContext;
    }



    @Override
    public CartViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.cart_item, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CartViewHolder holder, int position) {
        // Indices for the _id, description, and priority columns
        int idIndex = mCursor.getColumnIndex(OrderContract.OrderEntry._CARTID);
        int fragranceName = mCursor.getColumnIndex(OrderContract.OrderEntry.COLUMN_CART_NAME);
        int image = mCursor.getColumnIndex(OrderContract.OrderEntry.COLUMN_CART_IMAGE);
        int quantity = mCursor.getColumnIndex(OrderContract.OrderEntry.COLUMN_CART_QUANTITY);
        int price = mCursor.getColumnIndex(OrderContract.OrderEntry.COLUMN_CART_TOTAL_PRICE);
        int status = mCursor.getColumnIndex(OrderContract.OrderEntry.COLUMN_CART_ORDER_STATUS);

        mCursor.moveToPosition(position); // get to the right location in the cursor

        // Determine the values of the wanted data
        final int id = mCursor.getInt(idIndex);
        final String name = mCursor.getString(fragranceName);
        String fragranceImage = mCursor.getString(image);
        final int fragranceQuantity = mCursor.getInt(quantity);
        final Double fragrancePrice = mCursor.getDouble(price);
        String orderStatus = mCursor.getString(status);
        final double unitPrice = fragrancePrice/fragranceQuantity;
        DecimalFormat precision = new DecimalFormat("0.00");


        holder.tvId.setText("" + id);
        holder.tvName.setText(name);
        holder.tvUnitMeasure.setText("Quantity ordering: " + String.valueOf(fragranceQuantity));
        holder.tvPrice.setText("Kshs." + precision.format(fragrancePrice));
        Glide.with(mContext)
                .load(RetrofitInstance.BASE_URL+"images/uploads/products/thumb_"+fragranceImage)
                .into(holder.imageView);

       if(orderStatus.equals("NEW")){
           holder.order.setText("Order");
           holder.order.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   // get the current order_id and send a request to add it to it
                   OrderItem orderItem = new OrderItem(Integer.toString(id), name, fragranceQuantity, fragrancePrice);
                   String currentOrderId = utils.getOrderId(mContext);
                   HotelService service = RetrofitInstance.getRetrofitInstance ().create ( HotelService.class );
                   Call<OrderResponse> call = service.addItemToOrder(currentOrderId, orderItem);
                   call.enqueue ( new Callback<OrderResponse>() {
                       @Override
                       public void onResponse(Call<OrderResponse> call, Response<OrderResponse> response) {
                           if (response.body().isSuccess()){
                               utils.setOrderStatus(mContext, "SENT");
                               Toast.makeText(mContext, "Order placed successfully", Toast.LENGTH_SHORT).show();
                               String orderId = response.body().getOrder().getOrderId();
                               utils.setOrderId(mContext, orderId);
                               Uri uri = OrderContract.OrderEntry.CONTENT_URI;
                               String itemId = Integer.toString(id);
                               uri = uri.buildUpon().appendPath(itemId).build();
                               ContentValues contentValues = new ContentValues();
                               contentValues.put(OrderContract.OrderEntry.COLUMN_CART_ORDER_ID, orderId);
                               contentValues.put(OrderContract.OrderEntry.COLUMN_CART_ORDER_STATUS, "SENT");
                               mContext.getContentResolver().update(uri, contentValues, "_id = ?", new String[]{ itemId });
                           } else {
                               Toast.makeText(mContext, response.body().getMessage(), Toast.LENGTH_LONG).show();
                           }
                       }

                       @Override
                       public void onFailure(Call<OrderResponse> call, Throwable t) {
                           Toast.makeText ( mContext, "Something went wrong...Please try later!", Toast.LENGTH_SHORT ).show ();
                       }
                   } );

                   // On response update the  local copy
               }
           });

           holder.edit.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                   LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                   View dialogLayout = inflater.inflate(R.layout.edit_cart_item, null);
                   final TextView qty = dialogLayout.findViewById(R.id.quantity_text_view);
                   qty.setText(String.valueOf(fragranceQuantity));
                   Button decrement = dialogLayout.findViewById(R.id.decrement_button);
                   Button increment = dialogLayout.findViewById(R.id.increment_button);

                   decrement.setOnClickListener(new View.OnClickListener() {
                       @Override
                       public void onClick(View v) {
                           int items = Integer.parseInt(qty.getText().toString());
                           if(items > 1)
                               qty.setText(""+(items-1));
                       }
                   });

                   increment.setOnClickListener(new View.OnClickListener() {
                       @Override
                       public void onClick(View v) {
                           int items = Integer.parseInt(qty.getText().toString());
                           qty.setText(""+(items+1));
                       }
                   });

                   builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                       @Override
                       public void onClick(DialogInterface dialog, int which) {

                       }
                   });
                   builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                       @Override
                       public void onClick(DialogInterface dialog, int which) {
                           int items = Integer.parseInt(qty.getText().toString());
                           Uri uri = OrderContract.OrderEntry.CONTENT_URI;
                           uri = uri.buildUpon().appendPath(Integer.toString(id)).build();
                           ContentValues contentValues = new ContentValues();
                           contentValues.put(OrderContract.OrderEntry.COLUMN_CART_QUANTITY, items);
                           contentValues.put(OrderContract.OrderEntry.COLUMN_CART_TOTAL_PRICE, (unitPrice*items));
                           mContext.getContentResolver().update(uri, contentValues, "_id = ?", new String[id]);
                       }
                   });
                   builder.setView(dialogLayout);
                   builder.show();
               }
           });
       }

        if(orderStatus.equals("SENT")){
            holder.order.setText("Cancel");
            holder.edit.setVisibility(View.GONE);
        }
    }


    @Override
    public int getItemCount() {
        if (mCursor == null) {
            return 0;
        }
        return mCursor.getCount();
    }

    public Cursor swapCursor(Cursor c) {
        // check if this cursor is the same as the previous cursor (mCursor)
        if (mCursor == c) {
            return null; // bc nothing has changed
        }
        Cursor temp = mCursor;
        this.mCursor = c; // new cursor value assigned

        //check if this is a valid cursor, then update the cursor
        if (c != null) {
            this.notifyDataSetChanged();
        }
        return temp;
    }

    public class CartViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvUnitMeasure, tvPrice, tvId;
        ImageView imageView, edit;
        Button order;
        public CartViewHolder(View itemView) {
            super(itemView);

            tvId = (TextView) itemView.findViewById(R.id.tvId);
            tvName = (TextView) itemView.findViewById(R.id.tvName);
            tvUnitMeasure = (TextView) itemView.findViewById(R.id.tvUnitMeasure);
            tvPrice = (TextView) itemView.findViewById(R.id.price);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);
            order = (Button) itemView.findViewById(R.id.order_item);
            edit = (ImageView) itemView.findViewById(R.id.edit_cart_item);
        }

    }
}
