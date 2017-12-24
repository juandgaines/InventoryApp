package com.mytechideas.inventoryapp.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by JuanDavid on 5/12/2017.
 */

public class InventoryContract {

    public static final String CONTENT_AUTHORITY="com.mytechideas.inventoryapp";
    public static final Uri BASE_CONTENT=Uri.parse("content://" +CONTENT_AUTHORITY);
    public static final String PATH_INVENTORY="products";
    private InventoryContract(){}

    public static class InventoryEntry implements BaseColumns{

        public static final Uri CONTENT_URI=Uri.withAppendedPath(BASE_CONTENT,PATH_INVENTORY);
        public static final String TABLE_NAME= "products";

        public static final String _ID=BaseColumns._ID;
        public static final String PICTURE="product_image";
        public static final String NAME="product_name";
        public static final String QTY="product_qty";
        public static final String PRICE="product_price";
        public static final String CURRENCY="product_currency";
        public static final String SUPPLIER="product_supplier";

        public static final int CURRECNCY_COP= 0;
        public static final int CURRENCY_USD= 1;
    }
}
