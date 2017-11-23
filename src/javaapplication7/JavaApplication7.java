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

/**
 *
 * @author shrut
 */
public class JavaApplication7 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws MalformedURLException, IOException {
        // TODO code application logic here
        HttpURLConnection connection = null;
        URL url = new URL("https://maps.googleapis.com/maps/api/geocode/json?address=1600+Amphitheatre+Parkway,+Mountain+View,+CA&key=AIzaSyDdmifSk9Ikxup4jhtnZ_dtglMK8KDYskA");
        connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        
        BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String line;
        while ((line = rd.readLine()) != null) 
        {
            System.out.println(line);        
        }
      rd.close();
              
    }
}
