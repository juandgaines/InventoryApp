package com.mytechideas.inventoryapp;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mytechideas.inventoryapp.data.InventoryContract;

/**
 * Created by JuanDavid on 20/12/2017.
 */

public class ProductCursorAdapter extends CursorAdapter{
    public static final String LOG_TAG = ProductCursorAdapter.class.getSimpleName();
    public ProductCursorAdapter(Context context, Cursor c) {
        super(context, c);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item,parent,false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ImageView product_pic= (ImageView) view.findViewById(R.id.product_image);
        TextView name= (TextView) view.findViewById(R.id.name);
        TextView qty= (TextView) view.findViewById(R.id.qty_available);

        String nameString=cursor.getString(cursor.getColumnIndex(InventoryContract.InventoryEntry.NAME));
        String qtyString=cursor.getString(cursor.getColumnIndex(InventoryContract.InventoryEntry.QTY));

        Log.v(LOG_TAG,"nameString DB: "+ nameString);
        Log.v(LOG_TAG,"qty DB: "+ qtyString);
        name.setText(nameString);
        qty.setText(qtyString +" USD");
        product_pic.setImageResource(R.drawable.ic_launcher_foreground);
    }
}
