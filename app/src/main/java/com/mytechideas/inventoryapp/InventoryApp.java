package com.mytechideas.inventoryapp;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

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

        insertPet();


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
    protected void onDestroy() {
        super.onDestroy();

        mInventoryDBHelper.close();
    }

    private void insertPet() {

        SQLiteDatabase database = mInventoryDBHelper.getWritableDatabase();

        ContentValues dummyProductToInsert = new ContentValues();

        dummyProductToInsert.put(InventoryContract.InventoryEntry.NAME, "Garfield");
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
}
