package com.jhdev.coinfriends;

import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;

public class SettingsFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.user_settings);
    
    
	    Preference share_with_friendsButton = (Preference) findPreference("share_with_friends");
	    share_with_friendsButton.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
	        @Override
	        public boolean onPreferenceClick(Preference arg0) {
	
	            Intent sendIntent = new Intent();
	            sendIntent.setAction(Intent.ACTION_SEND);
	            sendIntent.putExtra(Intent.EXTRA_TEXT,
	                "Hey download CoinFriends app at: https://play.google.com/store/apps/details?id=com.jhdev.coinfriends");
	            sendIntent.setType("text/plain");
	            startActivity(sendIntent);
	            
				return true;
	        } 
	    });
	    
	    Preference send_btcButton = (Preference) findPreference("send_btc");
	    send_btcButton.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
	        @Override
	        public boolean onPreferenceClick(Preference arg0) {
	
	            Intent sendIntent = new Intent();
	            sendIntent.setAction(Intent.ACTION_SEND);
	            sendIntent.putExtra(Intent.EXTRA_TEXT, "14ttsooq42L35imoNWThdJ57x5FcytwwQh");
	            sendIntent.setType("text/plain");
	            startActivity(sendIntent);
	            
				return true;
	        }
	    });
	    Preference send_ltcButton = (Preference) findPreference("send_ltc");
	    send_ltcButton.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
	        @Override
	        public boolean onPreferenceClick(Preference arg0) {
	
	            Intent sendIntent = new Intent();
	            sendIntent.setAction(Intent.ACTION_SEND);
	            sendIntent.putExtra(Intent.EXTRA_TEXT, "LTmwQvJ9Bh4sAgz3mFrQXMrxHUYmd17kAc");
	            sendIntent.setType("text/plain");
	            startActivity(sendIntent);
	            
				return true;
	        }
	    });
	    Preference send_dogeButton = (Preference) findPreference("send_doge");
	    send_dogeButton.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
	        @Override
	        public boolean onPreferenceClick(Preference arg0) {
	
	            Intent sendIntent = new Intent();
	            sendIntent.setAction(Intent.ACTION_SEND);
	            sendIntent.putExtra(Intent.EXTRA_TEXT, "DGCZD4aPV7QSBw84qFFACnsWFGt8YUjLHT");
	            sendIntent.setType("text/plain");
	            startActivity(sendIntent);
	            
				return true;
	        }
	    });
    
    }
    
}