package com.jhdev.coinfriends;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.app.backup.BackupManager;

public class ItemAddActivity extends Activity {

    Button scanButton;
    Button saveButton;
    Button cancelButton;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_form_add);

        resetFields();
          
        //button to scan QR codes
        scanButton = (Button) findViewById(R.id.scan_button2);
        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                IntentIntegrator integrator = new IntentIntegrator(ItemAddActivity.this);
                integrator.initiateScan();
            }
        });
        
        //button to save data
        saveButton = (Button) findViewById(R.id.save_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Log.i("Save Button", "Positive click!");
 
                // look for text
                EditText newItemNameEdit = (EditText) findViewById(R.id.editText);
                String newItemName = newItemNameEdit.getText().toString();
                EditText newItemCoinAddressEdit = (EditText) findViewById(R.id.editText3);
                String newItemCoinAddress = newItemCoinAddressEdit.getText().toString();
                Spinner spinner = (Spinner) findViewById(R.id.spinner);
                String newItemCoinType = spinner.getSelectedItem().toString();
  
                // Creating an instance of ContentValues
                ContentValues contentValues = new ContentValues();
                // Setting values in ContentValues
                contentValues.put(ItemContentProvider.FIELD_NAME, newItemName );
                contentValues.put(ItemContentProvider.FIELD_COIN_TYPE, newItemCoinType);
                contentValues.put(ItemContentProvider.FIELD_COIN_ADDRESS, newItemCoinAddress);
    
                // insert
                getContentResolver().insert(ItemContentProvider.CONTENT_URI, contentValues);
                resetFields();
                
                // confirmation
                Toast.makeText(getBaseContext(), "Added: " + newItemCoinAddress, Toast.LENGTH_SHORT).show();

                // backup
                requestBackup();

                //Switch back to ItemListActivity and return 'yes' parameter
                Intent myIntent = new Intent(ItemAddActivity.this, ItemListActivity.class);
                myIntent.putExtra("key", "yes"); //Optional parameters
                ItemAddActivity.this.startActivity(myIntent);
                
            }
        });
        
        
        //button to cancel
        cancelButton = (Button) findViewById(R.id.cancel_button);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                //Switch back to ItemListActivity and return 'no' parameter
                Intent myIntent = new Intent(ItemAddActivity.this, ItemListActivity.class);
                myIntent.putExtra("key", "no"); //Optional parameters
                ItemAddActivity.this.startActivity(myIntent);

            }
        });
        
    }
    
    private void resetFields() {
        EditText newItemNameEdit = (EditText) findViewById(R.id.editText);
        newItemNameEdit.setText("");
        EditText newItemCoinAddressEdit = (EditText) findViewById(R.id.editText3);
        newItemCoinAddressEdit.setText("");
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setSelection(0);
    }

    //watch for scan return
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanResult != null) {
            // handle scan result
            String scanned_string = scanResult.getContents();
            //Toast.makeText(this, "scanned back: " + scanned_string, Toast.LENGTH_SHORT).show();
            EditText eT = (EditText) findViewById(R.id.editText3);
            eT.setText(scanned_string);
        }
        // else continue with any other code you need in the method
        //...
    }

    public void requestBackup() {
        BackupManager bm = new BackupManager(this);
        bm.dataChanged();
    }

}