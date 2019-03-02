package com.example.karokojnr.nadab_customer;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.karokojnr.nadab_customer.adapter.CartAdapter;
import com.example.karokojnr.nadab_customer.api.HotelService;
import com.example.karokojnr.nadab_customer.api.RetrofitInstance;
import com.example.karokojnr.nadab_customer.model.Order;
import com.example.karokojnr.nadab_customer.model.OrderItem;
import com.example.karokojnr.nadab_customer.order.OrderContract;
import com.example.karokojnr.nadab_customer.utils.Constants;
import com.example.karokojnr.nadab_customer.utils.utils;

import java.text.NumberFormat;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CartActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private Toolbar toolbar;
    private static final int CART_LOADER = 0;

    /** Adapter for the ListView */
    CartAdapter cartAdapter;
    RecyclerView mRecyclerView;
    Double totalPrice;
    Button placeOrderBt;

    private Order order = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        toolbar = (Toolbar) findViewById ( R.id.toolbar );
        setSupportActionBar ( toolbar );

        getSupportActionBar ().setDisplayHomeAsUpEnabled ( true );
        getSupportActionBar ().setDisplayShowHomeEnabled ( true );

        //toolbar.setNavigationIcon(R.drawable.ic_arrow);
        toolbar.setNavigationOnClickListener ( new View.OnClickListener () {

            @Override
            public void onClick(View view) {

                // Your code
                finish ();
            }
        } );



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
                // Here is where you'll implement swipe to delete

                // COMPLETED (1) Construct the URI for the item to delete
                //[Hint] Use getTag (from the adapter code) to get the id of the swiped item
                // Retrieve the id of the task to delete
                int id = (int) viewHolder.itemView.getTag();

                // Build appropriate uri with String row id appended
                String stringId = Integer.toString(id);
                Uri uri = OrderContract.OrderEntry.CONTENT_URI;
                uri = uri.buildUpon().appendPath(stringId).build();

                // COMPLETED (2) Delete a single row of data using a ContentResolver
                getContentResolver().delete(uri, null, null);
                // COMPLETED (3) Restart the loader to re-query for all tasks after a deletion
                getLoaderManager().restartLoader(CART_LOADER, null, CartActivity.this);

            }
        }).attachToRecyclerView(mRecyclerView);

        getLoaderManager().initLoader(CART_LOADER, null, this);

        placeOrderBt = (Button) findViewById(R.id.button_order);

        String orderStatus = utils.getOrderStatus(CartActivity.this);
        if (orderStatus.equals("SENT"))
            placeOrderBt.setText("PAY");

        placeOrderBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String orderStatus = utils.getOrderStatus(CartActivity.this);
                Log.wtf("ORDER Status", "onClick: "+ orderStatus );
                if (!orderStatus.equals("NEWs")) {
                    HotelService service = RetrofitInstance.getRetrofitInstance ().create ( HotelService.class );
                    Call<Order> call = service.placeOrder(order);
                    call.enqueue ( new Callback<Order>() {
                        @Override
                        public void onResponse(Call<Order> call, Response<Order> response) {
                            if (response.body().isSuccess()){
                                utils.setOrderStatus(CartActivity.this, "SENT");
                                Toast.makeText(CartActivity.this, "Order placed successfully", Toast.LENGTH_SHORT).show();
                                utils.setOrderId(CartActivity.this, response.body().getOrderId());
                            } else {
                                Toast.makeText(CartActivity.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Order> call, Throwable t) {
                            Toast.makeText ( getApplicationContext (), "Something went wrong...Please try later!", Toast.LENGTH_SHORT ).show ();
                        }
                    } );
                } else if (orderStatus.equals("SENT")) {
                    Toast.makeText(CartActivity.this, "Order already sent pay", Toast.LENGTH_LONG).show();
                }
            }
        });
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
        };

        // This loader will execute the ContentProvider's query method on a background thread
        return new CursorLoader(this,   // Parent activity context
                OrderContract.OrderEntry.CONTENT_URI,   // Provider content URI to query
                projection,             // Columns to include in the resulting Cursor
                null,                   // No selection clause
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
            int price = cursor.getColumnIndex(OrderContract.OrderEntry.COLUMN_CART_TOTAL_PRICE);
            int name = cursor.getColumnIndex(OrderContract.OrderEntry.COLUMN_CART_NAME);
            int qty = cursor.getColumnIndex(OrderContract.OrderEntry.COLUMN_CART_QUANTITY);

            cursor.moveToPosition(i);

            String itemName = cursor.getString(name);
            int itemQty = cursor.getInt(qty);
            Double fragrancePrice = cursor.getDouble(price);

            item = new OrderItem(itemName, itemQty, fragrancePrice);
            orderItems[i] = item;
            totalPrice += fragrancePrice;
        }

        order.setOrderItems(orderItems);
        order.setTotalPrice(totalPrice);
        order.setTotalItems(cursor.getCount());
        order.setOrderStatus("NEW");
        order.setHotel(utils.getSharedPrefsString(this, Constants.M_SHARED_PREFERENCE, Constants.M_ORDER_HOTEL));
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

    //menu
    @Override
    public boolean onCreateOptionsMenu (Menu menu){
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater ().inflate ( R.menu.main_minor, menu );
        return true;
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item){
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId ();

        /*//noinspection SimplifiableIfStatement
        if (id == R.id.action_cart) {
            startActivity(new Intent (HotelMeals.this, CartActivity.class));

            // notificationCount=0;//clear notification count
            invalidateOptionsMenu();

            //Toast.makeText ( HotelMeals.this, "Action clicked", Toast.LENGTH_LONG ).show ();
            return true;
        }
*/
        return super.onOptionsItemSelected ( item );
    }



}
