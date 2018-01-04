package com.mytechideas.inventoryapp;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mytechideas.inventoryapp.data.InventoryContract;

/**
 * Created by JuanDavid on 20/12/2017.
 */

public class ProductCursorAdapter extends CursorAdapter{
    public static final String LOG_TAG = ProductCursorAdapter.class.getSimpleName();
    public ProductCursorAdapter(Context context, Cursor c) {
        super(context, c,0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item,parent,false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {


        TextView name= (TextView) view.findViewById(R.id.name);
        TextView qty= (TextView) view.findViewById(R.id.qty_available);
        TextView price= (TextView) view.findViewById(R.id.price);

        ImageView pic= (ImageView) view.findViewById(R.id.product_image);
        ImageView button=(ImageView) view.findViewById(R.id.buy_button);
        //pic.setImageResource(R.drawable.ic_launcher_foreground);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "Compraaaaa!!", Toast.LENGTH_SHORT).show();
            }
        });
        button.setClickable(true);



        String nameString=cursor.getString(cursor.getColumnIndexOrThrow(InventoryContract.InventoryEntry.NAME));
        String priceString=cursor.getString(cursor.getColumnIndexOrThrow(InventoryContract.InventoryEntry.PRICE));
        String qtyString=Integer.toString(cursor.getInt(cursor.getColumnIndexOrThrow(InventoryContract.InventoryEntry.QTY)));
        int currencyInteger=cursor.getInt(cursor.getColumnIndexOrThrow(InventoryContract.InventoryEntry.CURRENCY));

        byte[] image =cursor.getBlob(cursor.getColumnIndexOrThrow(InventoryContract.InventoryEntry.PICTURE));
        Bitmap bm = BitmapFactory.decodeByteArray(image, 0 ,image.length);

        String currencyMessage;
        if (currencyInteger== InventoryContract.InventoryEntry.CURRECNCY_COP){
            currencyMessage= context.getResources().getString((R.string.currency_colombia));
        }
        else{
            currencyMessage= context.getResources().getString((R.string.currency_usa));
        }

        Log.v(LOG_TAG,"nameString DB: "+ nameString);
        //Log.v(LOG_TAG,"qty DB: "+ qtyString);
        name.setText(nameString);
        qty.setText(qtyString+ " pcs available");
        pic.setImageBitmap(bm);
        price.setText(priceString+ " "+currencyMessage);

    }
}
