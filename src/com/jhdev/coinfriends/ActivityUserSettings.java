package com.jhdev.coinfriends;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;

public class ActivityUserSettings extends PreferenceActivity {

	   @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);

	        // Show the Up button in the action bar.
	        getActionBar().setDisplayHomeAsUpEnabled(true);

	        
	        // Display the fragment as the main content.
	        getFragmentManager().beginTransaction()
	                .replace(android.R.id.content, new SettingsFragment())
	                .commit();
	    }
	     
	   
	    @Override
	    public boolean onOptionsItemSelected(MenuItem item) {
	        switch (item.getItemId()) {
	            case android.R.id.home:
	                // This ID represents the Home or Up button. In the case of this
	                // activity, the Up button is shown. Use NavUtils to allow users
	                // to navigate up one level in the application structure. For
	                // more details, see the Navigation pattern on Android Design:
	                //
	                // http://developer.android.com/design/patterns/navigation.html#up-vs-back
	                //
	                NavUtils.navigateUpTo(this, new Intent(this, ItemListActivity.class));
	                return true;


	        }
	        return super.onOptionsItemSelected(item);

	    }

	   
	   
}