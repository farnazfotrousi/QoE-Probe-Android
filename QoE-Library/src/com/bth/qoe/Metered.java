package com.bth.qoe;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.provider.Settings.Secure;
import android.util.Log;
import android.view.View;



public class Metered {
	
	/** Messenger for communicating with the service. */
    Messenger mService = null;
    /** Constants that each provides a command to the service to display a message */

    private final int LOG_START_FEATURE = 0;
    private final int LOG_USER_INPUT = 1;
    private final int LOG_APPLICATION_OUTPUT = 2;
    private final int LOG_FEATURE_COMPLETED = 3;
    private final int FIRE_QUESTIONNAIRE=4;
    private final int LIKELIHOOD = 5;
	private final int INTERVAL = 6;
	private final int ACCEPTED_TERMS = 7;

	
	
	/** application_name -- The name of android application to be recognized in the log file*/
	private String application_name ;
	/** application_name -- The name of android feature to be recognized in the log file*/
	private String feature_name ;
	/** user_id -- the unique mobile id that is referred as the user id.*/
	private String user_id;
    /** mBound -- Flag indicating whether we have called bind on the service. */
    private boolean mBound;
    /** mConnection -- Connection of the Activity */
    private ActivityServiceConnection mConnection=new ActivityServiceConnection();


   private static Metered instance = new Metered( );
   
   /** 
    * A private Constructor prevents any other 
    * class from instantiating.
    */
   private Metered(){ }
   
   /**
    * @return a static instance of the class
    */
   public static Metered getInstance( ) {
      return instance;
   }
   
    /**
     * Start the service by creating an intent parameter and bind the activity to the right service
     * @param context -- the reference to the activity context
     */
    public void startQoEService(Context context){
    Intent mIntent=new Intent();
    mIntent.setComponent(new ComponentName("com.bth.qoe", "com.bth.qoe.LogService"));
	context.bindService(mIntent, mConnection,Context.BIND_AUTO_CREATE);
    }
    
	/**
	 * Initialize the connection of the probe with an application. 
	 */
    	
	public class ActivityServiceConnection implements ServiceConnection{
        @Override
        public void onServiceConnected(ComponentName className,
                IBinder service) {
        	mService = new Messenger(service);
            mBound = true;
            Log.d("com.bth.qoe","QoE Service is connected.");

        }

		@Override
		public void onServiceDisconnected(ComponentName name) {
			mService = null;
	        mBound = false;
	        Log.d("com.bth.qoe","QoE Service is disconnected.");
			
		}
  };
  /**
   * Stop the service by unbinding the activity 
   * @param context -- the reference to the activity context
   */
	public void stopQoEService(Context context){
        context.unbindService(mConnection);
	}
	
    /** 
     * Register application to identify which application is using the QoE application
     * @param context -- Context
     * @param content -- ContentResolver
     * @param application -- name of the application
     */
	public void registerApplication(Context context, ContentResolver content ){
    	application_name=context.getPackageName();
		registerApplication(content,application_name); 
	} 
    
	/** 
     * Register application to identify which application is using the QoE application.
     * The method calculate the mobile_id as the user_id
     * @param content -- ContentResolver
     * @param application -- name of the application
     */
	
	public void registerApplication(ContentResolver content, String application){
		application_name=application;
	    user_id=Secure.getString(content,Secure.ANDROID_ID);
	}
    
	
	/**
	 * Log starting of the feature
	 * @param feature_name -- the name of feature that is going to be started
	 */
	public void logFeatureStart(String feature_name){
		this.feature_name=feature_name;
		 Log.d("com.bth.qoe","Request to log Starting Feature "+ feature_name +" is sent.");
		 sendMessage(LOG_START_FEATURE, application_name+';'+ user_id+';'+ getCurrentTimestamp()+';' +"Starting Feature"+';'+ feature_name+";;;;;"+'\n');   
	}
	
	 /**
     * Log starting of the feature
     * @param feature_name -- name of the feature 
     * @param view -- the view of the relevant activity
     */
	public void logFeatureStart(String feature_name, View view ){
		this.feature_name=feature_name;
		 Log.d("com.bth.qoe","Request to log Staring Feature "+ feature_name +" is sent.");
		 //sendMessage(LOG_START_FEATURE, user_id+';'+application_name+';'+feature_name+';');
		 //sendMessage(LOG_START_FEATURE, application_name+';'+ user_id+';'+ getCurrentTimestamp()+';' +"Starting Feature"+';'+ feature_name+";;;;;"+'\n');   

		 ButtonListener btnlsr=new ButtonListener();
		 
		 /*ViewGroup viewgrp=(ViewGroup)view;
		 Log.d("com.bth.qoe","View id would be "+ viewgrp.getChildCount());

				
				//getWindow().getDecorView().findViewById(android.R.id.content);
		for (int i=0; i<viewgrp.getChildCount(); i++){
			
			 Log.d("com.bth.qoe","Child is: "+ viewgrp.getChildAt(i).getId());
		}
		*/
		 btnlsr.onClick(view);
		
		    view.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
				     Log.d("com.bth.qoe","A click occured.");

	                //Toast.makeText(getBaseContext(), "Button Clicked", Toast.LENGTH_SHORT).show();
    		            }
    		 
   		        });
		    
	  //   Log.d("com.bth.qoe","View id would be "+ view.getId());
	 
		 
	}
	
	class ButtonListener implements android.view.View.OnClickListener {
	    public void onClick(View v) {
	    	Log.d("com.bth.qoe", "button id is:"+v.getId());
	       //View p = (View) v.getRootView(); 
	       //ViewGroup viewgrp=(ViewGroup)p;
	        //if (p != null) {
	        //    
	       // }
	    }
	}
    /**
     * Log user actions
     * @param action_name -- name of the user action (e.g. "submit login form")
     */
    public void logUserInput(String action_name){
  	   Log.d("com.bth.qoe","Request to log User Input event "+ action_name +" is sent.");	
		 sendMessage(LOG_USER_INPUT, application_name+';'+ user_id+';'+ getCurrentTimestamp()+';' +"User Input"+';'+ feature_name+';'+purifyString(action_name)+";;;;"+'\n');   
    }
    	
    /**
     * Log application output
     * @param action_name -- name of the application output (e.g. Display error message )
     */
    public void logApplicationOutput(String action_name){
   	   Log.d("com.bth.qoe","Request to log Application output event "+ action_name +"is sent.");	
		 sendMessage(LOG_APPLICATION_OUTPUT, application_name+';'+ user_id+';'+ getCurrentTimestamp()+';' +"Application Output"+';'+ feature_name+';'+purifyString(action_name)+";;;;"+'\n');   

    }
    
    /**
     * Log completing feature
     * @param feature_name -- name of the feature
     */
    public void logFeatureCompleted(String feature_name){
  	   Log.d("com.bth.qoe","Request to log Completing Feature "+ feature_name +"is sent.");
	   sendMessage(LOG_FEATURE_COMPLETED, application_name+';'+ user_id+';'+ getCurrentTimestamp()+';' +"Completing Feature"+';'+ feature_name+";;;;;"+'\n');   	   
    }
    
    /**
     * Log the Fire questionnaire for a feature
     * @param feature_name -- name of the feature
     */
    public void logFireQuestionnaire(String feature_name ){
 	   Log.d("com.bth.qoe","Request to log Fire Questionnaire for Feature "+ feature_name +"is sent.");
 	   Thread t = new Thread(new MessageSender());
	   t.start();
	   }
    /**
	 * Delay for sending the completion of feature 
	 */
	private class MessageSender implements Runnable {
		public void run () {
		 // 0.5 second delay
		      try {
		          Thread.sleep(500);  
		   	      sendMessage(FIRE_QUESTIONNAIRE, application_name+';'+ user_id+';'+ getCurrentTimestamp()+';' +"Fire Questionnaire"+';'+ feature_name+";;;;;"+'\n');   	   
		      } catch(InterruptedException ex) {
		          Thread.currentThread().interrupt();
		      }
		}
	}
    
    /** 
     * @param likelihood -- the probability that a QoE questionnaire will be fired. This probability can be set in range of 0 to 100. 
     * As an example, if the likelihood is set to 20, it means that the probability of firing the questionnaire in completion of the feature use would be 20 percent. 
     * The user can set it through the preferences menu, but it can be also implemented.
     */
    public void setQuestionnaireLikelihood(int likelihood){	
    	sendMessage(LIKELIHOOD, ""+likelihood);
		Log.d("com.bth.qoe","Likelihood is set to "+ likelihood);

    }
	
    /**
     * @param interval -- configures the maximal time-span to wait before the user is requested to share QoE and QoS data with the QoE/QoS back-end application.
     * When the end-users are not interested to submit the questionnaire, the collected data on the mobile phone will be submitted automatically and the log file in the mobile device will be reset.
     * The default value for this parameter is 15 days, which can be extended to 90 days as well. 
     * This parameter is configured by the end-user as well as by a code implementation.
     */
    public void setDataCollectionInterval(int interval){
    	sendMessage(INTERVAL, ""+interval); 
		Log.d("com.bth.qoe","Data collection interval is set to "+ interval);

	}

    /**
     * To perform QoE/QoS data sharing within the trusted zone, the informed consent for data sharing should be accepted.
     * This acceptance or rejection can be also done by code. 
     * @param accepted_terms -- true of the informed consent is accepted, otherwise false.
     */
    public void setAccceptRule(boolean accepted_terms){
    	sendMessage(ACCEPTED_TERMS, ""+accepted_terms);
		  Log.d("com.bth.qoe","Acceptance of terms&condition is set to "+ accepted_terms);

	}
    
    /**
     * Send messages to the QoE service
     * @param message -- message Id that have been defined as constants
     * @param param -- the content of the message
     */
    private void sendMessage(int message, String param){
  	   if (!mBound) return;
         // Create and send a message to the service, using a supported 'what' value
         Message msg = Message.obtain(null, message, 0, 0);
         Bundle bundle=new Bundle();
         bundle.putString("param", param);
         msg.setData(bundle);
         try {
             mService.send(msg);
         } catch (RemoteException e) {
             e.printStackTrace();
  	     
         }	   
 	} 
    /**
     * Calculate the current time stamp
     */ 
    private String getCurrentTimestamp(){
    	//calculate time stamp
		SimpleDateFormat s = new SimpleDateFormat("yyyyMMddhhmmssSSS", new Locale("en","GB"));
	 	String timestamp = s.format(new Date());
	
	  return timestamp;
    	
    }
    /**
     * Remove ; character from the string to make it readable in logfile
     * @param str -- the string to be checked
     */ 
    private String purifyString(String str){
    	str.replaceAll(";",",");
    	return str;
    	
    }
}