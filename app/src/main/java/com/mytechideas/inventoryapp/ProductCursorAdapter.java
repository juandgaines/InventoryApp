package com.mytechideas.inventoryapp;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
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
    public void bindView(View view, final Context context, final Cursor cursor) {


        TextView name= (TextView) view.findViewById(R.id.name);
        final TextView qty= (TextView) view.findViewById(R.id.qty_available);
        TextView price= (TextView) view.findViewById(R.id.price);
        ImageView pic= (ImageView) view.findViewById(R.id.product_image);
        final ImageView button=(ImageView) view.findViewById(R.id.buy_button);
        //pic.setImageResource(R.drawable.ic_launcher_foreground);



        /*String supplierSchema= cursor.getString(cursor.getColumnIndexOrThrow(InventoryContract.InventoryEntry.SUPPLIER)).replaceAll("\\s", "").toLowerCase();
        //Toast.makeText(context,supplierSchema,Toast.LENGTH_LONG).show();
        final String[] supplierEmail={supplierSchema+"@"+supplierSchema+".com"};
                *//*{(cursor.getString(cursor.getColumnIndexOrThrow(InventoryContract.InventoryEntry.SUPPLIER)).replaceAll("\\s", ""))+
                "@"+(cursor.getString(cursor.getColumnIndexOrThrow(InventoryContract.InventoryEntry.SUPPLIER)).replaceAll("\\s", ""))+".com"};
                *//*
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:"));
                intent.putExtra(Intent.EXTRA_EMAIL, supplierEmail);
                intent.putExtra(Intent.EXTRA_SUBJECT, "New order:");
                if (intent.resolveActivity(context.getPackageManager()) != null) {
                    context.startActivity(intent);
                }
            }
        });
        button.setClickable(true);*/

        String nameString=cursor.getString(cursor.getColumnIndexOrThrow(InventoryContract.InventoryEntry.NAME));
        String priceString=cursor.getString(cursor.getColumnIndexOrThrow(InventoryContract.InventoryEntry.PRICE));
        final Integer qtyInteger=cursor.getInt(cursor.getColumnIndexOrThrow(InventoryContract.InventoryEntry.QTY));

        String qtyString=qtyInteger.toString();


        int currencyInteger=cursor.getInt(cursor.getColumnIndexOrThrow(InventoryContract.InventoryEntry.CURRENCY));
        final Integer id=cursor.getInt(cursor.getColumnIndexOrThrow(InventoryContract.InventoryEntry._ID));

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                if(qtyInteger!=0) {
                    Integer holder=qtyInteger;
                    ContentValues valores =new ContentValues();
                    holder --;
                    qty.setText(holder.toString()+" pcs available");


                    valores.put(InventoryContract.InventoryEntry.QTY,holder);
                    Uri currentProductUri = ContentUris.withAppendedId(InventoryContract.InventoryEntry.CONTENT_URI,id);
                    Toast.makeText(context, "ID DB:"+id,Toast.LENGTH_SHORT).show();

                    int stateOfUpdate=context.getContentResolver().update(currentProductUri,valores,null,null);
                    if (stateOfUpdate == 0) {
                        // If no rows were affected, then there was an error with the update.
                        Toast.makeText(context, "Error",Toast.LENGTH_SHORT).show();
                    } else {
                        // Otherwise, the update was successful and we can display a toast.
                        Toast.makeText(context, "Sold" + id +" item",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(context,"No products available. Refill stock from your supplier.", Toast.LENGTH_LONG).show();
                }
            }
        });
        button.setClickable(true);


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
