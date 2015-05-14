package com.bth.qoe.activity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import com.bth.qoe.R;
import com.bth.qoe.service.ConfigurationService;
import com.bth.qoe.service.ConfigurationService.ConfBinder;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

/**
 * 
 * Main activity for the questionnaire form which binds activities for about, terms&condition and show logged data
 *
 */
public class MainActivity extends ActionBarActivity {
	public final static String EXTRA_MESSAGE = "com.bth.qoe.MESSAGE";
	public final static String LIKELIHOOD = "com.bth.qoe.LIKELIHOOD";
	public final static String TIMESPAN = "com.bth.qoe.TIMESPAN";
	ConfigurationService confService;
    boolean mBound = false;
    boolean menu_deactive=false;
    private String feature_name="";
    private String last_questionnaire_record="";

    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
       
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);        
        
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }   
    }
    
    @Override 
    protected void onResume(){
    	super.onResume();
    	
    	 Intent intent = new Intent(this, ConfigurationService.class);
         bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
       	Intent mIntent = getIntent();
    	if(mIntent.hasExtra("FEATURE_NAME")){
    	feature_name=mIntent.getExtras().getString("FEATURE_NAME");
    	}
        String runType="";
        if(mIntent.hasExtra("RUNETYPE")){
             runType = mIntent.getExtras().getString("RUNETYPE");
  	     	Log.d("com.bth.qoe","RUNTIME extra"+ runType);
  	        if (runType.equals("connected")){ 
          	   findViewById(R.id.welcomeLayout).setVisibility(View.GONE);  
           }
        }
         else {
        	menu_deactive=true; 
        	findViewById(R.id.questionsLayout).setVisibility(View.GONE);
            findViewById(R.id.buttonsLayout).setVisibility(View.GONE); 
            }
    	
    }
    
    @Override
    protected void onPause() {
        super.onPause();
    }
      
    @Override
    protected void onStop() {
        super.onStop();
        if (mBound) {
            unbindService(mConnection);
            mBound = false;
        }   
    }
    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
			Log.d("com.bth.qoe","A localService instance is received. QoE activity is being connected.");

			ConfBinder binder =(ConfBinder) service;
            confService = binder.getService();
            mBound = true;    
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
            confService=null;
        }
    };
    
    
	/**
	 * @param view
	 * Validate the questionnaire on Submit.
	 * Check availability of Internet connection
	 */
	public void fillOutQuestionnaire(View view){ 
		 	RadioGroup radioGroup = (RadioGroup) findViewById(R.id.RadioGroup01);
		 	int selectedId = radioGroup.getCheckedRadioButtonId();
		 	//Check whether the experience has been rated.
		 	if (selectedId==-1){
            Toast.makeText(getBaseContext(), "Please rate your experience.", Toast.LENGTH_LONG).show();
		 	}else {
		 	RadioButton radio = (RadioButton)findViewById(selectedId);

		    EditText editText=(EditText) findViewById(R.id.editText_comment);
		    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
		    last_questionnaire_record=confService.getApplicationName()+";"+ confService.getUserId()+";"+ getCurrentTimestamp()+";" +"Questionnaire;"+feature_name+";;"+radio.getTag().toString()+";"+purifyString(radio.getContentDescription().toString())+";"+purifyString(editText.getText().toString())+";"+"\n";
			confService.storeLog (last_questionnaire_record);
		  
		    if (isNetworkAvailable(getBaseContext())){
		    Log.d("com.bth.qoe","Internet is available");
			confService.shareQoEQoSData();   		    	
			alertDialogBuilder.setTitle(R.string.form_submitted);
			alertDialogBuilder
				.setMessage(getString(R.string.form_submitted_content))
				.setCancelable(false)
				.setNeutralButton("Back to Application",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
						dialog.cancel();
						finish();
					}	});
				alertDialogBuilder.show();  
		 	} else {		
		    	Log.d("com.bth.qoe","Internet is not available");
		    	// Start submitOnInternet thread to submit data when Internet is connected.
		 	    Thread t=new Thread(new submitOnInternet());
		 	    t.start();
		 	    
		    	alertDialogBuilder.setTitle(R.string.form_not_submitted);
				alertDialogBuilder
					.setMessage(getString(R.string.form_not_submitted_content))
					.setCancelable(false)
					.setNeutralButton("Back to Application",new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,int id) {
							dialog.cancel();
							finish();
						}	});
					alertDialogBuilder.show();  
		         }
		 	   }
		    }
	
	public void cancelQuestionnaire(View view){
		finish();
	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
    	getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    @Override
    public boolean onPrepareOptionsMenu (Menu menu) {
    	if(menu_deactive){
            menu.getItem(1).setEnabled(false);
    	}
        return true;
    	
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
       
        switch (item.getItemId()) {
        case R.id.menu_information: 
        	Intent intent_info = new Intent(MainActivity.this, AboutActivity.class);
    	    MainActivity.this.startActivity(intent_info);
        	return true;
        case R.id.menu_showData:
        	showLoggedData();
        	return true;
        case R.id.menu_preferences:
        	Intent intent_config = new Intent(MainActivity.this, PreferencesActivity.class);
    	    MainActivity.this.startActivity(intent_config);
        	return true;		
        default:
        	return super.onOptionsItemSelected(item);   
        }
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
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }
    }
    
    /**
     * Menu to show logged data
     */
    @SuppressLint("InflateParams") 
    private void showLoggedData(){   
	    LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    View ScrollView = inflater.inflate(R.layout.scroll_dialog, null, false);
	    
	    String logData=confService.readLog();
	    if (logData.equals(null) ) logData=getString(R.string.menu_showData_nocontent);
	   
	  
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
		alertDialogBuilder.setTitle(R.string.menu_showData);
		alertDialogBuilder
			.setMessage(getString(R.string.menu_showData_content)+":"+logData)
			.setCancelable(false)
			.setView(ScrollView)
			.setNeutralButton("Close",new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
					dialog.cancel();				
				}	});
			alertDialogBuilder.show();   	
 	}
    
    /**
     * @return current timestamp based on defined format
     */
    private String getCurrentTimestamp(){
    	//calculate time stamp
		SimpleDateFormat s = new SimpleDateFormat("yyyyMMddhhmmssSSS", new Locale("en","GB"));
	 	String timestamp = s.format(new Date());
	
	  return timestamp;
    	
    }
   
    private String purifyString(String str){
    	str.replaceAll(";",",");
    	return str;	
    }
    
    
    /**
     * @param context
     * @return boolean, whether Network is available
     */
    private boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();    
    } 

  
	/**
	 *  Thread to Submit the logfile when Internet is available
     * Sleep time is defined in the property file
	 *
	 */
	private class submitOnInternet implements Runnable {
  		  private int sleeptime=Integer.parseInt(confService.getProperty(getBaseContext(),"internetconnection_check_interval"));

			public void run () {

			      try {
			    	  while (!isNetworkAvailable(getBaseContext())){
			    	  Thread.sleep(sleeptime);
					  Log.d("com.bth.qoe", "Runnable: internet is not connected");
			    	  }
					  confService.shareQoEQoSData();   		    	
			    	  Log.d("com.bth.qoe", "Runnable internet is connected");
			    
			      } catch(InterruptedException ex) {
			          Thread.currentThread().interrupt();
			      }
			}
			
		}	
}
 
 

