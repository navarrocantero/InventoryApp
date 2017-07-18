package com.android.driftineo.inventoryapp;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by driftineo on 14/7/17.
 */

public class ProductContract {

    private ProductContract() {
    }

    public static final String PACKAGE_AUTHORITY = "com.android.driftineo.inventoryapp";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + PACKAGE_AUTHORITY);

    public static final String PRODUCT_PATH = "inventoryapp";

    public static final class ProductEntry implements BaseColumns {

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PRODUCT_PATH);

        public static final String CONTENT_LIST_TYPE =

                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + PACKAGE_AUTHORITY + "/" + PRODUCT_PATH;

        public static final String CONTENT_ITEM_TYPE =

                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + PACKAGE_AUTHORITY + "/" + PRODUCT_PATH;


        public final static String TABLE_NAME = "products ";

        public final static String _ID = BaseColumns._ID;

        public final static String COLUMN_PRODUCT_NAME = "name";

        public final static String COLUMN_PRODUCT_QUANTITY = "quantity";

        public final static String COLUMN_PRODUCT_PRICE = "price";

        public final static String COLUMN_PRODUCT_IMAGE = "image";

        public final static String COLUMN_PRODUCT_PHONE = "phone";

        public final static String COLUMN_PRODUCT_EMAIL = "mail";
    }
}
