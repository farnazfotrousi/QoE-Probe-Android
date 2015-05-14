package com.bth.qoe.service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.Context;
import android.os.Looper;
import android.util.Log;

public class ShareDataService{
	 
	private JSONObject json;
	ConfigurationService configService=new ConfigurationService();	   

	
	protected void shareQoEQoSData(Context context){
		String qoeqos_data=readLog(context);
		if (qoeqos_data!=null){
		Log.d("com.bth.qoe","The following data has been logged: \n"+qoeqos_data);

		resetLog(context);
		JSONObject jsonObj=createJson(qoeqos_data);
		sendJson(context,jsonObj);	
		}
}
	
	protected String readLog(Context context){	
		String read_data=null;	
		 try {
			    FileInputStream fis =  context.openFileInput(configService.getProperty(context, "filename"));
			    byte[] dataArray = new byte[fis.available()];
			    if (fis.read(dataArray) != -1) {
			     read_data = new String(dataArray);
			    }
			    fis.close();
	           return read_data;

			   } catch (FileNotFoundException e) {
			    e.printStackTrace();
			    return null;
			   } catch (IOException e) {

			    e.printStackTrace();
			    return null;
			   }
	 }
	
	private boolean resetLog(Context context){
		try {
			  FileOutputStream outputStream = context.openFileOutput(configService.getProperty(context, "filename"), Context.MODE_PRIVATE);
			  outputStream.write("".getBytes());
			  outputStream.close();
			  return true;
			  
			} catch (Exception e) {
			  e.printStackTrace();
			  return false;
			}
      }
		
     private JSONObject createJson(String data){
	     
	   try {
           JSONObject jsonObject = new JSONObject();
           jsonObject.put("qoeqos", data);
           return jsonObject;
       } catch (JSONException e) {
           e.printStackTrace();
           return null;
       }
	   
   }
 
   private void sendJson(final Context context,JSONObject jsonObj) {
	   json=jsonObj;
       Thread t = new Thread() {
          
           public void run() {
        	   Looper.prepare();          
               try {
            	   HttpClient httpClient = new DefaultHttpClient();
            	   int webservice_timeout=Integer.parseInt(configService.getProperty(context, "webservice_connection_timeout"));
                   String webservice_url=configService.getProperty(context, "webservice_url");
            	   HttpConnectionParams.setConnectionTimeout(httpClient.getParams(), webservice_timeout ); //Timeout Limit
                   Log.d("com.bth.qoe","Web service URL is:"+ webservice_url);

                   HttpPost post = new HttpPost(webservice_url);
                   StringEntity se = new StringEntity(json.toString());  
                   se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
                   post.setEntity(se);
                   HttpResponse response = httpClient.execute(post);
                   if(response!=null){                	  
                	   EntityUtils.toString(response.getEntity());
                   }
                   else Log.d("com.bth.qoe","No response was received from the webservice.");

                  
               } catch(Exception e) {
                   e.printStackTrace();
               }
               Looper.loop();
           }
       };

       t.start();      
   }
   

}
