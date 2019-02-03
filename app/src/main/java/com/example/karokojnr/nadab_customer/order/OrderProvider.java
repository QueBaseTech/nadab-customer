package com.example.karokojnr.nadab_customer.order;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;


public class OrderProvider extends ContentProvider {

    public static final String LOG_TAG = OrderProvider.class.getSimpleName();

    /** URI matcher code for the content URI for the cart table */
    private static final int CART = 102;

    /** URI matcher code for the content URI for a single cart in the cart table */
    private static final int CART_ID = 103;

    /**
     * UriMatcher object to match a content URI to a corresponding code.
     * The input passed into the constructor represents the code to return for the root URI.
     * It's common to use NO_MATCH as the input for this case.
     */
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    // Static initializer. This is run the first time anything is called from this class.
    static {
        // The calls to addURI() go here, for all of the content URI patterns that the provider
        // should recognize. All paths added to the UriMatcher have a corresponding code to return
        // when a match is found.

        // The content URI of the form "content://com.example.android.fragrance/fragrance" will map to the
        // integer code {@link #fragrance}. This URI is used to provide access to MULTIPLE rows
        // of the fragrance table.
//        sUriMatcher.addURI(OrderContract.CONTENT_AUTHORITY, OrderContract.PATH_FRAGRANCE, FRAGRANCES);

        sUriMatcher.addURI(OrderContract.CONTENT_AUTHORITY, OrderContract.PATH_CART, CART);

        // The content URI of the form "content://com.example.android.fragrance/fragrance/#" will map to the
        // integer code {@link #fragrance_ID}. This URI is used to provide access to ONE single row
        // of the fragrance table.
        //
        // In this case, the "#" wildcard is used where "#" can be substituted for an integer.
        // For example, "content://com.example.android.fragrance/fragrance/3" matches, but
        // "content://com.example.android.fragrance/fragrance" (without a number at the end) doesn't match.
//        sUriMatcher.addURI(OrderContract.CONTENT_AUTHORITY, OrderContract.PATH_FRAGRANCE + "/#", FRAGRANCE_ID);
        sUriMatcher.addURI(OrderContract.CONTENT_AUTHORITY, OrderContract.PATH_CART + "/#", CART_ID);

    }

    /** Database helper object */
    private OrderDbHelper orderDbHelper;

    @Override
    public boolean onCreate() {
        orderDbHelper = new OrderDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        // Get readable database
        SQLiteDatabase database = orderDbHelper.getReadableDatabase();

        // This cursor will hold the result of the query
        Cursor cursor;

        // Figure out if the URI matcher can match the URI to a specific code
        int match = sUriMatcher.match(uri);
        switch (match) {
            case CART:
                cursor = database.query(OrderContract.OrderEntry.CART_TABLE, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case CART_ID:
                selection = OrderContract.OrderEntry._CARTID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };

                // This will perform a query on the fragrance table where the _id equals 3 to return a
                // Cursor containing that row of the table.
                cursor = database.query(OrderContract.OrderEntry.CART_TABLE, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }

        // Set notification URI on the Cursor,
        // so we know what content URI the Cursor was created for.
        // If the data at this URI changes, then we know we need to update the Cursor.
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        // Return the cursor
        return cursor;
    }


    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        Log.wtf("Inserting:: ", "insert: "+contentValues );
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case CART:
                return insertCart(uri, contentValues);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    private Uri insertCart(Uri uri, ContentValues values) {

        // Get writeable database
        SQLiteDatabase database = orderDbHelper.getWritableDatabase();

        // Insert the new cart with the given values
        long id = database.insert(OrderContract.OrderEntry.CART_TABLE, null, values);
        // If the ID is -1, then the insertion failed. Log an error and return null.
        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }

        // Notify all listeners that the data has changed for the cart content URI
        getContext().getContentResolver().notifyChange(uri, null);

        // Return the new URI with the ID (of the newly inserted row) appended at the end
        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        // Get access to the database and write URI matching code to recognize a single item
        final SQLiteDatabase db = orderDbHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);
        // Keep track of the number of deleted tasks
        int cartDeleted; // starts as 0

        // Write the code to delete a single row of data
        // [Hint] Use selections to delete an item by its row ID
        switch (match) {
            // Handle the single item case, recognized by the ID included in the URI path
            case CART_ID:
                // Get the task ID from the URI path
                String id = uri.getPathSegments().get(1);
                // Use selections/selectionArgs to filter for this ID
                cartDeleted = db.delete(OrderContract.OrderEntry.CART_TABLE, "_id=?", new String[]{id});
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        // Notify the resolver of a change and return the number of items deleted
        if (cartDeleted != 0) {
            // A task was deleted, set notification
            getContext().getContentResolver().notifyChange(uri, null);
        }

        // Return the number of tasks deleted
        return cartDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}
