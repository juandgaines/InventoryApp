package com.mytechideas.inventoryapp.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by JuanDavid on 23/12/2017.
 */

public class InventoryProvider extends ContentProvider {
    public static final String LOG_TAG = InventoryProvider.class.getSimpleName();
    private InventoryDbHelper mInventoryDBHelper;
    private static final int PRODUCTS = 100;

    /**
     * URI matcher code for the content URI for a single pet in the pets table
     */
    private static final int PRODUCTS_ID = 101;


    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        // The calls to addURI() go here, for all of the content URI patterns that the provider
        // should recognize. All paths added to the UriMatcher have a corresponding code to return
        // when a match is found.
        sUriMatcher.addURI(InventoryContract.CONTENT_AUTHORITY, InventoryContract.PATH_INVENTORY, PRODUCTS);
        sUriMatcher.addURI(InventoryContract.CONTENT_AUTHORITY, InventoryContract.PATH_INVENTORY + "/#", PRODUCTS_ID);

    }

    @Override
    public boolean onCreate() {

        mInventoryDBHelper=  new InventoryDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase database = mInventoryDBHelper.getReadableDatabase();
        Cursor cursor;

        int match = sUriMatcher.match(uri);

        switch (match) {
            case PRODUCTS:
                cursor= database.query(InventoryContract.InventoryEntry.TABLE_NAME,
                        projection, selection,selectionArgs,null,null,sortOrder);
                break;
            case PRODUCTS_ID:

                selection = InventoryContract.InventoryEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};

                cursor=database.query(InventoryContract.InventoryEntry.TABLE_NAME,
                        projection,selection,selectionArgs,null,
                        null,sortOrder);
                break;

            default:
                throw new IllegalArgumentException("Cannot query unkwon URI"+ uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(),uri);

        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {

        final int match=sUriMatcher.match(uri);

        switch(match){
            case PRODUCTS:
                return insertProduct(uri,contentValues);
            default:
                throw new IllegalArgumentException("Insertion is not supported for "+ uri);
        }

    }

    private Uri insertProduct(Uri uri, ContentValues contentValues) {

        String name= contentValues.getAsString(InventoryContract.InventoryEntry.NAME);
        String picture= contentValues.getAsString(InventoryContract.InventoryEntry.PICTURE);
        Integer quantity=contentValues.getAsInteger(InventoryContract.InventoryEntry.QTY);
        String price=contentValues.getAsString(InventoryContract.InventoryEntry.PRICE);
        String supplier=contentValues.getAsString(InventoryContract.InventoryEntry.SUPPLIER);
        Integer currency=contentValues.getAsInteger(InventoryContract.InventoryEntry.CURRENCY);
        if (name == null || picture==null || price ==null ||supplier==null) {
            throw new IllegalArgumentException("Product requires a name, picture, price and supplier");
        }

        else if (quantity != null && quantity < 0) {
            throw new IllegalArgumentException("Product requires valid qty");
        }
        SQLiteDatabase database=mInventoryDBHelper.getWritableDatabase();
        long id=database.insert(InventoryContract.InventoryEntry.TABLE_NAME,
                null,contentValues);
        if (id==-1){
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }
        //getContext().getContentResolver().notifyChange(uri,null); // Line for loader
        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}
