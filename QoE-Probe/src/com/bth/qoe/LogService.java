package com.bth.qoe;

import java.util.Random;
import com.bth.qoe.activity.MainActivity;
import com.bth.qoe.service.ConfigurationService;
import com.bth.qoe.service.ConfigurationService.ConfBinder;
import android.annotation.SuppressLint;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;

/**
 * Handle logging messages from the library integrated in the third-parties' application
 * Log requested data in the local logfile
 */
public class LogService extends Service {

	   /** Command to the service to display a message */
	  static final int LOG_START_FEATURE = 0;
	  static final int LOG_USER_INPUT = 1;
	  static final int LOG_APPLICATION_OUTPUT = 2;
	  static final int LOG_FEATURE_COMPLETED = 3;
	  static final int FIRE_QUESTIONNAIRE=4;
	  static final int LIKELIHOOD = 5;
	  static final int INTERVAL = 6;
	  static final int ACCEPTED_TERMS = 7;
	  public Handler handler = new Handler();
	  ConfigurationService confService;
	  boolean cBound = false;
	  private boolean registeringApp=true;


	    /**
	     * Handler of incoming messages from clients.
	     */
	   
	   @SuppressLint("HandlerLeak") 
	   class IncomingHandler extends Handler {
	        @Override
	        public void handleMessage(Message msg) {

	            switch (msg.what) {	  
	              
	                case LOG_START_FEATURE:
	                	//Terms&conditions has not been accepted
	                	/*if (!confService.getAcceptRule()){
	                		//first usage of application	
	                		 if (confService.getFirstRunApp()){
			            	    Intent intent= new Intent (getBaseContext(),TermsAndConditionsActivity.class);
			            	    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
			            	    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
							    intent.putExtra("termstype", "application");
								getApplication().startActivity(intent);
		                	} 			
	                	}*/
	                	//Terms&conditions has been accepted
	                	if (confService.getAcceptRule()){
	                		if (registeringApp){
	                        	Log.d("com.bth.qoe","Application is connected with the agreed informed consent.");
	 			     	     	Log.d("com.bth.qoe","Start the thread for the first registeration");
	 			     	     	handler.postDelayed(dataSubmit, confService.getDataCollectionInterval()*86400*1000);
	 			     	     	
	                        	registeringApp=false;
	                		}
	                	  // Start features is logged	
	                	   logUserActions(msg.getData().getString("param"));
	    	               Log.d("com.bth.qoe","Feature Starting command is logged.");	
	    	               filterParams(msg.getData().getString("param"));
	                	}	                	
	                    break;
	                case LOG_USER_INPUT:
	                	if (confService.getAcceptRule()){
		     	     	logUserActions(msg.getData().getString("param"));
		     	     	Log.d("com.bth.qoe","User Input command is logged.");
	                	}
		                break;
	                case LOG_APPLICATION_OUTPUT:
	                	if (confService.getAcceptRule()){
		     	     	logUserActions(msg.getData().getString("param"));
		     	     	Log.d("com.bth.qoe","Application Output command is being logged.");
	                	}
		                break;
	                case LOG_FEATURE_COMPLETED:
	                	if (confService.getAcceptRule()){
			     	     	logUserActions(msg.getData().getString("param"));
			     	     	Log.d("com.bth.qoe","Completing Feature command is logged.");
		                	}
			                break;
	                	
	                case FIRE_QUESTIONNAIRE:	
	                	if (confService.getAcceptRule()){
		                	Log.d("com.bth.qoe","Fire Questionnaire is logged.");
		                	String feature_complete_param=msg.getData().getString("param");
			     	     	logUserActions(msg.getData().getString("param"));
			     	     	String feature_name=feature_complete_param.substring(nthOccurrence(feature_complete_param, ';', 3)+1, nthOccurrence(feature_complete_param, ';', 4));
			     	     	
			     	     	boolean act=RandomGenerator(confService.getQuestionnaireLikelihood());
			     	     	Log.d("com.bth.qoe","Random generator result to show the questionnaire:"+act+" with likelihod:"+confService.getQuestionnaireLikelihood());
			     	     	if (act){
			     	     	Intent intent= new Intent (getBaseContext(),MainActivity.class);
			     	     	intent.putExtra("RUNETYPE", "connected");
			     	     	intent.putExtra("FEATURE_NAME", feature_name);
			     	     	Log.d("com.bth.qoe","Input extra connected is assigned");
			     	     	
							intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
			     	     	intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			     	    
							getApplication().startActivity(intent);
			     	     	}
	                	}
		                break;
	                case LIKELIHOOD:
	                	confService.setQuestionnaireLikelihood(Integer.parseInt(msg.getData().getString("param")));
	                	Log.d("com.bth.qoe","Questionnaire Likelihood is configured.");
	            		break;
	            	case INTERVAL:
	            		confService.setDataCollectionInterval(Integer.parseInt(msg.getData().getString("param")));
	                	Log.d("com.bth.qoe","Data Submission Interval is configured.");

	            		break;
	            	case ACCEPTED_TERMS:
	            		confService.setAccceptRule(Boolean.parseBoolean(msg.getData().getString("param")));
	                	Log.d("com.bth.qoe","Terms&condition acceptance is configured.");

	            		break;
	                default:
	                    super.handleMessage(msg);
	            }
	        }
	    }
	   
	   
	   	 /**
	     * Target to publish for clients to send messages to IncomingHandler.
	     */
	    final Messenger mMessenger = new Messenger(new IncomingHandler());

	    /**
	     * When binding to the service, we return an interface to our messenger
	     * for sending messages to the service.
	     */
	    @Override
	    public IBinder onBind(Intent intent) {
	    	Intent intent_conf = new Intent(this, ConfigurationService.class);
	        bindService(intent_conf, mConnection, Context.BIND_AUTO_CREATE);
	    	 return mMessenger.getBinder();
	    }

	    @Override
	    public void onDestroy() {
	        super.onDestroy();
	        if (cBound) {
	            unbindService(mConnection);
	            cBound = false;
               
	        }
	        //Remove the handler for submitting data on Interval
	        Log.d("com.bth.qoe","Stop the handler for submitting data on Intervel on distroy");

	        
	    }
	    
	    private ServiceConnection mConnection = new ServiceConnection() {
	        @Override
	        public void onServiceConnected(ComponentName className, IBinder service) {
	            //bound to LocalService, cast the IBinder and get LocalService instance
				ConfBinder binder =(ConfBinder) service;
				confService = binder.getService();
				

				
	            cBound = true;      
	        }

	        @Override
	        public void onServiceDisconnected(ComponentName arg0) {
	        	cBound = false;
	        	}
	    	};
	    	
	    	/**
	    	 * @param str
	    	 * Configure application_name and user_id
	    	 */
	    	private void filterParams(String str){
	  		  String app="";
	  		  String user_id="";
	  		  int index1=str.indexOf(';');
	  		  int index2=str.indexOf(';', index1+1);
	  		  app=str.substring(0,index1);
	  		  user_id=str.substring(index1+1,index2);
	  		  confService.setApplicationName(app);
	  		  confService.setUserId(user_id);
	  	   }
	    	
	    	private static int nthOccurrence(String str, char c, int n) {
	    	    int pos = str.indexOf(c, 0);
	    	    while (n-- > 0 && pos != -1)
	    	        pos = str.indexOf(c, pos+1);
	    	    return pos;
	    	}
	
		/**
		 * @param data
		 * Write the record in the log-file
		 */
		private void logUserActions(String data){
			confService.storeLog(data);
		}

	   /**
	 * @param likelihood
	 * @return generate a random boolean
	 */
	private boolean RandomGenerator(float likelihood){
		   Random rand = new Random(System.currentTimeMillis()); 
		   int randInt=rand.nextInt((int)(100/likelihood));
		   Log.d("com.bth.qoe", "Generated random number:"+ randInt);
		   boolean val = randInt==0;
		   Log.d("com.bth.qoe", "Random generator returns:"+ val +". Ture will fire up the questionnaire");
		   return val;
	   }
	   
	public Runnable dataSubmit = new Runnable() {
			   @Override
			   public void run() {
				   Log.d("com.bth.qoe", "Runnable data submit thread is run.");
				   confService.shareQoEQoSData();
			       handler.removeCallbacks(dataSubmit); 
			   }
			};	
	}

