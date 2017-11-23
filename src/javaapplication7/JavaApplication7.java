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
        String base_url = "https://maps.googleapis.com/maps/api/directions/json?";
        String origin = "origin="+"hadley,MA,US";
        String destination = "&destination="+"Umass+amherst,MA,US";
        String mode = "&mode=bicycling";
        String alternatives = "&alternatives=true";
        String key = "&key=AIzaSyDbJBCyTJBUmRSrlAOfzc4AbdjBqZgoSRU";
        String full_url = base_url+origin+destination+mode+alternatives+key;
        
        URL url = new URL(full_url);
        connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        
        BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String line;
        
        while ((line = rd.readLine()) != null) 
        {
        	//System.out.println(line);       
                result.append(line);
        }
      rd.close();
      
      //System.out.println(result.toString()); 
      //int responseCode = connection.getResponseCode();
      //System.out.println("Response Code : " + responseCode);
      JSONObject jsonObject = new JSONObject(result.toString());
      //System.out.println(jsonObject.get("routes").toString());      
      
      JSONArray resultsArray=jsonObject.getJSONArray("routes");
      System.out.println(resultsArray.length());
      for ( int i=0;i<resultsArray.length();i++){
         
    	  JSONObject routeObj=(JSONObject)resultsArray.get(i);
          JSONArray legs = routeObj.getJSONArray("legs");
          
          for ( int j=0;j<legs.length();j++){
              JSONObject legObj=(JSONObject)legs.get(j);
              JSONArray steps = legObj.getJSONArray("steps");
              
              for ( int s=0;s<steps.length();s++){
                  JSONObject stepObj=(JSONObject)steps.get(s);
                  JSONObject start_location = (JSONObject)stepObj.get("start_location");
                  Double latitude = start_location.getDouble("lat");
                  Double longitude = start_location.getDouble("lng");
                  System.out.println(latitude.toString());
              }

          }
          
    	  
          //JSONObject legs = (JSONObject) event.get("legs");
    	  //JSONObject steps = (JSONObject) legs.get("steps");
    	  //JSONObject instructions = (JSONObject) steps.get("html_instructions");
    	  //Double latitude = (Double)northEastObj.get("lat");
          
      }
      
      
           
    }
}
