package com.mytechideas.inventoryapp;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mytechideas.inventoryapp.data.InventoryContract;

import java.io.ByteArrayOutputStream;

public class InsertActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int PRODUCTS_LOADER_ID=1;
    private boolean mProductHasChanged = false;
    private ImageView mPhotoButton;
    private ImageView mImageView;
    private Spinner mCurrencySpinner;
    private TextView mCurrencyForText;
    private TextView mNameProduct;
    private TextView mSupplierName;
    private TextView mPrice;
    private TextView mQty;
    private int mCurrency = 0;
    private Uri mProductUri;

    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            mProductHasChanged = true;
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert);
        Intent intent = getIntent();
        mProductUri= intent.getData();
        if (mProductUri == null) {
            // This is a new pet, so change the app bar to say "Add a Pet"
            setTitle(getString(R.string.editor_activity_title_new_product));

            // Invalidate the options menu, so the "Delete" menu option can be hidden.
            // (It doesn't make sense to delete a pet that hasn't been created yet.)
            invalidateOptionsMenu();
        } else {
            // Otherwise this is an existing pet, so change app bar to say "Edit Pet"
            setTitle(getString(R.string.editor_activity_title_edit_product));

            // Initialize a loader to read the pet data from the database
            // and display the current values in the editor

        }

        mCurrencySpinner = (Spinner) findViewById(R.id.spinner_currency);
        mNameProduct= (TextView)findViewById(R.id.edit_product_name);
        mSupplierName= (TextView)findViewById(R.id.edit_supplier);
        mPhotoButton= (ImageView) findViewById(R.id.image_button);
        mPrice=(TextView)findViewById(R.id.edit_product_price);
        mQty=(TextView)findViewById(R.id.edit_product_qty);
        mCurrencyForText=(TextView)findViewById(R.id.label_currency_units);
        mImageView=(ImageView)findViewById(R.id.product_cap);

        mPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
            }
        });

        mNameProduct.setOnTouchListener(mTouchListener);
        mSupplierName.setOnTouchListener(mTouchListener);
        mQty.setOnTouchListener(mTouchListener);
        mCurrencySpinner.setOnTouchListener(mTouchListener);
        mPrice.setOnTouchListener(mTouchListener);
        mPhotoButton.setOnTouchListener(mTouchListener);


        setupSpinner();
        getLoaderManager().initLoader(PRODUCTS_LOADER_ID, null, this);
    }
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        // If this is a new pet, hide the "Delete" menu item.
        if (mProductUri == null) {
            MenuItem menuItem = menu.findItem(R.id.action_delete);
            menuItem.setVisible(false);
        }
        return true;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_editor.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_insert, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Save" menu option
            case R.id.action_save:
                // Do nothing for now
                saveProduct();
                finish();
                return true;
            // Respond to a click on the "Delete" menu option
            case R.id.action_delete:

                // Pop up confirmation dialog for deletion
                showDeleteConfirmationDialog();
                return true;
            // Respond to a click on the "Up" arrow button in the app bar
            case android.R.id.home:
                // Navigate back to parent activity (CatalogActivity)
                if (!mProductHasChanged) {
                    NavUtils.navigateUpFromSameTask(InsertActivity.this);
                    return true;
                }
                DialogInterface.OnClickListener discardButtonClickListener =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // User clicked "Discard" button, navigate to parent activity.
                                NavUtils.navigateUpFromSameTask(InsertActivity.this);
                            }
                        };

                // Show a dialog that notifies the user they have unsaved changes
                showUnsavedChangesDialog(discardButtonClickListener);
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    private void setupSpinner() {
        // Create adapter for spinner. The list options are from the String array it will use
        // the spinner will use the default layout
        ArrayAdapter genderSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_gender_options, android.R.layout.simple_spinner_item);

        // Specify dropdown layout style - simple list view with 1 item per line
        genderSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        // Apply the adapter to the spinner
        mCurrencySpinner.setAdapter(genderSpinnerAdapter);

        // Set the integer mSelected to the constant values
        mCurrencySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals(getString(R.string.currency_colombia))) {
                        mCurrency = InventoryContract.InventoryEntry.CURRECNCY_COP; // COP
                        mCurrencyForText.setText(getResources().getString(R.string.currency_colombia));
                    } else if (selection.equals(getString(R.string.currency_usa))) {
                        mCurrency= InventoryContract.InventoryEntry.CURRENCY_USD; // USD
                        mCurrencyForText.setText(getResources().getString(R.string.currency_usa));
                    }
                }
            }

            // Because AdapterView is an abstract class, onNothingSelected must be defined
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mCurrency= InventoryContract.InventoryEntry.CURRECNCY_COP; // Unknown
            }
        });

    }
    private void saveProduct() {
        // Read from input fields
        // Use trim to eliminate leading or trailing white space
        String nameString =  mNameProduct.getText().toString().trim();
        String supplierString = mSupplierName.getText().toString().trim();
        String priceString = mPrice.getText().toString().trim();
        String qtyString = mQty.getText().toString().trim();
        Bitmap image= ((BitmapDrawable)mImageView.getDrawable()).getBitmap();

        // Check if this is supposed to be a new pet
        // and check if all the fields in the editor are blank
        if (mProductUri == null &&
                TextUtils.isEmpty(nameString) && TextUtils.isEmpty(supplierString)&& TextUtils.isEmpty(priceString) &&
                TextUtils.isEmpty(qtyString) && mCurrency == InventoryContract.InventoryEntry.CURRECNCY_COP) {
            // Since no fields were modified, we can return early without creating a new pet.
            // No need to create ContentValues and no need to do any ContentProvider operations.
            return;
        }

        // Create a ContentValues object where column names are the keys,
        // and pet attributes from the editor are the values.
        ContentValues values = new ContentValues();
        values.put(InventoryContract.InventoryEntry.NAME, nameString);
        values.put(InventoryContract.InventoryEntry.SUPPLIER, supplierString);
        values.put(InventoryContract.InventoryEntry.PRICE,priceString);

        values.put(InventoryContract.InventoryEntry.CURRENCY, mCurrency);
        //values.put(InventoryContract.InventoryEntry.PICTURE, "foto123.jpg");
        // If the weight is not provided by the user, don't try to parse the string into an
        // integer value. Use 0 by default.
        int qty = 0;
        if (!TextUtils.isEmpty(qtyString)) {
            qty = Integer.parseInt(qtyString);
        }
        values.put(InventoryContract.InventoryEntry.QTY, qtyString);

        if(image!=null){
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.PNG, 100, bos);
            byte[] bArray = bos.toByteArray();

            values.put(InventoryContract.InventoryEntry.PICTURE, bArray);
        }
        else{
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            mImageView.setImageResource(R.drawable.nophoto);
            image= ((BitmapDrawable)mImageView.getDrawable()).getBitmap();
            image.compress(Bitmap.CompressFormat.PNG, 100, bos);
            byte[] bArray = bos.toByteArray();
            values.put(InventoryContract.InventoryEntry.PICTURE, bArray);

        }


        // Determine if this is a new or existing pet by checking if mCurrentPetUri is null or not
        if (mProductUri == null) {
            // This is a NEW pet, so insert a new pet into the provider,
            // returning the content URI for the new pet.
            Uri newUri = getContentResolver().insert(InventoryContract.InventoryEntry.CONTENT_URI, values);

            // Show a toast message depending on whether or not the insertion was successful.
            if (newUri == null) {
                // If the new content URI is null, then there was an error with insertion.
                Toast.makeText(this, getString(R.string.editor_insert_product_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the insertion was successful and we can display a toast.
                Toast.makeText(this, getString(R.string.editor_insert_product_successful),
                        Toast.LENGTH_SHORT).show();
            }
        } else {
            // Otherwise this is an EXISTING pet, so update the pet with content URI: mCurrentPetUri
            // and pass in the new ContentValues. Pass in null for the selection and selection args
            // because mCurrentPetUri will already identify the correct row in the database that
            // we want to modify.
            int rowsAffected = getContentResolver().update(mProductUri, values, null, null);

            // Show a toast message depending on whether or not the update was successful.
            if (rowsAffected == 0) {
                // If no rows were affected, then there was an error with the update.
                Toast.makeText(this, getString(R.string.editor_update_product_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the update was successful and we can display a toast.
                Toast.makeText(this, getString(R.string.editor_update_product_successful),
                        Toast.LENGTH_SHORT).show();
            }
        }
    }
    @Override
    public void onBackPressed() {
        // If the pet hasn't changed, continue with handling back button press
        if (!mProductHasChanged) {
            super.onBackPressed();
            return;
        }

        // Otherwise if there are unsaved changes, setup a dialog to warn the user.
        // Create a click listener to handle the user confirming that changes should be discarded.
        DialogInterface.OnClickListener discardButtonClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // User clicked "Discard" button, close the current activity.
                        finish();
                    }
                };

        // Show dialog that there are unsaved changes
        showUnsavedChangesDialog(discardButtonClickListener);
    }
    private void showUnsavedChangesDialog(
            DialogInterface.OnClickListener discardButtonClickListener) {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the positive and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.unsaved_changes_dialog_msg);
        builder.setPositiveButton(R.string.discard, discardButtonClickListener);
        builder.setNegativeButton(R.string.keep_editing, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Keep editing" button, so dismiss the dialog
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

    private void showDeleteConfirmationDialog() {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the postivie and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_dialog_msg);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Delete" button, so delete the pet.
                deletePet();
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

    /**
     * Perform the deletion of the pet in the database.
     */
    private void deletePet() {
        if (mProductUri != null) {
            // Call the ContentResolver to delete the pet at the given content URI.
            // Pass in null for the selection and selection args because the mCurrentPetUri
            // content URI already identifies the pet that we want.
            int rowsDeleted = getContentResolver().delete(mProductUri, null, null);

            if (rowsDeleted == 0) {
                // If no rows were deleted, then there was an error with the delete.
                Toast.makeText(this, getString(R.string.editor_delete_product_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the delete was successful and we can display a toast.
                Toast.makeText(this, getString(R.string.editor_delete_product_successful),
                        Toast.LENGTH_SHORT).show();
            }

            finish();
        }

    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {

        if (mProductUri != null) {
            String[] projection = {
                    InventoryContract.InventoryEntry._ID,
                    InventoryContract.InventoryEntry.NAME,
                    InventoryContract.InventoryEntry.PRICE,
                    InventoryContract.InventoryEntry.QTY,
                    InventoryContract.InventoryEntry.SUPPLIER,
                    InventoryContract.InventoryEntry.PICTURE,
                    InventoryContract.InventoryEntry.CURRENCY};

            return new CursorLoader(this,   // Parent activity context
                    mProductUri,         // Query the content URI for the current pet
                    projection,             // Columns to include in the resulting Cursor
                    null,                   // No selection clause
                    null,                   // No selection arguments
                    null);
        }
        else {
            return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor == null || cursor.getCount() < 1) {
            return;
        }
        if (cursor.moveToFirst()) {
            int nameColumnIndex = cursor.getColumnIndex(InventoryContract.InventoryEntry.NAME);
            int priceColumnIndex = cursor.getColumnIndex(InventoryContract.InventoryEntry.PRICE);
            int qtyColumnIndex = cursor.getColumnIndex(InventoryContract.InventoryEntry.QTY);
            int supplierColumnIndex = cursor.getColumnIndex(InventoryContract.InventoryEntry.SUPPLIER);
            int pictureColumnIndex = cursor.getColumnIndex(InventoryContract.InventoryEntry.PICTURE);
            int currencyColumnIndex = cursor.getColumnIndex( InventoryContract.InventoryEntry.CURRENCY);

            // Extract out the value from the Cursor for the given column index
            String name = cursor.getString(nameColumnIndex);
            String price = cursor.getString(priceColumnIndex);
            int qty = cursor.getInt(qtyColumnIndex);
            String supplier = cursor.getString(supplierColumnIndex);
            int currency = cursor.getInt(currencyColumnIndex);
            byte[] picture = cursor.getBlob(pictureColumnIndex);
            Bitmap bm = BitmapFactory.decodeByteArray(picture, 0 ,picture.length);
            // Update the views on the screen with the values from the database
            mNameProduct.setText(name);
            mSupplierName.setText(supplier);
            mQty.setText(Integer.toString(qty));
            mPrice.setText(price);
            mImageView.setImageBitmap(bm);
            ///mPhotoButton set immage


            switch (currency) {
                case InventoryContract.InventoryEntry.CURRECNCY_COP:
                    mCurrencySpinner.setSelection(0);
                    break;
                case InventoryContract.InventoryEntry.CURRENCY_USD:
                    mCurrencySpinner.setSelection(1);
                    break;

            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            mImageView.setImageBitmap(imageBitmap);

        }
    }
}
