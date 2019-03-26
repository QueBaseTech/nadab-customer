package com.example.karokojnr.nadab_customer;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.karokojnr.nadab_customer.adapter.CartAdapter;
import com.example.karokojnr.nadab_customer.api.HotelService;
import com.example.karokojnr.nadab_customer.api.RetrofitInstance;
import com.example.karokojnr.nadab_customer.model.Order;
import com.example.karokojnr.nadab_customer.model.OrderItem;
import com.example.karokojnr.nadab_customer.model.OrderResponse;
import com.example.karokojnr.nadab_customer.order.OrderContract;
import com.example.karokojnr.nadab_customer.utils.Constants;
import com.example.karokojnr.nadab_customer.utils.utils;

import java.text.NumberFormat;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static java.lang.Integer.parseInt;


public class CartActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int CART_LOADER = 0;

    /** Adapter for the ListView */
    CartAdapter cartAdapter;
    RecyclerView mRecyclerView;
    Double totalPrice;
    Button placeOrderBt;
    private TextView noItems;
    private Context mContext;

    private Order order = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        mContext = this;

        noItems = findViewById(R.id.no_orders);
        mRecyclerView = (RecyclerView) findViewById(R.id.cart_recycler);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        cartAdapter = new CartAdapter(this);
        mRecyclerView.setAdapter(cartAdapter);
        mRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(this));

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            // Called when a user swipes left or right on a ViewHolder
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                TextView tvId = viewHolder.itemView.findViewById(R.id.tvId);
                int id = parseInt(tvId.getText().toString());
                String stringId = Integer.toString(id);
                Uri uri = OrderContract.OrderEntry.CONTENT_URI;
                uri = uri.buildUpon().appendPath(stringId).build();

                getContentResolver().delete(uri, null, null);
                getLoaderManager().restartLoader(CART_LOADER, null, CartActivity.this);

                // Reset hotel id and order status
                updateOrderStatus();
            }
        }).attachToRecyclerView(mRecyclerView);

        getLoaderManager().initLoader(CART_LOADER, null, this);

        placeOrderBt = (Button) findViewById(R.id.button_order);

        String orderStatus = utils.getOrderStatus(CartActivity.this);
        if (orderStatus.equals("SENT")) placeOrderBt.setText("PAY");

        if (orderStatus.equals("EMPTY")) {
            noItems.setVisibility(View.VISIBLE);
            placeOrderBt.setVisibility(View.INVISIBLE);
        }

        placeOrderBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String orderStatus = utils.getOrderStatus(CartActivity.this);
                Log.wtf("Cart", "onClick: "+orderStatus );
                if (orderStatus.equals("NEW")) {
                    HotelService service = RetrofitInstance.getRetrofitInstance ().create ( HotelService.class );
                    Call<OrderResponse> call = service.placeOrder(order);
                    call.enqueue ( new Callback<OrderResponse>() {
                        @Override
                        public void onResponse(Call<OrderResponse> call, Response<OrderResponse> response) {
                            if (response.body().isSuccess()){
                                utils.setOrderStatus(CartActivity.this, "SENT");
                                placeOrderBt.setText("PAY");
                                Toast.makeText(CartActivity.this, "Order placed successfully", Toast.LENGTH_SHORT).show();
                                String orderId = response.body().getOrder().getOrderId();
                                utils.setOrderId(CartActivity.this, orderId);
                                for(int i=0; i<order.getOrderItems().length; i++) {
                                    OrderItem orderItem = order.getOrderItems()[i];
                                    Uri uri = OrderContract.OrderEntry.CONTENT_URI;
                                    String itemId = orderItem.getId();
                                    uri = uri.buildUpon().appendPath(itemId).build();
                                    ContentValues contentValues = new ContentValues();
                                    contentValues.put(OrderContract.OrderEntry.COLUMN_CART_ORDER_ID, orderId);
                                    contentValues.put(OrderContract.OrderEntry.COLUMN_CART_ORDER_STATUS, "SENT");
                                    mContext.getContentResolver().update(uri, contentValues, "_id = ?", new String[]{ itemId });
                                }
                            } else {
                                Toast.makeText(CartActivity.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<OrderResponse> call, Throwable t) {
                            Toast.makeText ( getApplicationContext (), "Something went wrong...Please try later!", Toast.LENGTH_SHORT ).show ();
                        }
                    } );
                } else if (orderStatus.equals("SENT")) {
                    Toast.makeText(CartActivity.this, "Order already sent pay", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void updateOrderStatus() {
        if(cartAdapter.getItemCount() == 1) {
            utils.setSharedPrefsString(mContext, Constants.M_ORDER_HOTEL, "none");
            utils.setSharedPrefsString(mContext, Constants.M_ORDER_STATUS, "EMPTY");
            noItems.setVisibility(View.VISIBLE);
            placeOrderBt.setVisibility(View.INVISIBLE);
        }
    }


    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        // Define a projection that specifies the columns from the table we care about.
        String[] projection = {
                OrderContract.OrderEntry._CARTID,
                OrderContract.OrderEntry.COLUMN_CART_NAME,
                OrderContract.OrderEntry.COLUMN_CART_IMAGE,
                OrderContract.OrderEntry.COLUMN_CART_QUANTITY,
                OrderContract.OrderEntry.COLUMN_CART_TOTAL_PRICE,
                OrderContract.OrderEntry.COLUMN_CART_ORDER_STATUS,
                OrderContract.OrderEntry.COLUMN_CART_ORDER_ID,
        };

        String selection = OrderContract.OrderEntry.COLUMN_CART_ORDER_STATUS + "='NEW'";

        // This loader will execute the ContentProvider's query method on a background thread
        return new CursorLoader(this,   // Parent activity context
                OrderContract.OrderEntry.CONTENT_URI,   // Provider content URI to query
                projection,             // Columns to include in the resulting Cursor
                selection,                   // Only new items
                null,                   // No selection arguments
                null);                  // Default sort order
    }


    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

        cartAdapter.swapCursor(cursor);
        calculateTotal(cursor);
    }

    @Override
    public void onResume(){
        super.onResume();
        getLoaderManager().restartLoader(CART_LOADER, null, CartActivity.this);
    }

    public double calculateTotal(Cursor cursor){
        totalPrice = 0.00;
        OrderItem[] orderItems = new OrderItem[cursor.getCount()];
        if( order == null)
            order = new Order();

        for (int i = 0; i<cursor.getCount(); i++)
        {
            OrderItem item;
            int id = cursor.getColumnIndex(OrderContract.OrderEntry._CARTID);
            int price = cursor.getColumnIndex(OrderContract.OrderEntry.COLUMN_CART_TOTAL_PRICE);
            int name = cursor.getColumnIndex(OrderContract.OrderEntry.COLUMN_CART_NAME);
            int qty = cursor.getColumnIndex(OrderContract.OrderEntry.COLUMN_CART_QUANTITY);
            int itemStatus = cursor.getColumnIndex(OrderContract.OrderEntry.COLUMN_CART_ORDER_STATUS);

            cursor.moveToPosition(i);

            String _id = cursor.getString(id);
            String itemName = cursor.getString(name);
            int itemQty = cursor.getInt(qty);
            Double fragrancePrice = cursor.getDouble(price);
            String status = cursor.getString(itemStatus);

            if(status.equals("NEW")){
                item = new OrderItem(_id, itemName, itemQty, fragrancePrice);
                orderItems[i] = item;
                totalPrice += fragrancePrice;
            }
        }

        order.setOrderItems(orderItems);
        order.setTotalPrice(totalPrice);
        order.setTotalItems(orderItems.length);
        order.setOrderStatus("NEW");
        order.setHotelId(utils.getSharedPrefsString(this, Constants.M_SHARED_PREFERENCE, Constants.M_ORDER_HOTEL));
        order.setCustomerId(utils.getSharedPrefsString(this, Constants.M_USER_SHARED_PREFERENCE, Constants.M_USER_ID));
        order.setOrderPayments(null);

        TextView totalCost = (TextView) findViewById(R.id.totalPrice);
        String convertPrice = NumberFormat.getCurrencyInstance().format(totalPrice);
        totalCost.setText(convertPrice);
        return totalPrice;
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        cartAdapter.swapCursor(null);
    }

}
