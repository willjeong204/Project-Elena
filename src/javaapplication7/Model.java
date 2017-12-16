package javaapplication7;

import com.opencsv.CSVReader;
import org.jdom2.JDOMException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xml.sax.SAXException;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Model extends java.util.Observable {

    private String source;
    private String destination;
    private String apikey;
    private float deviation;
    private boolean isMax, isMin;
    private Google_Map_UI map;
    static HashMap<String, ArrayList<String>> adjMatrix;
    static HashMap<String,Integer> indexIDMap = new HashMap<>();
    ArrayList<NodeObject> mapNodes;
    ArrayList<NodeObject> routeNodes;
    float max_elevation;
    ArrayList<String> final_route = new ArrayList<String>();
    ArrayList<String> fav_route_file_mapping = new ArrayList<String>();

    private String filelocation = System.getProperty("user.dir");
    PrintWriter pw = createFile();
    StringBuilder sb = new StringBuilder();
    ArrayList<String> fav_source_dest = new ArrayList<String>();

    public Model() throws JDOMException, SAXException, IOException, Exception{


		OsmParsing osm = new OsmParsing();
		mapNodes = osm.parser();
		indexIDMap = osm.indexIDMap;
                max_elevation = osm.max_elevation;

                
		adjMatrix = readCSV();
	}
    public void setDeviation(float dev) {
    		deviation = dev/100;
    }
    public float getDeviation() {
    		return deviation;
    }
    public boolean getisMax() {
        return isMax;
    }
    public boolean getisMin() {
        return isMin;
    }
    public void setisMax(boolean value) {
        isMax = value;
        setChanged();
        notifyObservers(value);
    }
    public void setisMin(boolean value) {
        isMin = value;
        setChanged();
        notifyObservers(value);
    }
    public void setSource(String src) {
        source = src;
        setChanged();
        notifyObservers(src);
    }
    public String getSource() {
        return source;
    }
    public void setDestination(String dest) {
        destination = dest;
        setChanged();
        notifyObservers(dest);
    }
    public String getDestination() {
        return destination;
    }
    public void setAPIKey(String key) {
    		apikey = key;
    }
    public String getAPIKey() {
    		return apikey;
    }
    public ArrayList<Integer> getNodeId(Google_Map_UI mapView) throws IOException, JSONException{
        //Google_Map_UI ui =new Google_Map_UI();
        double srcLat = mapView.getsrcLatLng().getLat();
        double srcLng = mapView.getsrcLatLng().getLng();
        double dstLat = mapView.destLatLng.getLat();
        double dstLng = mapView.destLatLng.getLng() ;
        
        ArrayList<Integer> srcDestIndex = new ArrayList<>();
        int srcIndex = getId(srcLat,srcLng);
        int dstIndex = getId(dstLat,dstLng);
        srcDestIndex.add(srcIndex);
        srcDestIndex.add(dstIndex);
        return srcDestIndex;

    }

    /***
     * Converts latitude - longitude obtained from google maps API to the nearest OSM node
     * @param lat latitude
     * @param lng longitude
     * @return : The index of the nearest node found. Returns -1 if the node is not found.
     * @throws IOException
     * @throws JSONException
     */
    private int getId(Double lat,Double lng) throws IOException, JSONException {
        // TODO Auto-generated method stub
        //http://router.project-osrm.org/nearest/v1/driving/13.388860,52.517037?number=3&bearings=0,20

        List<String> testArray = new ArrayList<String>();
        HttpURLConnection connection = null;
		 StringBuilder result = new StringBuilder();
	        String base_url = "http://router.project-osrm.org/nearest/v1/foot/";
	        String location = lng + "," +lat;
	        String number = "?number=3";
	        String bearings = "&bearings=0,20";
	        String full_url = base_url+location+number+bearings;
	        //System.out.println(full_url);
	        URL url = new URL(full_url);
	        //URL url = new URL("https://maps.googleapis.com/maps/api/directions/json?origin=hadley,MA,US&destination=Umass+amherst,MA,US&mode=bicycling&alternatives=true&key=AIzaSyDbJBCyTJBUmRSrlAOfzc4AbdjBqZgoSRU");
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
	      
	      HashMap<String,String> srcDest = new HashMap<>();
	      JSONObject jsonObject = new JSONObject(result.toString());
	      
	      JSONArray resultsArray=jsonObject.getJSONArray("waypoints");
	      String x=null;
	      for ( int i=0;i<resultsArray.length();i++){
	    	  JSONObject routeObj=(JSONObject)resultsArray.get(i);
	          JSONArray legs = routeObj.getJSONArray("nodes");
	          
	          for ( int j=0;j<legs.length();j++){
	              String nodeID=(String)legs.get(j).toString();
	                  //System.out.println("here"+nodeID);
	                  if (indexIDMap.containsKey(nodeID)){
	                  int index = indexIDMap.get(nodeID);
	                  //System.out.println("index"+index);
	          				return index;
	                  }
	          }
	     }
	          
		return -1;
	}

    /***
     * Reads adjacency_all csv to get information of adjacent nodes for each index.
     * @return a matrix describing the adjacency relationship between nodes.
     */
	private static HashMap<String, ArrayList<String>> readCSV() {
		CSVReader csvReader = null;
		HashMap<String,ArrayList<String>> adjMatrix = new HashMap<>();
		try
		{
			ArrayList<NodeObject> mapNodes = new ArrayList<NodeObject>();
	        String filelocation = System.getProperty("user.dir");
	        File inputFile = new File(filelocation+"/adjacency_all.csv");
			csvReader = new CSVReader(new FileReader(inputFile),',','"');
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

    /***
     * write latitude and longitude information of the favourite path to a csv file
     * @param favRouteStr : string containing latitude-longitude pairs for the favourite-route
     */
    public void writeToFavsFile(String favRouteStr){
        sb.append(favRouteStr+"\n");
        pw.write(sb.toString());
        sb.delete(0, sb.length());
        pw.flush();
    }

    public Google_Map_UI getMapObj() {
        return map;
    }
    public void setMapObj(Google_Map_UI map) {
        this.map = map;
        setChanged();
        notifyObservers(map);
    }

    /**
     * Creates/initializes a file to store favourite routes
     * @return a printWriter in append mode
     * @throws IOException
     */
    private PrintWriter createFile() throws IOException {

        File file = new File(filelocation+"\\fav_routes.csv");
        file.createNewFile(); // if file already exists will do nothing
        PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));
        return out;
    }

    /**
     * To populate arrayList with the existing favourite routes.
     */
    public void populate_fav_route_list() { // call at the start of the program
        //Populates the array with the existing routes added under Add-To-Fav
        File file = new File(filelocation+"\\fav_routes_names.csv");

        try {
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                fav_route_file_mapping.add(line);
            }
            fileReader.close();
            System.out.println(fav_route_file_mapping);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Adds routes to both the arrayList of favourite routes and to the file(to store existing routes).
     * @param routeName: name selected by the user for the route
     * @throws IOException
     */
    public void add_fav_route(String routeName) throws IOException {
        //add to the arrayList
        fav_route_file_mapping.add(routeName);

        // add to the file
        File file = new File(filelocation+"\\fav_routes_names.csv");
        file.createNewFile();
        PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));
        StringBuilder sb = new StringBuilder();
        sb.append(routeName+"\n");
        pw.write(sb.toString());
        sb.delete(0, sb.length());
        pw.flush();
    }

    /***
     * Get the lat-long pairs(separated by ;) from the file to display on the UI
     * @param routeName Name of the required route.
     * @return : String with lat-long pairs with ; as the delimiter
     */
    public String get_fav_route_by_name(String routeName){ // to get
        String line = "";
        try {
            
            line = Files.readAllLines(Paths.get("fav_route.csv"),StandardCharsets.UTF_8).get(fav_route_file_mapping.indexOf(routeName));
        } catch (IOException | IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
        return line;
    }
}