package waheed.nanodegree.udacity.android.inventoryapp;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.zip.Inflater;

import waheed.nanodegree.udacity.android.inventoryapp.data.InventoryContract.InventoryEntry;

/**
 * Created by waheed on 3/28/2018.
 */

public class InventoryCursorAdapter extends CursorAdapter{


    public InventoryCursorAdapter(Context context, Cursor c) {
        super(context,c,0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.list_item,viewGroup,false);
    }


    @Override
    public void bindView(View view, final Context context, final Cursor cursor) {
        final long id = cursor.getInt(cursor.getColumnIndex(InventoryEntry._ID));

        TextView textProductName = (TextView) view.findViewById(R.id.textProductName);
        TextView textProductCost = (TextView) view.findViewById(R.id.textCost);
        TextView textProductQuantity = (TextView) view.findViewById(R.id.textQuantity);

        final String productName = cursor.getString(cursor.getColumnIndex(InventoryEntry.PRODUCT_NAME));
        final int productCost = cursor.getInt(cursor.getColumnIndex(InventoryEntry.PRODUCT_PRICE));
        final int productQuantity = cursor.getInt(cursor.getColumnIndex(InventoryEntry.PRODUCT_QUANTITY));
        final int finalQuantity = productQuantity - 1;
        final String supplierName = cursor.getString(cursor.getColumnIndex(InventoryEntry.SUPPLIER_NAME));
        final String supplierNumber = cursor.getString(cursor.getColumnIndex(InventoryEntry.SUPPLIER_PHONE_NUMBER));

        textProductName.setText(productName);
        textProductCost.setText(Integer.toString(productCost)+" $");
        textProductQuantity.setText(Integer.toString(productQuantity));

        ImageView decreaseQuantityFab = (ImageView) view.findViewById(R.id.quantity_button);
        decreaseQuantityFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                    ContentValues values = new ContentValues();
                    values.put(InventoryEntry.PRODUCT_NAME, productName);
                    values.put(InventoryEntry.PRODUCT_PRICE, productCost);
                if(finalQuantity < 0) {
                    values.put(InventoryEntry.PRODUCT_QUANTITY, 0);
                }else{
                    values.put(InventoryEntry.PRODUCT_QUANTITY, finalQuantity);
                }
                    values.put(InventoryEntry.SUPPLIER_NAME, supplierName);
                    values.put(InventoryEntry.SUPPLIER_PHONE_NUMBER, supplierNumber);

                    Uri currentProductUri = ContentUris.withAppendedId(InventoryEntry.CONTENT_URI, id);

                    context.getContentResolver().update(currentProductUri, values, null, null);

            }
        });

    }

}
