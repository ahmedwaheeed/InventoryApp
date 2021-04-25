package waheed.nanodegree.udacity.android.inventoryapp.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by waheed on 3/26/2018.
 */

public final class InventoryContract {
    public static final String CONTENT_AUTHORITY = "waheed.nanodegree.udacity.android.inventoryapp";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final class InventoryEntry implements BaseColumns{

        public static final String TABLE_NAME = "products";
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, TABLE_NAME);

        public static final String ID = BaseColumns._ID;
        public static final String PRODUCT_NAME = "product_name";
        public static final String PRODUCT_PRICE = "product_price";
        public static final String PRODUCT_QUANTITY = "product_quantity";
        public static final String SUPPLIER_NAME = "supplier_name";
        public static final String SUPPLIER_PHONE_NUMBER = "supplier_phone_number";
    }
}
