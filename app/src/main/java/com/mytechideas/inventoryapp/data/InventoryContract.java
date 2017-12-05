package com.mytechideas.inventoryapp.data;

import android.provider.BaseColumns;

/**
 * Created by JuanDavid on 5/12/2017.
 */

public class InventoryContract {

    private InventoryContract(){}

    public static class InventoryEntry implements BaseColumns{
        public static final String _ID=BaseColumns._ID;
        public static final String PICTURE="product_image";
        public static final String NAME="product_name";
        public static final String QTY="product_qty";
        public static final String PRICE="product_price";
        public static final String SUPPLIER="product_supplier";
    }
}
