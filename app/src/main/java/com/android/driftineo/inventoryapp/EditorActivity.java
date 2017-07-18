package com.android.driftineo.inventoryapp;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;

/**
 * Created by driftineo on 15/7/17.
 */

public class EditorActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {


    private static final int ACTIVITY_SELECT_IMAGE = 1020;
    private static final int ACTIVITY_SELECT_FROM_CAMERA = 1040;
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;
    private Uri currentProductUri;
    private AlertDialog _photoDialog;
    private EditText nameEditText;
    private EditText quantityEditText;
    private EditText priceEditText;
    private EditText emailEditText;
    private EditText phoneEditText;
    private ImageView productImage;
    private Uri aux;
    private PhotoUtils photoUtils;
    private static String blankSpace = "";
    private String title;
    final private int LESS = -1;
    final private int ADD = 1;


    protected void onCreate(Bundle bundle) {

        super.onCreate(bundle);
        Intent intent = getIntent();
        setContentView(R.layout.activity_editor);
        photoUtils = new PhotoUtils(this);

        final ContentValues contentValues = new ContentValues();
        productImage = (ImageView) findViewById(R.id.productImage);
        final Button addButon = (Button) findViewById(R.id.addButtonID);
        final Button lessButton = (Button) findViewById(R.id.lessButtonId);
        final Button phoneButton = (Button) findViewById(R.id.callButonID);
        final Button photoBUtton = (Button) findViewById(R.id.photoButton);

        currentProductUri = intent.getData();

        nameEditText = (EditText) findViewById(R.id.productNameEdit);
        quantityEditText = (EditText) findViewById(R.id.productQuantityEdit);
        priceEditText = (EditText) findViewById(R.id.productPriceEdit);
        phoneEditText = (EditText) findViewById(R.id.productPhoneEditText);
        emailEditText = (EditText) findViewById(R.id.productEmailEdit);
        productImage = (ImageView) findViewById(R.id.productImage);

        photoBUtton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!getPhotoDialog().isShowing() && !isFinishing())
                    getPhotoDialog().show();

            }
        });


        /**
         * Function to make the call Intent
         */
        phoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Cursor cursor = getContentResolver().query(currentProductUri, null, null, null, null);
                if (cursor != null && cursor.moveToFirst()) {

                    String phoneNumber = cursor.getString(cursor.getColumnIndexOrThrow(ProductContract.ProductEntry.COLUMN_PRODUCT_PHONE));
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phoneNumber, null));
                    startActivity(intent);
                }
                cursor.close();
            }
        });


        /**
         * Function to remove a single product inside the editor view
         */
        lessButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor cursor = getContentResolver().query(currentProductUri, null, null, null, null);
                modifyQuantity(cursor, contentValues, LESS);
            }
        });
/**
 *  Function to add a single product inside the editor view
 */
        addButon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor cursor = getContentResolver().query(currentProductUri, null, null, null, null);
                modifyQuantity(cursor, contentValues, ADD);
            }
        });


        if (currentProductUri == null) {
            setTitle(getString(R.string.activity_add_product));
            title = getString(R.string.activity_add_product);
            lessButton.setVisibility(View.GONE);
            addButon.setVisibility(View.GONE);
        } else {
            setTitle(getString(R.string.activity_editor));
            title = getString(R.string.activity_editor);

            Cursor cursor = getContentResolver().query(currentProductUri, null, null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                String name = cursor.getString(cursor.getColumnIndexOrThrow(ProductContract.ProductEntry.COLUMN_PRODUCT_NAME));
                String quantity = cursor.getString(cursor.getColumnIndexOrThrow(ProductContract.ProductEntry.COLUMN_PRODUCT_QUANTITY));
                String price = cursor.getString(cursor.getColumnIndexOrThrow(ProductContract.ProductEntry.COLUMN_PRODUCT_PRICE));
                String phone = cursor.getString(cursor.getColumnIndexOrThrow(ProductContract.ProductEntry.COLUMN_PRODUCT_PHONE));
                String email = cursor.getString(cursor.getColumnIndexOrThrow(ProductContract.ProductEntry.COLUMN_PRODUCT_EMAIL));
                String image = cursor.getString(cursor.getColumnIndexOrThrow(ProductContract.ProductEntry.COLUMN_PRODUCT_IMAGE));
                Uri uri = Uri.parse(image);
                nameEditText.setText(name);
                quantityEditText.setText(quantity);
                priceEditText.setText(price);
                phoneEditText.setText(phone);
                emailEditText.setText(email);
                productImage.setImageURI(uri);
            }
            cursor.close();
        }
    }


    private boolean insertProduct() {
        Cursor cursor = getContentResolver().query(currentProductUri, null, null, null, null);
        boolean bol = false;
        String nameString;
        String quantityString;
        String priceString;
        String phoneString;
        String emailString;
        String image;
        ContentValues contentValues = new ContentValues();
        int count = 0;

        if (cursor != null && cursor.moveToFirst()) {

            nameString = cursor.getString(cursor.getColumnIndexOrThrow(ProductContract.ProductEntry.COLUMN_PRODUCT_NAME));
            quantityString = cursor.getString(cursor.getColumnIndexOrThrow(ProductContract.ProductEntry.COLUMN_PRODUCT_QUANTITY));
            priceString = cursor.getString(cursor.getColumnIndexOrThrow(ProductContract.ProductEntry.COLUMN_PRODUCT_PRICE));
            phoneString = cursor.getString(cursor.getColumnIndexOrThrow(ProductContract.ProductEntry.COLUMN_PRODUCT_PHONE));
            emailString = cursor.getString(cursor.getColumnIndexOrThrow(ProductContract.ProductEntry.COLUMN_PRODUCT_EMAIL));
            image = cursor.getString(cursor.getColumnIndexOrThrow(ProductContract.ProductEntry.COLUMN_PRODUCT_IMAGE));
            Uri uri = Uri.parse(image);


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

            if (!phoneString.equals(blankSpace)) {
                contentValues.put(ProductContract.ProductEntry.COLUMN_PRODUCT_PHONE, phoneString);
            } else {
                count++;
            }

            if (!emailString.equals(blankSpace)) {
                contentValues.put(ProductContract.ProductEntry.COLUMN_PRODUCT_EMAIL, emailString);
            } else {
                count++;
            }

            if (!image.equals(blankSpace)) {
                contentValues.put(ProductContract.ProductEntry.COLUMN_PRODUCT_IMAGE, image.toString());
            } else {
                count++;
            }
            if (count != 0) {
                Toast.makeText(this, getString(R.string.action_insert_data_more_than_one_element_empty), Toast.LENGTH_LONG).show();
            }
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
                ProductContract.ProductEntry.COLUMN_PRODUCT_PRICE,
                ProductContract.ProductEntry.COLUMN_PRODUCT_IMAGE,
                ProductContract.ProductEntry.COLUMN_PRODUCT_PHONE,
                ProductContract.ProductEntry.COLUMN_PRODUCT_EMAIL,};

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
            int phoneColumnIndex = cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRODUCT_PHONE);
            int emailColumnIndex = cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRODUCT_EMAIL);
            int imageColumnIndex = cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRODUCT_IMAGE);

            String name = cursor.getString(nameColumnIndex);
            String quantity = cursor.getString(quantityColumnIndex);
            String price = cursor.getString(priceColumnIndex);
            String phone = cursor.getString(phoneColumnIndex);
            String email = cursor.getString(emailColumnIndex);
            String image = cursor.getString(imageColumnIndex);

            nameEditText.setText(name);
            quantityEditText.setText(quantity);
            priceEditText.setText(price);
            phoneEditText.setText(phone);
            emailEditText.setText(email);
            productImage.setImageURI(Uri.parse(image));
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {


        nameEditText.setText(blankSpace);
        quantityEditText.setText(blankSpace);
        priceEditText.setText(blankSpace);
        phoneEditText.setText(blankSpace);
        emailEditText.setText(blankSpace);
        productImage.setImageBitmap(null);
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

                deleteProduct();

            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });


        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if (currentProductUri == null) {
            MenuItem menuItem = menu.findItem(R.id.action_delete);
            menuItem.setVisible(false);
        }
        return true;
    }

    public void modifyQuantity(Cursor cursor, ContentValues contentValues, int operation) {


        if (cursor != null && cursor.moveToFirst()) {

            String quantity = cursor.getString(cursor.getColumnIndexOrThrow(ProductContract.ProductEntry.COLUMN_PRODUCT_QUANTITY));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(ProductContract.ProductEntry.COLUMN_PRODUCT_NAME));
            String price = cursor.getString(cursor.getColumnIndexOrThrow(ProductContract.ProductEntry.COLUMN_PRODUCT_PRICE));
            String image = cursor.getString(cursor.getColumnIndexOrThrow(ProductContract.ProductEntry.COLUMN_PRODUCT_IMAGE));
            String phone = cursor.getString(cursor.getColumnIndexOrThrow(ProductContract.ProductEntry.COLUMN_PRODUCT_PHONE));
            String email = cursor.getString(cursor.getColumnIndexOrThrow(ProductContract.ProductEntry.COLUMN_PRODUCT_EMAIL));
            String id = cursor.getString(cursor.getColumnIndexOrThrow(ProductContract.ProductEntry._ID));

            int quantityValor = Integer.valueOf(quantity);
            quantityValor = quantityValor + operation;

            if (quantityValor > 0) {

                String quantityString = Integer.valueOf(quantityValor).toString();

                contentValues.put(ProductContract.ProductEntry.COLUMN_PRODUCT_NAME, name);
                contentValues.put(ProductContract.ProductEntry.COLUMN_PRODUCT_QUANTITY, quantityValor);
                contentValues.put(ProductContract.ProductEntry.COLUMN_PRODUCT_PRICE, price);
                contentValues.put(ProductContract.ProductEntry.COLUMN_PRODUCT_PHONE, phone);
                contentValues.put(ProductContract.ProductEntry.COLUMN_PRODUCT_IMAGE, image);
                contentValues.put(ProductContract.ProductEntry.COLUMN_PRODUCT_EMAIL, email);

                String selection = ProductContract.ProductEntry._ID + "=?";

                String[] selectionArgs = new String[]{String.valueOf(id)};


                int rowsUpdated = getContentResolver().update(currentProductUri, contentValues, selection, selectionArgs);
                quantityEditText.setText(quantityString);
            }


            cursor.close();
        }
    }

    private AlertDialog getPhotoDialog() {

        if (_photoDialog == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.photo_source);
            builder.setPositiveButton(R.string.camera, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                    File photo = null;
                    try {
                        // place where to store camera taken picture
                        photo = PhotoUtils.createTemporaryFile("picture", ".jpg", EditorActivity.this);
                        photo.delete();
                    } catch (Exception e) {
                        Log.v(getClass().getSimpleName(), "Can't create file to take picture!");
                    }
                    currentProductUri = Uri.fromFile(photo);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, currentProductUri);
                    startActivityForResult(intent, ACTIVITY_SELECT_FROM_CAMERA);
                }

            });
            builder.setNegativeButton(R.string.gallery, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);

                    galleryIntent.setType("image/*");
                    File photo = null;
                    try {
                        photo = PhotoUtils.createTemporaryFile("picture", ".jpg", EditorActivity.this);


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    currentProductUri = Uri.fromFile(photo);
                    startActivityForResult(galleryIntent, ACTIVITY_SELECT_IMAGE);
                }

            });
            _photoDialog = builder.create();

        }
        return _photoDialog;

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ACTIVITY_SELECT_IMAGE && resultCode == RESULT_OK) {
            currentProductUri = data.getData();
            getImage(currentProductUri);

        } else if (requestCode == ACTIVITY_SELECT_FROM_CAMERA
                && resultCode == RESULT_OK) {
            getImage(currentProductUri);
        }
    }

    public void getImage(Uri uri) {
        Bitmap bounds = photoUtils.getImage(uri);
        ContentValues contentValues = new ContentValues();
        contentValues.put(ProductContract.ProductEntry.COLUMN_PRODUCT_IMAGE, uri.toString());
        getContentResolver().update(currentProductUri, contentValues, null, null);
        if (bounds != null) {
            setImage(bounds);
        }
    }

    public void setImage(Bitmap image) {
        productImage.setImageBitmap(image);
    }
}


