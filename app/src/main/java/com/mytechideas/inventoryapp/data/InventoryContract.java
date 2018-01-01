package com.mytechideas.inventoryapp.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;
/**
 * Created by JuanDavid on 5/12/2017.
 */

public class InventoryContract {

    public static final String CONTENT_AUTHORITY="com.mytechideas.inventoryapp";
    public static final Uri BASE_CONTENT_URI=Uri.parse("content://" +CONTENT_AUTHORITY);
    public static final String PATH_INVENTORY="products";


    private InventoryContract(){}

    public static class InventoryEntry implements BaseColumns{



        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_INVENTORY;

        /**
         * The MIME type of the {@link #CONTENT_URI} for a single pet.
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_INVENTORY;

        public static final String TABLE_NAME= "products";

        public static final String _ID=BaseColumns._ID;
        public static final String PICTURE="product_image";
        public static final String NAME="product_name";
        public static final String QTY="product_qty";
        public static final String PRICE="product_price";
        public static final String CURRENCY="product_currency";
        public static final String SUPPLIER="product_supplier";

        public static final int UNKNOW= 0;
        public static final int CURRECNCY_COP= 1;
        public static final int CURRENCY_USD= 2;


        public static final Uri CONTENT_URI=Uri.withAppendedPath(BASE_CONTENT_URI,PATH_INVENTORY);
        public static boolean isValidCurrency(int currency) {
            if (currency == CURRENCY_USD || currency == CURRECNCY_COP) {
                return true;
            }
            return false;
        }
    }

}
