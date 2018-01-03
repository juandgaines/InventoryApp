package com.mytechideas.inventoryapp;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.mytechideas.inventoryapp.data.InventoryContract;

public class InventoryApp extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{
    public static final String LOG_TAG = InventoryApp.class.getSimpleName();
    private ProductCursorAdapter mProductCursorAdapter;


    private static final int PRODUCTS_LOADER=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inventory_app);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(InventoryApp.this, InsertActivity.class);
                startActivity(intent);
            }
        });


        ListView productsListView= (ListView) findViewById(R.id.list);

        View emptyView=findViewById(R.id.empty_view);
        productsListView.setEmptyView(emptyView);

        mProductCursorAdapter= new ProductCursorAdapter(this,null);
        productsListView.setAdapter(mProductCursorAdapter);

        productsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {


                Toast.makeText(getApplicationContext(), "Funciona!!", Toast.LENGTH_SHORT).show();
                Intent intent   =new Intent(InventoryApp.this,InsertActivity.class);

                Uri uri= ContentUris.withAppendedId(InventoryContract.InventoryEntry.CONTENT_URI,id);
                intent.setData(uri);

                startActivity(intent);
            }
        });
        getLoaderManager().initLoader(PRODUCTS_LOADER, null, this);


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_catalog.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_inventory, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Insert dummy data" menu option
            case R.id.action_insert_dummy_data:
                insertProduct();



                return true;
            // Respond to a click on the "Delete all entries" menu option
            case R.id.action_delete_all_entries:
                // Do nothing for now
                deleteAllProducts();

                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void insertProduct() {



        ContentValues dummyProductToInsert = new ContentValues();

        dummyProductToInsert.put(InventoryContract.InventoryEntry.PRICE, "7");
        dummyProductToInsert.put(InventoryContract.InventoryEntry.NAME, "Cosito");
        dummyProductToInsert.put(InventoryContract.InventoryEntry.PICTURE, "image.jpg");
        dummyProductToInsert.put(InventoryContract.InventoryEntry.QTY, 3);

        dummyProductToInsert.put(InventoryContract.InventoryEntry.SUPPLIER, "EL PESCADOR");
        dummyProductToInsert.put(InventoryContract.InventoryEntry.CURRENCY, InventoryContract.InventoryEntry.CURRECNCY_COP);


        Uri newUri = getContentResolver().insert(InventoryContract.InventoryEntry.CONTENT_URI, dummyProductToInsert);
        Log.v(LOG_TAG, "Uri received:"+newUri.toString());
        int StateOfInsert =Integer.parseInt(String.valueOf(ContentUris.parseId(newUri)));

        if (StateOfInsert < 0) {
            Toast.makeText(this, "Data Base Insert Error", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Data inserted status ok. Item: #" + StateOfInsert, Toast.LENGTH_SHORT).show();
        }

    }

    private void deleteAllProducts() {

        int StateOfDelete =getContentResolver().delete(InventoryContract.InventoryEntry.CONTENT_URI,null,null);

        if (StateOfDelete == 0) {
            Toast.makeText(this, "Data Base is empty already", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Items deleted:" +StateOfDelete, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String[] projection= {
                InventoryContract.InventoryEntry._ID,
                InventoryContract.InventoryEntry.NAME,
                InventoryContract.InventoryEntry.QTY,
                InventoryContract.InventoryEntry.PRICE,
                InventoryContract.InventoryEntry.CURRENCY
        };
        return new CursorLoader(this,
                InventoryContract.InventoryEntry.CONTENT_URI,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        mProductCursorAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mProductCursorAdapter.swapCursor(null);
    }
}
