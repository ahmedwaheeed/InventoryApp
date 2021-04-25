package waheed.nanodegree.udacity.android.inventoryapp;

import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;


import waheed.nanodegree.udacity.android.inventoryapp.data.InventoryContract.InventoryEntry;
import waheed.nanodegree.udacity.android.inventoryapp.data.InventoryDbHelper;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    ListView listView;
    InventoryCursorAdapter adapter;
    InventoryDbHelper dbHelper;
    private static final int LOADER_CONSTANT = 3;
    LinearLayout emptyView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getLoaderManager().initLoader(LOADER_CONSTANT,null,this);

        setTitle("Products list");

        dbHelper = new InventoryDbHelper(getBaseContext());

        listView = (ListView) findViewById(R.id.listview);

        emptyView = (LinearLayout) findViewById(R.id.emptyview);

        adapter = new InventoryCursorAdapter(getBaseContext(),null);

        listView.setAdapter(adapter);
        listView.setEmptyView(emptyView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                Uri currentProductUri = ContentUris.withAppendedId(InventoryEntry.CONTENT_URI, id);

                Intent i = new Intent(MainActivity.this,EditorActivity.class);
                i.setData(currentProductUri);
                startActivity(i);
            }
        });

    }

    private void insertData(){
        // Insert into database.
        ContentValues values = new ContentValues();
        values.put(InventoryEntry.PRODUCT_NAME,"Laptop");
        values.put(InventoryEntry.PRODUCT_PRICE,6000);
        values.put(InventoryEntry.PRODUCT_QUANTITY,6);
        values.put(InventoryEntry.SUPPLIER_NAME,"Dell");
        values.put(InventoryEntry.SUPPLIER_PHONE_NUMBER,"152245563");

        Uri result = getContentResolver().insert(InventoryEntry.CONTENT_URI,values);

        if(result == null){
            Toast.makeText(getBaseContext(),"Error while adding",Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(getBaseContext(),"Added successfully",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public android.content.Loader<Cursor> onCreateLoader(int i, Bundle bundle) {

        String[] projection = {
                InventoryEntry.ID,
                InventoryEntry.PRODUCT_NAME,
                InventoryEntry.PRODUCT_PRICE,
                InventoryEntry.PRODUCT_QUANTITY,
                InventoryEntry.SUPPLIER_NAME,
                InventoryEntry.SUPPLIER_PHONE_NUMBER
        };

        return new CursorLoader(getBaseContext(),InventoryEntry.CONTENT_URI,projection,null,null,null);
    }

    @Override
    public void onLoadFinished(android.content.Loader<Cursor> loader, Cursor cursor) {
        adapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(android.content.Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }



       @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_catalog.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Insert dummy data" menu option
            case R.id.action_insert_dummy_data:
                insertData();
                return true;
            // Respond to a click on the "Delete all entries" menu option
            case R.id.action_delete_all_entries:
                // Do nothing for now
                deleteAllProductsData();
                return true;
            case R.id.action_add_new_product:
                Intent i = new Intent(MainActivity.this,EditorActivity.class);
                startActivity(i);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void deleteAllProductsData(){
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(MainActivity.this);

        builder.setTitle("Delete Confirmation")
                .setMessage("Are you sure that you want to delete all products in the database?")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Intent to view data
                        int result = getContentResolver().delete(InventoryEntry.CONTENT_URI,null,null);
                        if(result != -1){
                            Toast.makeText(getBaseContext(),"All products deleted successfully",Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(getBaseContext(),"Error while deleting products",Toast.LENGTH_LONG).show();
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Intent to edit data
                    }
                })
                .show();
    }
}
