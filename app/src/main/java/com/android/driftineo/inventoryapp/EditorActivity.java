package com.android.driftineo.inventoryapp;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by driftineo on 15/7/17.
 */

public class EditorActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {


    private static final int EXISTING_PRODUCT_LOADER = 0;
    private Uri currentProductUri;
    private EditText nameEditText;
    private EditText quantityText;
    private EditText priceText;
    private static String blankSpace = "";

    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_editor);
        Intent intent = getIntent();
        currentProductUri = intent.getData();

        if (currentProductUri == null) {
            setTitle(getString(R.string.editor_activity));
        }
        nameEditText = (EditText) findViewById(R.id.productNameEdit);
        quantityText = (EditText) findViewById(R.id.productQuantityEdit);
        priceText = (EditText) findViewById(R.id.productPriceEdit);
    }

    private boolean insertProduct() {


        boolean bol = true;

        int count = 0;
        String nameString = nameEditText.getText().toString().trim();
        String quantityString = quantityText.getText().toString().trim();
        String priceString = priceText.getText().toString().trim();
        ContentValues contentValues = new ContentValues();

        if (!nameString.equals(blankSpace)) {
            contentValues.put(ProductContract.ProductEntry.COLUMN_PRODUCT_NAME, nameString);
        } else {
            count++;
        }

        if (!quantityString.equals(blankSpace)) {
            contentValues.put(ProductContract.ProductEntry.COLUMN_PRODUCT_QUANTITY, quantityString);
        } else {
            count++;
        }

        if (!priceString.equals(blankSpace)) {
            contentValues.put(ProductContract.ProductEntry.COLUMN_PRODUCT_PRICE, priceString);
        } else {
            count++;
        }
        if (count != 0) {
            bol = true;
            if (count == 1) {
                Toast.makeText(this, getString(R.string.action_insert_data_one_element_empty), Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, getString(R.string.action_insert_data_more_than_one_element_empty), Toast.LENGTH_LONG).show();
            }

        } else {
            Uri uri = getContentResolver().insert(ProductContract.ProductEntry.CONTENT_URI, contentValues);

            if (uri == null) {
                bol = true;
                Toast.makeText(this, getString(R.string.action_insert_data_failed), Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, getString(R.string.action_insert_data_sucess), Toast.LENGTH_LONG).show();
                bol = false;
            }

        }
        return bol;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_editor.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                if (!insertProduct()) {
                    finish();
                    return true;
                } else {
                    break;
                }

            case R.id.action_cancel:
                finish();
                return true;
            case android.R.id.home:

                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        String[] projection = {
                ProductContract.ProductEntry._ID,
                ProductContract.ProductEntry.COLUMN_PRODUCT_NAME,
                ProductContract.ProductEntry.COLUMN_PRODUCT_QUANTITY,
                ProductContract.ProductEntry.COLUMN_PRODUCT_PRICE};

        return new CursorLoader(this,
                ProductContract.ProductEntry.CONTENT_URI,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor == null || cursor.getCount() < 1) {
            return;
        }
        if (cursor.moveToFirst()) {
            int nameColumnIndex = cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRODUCT_NAME);
            int quantityColumnIndex = cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRODUCT_QUANTITY);
            int priceColumnIndex = cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRODUCT_PRICE);

            String name = cursor.getString(nameColumnIndex);
            String quantity = cursor.getString(quantityColumnIndex);
            String price = cursor.getString(priceColumnIndex);

            nameEditText.setText(name);
            quantityText.setText(quantity);
            priceText.setText(price);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {


        nameEditText.setText(blankSpace);
        quantityText.setText(blankSpace);
        priceText.setText(blankSpace);
    }
}
