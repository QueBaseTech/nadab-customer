package com.example.karokojnr.nadab_customer;
import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.karokojnr.nadab_customer.api.RetrofitInstance;
import com.example.karokojnr.nadab_customer.model.Order;
import com.example.karokojnr.nadab_customer.model.OrderItem;
import com.example.karokojnr.nadab_customer.order.OrderContract;
import com.example.karokojnr.nadab_customer.order.OrderDbHelper;
import com.example.karokojnr.nadab_customer.utils.Constants;
import com.example.karokojnr.nadab_customer.utils.utils;

import java.text.NumberFormat;

import static com.example.karokojnr.nadab_customer.order.OrderContract.OrderEntry.CART_TABLE;

public class ItemDetails extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final String  FRAGRANCE_NAME = "fragranceName";
    public static final String  FRAGRANCE_DESCRIPTION = "fragranceDescription";
    public static final String  FRAGRANCE_RATING = "fragranceRating";
    public static final String  FRAGRANCE_IMAGE = "fragranceImage";
    public static final String  FRAGRANCE_PRICE = "fragrancePrice";

    private ImageView mImage;


    private String fragranceName, name;
    private int rating;
    private int price;
    private int mQuantity = 1;
    private double mTotalPrice;
    private TextView costTextView;
    private ContentResolver mContentResolver;
    private SQLiteDatabase mDb;

    private int mNotificationsCount = 0;
    Button addToCartButton;

    // Item values
    private String itemHotelId;
    private String itemName;
    private String itemPrice;
    private String itemUnitMeasure;
    private String itemImageUrl;

    public static final String TAG = ItemDetails.class.getSimpleName();
    private ImageView imageView;
    private TextView tvName;
    private TextView tvPrice;
    private TextView tvUnitMeasure;
    private RatingBar ratingBar;
    private Toolbar mTopToolbar;

    private Cursor mCursor;
    boolean exists = false;
    double unitPrice;
    int qty = 0;
    int id = 0;
    private static final int CART_LOADER = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_deatails);

        mTopToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mTopToolbar);


        getSupportActionBar ().setDisplayHomeAsUpEnabled ( true );
        getSupportActionBar ().setDisplayShowHomeEnabled ( true );

        mTopToolbar.setNavigationIcon(R.drawable.ic_arrow);
        mTopToolbar.setNavigationOnClickListener ( new View.OnClickListener () {

            @Override
            public void onClick(View view) {

                // Your code
                finish ();
            }
        } );


        Intent intent = getIntent();
        itemName = intent.getStringExtra(Constants.M_NAME);
        itemPrice = intent.getStringExtra(Constants.M_PRICE);
       // itemUnitMeasure = intent.getStringExtra(Constants.M_UNITMEASURE);
        itemImageUrl = intent.getStringExtra(Constants.M_IMAGE);
        itemHotelId = intent.getStringExtra(Constants.M_HOTEL_ID);

       /* try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (NullPointerException e) {
            Log.wtf(TAG, "onCreate: "+e.getMessage());
        }*/
       mContentResolver = this.getContentResolver();
       OrderDbHelper dbHelper = new OrderDbHelper(this);
       mDb = dbHelper.getWritableDatabase();
        addToCartButton = (Button) findViewById(R.id.cart_button);
        costTextView = (TextView) findViewById(R.id.cost_text_view);
        imageView = (ImageView) findViewById(R.id.ivImage);
        tvName = (TextView) findViewById(R.id.tvName);
        //  tvUnitMeasure = (TextView) findViewById(R.id.tvUnitMeasure);
        tvPrice = (TextView) findViewById(R.id.tvPrice);

        tvName.setText(itemName);
        //        tvUnitMeasure.setText(itemUnitMeasure);
        tvPrice.setText("Kshs." + itemPrice);
        float f = Float.parseFloat(Double.toString(rating));
        setTitle(fragranceName);
        /*ratingBar = (RatingBar) findViewById(R.id.ratingLevel);
        ratingBar.setRating(f);*/
        Glide.with(this)
                .load(RetrofitInstance.BASE_URL+"images/uploads/products/thumb_"+ itemImageUrl)
                .into(imageView);

        if (mQuantity == 1){
            mTotalPrice = Integer.parseInt(itemPrice);
            displayCost(mTotalPrice);
        }

        getLoaderManager().initLoader(CART_LOADER, null, this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {


        getMenuInflater().inflate(R.menu.menu_main, menu);

        // Get the notifications MenuItem and
        // its LayerDrawable (layer-list)
        MenuItem item = menu.findItem(R.id.action_notifications);
        LayerDrawable icon = (LayerDrawable) item.getIcon();

        // Update LayerDrawable's BadgeDrawable
        Utils.setBadgeCount(this, icon, mNotificationsCount);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_notifications:
                Intent intent = new Intent(this, CartActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onPause(){
        super.onPause();
        new FetchCountTask().execute();
    }

    public void increment(View view){
        mQuantity = mQuantity + 1;
        displayQuantity(mQuantity);
        mTotalPrice = mQuantity * Integer.parseInt(itemPrice);
        displayCost(mTotalPrice);
    }

    public void decrement(View view){
        if (mQuantity > 1){
            mQuantity = mQuantity - 1;
            displayQuantity(mQuantity);
            mTotalPrice = mQuantity * Integer.parseInt(itemPrice);
            displayCost(mTotalPrice);
        }
    }

    private void displayQuantity(int numberOfItems) {
        TextView quantityTextView = (TextView) findViewById(R.id.quantity_text_view);
        quantityTextView.setText(String.valueOf(numberOfItems));
    }

    private void displayCost(double totalPrice) {
        String convertPrice = NumberFormat.getCurrencyInstance().format(totalPrice);
        costTextView.setText(convertPrice);
    }

    private void addValuesToCart() {
        ContentValues cartValues = new ContentValues();
        cartValues.put(OrderContract.OrderEntry.COLUMN_CART_NAME, itemName);
        cartValues.put(OrderContract.OrderEntry.COLUMN_CART_IMAGE, itemImageUrl);
        cartValues.put(OrderContract.OrderEntry.COLUMN_CART_QUANTITY, mQuantity);
        cartValues.put(OrderContract.OrderEntry.COLUMN_CART_TOTAL_PRICE, mTotalPrice);
        cartValues.put(OrderContract.OrderEntry.COLUMN_CART_ORDER_STATUS, "NEW");
        cartValues.put(OrderContract.OrderEntry.COLUMN_CART_ORDER_ID, "Null");

        String currentHotel = utils.getSharedPrefsString(ItemDetails.this, Constants.M_SHARED_PREFERENCE, Constants.M_ORDER_HOTEL);

        // Check if item already exists in database and it's not ordered, if so, updated the qty otherwise add a new entry

        unitPrice = mTotalPrice/mQuantity;

        if(exists) {
            int items = mQuantity + qty;
            Uri uri = OrderContract.OrderEntry.CONTENT_URI;
            uri = uri.buildUpon().appendPath(Integer.toString(id)).build();
            ContentValues contentValues = new ContentValues();
            contentValues.put(OrderContract.OrderEntry.COLUMN_CART_QUANTITY, items);
            contentValues.put(OrderContract.OrderEntry.COLUMN_CART_TOTAL_PRICE, (unitPrice*items));
            getContentResolver().update(uri, contentValues, "_id = ?", new String[id]);
        } else {
            if(currentHotel.equals("none")){
                utils.setSharedPrefsString(ItemDetails.this, Constants.M_ORDER_HOTEL, itemHotelId);
                utils.setSharedPrefsString(ItemDetails.this, Constants.M_ORDER_STATUS, "NEW");
                mContentResolver.insert(OrderContract.OrderEntry.CONTENT_URI, cartValues);
                Toast.makeText(this, "Successfully added to Cart", Toast.LENGTH_SHORT).show();
            } else if(!currentHotel.equals(itemHotelId)) {
                Toast.makeText(this, "You have incomplete orders from another hotel, you can't order from two hotels.", Toast.LENGTH_LONG).show();
            } else if(currentHotel.equals(itemHotelId)) {
                mContentResolver.insert(OrderContract.OrderEntry.CONTENT_URI, cartValues);
                Toast.makeText(this, "Successfully added to Cart", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Item not to Cart", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void addToCart(View view) {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the postivie and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.add_to_cart);
        builder.setPositiveButton(R.string.add, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                addValuesToCart();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Cancel" button, so dismiss the dialog
                // and continue editing the items.
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public Cursor swapCursor(Cursor c) {
        // check if this cursor is the same as the previous cursor (mCursor)
        if (mCursor == c) {
            return null; // bc nothing has changed
        }
        Cursor temp = mCursor;
        this.mCursor = c; // new cursor value assigned

        return temp;
    }

    private void checkIfItemExits(Cursor cursor) {
        for (int i = 0; i<cursor.getCount(); i++)
        {
            int dbId = cursor.getColumnIndex(OrderContract.OrderEntry._CARTID);
            int dbName = cursor.getColumnIndex(OrderContract.OrderEntry.COLUMN_CART_NAME);
            int dbQty = cursor.getColumnIndex(OrderContract.OrderEntry.COLUMN_CART_QUANTITY);

            cursor.moveToPosition(i);

            qty = cursor.getInt(dbQty);
            id = cursor.getInt(dbId);
            String name = cursor.getString(dbName);
            exists = !name.isEmpty();
        }
    }
    private void updateNotificationsBadge(int count) {
        mNotificationsCount = count;
        Log.wtf(TAG, "updateNotificationsBadge: "+count );
        // force the ActionBar to relayout its MenuItems.
        // onCreateOptionsMenu(Menu) will be called again.
        invalidateOptionsMenu();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // Define a projection that specifies the columns from the table we care about.
        String[] projection = {
                OrderContract.OrderEntry._CARTID,
                OrderContract.OrderEntry.COLUMN_CART_QUANTITY,
                OrderContract.OrderEntry.COLUMN_CART_NAME,
                OrderContract.OrderEntry.COLUMN_CART_TOTAL_PRICE,
        };

        String selection = OrderContract.OrderEntry.COLUMN_CART_NAME + "=? and " + OrderContract.OrderEntry.COLUMN_CART_ORDER_STATUS + "='NEW'";

        // This loader will execute the ContentProvider's query method on a background thread
        return new CursorLoader(this,   // Parent activity context
                OrderContract.OrderEntry.CONTENT_URI,   // Provider content URI to query
                projection,             // Columns to include in the resulting Cursor
                selection,                   // No selection clause
                new String[]{itemName},                   // No selection arguments
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
//        swapCursor(data);
        checkIfItemExits(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        swapCursor(null);
    }

    /*
Sample AsyncTask to fetch the notifications count
*/
    class FetchCountTask extends AsyncTask<Void, Void, Integer> {

        @Override
        protected Integer doInBackground(Void... params) {
            String countQuery = "SELECT  * FROM " + CART_TABLE;
            Cursor cursor = mDb.rawQuery(countQuery, null);
            int count = cursor.getCount();
            cursor.close();
            return count;

        }

        @Override
        public void onPostExecute(Integer count) {
            updateNotificationsBadge(count);
        }
    }


}
