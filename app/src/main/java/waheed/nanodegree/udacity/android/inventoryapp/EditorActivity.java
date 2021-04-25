package waheed.nanodegree.udacity.android.inventoryapp;

import android.Manifest;
import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import waheed.nanodegree.udacity.android.inventoryapp.data.InventoryContract.InventoryEntry;

public class EditorActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    Uri currentProductUri;
    EditText textProductName;
    EditText textProductPrice;
    TextView textProductQuantity;
    EditText textSupplierName;
    EditText textSupplierPhone;
    Button buIncreaseQuantity;
    Button buDecreaseQuantity;
    int productQuantityCounter = 0;
    FloatingActionButton fabCallSupplier;

    private final static int LOADER_CONSTANT = 6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);


        Intent intent = getIntent();
        currentProductUri = intent.getData();

        textProductName = (EditText) findViewById(R.id.edit_product_name);
        textProductPrice = (EditText) findViewById(R.id.edit_product_price);
        textSupplierName = (EditText) findViewById(R.id.edit_supplier_name);
        textSupplierPhone = (EditText) findViewById(R.id.edit__supplier_phone);
        textProductQuantity = (TextView) findViewById(R.id.textQuantityEdit);
        buIncreaseQuantity = (Button) findViewById(R.id.buPlusQuantity);
        buDecreaseQuantity = (Button) findViewById(R.id.buMinusQuantity);
        fabCallSupplier = (FloatingActionButton) findViewById(R.id.buCallSupplier);

        if (currentProductUri == null) {
            fabCallSupplier.hide();
        }

        fabCallSupplier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phoneNumber = textSupplierPhone.getText().toString();
                if (!phoneNumber.isEmpty()) {
                    Intent callIntent = new Intent(Intent.ACTION_DIAL);
                    callIntent.setData(Uri.parse("tel:" + phoneNumber));//change the number
                    startActivity(callIntent);
                    } else {
                        Toast.makeText(getBaseContext(), "No phone number detected, please write phone number", Toast.LENGTH_LONG).show();
                    }
            }
        });

        buIncreaseQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //increase quantity of product by one
                productQuantityCounter = productQuantityCounter + 1;
                textProductQuantity.setText(Integer.toString(productQuantityCounter));
            }
        });

        buDecreaseQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //decrease quantity of product by one
                if(productQuantityCounter > 0) {
                    productQuantityCounter = productQuantityCounter - 1;
                    textProductQuantity.setText(Integer.toString(productQuantityCounter));
                }
            }
        });

        if(currentProductUri != null) {
            setTitle("Edit product");
            getLoaderManager().initLoader(LOADER_CONSTANT, null, this);
        }else{
            setTitle("Add a product");
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_catalog.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.editor_menu, menu);
        if(currentProductUri == null) {
            MenuItem menuDelete = menu.findItem(R.id.action_delete);
            menuDelete.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            case R.id.action_delete:
                // Do nothing for now
               deleteProductData();
                return true;
            case R.id.action_done:
                saveProductData();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {

        String[] projection = {
                InventoryEntry.ID,
                InventoryEntry.PRODUCT_NAME,
                InventoryEntry.PRODUCT_PRICE,
                InventoryEntry.PRODUCT_QUANTITY,
                InventoryEntry.SUPPLIER_NAME,
                InventoryEntry.SUPPLIER_PHONE_NUMBER
        };

        return new CursorLoader(getBaseContext(),currentProductUri,projection,null,null,null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

        if(cursor.moveToFirst()){
            int productNameIndex = cursor.getColumnIndex(InventoryEntry.PRODUCT_NAME);
            int productPriceIndex = cursor.getColumnIndex(InventoryEntry.PRODUCT_PRICE);
            int productQuantityIndex = cursor.getColumnIndex(InventoryEntry.PRODUCT_QUANTITY);
            int supplierNameIndex = cursor.getColumnIndex(InventoryEntry.SUPPLIER_NAME);
            int supplierPhoneNumberIndex = cursor.getColumnIndex(InventoryEntry.SUPPLIER_PHONE_NUMBER);

            String productName = cursor.getString(productNameIndex);
            int productPrice = cursor.getInt(productPriceIndex);
            int productQuantity = cursor.getInt(productQuantityIndex);
            String supplierName = cursor.getString(supplierNameIndex);
            String supplierPhone = cursor.getString(supplierPhoneNumberIndex);

            productQuantityCounter = productQuantity;

            textProductName.setText(productName);
            textProductPrice.setText(Integer.toString(productPrice));
            textProductQuantity.setText(Integer.toString(productQuantity));
            textSupplierName.setText(supplierName);
            textSupplierPhone.setText(supplierPhone);


        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    private void saveProductData(){
        String productName = textProductName.getText().toString();
        String productPrice = textProductPrice.getText().toString();
        String productQuantity = textProductQuantity.getText().toString();
        String supplierName = textSupplierName.getText().toString();
        String supplierPhone = textSupplierPhone.getText().toString();

       if(!productName.isEmpty() && !productPrice.isEmpty() && !supplierName.isEmpty() && !supplierPhone.isEmpty()) {
           ContentValues contentValues = new ContentValues();
           contentValues.put(InventoryEntry.PRODUCT_NAME, productName);
           contentValues.put(InventoryEntry.PRODUCT_PRICE, Integer.parseInt(productPrice));
           contentValues.put(InventoryEntry.PRODUCT_QUANTITY, Integer.parseInt(productQuantity));
           contentValues.put(InventoryEntry.SUPPLIER_NAME, supplierName);
           contentValues.put(InventoryEntry.SUPPLIER_PHONE_NUMBER, supplierPhone);


           if (currentProductUri == null) {
               Uri result = getContentResolver().insert(InventoryEntry.CONTENT_URI,contentValues);
               if(result != null){
                   Toast.makeText(getBaseContext(),"Added successfully",Toast.LENGTH_LONG).show();
               }else{
                   Toast.makeText(getBaseContext(),"Error while adding",Toast.LENGTH_LONG).show();
               }
           } else {
               getContentResolver().update(currentProductUri, contentValues, null, null);
           }

           finish();
       }else{
           Toast.makeText(getBaseContext(),"Please don't skip any field",Toast.LENGTH_LONG).show();
       }
    }

    private void deleteProductData(){
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(EditorActivity.this);

        builder.setTitle("Delete Confirmation")
                .setMessage("Are you sure that you want to delete this product?")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Intent to view data
                        int result = getContentResolver().delete(currentProductUri,null,null);
                        if(result != -1){
                            Toast.makeText(getBaseContext(),"Product deleted successfully",Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(getBaseContext(),"Error while deleting product",Toast.LENGTH_LONG).show();
                        }
                        finish();
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
