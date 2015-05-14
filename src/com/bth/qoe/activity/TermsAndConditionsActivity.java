package com.bth.qoe.activity;

import com.bth.qoe.R;
import com.bth.qoe.service.ConfigurationService;
import com.bth.qoe.service.ConfigurationService.ConfBinder;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

/**
 * The activity to manage Terms and Conditions in the menu
 *
 */
public class TermsAndConditionsActivity extends ActionBarActivity {
	ConfigurationService cService;
	Intent intent_terms;
	boolean mBound = false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);			
		setContentView(R.layout.activity_terms_and_conditions); 
		 
		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}	
	
	}
	@Override 
    protected void onStart(){
		
	
		
    	super.onStart();
    	
    	intent_terms=getIntent();
    	String termsType=intent_terms.getExtras().getString("termstype");
		Log.d ("com.bth.qoe","TermType:"+termsType);
    	if (termsType.equals("content")){
			Button acceptBtn=(Button) findViewById(R.id.button_accept);
			acceptBtn.setVisibility(View.GONE);
			Button rejectBtn=(Button) findViewById(R.id.button_reject);
			rejectBtn.setVisibility(View.GONE);
			LinearLayout linearLayout =  (LinearLayout)findViewById(R.id.terms_layout);
			linearLayout.removeView(findViewById(R.id.applicationRequest));	

		} 
		
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
        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            //bound to LocalService, cast the IBinder and get LocalService instance
			ConfBinder binder =(ConfBinder) service;
            cService = binder.getService();
            mBound = true;      
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };
	public void acceptTerms(View view){
		cService.setAccceptRule(true);
		cService.setFirstRunApp(false);	
		finish();
	
		
	}
	public void rejectTerms(View view){
		cService.setAccceptRule(false);
		cService.setFirstRunApp(false);
		finish();
	}
	
	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(
					R.layout.fragment_terms_and_conditions, container, false);
			
			
			return rootView;
		}
	}

}
