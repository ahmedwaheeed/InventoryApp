package waheed.nanodegree.udacity.android.inventoryapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import waheed.nanodegree.udacity.android.inventoryapp.data.InventoryContract.InventoryEntry;
/**
 * Created by waheed on 3/26/2018.
 */

public class InventoryDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "inventory.db";

    private static final String DATABASE_TABLE_QUERY = "CREATE TABLE " + InventoryEntry.TABLE_NAME + " ("
            + InventoryEntry.ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + InventoryEntry.PRODUCT_NAME + " TEXT NOT NULL,"
            + InventoryEntry.PRODUCT_PRICE + " INTEGER NOT NULL,"
            + InventoryEntry.PRODUCT_QUANTITY + " INTEGER DEFAULT 0,"
            + InventoryEntry.SUPPLIER_NAME + " TEXT NOT NULL,"
            + InventoryEntry.SUPPLIER_PHONE_NUMBER + " TEXT NOT NULL );" ;

    private static final String DATABASE_TABLE_DROP = "DROP TABLE " + InventoryEntry.TABLE_NAME;

    public InventoryDbHelper(Context context) {
        super(context, DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_TABLE_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

    }
}
