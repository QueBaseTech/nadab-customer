package com.example.karokojnr.nadab;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;


public class OrderContract {

    private OrderContract() {}

    /**
     * The "Content authority" is a name for the entire content provider, similar to the
     * relationship between a domain name and its website.  A convenient string to use for the
     * content authority is the package name for the app, which is guaranteed to be unique on the
     * device.
     */
    public static final String CONTENT_AUTHORITY = "com.example.karokojnr.nadab";

    /**
     * Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
     * the content provider.
     */
    public static final String BASE_CONTENT_URI = "content://" + CONTENT_AUTHORITY;


    public static final String PATH_CART = "cart-path";


    /**
     * Inner class that defines constant values for the order database table.
     * Each entry in the table represents a single order.
     */
    public static final class OrderEntry implements BaseColumns {

        /** The content URI to access the fragrance data in the provider */
        public static final Uri CONTENT_URI = Uri.parse(BASE_CONTENT_URI +'/'+ PATH_CART);

        /**
         * The MIME type of the {@link #CONTENT_URI} for a list of fragrance.
         */
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_CART;

        /**
         * The MIME type of the {@link #CONTENT_URI} for a single fragrance.
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_CART;

        /** Name of database table for fragrance */
        public final static String TABLE_NAME = "orders";

        //cart table name
        public final static String CART_TABLE = "cart";

        /**
         * Unique ID number for the fragrance (only for use in the database table).
         *
         * Type: INTEGER
         */
        public final static String _ID = BaseColumns._ID;

        public final static String _CARTID = BaseColumns._ID;

        /**
         * Name of the fragrance.
         *
         * Type: TEXT
         */
        public final static String COLUMN_NAME = "name";
        public final static String COLUMN_DESCRIPTION = "description";
        public final static String COLUMN_IMAGE = "imageurl";
        public final static String COLUMN_PRICE = "price";
        public final static String COLUMN_USERRATING = "userrating";


        public final static String COLUMN_CART_NAME = "cartname";
        public final static String COLUMN_CART_IMAGE = "cartimageurl";
        public final static String COLUMN_CART_QUANTITY = "cartquantity";
        public final static String COLUMN_CART_TOTAL_PRICE = "carttotalprice";

    }

}

