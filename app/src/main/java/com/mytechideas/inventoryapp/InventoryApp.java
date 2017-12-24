package com.mytechideas.inventoryapp;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.mytechideas.inventoryapp.data.InventoryContract;
import com.mytechideas.inventoryapp.data.InventoryDbHelper;

public class InventoryApp extends AppCompatActivity {
    public static final String LOG_TAG = InventoryApp.class.getSimpleName();
    private ProductCursorAdapter mProductCursorAdapter;
    private InventoryDbHelper mInventoryDBHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inventory_app);
        mInventoryDBHelper=new InventoryDbHelper(this);



        SQLiteDatabase db =mInventoryDBHelper.getReadableDatabase();

        Cursor todoCursor= db.rawQuery("SELECT * FROM  "+ InventoryContract.InventoryEntry.TABLE_NAME,null);



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

        mProductCursorAdapter= new ProductCursorAdapter(this,todoCursor);
        productsListView.setAdapter(mProductCursorAdapter);


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
                insertPet();
                mProductCursorAdapter.notifyDataSetChanged();


                return true;
            // Respond to a click on the "Delete all entries" menu option
            case R.id.action_delete_all_entries:
                // Do nothing for now
                deletePets();
                mProductCursorAdapter.notifyDataSetChanged();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mInventoryDBHelper.close();
    }

    private void insertPet() {

        SQLiteDatabase database = mInventoryDBHelper.getWritableDatabase();

        ContentValues dummyProductToInsert = new ContentValues();

        dummyProductToInsert.put(InventoryContract.InventoryEntry.NAME, "Cosito");
        dummyProductToInsert.put(InventoryContract.InventoryEntry.PICTURE, "image.jpg");
        dummyProductToInsert.put(InventoryContract.InventoryEntry.QTY, 3);
        dummyProductToInsert.put(InventoryContract.InventoryEntry.PRICE, "$ 4");
        dummyProductToInsert.put(InventoryContract.InventoryEntry.SUPPLIER, "EL PESCADOR");
        dummyProductToInsert.put(InventoryContract.InventoryEntry.CURRENCY, InventoryContract.InventoryEntry.CURRENCY_USD);


        long id=database.insert(InventoryContract.InventoryEntry.TABLE_NAME,null,dummyProductToInsert);


        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for db");

        }
        database.close();

    }

    private void deletePets() {

        SQLiteDatabase database = mInventoryDBHelper.getWritableDatabase();




        int id=database.delete(InventoryContract.InventoryEntry.TABLE_NAME,
                        "1" ,
                        null);

        if (id == -1) {
            Log.e(LOG_TAG, "Rows affected:"+id);

        }
        Toast.makeText(this,id +" rows deleted", Toast.LENGTH_LONG).show();


        database.close();

    }
}
