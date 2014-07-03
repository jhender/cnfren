package com.jhdev.coinfriends;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import com.google.analytics.tracking.android.EasyTracker;

/**
 * An activity representing a list of Items. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link ItemDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 * <p>
 * The activity makes heavy use of fragments. The list of items is a
 * {@link ItemListFragment} and the item details
 * (if present) is a {@link ItemDetailFragment}.
 * <p>
 * This activity also implements the required
 * {@link ItemListFragment.Callbacks} interface
 * to listen for item selections.
 */
public class ItemListActivity extends FragmentActivity
        implements ItemListFragment.Callbacks 
        //, ItemAddDialogFragment.ItemAddDialogListener 
        {
	

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;
    
    private static final int SETTINGS_RESULT = 1;
 
 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);

        if (findViewById(R.id.item_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-large and
            // res/values-sw600dp). If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;

            // In two-pane mode, list items should be given the
            // 'activated' state when touched.
            ((ItemListFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.item_list))
                    .setActivateOnItemClick(true);
        }

        // TODO: If exposing deep links into your app, handle intents here.

    }

    /**
     * Callback method from {@link ItemListFragment.Callbacks}
     * indicating that the item with the given ID was selected.
     */
    @Override
    public void onItemSelected(String id) {
        if (mTwoPane) {
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putString(ItemDetailFragment.ARG_ITEM_ID, id);
            ItemDetailFragment fragment = new ItemDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.item_detail_container, fragment)
                    .commit();

        } else {
            // In single-pane mode, simply start the detail activity
            // for the selected item ID.
            Intent detailIntent = new Intent(this, ItemDetailActivity.class);
            detailIntent.putExtra(ItemDetailFragment.ARG_ITEM_ID, id);
            startActivity(detailIntent);
        }
    }


    // create the menu based on the XML defintion
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //MenuInflater inflater = getMenuInflater();
        getMenuInflater().inflate(R.menu.itemlistfragment_menu, menu);
        return true;
    }
 
    // Reaction to the menu selection
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add2:
            	// only allow adding up to 10 entries
            	if (checkItemCount() < 10) 
            	{ 
	                Intent intent = new Intent(this, ItemAddActivity.class);
	                startActivity(intent);
	                return true;
	                }
            	else  {
            		// show ding
            		
            		AlertDialog limitDialogBox =new AlertDialog.Builder(this) 
		                //set message, title, and icon
		                //.setTitle("Limit") 
		                .setMessage("This alpha free version is limited to 10 entries.") 
		                .create();
		                limitDialogBox.show();
            		            	
		                // TODO upgrade prompt	
            		return true;
            	}     
            	
            case R.id.menu_settings:
                Intent i = new Intent(this, ActivityUserSettings.class);
                startActivityForResult(i, SETTINGS_RESULT);
                break;     
	
            	
        } 
        return super.onOptionsItemSelected(item);
    }
    
    
    private int checkItemCount() {
    	Uri CONTENT_URI = ItemContentProvider.CONTENT_URI;
    	Cursor countCursor = getContentResolver().query(CONTENT_URI,
                new String[] {"count(*) AS count"},
                null,
                null,
                null);

        countCursor.moveToFirst();
        int itemCount = countCursor.getInt(0);
        
        return itemCount;
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
                super.onActivityResult(requestCode, resultCode, data);

                    if(requestCode==SETTINGS_RESULT)
                    {
                        displayUserSettings();
                    }

    }
    
    private void displayUserSettings() 
    {
            SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

            String  settings = "";
 
            settings=settings+"Password: " + sharedPrefs.getString("prefUserPassword", "NOPASSWORD");

            settings=settings+"\nRemind For Update:"+ sharedPrefs.getBoolean("prefLockScreen", false);

            settings=settings+"\nUpdate Frequency: "
                    + sharedPrefs.getString("prefUpdateFrequency", "NOUPDATE");

//            TextView textViewSetting = (TextView) findViewById(R.id.textViewSettings);
//
//            textViewSetting.setText(settings);
     }

    
    
    
    
    
    
//    Google Analytics
    @Override
    public void onStart() {
      super.onStart();
      // The rest of your onStart() code.
      EasyTracker.getInstance(this).activityStart(this);  // Add this method.
    }

    @Override
    public void onStop() {
      super.onStop();
      // The rest of your onStop() code.
      EasyTracker.getInstance(this).activityStop(this);  // Add this method.
    }


}
