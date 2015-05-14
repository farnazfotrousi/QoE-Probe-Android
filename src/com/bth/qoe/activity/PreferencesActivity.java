package com.bth.qoe.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.widget.Toast;
import android.preference.PreferenceGroup;

import com.bth.qoe.service.ConfigurationService;
import com.bth.qoe.service.ConfigurationService.ConfBinder;
import com.bth.qoe.FloatEditTextPreference;
import com.bth.qoe.IntEditTextPreference;
import com.bth.qoe.R;

/**
 * The activity to manage the preferences in menu
 *
 */
public class PreferencesActivity extends PreferenceActivity implements OnSharedPreferenceChangeListener {
	
	public ConfigurationService ccService;

	boolean mBound = false;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.configuration);
        PreferenceManager.setDefaultValues(getBaseContext(), R.xml.configuration,false);
			}	
	
	@Override 
    protected void onStart(){
    	super.onStart();
    	Intent intent = new Intent(this, ConfigurationService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }
    @Override
    protected void onStop() {
        super.onStop();
        // Unbind from the service
        if (mBound) {
            unbindService(mConnection);
            mBound = false;
        }
    }


    private ServiceConnection mConnection = new ServiceConnection() {
        @SuppressWarnings("deprecation")
		@Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            //bound to LocalService, cast the IBinder and get LocalService instance
			ConfBinder binder =(ConfBinder) service;
            ccService = binder.getService();
            mBound = true;  
            initSummary(getPreferenceScreen());

        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;

        }
    };
	 @SuppressWarnings("deprecation")
	@Override
	    protected void onResume() {
	        super.onResume();
	        // Set up a listener whenever a key changes
            
	        getPreferenceScreen().getSharedPreferences()
	                .registerOnSharedPreferenceChangeListener(this);
	    }

	    @SuppressWarnings("deprecation")
		@Override
	    protected void onPause() {
	        super.onPause();
	        // Unregister the listener whenever a key changes
	        getPreferenceScreen().getSharedPreferences()
	                .unregisterOnSharedPreferenceChangeListener(this);
	    }

	    @SuppressWarnings("deprecation")
		public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
	            String key) {
	       
	    	updatePref(findPreference(key));
	    	
	    }

	    private void initSummary(Preference p) {
	        if (p instanceof PreferenceGroup) {
	            PreferenceGroup pGrp = (PreferenceGroup) p;
	            for (int i = 0; i < pGrp.getPreferenceCount(); i++) {
	                initSummary(pGrp.getPreference(i));
	            }
	        } else {
	            updatePref(p);
	        }
	    }


	    public void updatePref(Preference p) { 
	        PreferenceManager.setDefaultValues(getBaseContext(), R.xml.configuration,false);
	        if (p instanceof IntEditTextPreference) {
	        	IntEditTextPreference editTextPref = (IntEditTextPreference) p;
	        	//PreferenceCategory curCat = (PreferenceCategory)findPreference("app");
	        	int newValue=Integer.valueOf(editTextPref.getText());
	        	String key=editTextPref.getKey();
	        	 if (key.equals("timespan")){

	        		if (0<newValue && newValue < 60) {
			        	p.setSummary(editTextPref.getText() + getResources().getString(R.string.menu_preferences_timespan_hints));
			        	//ccService.updateConfiguration(app,"timespan",newValue+"");	
	        		}
	        		else
	        		{
                   	 Toast.makeText(getBaseContext(), (CharSequence)getResources().getString(R.string.menu_preferences_new_timespan_error), Toast.LENGTH_LONG).show();
	        		}	
	        	 }

	        }
	        
	        else if (p instanceof FloatEditTextPreference) {
	        	FloatEditTextPreference editTextPref = (FloatEditTextPreference) p;
	        	//PreferenceCategory curCat = (PreferenceCategory)findPreference("app");
	        	float newValue_float=Float.valueOf(editTextPref.getText());
	        	String key=editTextPref.getKey();
	        	if (key.equals("likelihood")){
	        		if (newValue_float > 0 && newValue_float<=100) {
			          p.setSummary(editTextPref.getText() + getResources().getString(R.string.menu_preferences_likelihood_hints));
			          //ccService.updateConfiguration(app,"likelihood",newValue+"");				          
	        		}
	        		else
	        		{
                   	 Toast.makeText(getBaseContext(), (CharSequence)getResources().getString(R.string.menu_preferences_new_likelihood_error), Toast.LENGTH_LONG).show();
	        		}
	        	}	
	        	
	        }
	      
	    }

	

	
}
