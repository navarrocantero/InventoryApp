package com.android.driftineo.inventoryapp;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.android.driftineo.inventoryapp.ProductContract.ProductEntry;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        displayDatabaseInfo();
        addNewProducts();

    }

    private void displayDatabaseInfo() {

        String[] projection = {
                ProductEntry._ID,
                ProductEntry.COLUMN_PRODUCT_NAME,
                ProductEntry.COLUMN_PRODUCT_QUANTITY,
                ProductEntry.COLUMN_PRODUCT_PRICE};

        Cursor cursor = getContentResolver().query(
                ProductEntry.CONTENT_URI,
                projection, null, null, null);

        TextView displayView = (TextView) findViewById(R.id.productText);

        try {
            displayView.setText("The product table contains " + cursor.getCount()
                    + " products. \n\n");
            displayView.append(ProductEntry.COLUMN_PRODUCT_NAME + " -" +
                    ProductEntry.COLUMN_PRODUCT_QUANTITY + " - " +
                    ProductEntry.COLUMN_PRODUCT_PRICE + "\n");


            int indexName = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_NAME);
            int indexQuantity = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_QUANTITY);
            int indexPrice = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_PRICE);

            while (cursor.moveToNext()) {

                String currentName = cursor.getString(indexName);
                String currentQuantity = cursor.getString(indexQuantity);
                String currentPrice = cursor.getString(indexPrice);
                displayView.append(("\n" + currentName + " - " + currentQuantity + " - "
                        + currentQuantity));
            }
        } finally {
            cursor.close();
        }
    }

    private void addNewProducts() {

        ContentValues contentValues = new ContentValues();
        contentValues.put(ProductEntry.COLUMN_PRODUCT_NAME, "cola");
        contentValues.put(ProductEntry.COLUMN_PRODUCT_PRICE, "20");
        contentValues.put(ProductEntry.COLUMN_PRODUCT_QUANTITY, "20");
        for (int i = 0; i <100 ; i++) {

            Uri newUri = getContentResolver().insert(ProductEntry.CONTENT_URI, contentValues);
        }

    }

}

