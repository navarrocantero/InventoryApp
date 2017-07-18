package com.android.driftineo.inventoryapp;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.driftineo.inventoryapp.ProductContract.ProductEntry;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int THIS_LOADER = 0;
    ProductAdapter productAdapter;
    private byte[] photoByteArray;
    private PhotoUtils photoUtils;
    private Uri photoName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        photoUtils = new PhotoUtils(this);
        ListView listView = (ListView) findViewById(R.id.list_product);
        productAdapter = new ProductAdapter(this, null);
        listView.setAdapter(productAdapter);
        ImageView imageView = (ImageView) findViewById(R.id.firstImage);
        listView.setEmptyView(imageView);
        getLoaderManager().initLoader(THIS_LOADER, null, this);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, EditorActivity.class);
                Uri uri = ContentUris.withAppendedId(ProductEntry.CONTENT_URI, id);
                intent.putExtra("photoArray",photoByteArray);
                intent.setData(uri);
                startActivity(intent);
            }
        });
    }


    private void addNewProducts() {

        Uri uri = Uri.parse("android.resource://com.android.driftineo.inventoryapp/" + R.drawable.galaxysseven);
        getImage(uri);
        ContentValues contentValues = new ContentValues();
        contentValues.put(ProductEntry.COLUMN_PRODUCT_NAME, "Galaxy S7");
        contentValues.put(ProductEntry.COLUMN_PRODUCT_PRICE, "800");
        contentValues.put(ProductEntry.COLUMN_PRODUCT_QUANTITY, "6");
        contentValues.put(ProductEntry.COLUMN_PRODUCT_IMAGE, photoByteArray);
        contentValues.put(ProductEntry.COLUMN_PRODUCT_EMAIL, "samsung@samsung.ko");
        contentValues.put(ProductEntry.COLUMN_PRODUCT_PHONE, "345335");
        Uri newUri = getContentResolver().insert(ProductEntry.CONTENT_URI, contentValues);
        uri = Uri.parse("android.resource://com.android.driftineo.inventoryapp/" + R.drawable.iphoneseven);
        getImage(uri);
        contentValues = new ContentValues();
        contentValues.put(ProductEntry.COLUMN_PRODUCT_NAME, "Iphone 7");
        contentValues.put(ProductEntry.COLUMN_PRODUCT_PRICE, "1000");
        contentValues.put(ProductEntry.COLUMN_PRODUCT_QUANTITY, "13");
        contentValues.put(ProductEntry.COLUMN_PRODUCT_IMAGE,photoByteArray);
        contentValues.put(ProductEntry.COLUMN_PRODUCT_EMAIL, "apple@iphone.ch");
        contentValues.put(ProductEntry.COLUMN_PRODUCT_PHONE, "65464523");
        newUri = getContentResolver().insert(ProductEntry.CONTENT_URI, contentValues);
        uri = Uri.parse("android.resource://com.android.driftineo.inventoryapp/" + R.drawable.samsungj);
        getImage(uri);
        contentValues = new ContentValues();
        contentValues.put(ProductEntry.COLUMN_PRODUCT_NAME, "Samsung J7");
        contentValues.put(ProductEntry.COLUMN_PRODUCT_PRICE, "120");
        contentValues.put(ProductEntry.COLUMN_PRODUCT_QUANTITY, "45");
        contentValues.put(ProductEntry.COLUMN_PRODUCT_IMAGE,photoByteArray );
        contentValues.put(ProductEntry.COLUMN_PRODUCT_EMAIL, "samsung@samsung.ko");
        contentValues.put(ProductEntry.COLUMN_PRODUCT_PHONE, "78965468");
        newUri = getContentResolver().insert(ProductEntry.CONTENT_URI, contentValues);
        uri = Uri.parse("android.resource://com.android.driftineo.inventoryapp/" + R.drawable.movil_lg_stylus_ii_52_16gb_quad_c_1331843_l);
        getImage(uri);
        contentValues = new ContentValues();
        contentValues.put(ProductEntry.COLUMN_PRODUCT_NAME, "Lg g24  ");
        contentValues.put(ProductEntry.COLUMN_PRODUCT_PRICE, "324");
        contentValues.put(ProductEntry.COLUMN_PRODUCT_QUANTITY, "5");
        contentValues.put(ProductEntry.COLUMN_PRODUCT_IMAGE, photoByteArray);
        contentValues.put(ProductEntry.COLUMN_PRODUCT_EMAIL, "lifeis@good.com");
        contentValues.put(ProductEntry.COLUMN_PRODUCT_PHONE, "45783489");
        newUri = getContentResolver().insert(ProductEntry.CONTENT_URI, contentValues);
        uri = Uri.parse("android.resource://com.android.driftineo.inventoryapp/" + R.drawable.nokia3310);
        getImage(uri);
        contentValues = new ContentValues();
        contentValues.put(ProductEntry.COLUMN_PRODUCT_NAME, "Nokia 3310");
        contentValues.put(ProductEntry.COLUMN_PRODUCT_PRICE, "160");
        contentValues.put(ProductEntry.COLUMN_PRODUCT_QUANTITY, "8");
        contentValues.put(ProductEntry.COLUMN_PRODUCT_IMAGE, photoByteArray);
        contentValues.put(ProductEntry.COLUMN_PRODUCT_EMAIL, "nokia@nokia.com");
        contentValues.put(ProductEntry.COLUMN_PRODUCT_PHONE, "4234234");
        newUri = getContentResolver().insert(ProductEntry.CONTENT_URI, contentValues);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        String[] projection = {
                ProductEntry._ID,
                ProductEntry.COLUMN_PRODUCT_NAME,
                ProductEntry.COLUMN_PRODUCT_QUANTITY,
                ProductEntry.COLUMN_PRODUCT_PRICE,
                ProductEntry.COLUMN_PRODUCT_IMAGE,
                ProductEntry.COLUMN_PRODUCT_PHONE,
                ProductEntry.COLUMN_PRODUCT_EMAIL};

        return new CursorLoader(this,
                ProductEntry.CONTENT_URI,
                projection,
                null,
                null,
                null);
    }


    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        productAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        productAdapter.swapCursor(null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_catalog.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_catalog, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        ImageView imageView = (ImageView) findViewById(R.id.firstImage);
        switch (item.getItemId()) {
            case R.id.action_insert_dummy_data:
                addNewProducts();
                break;
            case R.id.action_insert_data:
                imageView.setVisibility(View.GONE);
                Intent intent = new Intent(MainActivity.this, EditorActivity.class);
                startActivity(intent);
                break;
            case R.id.action_delete_all_entries:
                showDeleteConfirmationDialog();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    private void deleteAllProduct() {
        ImageView imageView = (ImageView) findViewById(R.id.firstImage);
        int rowsDeleted = getContentResolver().delete(ProductEntry.CONTENT_URI, null, null);
        if (rowsDeleted == 0) {
            // If no rows were deleted, then there was an error with the delete.
            Toast.makeText(this, getString(R.string.editor_delete_product_failed),
                    Toast.LENGTH_SHORT).show();
        } else {
            // Otherwise, the delete was successful and we can display a toast.
            Toast.makeText(this, getString(R.string.editor_delete_product_successful),
                    Toast.LENGTH_SHORT).show();
        }
        imageView.setVisibility(View.VISIBLE);
    }

    private void showDeleteConfirmationDialog() {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the postivie and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_dialog_msg);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                deleteAllProduct();

            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Cancel" button, so dismiss the dialog
                // and continue editing the pet.
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    public void getImage(Uri uri) {
        Bitmap bounds = photoUtils.getImage(uri);
        photoByteArray = photoUtils.getBytes(bounds);
    }


}