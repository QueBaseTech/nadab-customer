package com.example.karokojnr.nadab;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.LayerDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
import com.example.karokojnr.nadab.api.RetrofitInstance;
import com.example.karokojnr.nadab.utils.Constants;

import java.text.NumberFormat;

import static com.example.karokojnr.nadab.OrderContract.OrderEntry.CART_TABLE;

public class ItemDetails extends AppCompatActivity {

    public static final String  FRAGRANCE_NAME = "fragranceName";
    public static final String  FRAGRANCE_DESCRIPTION = "fragranceDescription";
    public static final String  FRAGRANCE_RATING = "fragranceRating";
    public static final String  FRAGRANCE_IMAGE = "fragranceImage";
    public static final String  FRAGRANCE_PRICE = "fragrancePrice";

    private ImageView mImage;


    String fragranceName, name, fragImage;
    int rating;
    int price;
    private int mQuantity = 1;
    private double mTotalPrice;
    String imagePath;
    TextView costTextView;
    ContentResolver mContentResolver;
    private SQLiteDatabase mDb;

    private int mNotificationsCount = 0;
    Button addToCartButton;

    // Item values
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_deatails);

        Intent intent = getIntent();
        itemName = intent.getStringExtra(Constants.M_NAME);
        itemPrice = intent.getStringExtra(Constants.M_PRICE);
        itemUnitMeasure = intent.getStringExtra(Constants.M_UNITMEASURE);
        itemImageUrl = intent.getStringExtra(Constants.M_IMAGE);


//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mContentResolver = this.getContentResolver();
        OrderDbHelper dbHelper = new OrderDbHelper(this);
        mDb = dbHelper.getWritableDatabase();




//        Intent intentThatStartedThisActivity = getIntent();
        addToCartButton = (Button) findViewById(R.id.cart_button);
        costTextView = (TextView) findViewById(R.id.cost_text_view);
        imageView = (ImageView) findViewById(R.id.imageView);
        tvName = (TextView) findViewById(R.id.tvName);
        tvUnitMeasure = (TextView) findViewById(R.id.tvUnitMeasure);
        tvPrice = (TextView) findViewById(R.id.tvPrice);

        tvName.setText(itemName);
        tvUnitMeasure.setText(itemUnitMeasure);
        tvPrice.setText("Kshs." + price);
        float f = Float.parseFloat(Double.toString(rating));
        setTitle(fragranceName);
        ratingBar = (RatingBar) findViewById(R.id.ratingLevel);
        ratingBar.setRating(f);
        Glide.with(this)
                .load(RetrofitInstance.BASE_URL+"images/uploads/thumbs/"+ itemImageUrl)
                .into(imageView);

        if (mQuantity == 1){
            mTotalPrice = Integer.parseInt(itemPrice);
            displayCost(mTotalPrice);
        }

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

        mContentResolver.insert(OrderContract.OrderEntry.CONTENT_URI, cartValues);

        Toast.makeText(this, "Successfully added to Cart", Toast.LENGTH_SHORT).show();
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


    private void updateNotificationsBadge(int count) {
        mNotificationsCount = count;
        Log.wtf(TAG, "updateNotificationsBadge: "+count );
        // force the ActionBar to relayout its MenuItems.
        // onCreateOptionsMenu(Menu) will be called again.
        invalidateOptionsMenu();
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
