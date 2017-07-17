package com.android.driftineo.inventoryapp;

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

/**
 * Created by driftineo on 14/7/17.
 */

public class ProductProvider extends ContentProvider {
    public static final String LOG_TAG = ProductProvider.class.getSimpleName();

    private static final int PRODUCTS = 100;
    private static final int PRODUCT_ID = 101;
    private static final String blankString = "";

    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        uriMatcher.addURI(ProductContract.PACKAGE_AUTHORITY, ProductContract.PRODUCT_PATH, PRODUCTS);

        uriMatcher.addURI(ProductContract.PACKAGE_AUTHORITY, ProductContract.PRODUCT_PATH + "/#", PRODUCT_ID);
    }

    private ProductDbHelper productDbHelper;

    @Override
    public boolean onCreate() {
        productDbHelper = new ProductDbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {

        SQLiteDatabase sqLiteDatabase = productDbHelper.getReadableDatabase();
        Cursor cursor;

        int match = uriMatcher.match(uri);
        switch (match) {
            case PRODUCTS:
                cursor = sqLiteDatabase.query(ProductContract.ProductEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case PRODUCT_ID:
                selection = ProductContract.ProductEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};

                cursor = sqLiteDatabase.query(ProductContract.ProductEntry.TABLE_NAME, projection, selection, selectionArgs
                        , null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        final int match = uriMatcher.match(uri);
        switch (match) {
            case PRODUCTS:
                return ProductContract.ProductEntry.CONTENT_LIST_TYPE;
            case PRODUCT_ID:
                return ProductContract.ProductEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        final int match = uriMatcher.match(uri);
        switch (match) {
            case PRODUCTS:
                return insertProduct(uri, contentValues);

            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }

    }


    private Uri insertProduct(Uri uri, ContentValues contentValues) {


        String productName = contentValues.getAsString(ProductContract.ProductEntry.COLUMN_PRODUCT_NAME);
        String productQuantity = contentValues.getAsString(ProductContract.ProductEntry.COLUMN_PRODUCT_QUANTITY);
        String productPrice = contentValues.getAsString(ProductContract.ProductEntry.COLUMN_PRODUCT_PRICE);
        String productEmail = contentValues.getAsString(ProductContract.ProductEntry.COLUMN_PRODUCT_EMAIL);
        String productPhone = contentValues.getAsString(ProductContract.ProductEntry.COLUMN_PRODUCT_PHONE);



        if (productName == null) {
            throw new IllegalArgumentException("Product requires a name");
        }
        if (productQuantity == null) {
            throw new IllegalArgumentException("Product requires a quantity");
        }
        if (productPrice == null) {
            throw new IllegalArgumentException("Product requires a price");
        }

        if (productEmail == null) {
            throw new IllegalArgumentException("Product requires a price");
        }

        if (productPhone == null) {
            throw new IllegalArgumentException("Product requires a price");
        }


        SQLiteDatabase database = productDbHelper.getWritableDatabase();

        long id = database.insert(ProductContract.ProductEntry.TABLE_NAME, null, contentValues);
        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }
        getContext().getContentResolver().notifyChange(uri, null);

        return ContentUris.withAppendedId(uri, id);

    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection,
                      String[] selectionArgs) {


        final int match = uriMatcher.match(uri);
        switch (match) {
            case PRODUCTS:
                return updateProduct(uri, contentValues, selection, selectionArgs);
            case PRODUCT_ID:
                selection = ProductContract.ProductEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return updateProduct(uri, contentValues, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }


    }

    private int updateProduct(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {

        int count = 0;

        if (contentValues.containsKey(ProductContract.ProductEntry.COLUMN_PRODUCT_NAME)) {
            String productName = contentValues.getAsString(ProductContract.ProductEntry.COLUMN_PRODUCT_NAME);
            if (productName.equals(blankString)) {
                count++;
            }
        }

        if (contentValues.containsKey(ProductContract.ProductEntry.COLUMN_PRODUCT_QUANTITY)) {
            String productQuantity = contentValues.getAsString(ProductContract.ProductEntry.COLUMN_PRODUCT_QUANTITY);
            if (productQuantity.equals(blankString)) {
                count++;
            }
        }

        if (contentValues.containsKey(ProductContract.ProductEntry.COLUMN_PRODUCT_PRICE)) {
            String productPrice = contentValues.getAsString(ProductContract.ProductEntry.COLUMN_PRODUCT_PRICE);
            if (productPrice.equals(blankString)) {
                count++;
            }
        }

        if (contentValues.containsKey(ProductContract.ProductEntry.COLUMN_PRODUCT_PHONE)) {
            String productPhone = contentValues.getAsString(ProductContract.ProductEntry.COLUMN_PRODUCT_PHONE);
            if (productPhone.equals(blankString)) {
                count++;
            }
        }

        if (contentValues.containsKey(ProductContract.ProductEntry.COLUMN_PRODUCT_PHONE)) {
            String productEmail = contentValues.getAsString(ProductContract.ProductEntry.COLUMN_PRODUCT_EMAIL);
            if (productEmail.equals(blankString)) {
                count++;
            }
        }

        if (contentValues.size() == 0 || count == 5) {
            return 0;
        }
        SQLiteDatabase db = productDbHelper.getWritableDatabase();
        int rowsUpdated = db.update(ProductContract.ProductEntry.TABLE_NAME, contentValues, selection, selectionArgs);
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return db.update(ProductContract.ProductEntry.TABLE_NAME, contentValues, selection, selectionArgs);

    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[]
            selectionArgs) {
        SQLiteDatabase database = productDbHelper.getWritableDatabase();

        int rowsDeleted;
        final int match = uriMatcher.match(uri);
        switch (match) {
            case PRODUCTS:
                rowsDeleted = database.delete(ProductContract.ProductEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case PRODUCT_ID:
                selection = ProductContract.ProductEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowsDeleted = database.delete(ProductContract.ProductEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);

        }
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsDeleted;
    }


}
