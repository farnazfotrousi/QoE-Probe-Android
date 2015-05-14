package com.bth.qoe.service;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.os.Binder;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;
import android.util.Xml;

import org.w3c.dom.Node;


/**
 * The service to manage configuration and support QoE for multi-application
 *
 */
public class ConfigurationService extends Service {
	final String configurationFile="applicationsConf";   

	SharedPreferences prefs;
	private final IBinder mBinder = new ConfBinder();
	private String[] questions; 
	private String application;
	private String user_id;
	
	
    /**
     * Class used for the client Binder.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with IPC.
     */
    public class ConfBinder extends Binder {
    	public ConfigurationService getService() {
            // Return this instance of LocalService so clients can call public methods
    		prefs=PreferenceManager.getDefaultSharedPreferences(getBaseContext());
            return ConfigurationService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

		public void setQoEQuestions(String[] questions){
		     this.questions=questions;
		}
		public String[] getQoEQuestions(){
			return questions;
		}
		public void setApplicationName(String application){
		  this.application=application;
		}
		public String getApplicationName(){
		return application;
		}
		public void setUserId(String user_id){
			  this.user_id=user_id;
			}
			public String getUserId(){
			return user_id;
			}
		public void setQuestionnaireLikelihood(float likelihood){
		    prefs.edit().putFloat ("likelihood", likelihood).commit();
		
		}
		public float getQuestionnaireLikelihood(){
		return prefs.getFloat("likelihood", 1F);
		
		}
		public void setDataCollectionInterval(int timespan){
			 prefs.edit().putInt("timespan", Integer.valueOf(timespan)).commit();
		}
		public int getDataCollectionInterval(){
			return prefs.getInt("timespan", Integer.valueOf(1));
		}
		
		public void setAccceptRule(boolean acceptRule){
			prefs.edit().putBoolean("collectdata", Boolean.valueOf(acceptRule)).commit();
		}
		
		public boolean getAcceptRule(){
			return prefs.getBoolean("collectdata", true);
			
		}
		public void setFirstRunApp(boolean firstRunApp){
			prefs.edit().putBoolean("firstRunApp", Boolean.valueOf(firstRunApp)).commit();
		}
		
		public boolean getFirstRunApp(){
			return prefs.getBoolean("firstRunApp", true);
			
		}
		
		
		//public void setApplication(String application){
			
			//PreferenceCategory appCat = (PreferenceCategory)findPreference("app");
		//}
		/*public string getApplication(){
			return "test";
		}*/
		
		public String getProperty(Context context, String key){
			   
			AssetManager assetManager = context.getApplicationContext().getResources().getAssets();
	   
	   try {
	       InputStream inputStream = assetManager.open("system.properties");
	       Properties properties = new Properties();
	       properties.load(inputStream);
	       String value=properties.getProperty(key);
	       Log.d("com.bth.qoe","The value that is read from property file is:"+ value);
	       return (value);
	   } catch (IOException e) {
	       System.err.println("Failed to open system property file");
	       e.printStackTrace();
	       return null;
	   }
		
	}
		public void storeLog(String data){
			FileOutputStream outputStream;
			try {
			 outputStream = openFileOutput(getProperty(getBaseContext(),"filename"), Context.MODE_APPEND);
			 outputStream.write(data.getBytes());
			 outputStream.close();			  			  
			} catch (Exception e) {
			  e.printStackTrace();
			}
			
		}
		
		public String readLog(){
			ShareDataService shareSrv=new ShareDataService();
			return (shareSrv.readLog(getBaseContext()));
		}
		
		public void shareQoEQoSData(){
			ShareDataService shareSrv=new ShareDataService();
			shareSrv.shareQoEQoSData(getBaseContext());
		}
		
		
		
		public void registerApplication(String application){
	       try{
	
	    	FileOutputStream fileos= getApplicationContext().openFileOutput(configurationFile, Context.MODE_PRIVATE);   
		    XmlSerializer xmlSerializer = Xml.newSerializer();
		    StringWriter writer = new StringWriter();
		 
		    xmlSerializer.setOutput(writer);
		    // start DOCUMENT
		    xmlSerializer.startDocument("UTF-8", true);
		    // open tag: <PreferenceCategory>
		    xmlSerializer.startTag("","application" );
		    xmlSerializer.text(application);
		    // open tag: <collectData>
		    xmlSerializer.startTag("", "collectData");
		    xmlSerializer.text("false");
		    xmlSerializer.endTag("", "collectData");
		   // open tag: <likelihood>
		    xmlSerializer.startTag("", "likelihood");
		    xmlSerializer.text("100");
		    xmlSerializer.endTag("", "likelihood");
		    // open tag: <timespan>
		    xmlSerializer.startTag("", "timespan");
		    xmlSerializer.text("15");
		    xmlSerializer.endTag("", "timespan");
		    // open tag: <firstRunApp>
		    xmlSerializer.startTag("", "firstRunApp");
		    xmlSerializer.text("true");
		    xmlSerializer.endTag("", "firstRunApp");
		 // open tag: <startingApp>
		    xmlSerializer.startTag("", "startingApp");
		    xmlSerializer.text("true");
		    xmlSerializer.endTag("", "startingApp");
		    //end tag:<Application>
		    xmlSerializer.endTag("","application" );
		   
		    // end DOCUMENT
		    xmlSerializer.endDocument();
		    String dataWrite = writer.toString();
		    fileos.write(dataWrite.getBytes());
		    fileos.close();
		    readConfiguration(application);
	       }
	       catch (FileNotFoundException e) {
	           e.printStackTrace();
	       }
	       catch (IllegalArgumentException e) {
	           e.printStackTrace();
	       }
	       catch (IllegalStateException e) {
	           e.printStackTrace();
	       }
	       catch (IOException e) {
	           e.printStackTrace();
	       }
		 
		}
	
		public void readConfiguration(String application){
			InputStream stream=readXmlFile();
	     	
			try {
			    if (stream!=null){		
				    XmlPullParser parser = Xml.newPullParser();
				    int eventType = parser.getEventType();
			        String currentApp = null;
		            parser.setInput(stream, null);                 		    
				    
				    while (eventType != XmlPullParser.END_DOCUMENT){
			            String name = null;
			            switch (eventType){
			                case XmlPullParser.START_DOCUMENT:
			                    break;
			                case XmlPullParser.START_TAG:
			                    name = parser.getName();				            
			                    if (name.equals("application")){
			                        currentApp=application;
			                    } else if (currentApp != null){
			                        if (name.equals("collectData")){	
				                		setAccceptRule(Boolean.valueOf(parser.nextText()));
			                        } else if (name.equals("likelihood")){
			                        	setQuestionnaireLikelihood(Integer.valueOf(parser.nextText()));
			                        } else if (name.equals("timespan")){
			                        	setDataCollectionInterval(Integer.parseInt(parser.nextText()));
			                        } else if (name.equals("firstRunApp")){
			                        	setFirstRunApp(Boolean.valueOf(parser.nextText()));
			                        } 
			                        
			                    }
			                    break;
			                case XmlPullParser.END_TAG:
			            }
			            eventType = parser.next();
			        }
			    }  
	        }
			catch (FileNotFoundException e) {
			    e.printStackTrace();
			}
			catch (IOException e1) {
			    e1.printStackTrace();
			}
			catch (XmlPullParserException e2) {
			    e2.printStackTrace();
			}			

		}
		public void updateConfiguration(String application, String node, String value){
         InputStream stream=readXmlFile();
     	 try {	  
         if (stream!=null){				
				DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder docBuilder;
			    docBuilder = docFactory.newDocumentBuilder();
				org.w3c.dom.Document doc = docBuilder.parse(stream);
				Node app = doc.getFirstChild();
		 		Node element = doc.getElementsByTagName(node).item(0);
				Log.d("com.bth.qoe", "name:"+element.getNodeName()+element.getTextContent());
				element.setTextContent(value);
				Log.d("com.bth.qoe", "name:"+app.getNodeName()+element.getTextContent());
				}
			   
			} catch (ParserConfigurationException e) {
					e.printStackTrace();
			}catch (SAXException e) {
					e.printStackTrace();
			} catch (IOException e) {
					e.printStackTrace();
				}	
			
		}
		
		private InputStream readXmlFile(){
			
		try{
		 	FileInputStream fis = getApplicationContext().openFileInput(configurationFile);
		    InputStreamReader isr = new InputStreamReader(fis);
		    char[] inputBuffer = new char[fis.available()];
		    isr.read(inputBuffer);
		    String data = new String(inputBuffer);
		    isr.close();
		    fis.close();
		    Log.d("com.bth.qoe","file has been read");
		    InputStream stream = new ByteArrayInputStream(data.getBytes());
		    return stream;
		}
		  
		catch (FileNotFoundException e) {
		    e.printStackTrace();
		    return null;
		}
		catch (IOException e1) {
		    e1.printStackTrace();
		    return null;
		}
	}			

}
