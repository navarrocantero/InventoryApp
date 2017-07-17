package com.android.driftineo.inventoryapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;

/**
 * Created by driftineo on 15/7/17.
 */

public class ProductAdapter extends CursorAdapter {


    public ProductAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    @Override
    public void bindView(View view, final Context context, final Cursor cursor) {

        final Button sellButton = (Button) view.findViewById(R.id.sellButton);
        final TextView productNameTextView = (TextView) view.findViewById(R.id.productName);
        final TextView productQuantityTextView = (TextView) view.findViewById(R.id.productQuantity);
        final TextView productPriceTextView = (TextView) view.findViewById(R.id.productPrice);

        final ContentValues contenvalues = new ContentValues();
        final String productName = cursor.getString(cursor.getColumnIndexOrThrow(ProductContract.ProductEntry.COLUMN_PRODUCT_NAME));
        final String productQuantity = cursor.getString(cursor.getColumnIndexOrThrow(ProductContract.ProductEntry.COLUMN_PRODUCT_QUANTITY));
        final String price = cursor.getString(cursor.getColumnIndexOrThrow(ProductContract.ProductEntry.COLUMN_PRODUCT_PRICE));
        final String id = cursor.getString(cursor.getColumnIndexOrThrow(ProductContract.ProductEntry._ID));

//        ImageView imageView = (ImageView) view.findViewById(R.id.firstImage);
//        if(!imageView.equals(null)){
//
//            imageView.setVisibility(View.GONE);
//        }
        productNameTextView.setText(productName);
        productQuantityTextView.setText(productQuantity);
        productPriceTextView.setText(price);


        sellButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int quantity = Integer.valueOf(productQuantity);
                if (quantity > 0) {
                    quantity--;

                    contenvalues.put(ProductContract.ProductEntry.COLUMN_PRODUCT_NAME, productName);
                    contenvalues.put(ProductContract.ProductEntry.COLUMN_PRODUCT_QUANTITY, quantity);
                    contenvalues.put(ProductContract.ProductEntry.COLUMN_PRODUCT_PRICE, price);
                    String selection = ProductContract.ProductEntry._ID + "=?";

                    String[] selectionArgs = new String[]{String.valueOf(id)};
                    int rowsUpdated = context.getContentResolver().update(ProductContract.ProductEntry.CONTENT_URI,
                            contenvalues, selection, selectionArgs);

                }

            }
        });
    }
}
