package com.android.driftineo.inventoryapp;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

/**
 * Created by driftineo on 15/7/17.
 */

public class ProductAdapter extends CursorAdapter{
    public ProductAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {


        TextView  productNameTextView = (TextView) view.findViewById(R.id.productName);
        TextView  productQuantityTextView = (TextView) view.findViewById(R.id.productQuantity);
        TextView  productPriceTextView = (TextView) view.findViewById(R.id.productPrice);

        String  productName = cursor.getString(cursor.getColumnIndexOrThrow(ProductContract.ProductEntry.COLUMN_PRODUCT_NAME));
        String  productQuantity = cursor.getString(cursor.getColumnIndexOrThrow(ProductContract.ProductEntry.COLUMN_PRODUCT_QUANTITY));
        String  price = cursor.getString(cursor.getColumnIndexOrThrow(ProductContract.ProductEntry.COLUMN_PRODUCT_PRICE));

        productNameTextView.setText(productName);
        productQuantityTextView.setText(productQuantity);
        productPriceTextView.setText(price);



    }
}
