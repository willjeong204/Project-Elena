/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplication7;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.Map;



/**
 *
 * @author shrut
 */
public class JavaApplication7 {

    /**
     * @param args the command line arguments
     * @throws JSONException 
     */
    public static void main(String[] args) throws MalformedURLException, IOException, JSONException {
        // TODO code application logic here
        HttpURLConnection connection = null;
        StringBuilder result = new StringBuilder();
        URL url = new URL("https://maps.googleapis.com/maps/api/geocode/json?address=1600+Amphitheatre+Parkway,+Mountain+View,+CA&key=AIzaSyDdmifSk9Ikxup4jhtnZ_dtglMK8KDYskA");
        connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        
        BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String line;
        while ((line = rd.readLine()) != null) 
        {
        	result.append(line);       
        }
      rd.close();
      System.out.println(result.toString()); 
      int responseCode = connection.getResponseCode();
      System.out.println("Response Code : " + responseCode);
      JSONObject jsonObject = new JSONObject(result.toString());
      System.out.println(jsonObject.get("results").toString());      
      
      JSONArray resultsArray=jsonObject.getJSONArray("results");
      for ( int i=0;i<resultsArray.length();i++){
    	  JSONObject event=(JSONObject)resultsArray.get(i);
    	  JSONObject geoObj = (JSONObject) event.get("geometry");
    	  JSONObject boundsObj = (JSONObject) geoObj.get("bounds");
    	  JSONObject northEastObj = (JSONObject) boundsObj.get("northeast");
    	  Double latitude = (Double)northEastObj.get("lat");
          System.out.println(latitude.toString());
      }
      
      
           
    }
}
