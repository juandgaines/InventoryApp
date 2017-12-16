package com.mytechideas.inventoryapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by JuanDavid on 16/12/2017.
 */

public class InventoryDbHelper extends SQLiteOpenHelper {

    public static final String LOG_TAG=InventoryDbHelper.class.getSimpleName();
    private static final String DATABASE_NAME="store.db";
    private static final int DATABASE_VERSION=1;

    public InventoryDbHelper(Context context) {
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String SQL_CREATE_PETS_TABLE ="CREATE TABLE " + InventoryContract.InventoryEntry.TABLE_NAME+ "("
                + InventoryContract.InventoryEntry._ID+ " INTEGER PRIMARY KEY AUTOINCREMENT, "
                +InventoryContract.InventoryEntry.PICTURE + " TEXT NOT NULL, "
                +InventoryContract.InventoryEntry.NAME + " TEXT, "
                +InventoryContract.InventoryEntry.PRICE + " TEXT, "
                +InventoryContract.InventoryEntry.SUPPLIER + " TEXT, "
                +InventoryContract.InventoryEntry.CURRENCY + " INTEGER NOT NULL DEFAULT 0,"
                +InventoryContract.InventoryEntry.QTY +" INTEGER NOT NULL DEFAULT 0); ";


        db.execSQL(SQL_CREATE_PETS_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
