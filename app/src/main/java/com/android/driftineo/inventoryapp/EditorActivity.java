package com.android.driftineo.inventoryapp;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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
    private String title;

    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_editor);
        Intent intent = getIntent();

        final ContentValues contentValues = new ContentValues();
        Button addButon = (Button) findViewById(R.id.addButtonID);
        Button lessButton = (Button) findViewById(R.id.lessButtonId);


        /**
         * Function to remove a single product inside the editor view
         */
        lessButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Cursor cursor = getContentResolver().query(currentProductUri, null, null, null, null);

                if (cursor != null && cursor.moveToFirst()) {

                    String name = cursor.getString(cursor.getColumnIndexOrThrow(ProductContract.ProductEntry.COLUMN_PRODUCT_NAME));
                    String price = cursor.getString(cursor.getColumnIndexOrThrow(ProductContract.ProductEntry.COLUMN_PRODUCT_PRICE));
                    String id = cursor.getString(cursor.getColumnIndexOrThrow(ProductContract.ProductEntry._ID));

                    int quantityValor = Integer.valueOf(String.valueOf(quantityText.getText()));
                    quantityValor--;
                    if (quantityValor > 0) {

                        String quantityString = Integer.valueOf(quantityValor).toString();
                        contentValues.put(ProductContract.ProductEntry.COLUMN_PRODUCT_NAME, name);
                        contentValues.put(ProductContract.ProductEntry.COLUMN_PRODUCT_QUANTITY, quantityString);
                        contentValues.put(ProductContract.ProductEntry.COLUMN_PRODUCT_PRICE, price);
                        String selection = ProductContract.ProductEntry._ID + "=?";

                        String[] selectionArgs = new String[]{String.valueOf(id)};
                        int rowsUpdated = getContentResolver().update(currentProductUri, contentValues, selection, selectionArgs);
                        quantityText.setText(quantityString);
                    }
                }
                cursor.close();
            }
        });
/**
 *  Function to add a single product inside the editor view
 */
        addButon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Cursor cursor = getContentResolver().query(currentProductUri, null, null, null, null);

                if (cursor != null && cursor.moveToFirst()) {

                    String name = cursor.getString(cursor.getColumnIndexOrThrow(ProductContract.ProductEntry.COLUMN_PRODUCT_NAME));
                    String price = cursor.getString(cursor.getColumnIndexOrThrow(ProductContract.ProductEntry.COLUMN_PRODUCT_PRICE));
                    String id = cursor.getString(cursor.getColumnIndexOrThrow(ProductContract.ProductEntry._ID));

                    int quantityValor = Integer.valueOf(String.valueOf(quantityText.getText()));
                    quantityValor++;
                    if (quantityValor > 0) {

                        String quantityString = Integer.valueOf(quantityValor).toString();
                        contentValues.put(ProductContract.ProductEntry.COLUMN_PRODUCT_NAME, name);
                        contentValues.put(ProductContract.ProductEntry.COLUMN_PRODUCT_QUANTITY, quantityString);
                        contentValues.put(ProductContract.ProductEntry.COLUMN_PRODUCT_PRICE, price);
                        String selection = ProductContract.ProductEntry._ID + "=?";

                        String[] selectionArgs = new String[]{String.valueOf(id)};
                        int rowsUpdated = getContentResolver().update(currentProductUri, contentValues, selection, selectionArgs);
                        quantityText.setText(quantityString);
                    }
                }
                cursor.close();
            }
        });

        currentProductUri = intent.getData();
        nameEditText = (EditText) findViewById(R.id.productNameEdit);

        quantityText = (EditText) findViewById(R.id.productQuantityEdit);

        priceText = (EditText) findViewById(R.id.productPriceEdit);

        if (currentProductUri == null) {
            setTitle(getString(R.string.activity_add_product));
            title = getString(R.string.activity_add_product);
        } else {
            setTitle(getString(R.string.activity_editor));
            title = getString(R.string.activity_editor);

            Cursor cursor = getContentResolver().query(currentProductUri, null, null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                String name = cursor.getString(cursor.getColumnIndexOrThrow(ProductContract.ProductEntry.COLUMN_PRODUCT_NAME));
                String quantity = cursor.getString(cursor.getColumnIndexOrThrow(ProductContract.ProductEntry.COLUMN_PRODUCT_QUANTITY));
                String price = cursor.getString(cursor.getColumnIndexOrThrow(ProductContract.ProductEntry.COLUMN_PRODUCT_PRICE));
                nameEditText.setText(name);
                quantityText.setText(quantity);
                priceText.setText(price);
            }
            cursor.close();
        }
    }


    private boolean insertProduct() {

        boolean bol = true;
        String nameString = nameEditText.getText().toString().trim();
        String quantityString = quantityText.getText().toString().trim();
        String priceString = priceText.getText().toString().trim();
        ContentValues contentValues = new ContentValues();
        int count = 0;

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

        // Add a product especific code part

        if (title.equals(getString(R.string.activity_add_product))) {
            if (count != 0) {
                Toast.makeText(this, getString(R.string.action_insert_data_more_than_one_element_empty), Toast.LENGTH_LONG).show();
                return true;

            } else if (count == 0) {

                Uri uri = getContentResolver().insert(ProductContract.ProductEntry.CONTENT_URI, contentValues);
                Toast.makeText(this, getString(R.string.action_insert_data_sucess), Toast.LENGTH_LONG).show();
            }
            bol = false;
        }
        // Edit a product especific code part

        else {

            if (count == 0) {
                int rowsAffected = getContentResolver().update(currentProductUri, contentValues, null, null);
                if (rowsAffected == 1) {
                    Toast.makeText(this, getString(R.string.editor_update_product_successful),
                            Toast.LENGTH_SHORT).show();
                    return false;
                }
            } else {


                Toast.makeText(this, getString(R.string.editor_update_product_failed),
                        Toast.LENGTH_SHORT).show();
                Toast.makeText(this, getString(R.string.action_insert_data_more_than_one_element_empty), Toast.LENGTH_LONG).show();
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
                if (title.equals(getString(R.string.activity_editor))) {
                    Toast.makeText(this, getString(R.string.action_update_data_cancel), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, getString(R.string.action_insert_data_cancel), Toast.LENGTH_LONG).show();
                }
                finish();
                return true;
            case R.id.action_delete:
                showDeleteConfirmationDialog();
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

    /**
     * Perform the deletion of the pet in the database.
     */
    private void deleteProduct() {
        if (currentProductUri != null) {
            int rowsDeleted = getContentResolver().delete(currentProductUri, null, null);
            if (rowsDeleted == 0) {
                // If no rows were deleted, then there was an error with the delete.
                Toast.makeText(this, getString(R.string.editor_delete_product_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the delete was successful and we can display a toast.
                Toast.makeText(this, getString(R.string.editor_delete_product_successful),
                        Toast.LENGTH_SHORT).show();
            }
        }
        finish();
    }

    private void showUnsavedChangesDialog(DialogInterface.OnClickListener discardButtonClickListener) {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the postivie and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.unsaved_changes_dialog_msg);
        builder.setPositiveButton(R.string.discard, discardButtonClickListener);
        builder.setNegativeButton(R.string.keep_editing, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    /**
     * Prompt the user to confirm that they want to delete this pet.
     */
    private void showDeleteConfirmationDialog() {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the postivie and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_dialog_msg);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Delete" button, so delete the pet.
                deleteProduct();

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

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        // If this is a new pet, hide the "Delete" menu item.
        if (currentProductUri == null) {
            MenuItem menuItem = menu.findItem(R.id.action_delete);
            menuItem.setVisible(false);
        }
        return true;
    }

}
