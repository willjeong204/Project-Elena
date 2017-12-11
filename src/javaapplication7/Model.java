package javaapplication7;

import com.opencsv.CSVReader;

import org.jdom2.JDOMException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xml.sax.SAXException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Model extends java.util.Observable {
	private String source;
	private String destination;
	static HashMap<String, ArrayList<String>> adjMatrix;
	static HashMap<String,Integer> indexIDMap = new HashMap<>();
	ArrayList<NodeObject> mapNodes;
	ArrayList<NodeObject> routeNodes;
        float max_elevation;
        ArrayList<String> final_route = new ArrayList<String>();
        
	public Model() throws JDOMException, SAXException, IOException, Exception{

		adjMatrix = readCSV();

		OsmParsing osm = new OsmParsing();
		mapNodes = osm.parser();
		indexIDMap = osm.indexIDMap;
                max_elevation = osm.max_elevation;

	}


	public void setSource(String src) {
		source = src;
	}
	public String getSource() {
		return source;
	}
	public void setDestination(String dest) {
		destination = dest;
	}
	public String getDestination() {
		return destination;
	}

	public ArrayList<Integer> getNodeId(Google_Map_UI mapView) throws IOException, JSONException{
		//Google_Map_UI ui =new Google_Map_UI();
		double srcLat = mapView.getsrcLatLng().getLat();//
		double srcLng = mapView.getsrcLatLng().getLng();
		System.out.println(srcLat);
		System.out.println(srcLng);
		double dstLat = 42.3892763;//mapView.destLatLng.getLat();
		double dstLng =-72.5295258; //mapView.destLatLng.getLng() ;
		System.out.println(dstLat);
		System.out.println(dstLng);
		ArrayList<Integer> srcDestIndex = new ArrayList<>();
		int srcIndex = getId(srcLat,srcLng);
		int dstIndex = getId(dstLat,dstLng);
		srcDestIndex.add(srcIndex);
		srcDestIndex.add(dstIndex);
		return srcDestIndex;
		
	}
	private int getId(Double lat,Double lng) throws IOException, JSONException {
		// TODO Auto-generated method stub
		//http://router.project-osrm.org/nearest/v1/driving/13.388860,52.517037?number=3&bearings=0,20
		
		List<String> testArray = new ArrayList<String>();
        HttpURLConnection connection = null;
		 StringBuilder result = new StringBuilder();
	        String base_url = "http://router.project-osrm.org/nearest/v1/driving/";
	        String location = lng + "," +lat;
	        String number = "?number=3";
	        String bearings = "&bearings=0,20";
	        String full_url = base_url+location+number+bearings;
	        System.out.println(full_url);
	        URL url = new URL(full_url);
	        //URL url = new URL("https://maps.googleapis.com/maps/api/directions/json?origin=hadley,MA,US&destination=Umass+amherst,MA,US&mode=bicycling&alternatives=true&key=AIzaSyDbJBCyTJBUmRSrlAOfzc4AbdjBqZgoSRU");
	        connection = (HttpURLConnection) url.openConnection();
	        connection.setRequestMethod("GET");
	        
	        BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));
	        String line;
	        
	        while ((line = rd.readLine()) != null) 
	        {
	        	System.out.println(line);       
	                result.append(line);
	        }
	      rd.close();
	      
	      HashMap<String,String> srcDest = new HashMap<>();
	      JSONObject jsonObject = new JSONObject(result.toString());
	      
	      JSONArray resultsArray=jsonObject.getJSONArray("waypoints");
	      String x=null;
	      for ( int i=0;i<resultsArray.length();i++){
	    	  JSONObject routeObj=(JSONObject)resultsArray.get(i);
	          JSONArray legs = routeObj.getJSONArray("nodes");
	          
	          for ( int j=0;j<legs.length();j++){
	              String nodeID=(String)legs.get(j).toString();
	                  System.out.println("here"+nodeID);
	                  if (indexIDMap.containsKey(nodeID)){
	                  int index = indexIDMap.get(nodeID);
	                  System.out.println("index"+index);
	          				return index;
	                  }
	          			}
	                  }
	          
		return -1;
	}


	private static HashMap<String, ArrayList<String>> readCSV() {
		CSVReader csvReader = null;
		HashMap<String,ArrayList<String>> adjMatrix = new HashMap<>();
		try
		{
			csvReader = new CSVReader(new FileReader("C:\\Users\\shrut\\Documents\\SE\\Project\\Project-Elena\\adjacency.csv"),',','"');
			String[] adjList = null;
			while((adjList = csvReader.readNext())!=null)
			{
				ArrayList<String> lines = new ArrayList<String>();
				for(int i =1;i<adjList.length;i++){
					lines.add(adjList[i]);
				}
				adjMatrix.put(adjList[0], lines);
			}
		}
		catch(Exception ee)
		{
			ee.printStackTrace();
		}
		finally
		{
			try
			{
				csvReader.close();
			}
			catch(Exception ee)
			{
				ee.printStackTrace();
			}
		}
		return adjMatrix;

	}

}