package com.jhdev.coinfriends;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ShareActionProvider;
import android.widget.TextView;
import android.widget.Toast;
  
/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a {@link ItemListActivity}
 * in two-pane mode (on tablets) or a {@link ItemDetailActivity}
 * on handsets.
 */
public class ItemDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    /**
     * The dummy content this fragment is presenting.
     */
    //private DummyContent.DummyItem mItem;

    private String textName;
    private String textCoinAddress;
    private String textCoinType;
    private String id;
    Button button;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ItemDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            //mItem = DummyContent.ITEM_MAP.get(getArguments().getString(ARG_ITEM_ID));

            //String temp = getArguments().getString(ARG_ITEM_ID);
            //Toast.makeText(getActivity(), "arg item id: " + temp, Toast.LENGTH_SHORT).show();

            Uri uri = Uri.withAppendedPath(ItemContentProvider.CONTENT_URI, getArguments().getString(ARG_ITEM_ID));

            //need to use getActivity() first before getContentResolver
            Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
            cursor.moveToFirst();
            id = cursor.getString(0);
            textName = cursor.getString(1);
            textCoinAddress = cursor.getString(3);
            textCoinType = cursor.getString(2);
            //Toast.makeText(getActivity(), "name, address, type: " + textName + textCoinAddress + textCoinType, Toast.LENGTH_LONG).show();

            cursor.close();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_item_detail_view, container, false);

        // Set data into textviews
        ((TextView) rootView.findViewById(R.id.textView_fieldname)).setText(textName);
        ((TextView) rootView.findViewById(R.id.textView_fieldcointype)).setText(textCoinType);
        ((TextView) rootView.findViewById(R.id.textView_fieldcoinaddress)).setText(textCoinAddress);     
        
        //button to scan QR codes
        button = (Button) rootView.findViewById(R.id.showqrButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
            	  Intent intent = new Intent("com.google.zxing.client.android.ENCODE");
            	  intent.putExtra("ENCODE_TYPE", "TEXT_TYPE");
            	  intent.putExtra("ENCODE_DATA", textCoinAddress);
            	  startActivity(intent);
                
//                Intent intent2 = new Intent("com.google.zxing.integration.android.IntentIntegrator.shareText(this, textCoinAddress)");
//                startActivity(intent2);
            }
        });
        
        //Update share intent with real data
        setShareIntent(createShareIntent());
       	Log.d("shareintent", "update +" + textCoinAddress);
        
        return rootView;
    }
        

    
    // to create the share menu
    private ShareActionProvider mShareActionProvider;  
    @Override  
    public void onPrepareOptionsMenu(Menu menu) {  
         // Locate MenuItem with ShareActionProvider  
         MenuItem item = menu.findItem(R.id.menu_share);  
         // Fetch and store ShareActionProvider  
         mShareActionProvider = (ShareActionProvider) item.getActionProvider();  
         setShareIntent(createShareIntent());  
         // Return true to display menu  
         //return true;  
    }  
    // Call to update the share intent  
    private void setShareIntent(Intent shareIntent) {  
         if (mShareActionProvider != null) {  
              mShareActionProvider.setShareIntent(shareIntent);  
         }  
    }  
    // put in temp extra_text first since Menu is created before Fragment is instantiated.
    private Intent createShareIntent() {  
         Intent shareIntent = new Intent(Intent.ACTION_SEND);  
         shareIntent.setType("text/plain");  
         shareIntent.putExtra(Intent.EXTRA_TEXT, textCoinAddress);  
         return shareIntent;  
    }  



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.del:          	
                    
                AlertDialog myDeleteDialogBox =new AlertDialog.Builder(getActivity()) 
                //set message, title, and icon
                .setTitle("Delete") 
                .setMessage("Are you sure you want to delete this entry?") 
                //.setIcon(R.drawable.delete)
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) { 
                        //your deleting code
                     	   
                        // Delete this entry from database
                        //Toast.makeText(getActivity(), "del activated, id: " + id, Toast.LENGTH_SHORT).show();
                    	Toast.makeText(getActivity(), "Deleted", Toast.LENGTH_SHORT).show();
                        Uri uri = Uri.withAppendedPath(ItemContentProvider.CONTENT_URI, id);
                        getActivity().getContentResolver().delete(uri, null, null);
                        
                        //Switch back to ItemListActivity
                        Intent myIntent = new Intent(getActivity(), ItemListActivity.class);
                        startActivity(myIntent);
                        //return true;
                 	   
                        dialog.dismiss();
                    }   
                })
 
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();
                myDeleteDialogBox.show();
              
        	}
        return true;
    }    

}
