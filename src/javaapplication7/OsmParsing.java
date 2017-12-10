/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplication7;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

import org.jdom2.*;
import org.jdom2.input.SAXBuilder;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.w3c.dom.Element;

import com.opencsv.CSVReader;


/**
 *
 * @author shrut
 */
public class OsmParsing {
    static HashMap<String,Integer> indexIDMap = new HashMap<>();
    static HashMap<String, ArrayList<String>> adjMatrix =readCSV();
    public static void main(String[] args) throws JDOMException, IOException, SAXException, ParserConfigurationException, Exception
    {
        System.out.println(adjMatrix.get("66590928"));
        ArrayList<NodeObject> mapNodes = parser();
        //makeWays();

        System.out.println(mapNodes.get(0).lat + mapNodes.get(0).lng);
        System.out.println(mapNodes.get(718).lat + mapNodes.get(718).lng);
        
        
        System.out.println(indexIDMap.get("66590928"));
        
        boolean result = route(mapNodes);
        
        if(result)
            System.out.println("true");
        else
            System.out.println("false");
             
        
        //System.out.println(mapNodes.get(0).id);
        /*.out.println(mapNodes.get(10).lat+","+mapNodes.get(10).lng);
        System.out.println(mapNodes.get(11).lat+","+mapNodes.get(11).lng);
        System.out.println(mapNodes.get(12).lat+","+mapNodes.get(12).lng);
        System.out.println(mapNodes.get(13).lat+","+mapNodes.get(13).lng);
        System.out.println(mapNodes.get(14).lat+","+mapNodes.get(14).lng);
    
        
        System.out.println(mapNodes.get(10).elevation);
        System.out.println(mapNodes.get(11).elevation);
        System.out.println(mapNodes.get(12).elevation);
        System.out.println(mapNodes.get(13).elevation);
        System.out.println(mapNodes.get(14).elevation);
    
    */
    }
    
    
    public static ArrayList<NodeObject> parser() throws JDOMException, IOException, SAXException, ParserConfigurationException, Exception
    {
    
    ArrayList<NodeObject> mapNodes = new ArrayList<NodeObject>();
    
    SAXBuilder saxBuilder = new SAXBuilder();
    File inputFile = new File("C:\\Users\\shrut\\Documents\\SE\\Project\\Project-Elena\\map.osm"); //Replace with your location of map.osm
    Document document = saxBuilder.build(inputFile); 
    
    DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
    DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
    org.w3c.dom.Document doc = dBuilder.parse(inputFile);
    doc.getDocumentElement().normalize();
    
    System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
    NodeList nList = doc.getElementsByTagName("node");
   
    String locStr=null;
    int count =0;
    ArrayList<String> locList = new ArrayList<String>();
    
    
    int n=1;
   
    int index=0;
    for (int temp = 0; temp < nList.getLength(); temp++)
    {
        Node nNode = nList.item(temp);
        //System.out.println(temp+1);
        //System.out.println("\nCurrent Element :" + nNode.getNodeName());
        
        if (nNode.getNodeType() == Node.ELEMENT_NODE)
        {
            NodeObject point = new NodeObject();
            NamedNodeMap pNode = nNode.getAttributes();
            
            Node lat = pNode.getNamedItem("lat");
            String latitude = lat.getNodeValue();
            point.lat=latitude;
            
            Node lng = pNode.getNamedItem("lon");
            String longitude = lng.getNodeValue();
            point.lng = longitude;
            
            Node node_id = pNode.getNamedItem("id");
            String id = node_id.getNodeValue();
            point.id = id;
            
            point.index = index;
                        
            indexIDMap.put(id, index);
            index+=1;
            
            mapNodes.add(point);
        if (locStr==null){
            	locStr=point.lat.concat(",").concat(point.lng).concat("|");
            }else{
            	locStr = locStr.concat(point.lat.concat(",").concat(point.lng).concat("|"));            
            }
            count++;
            int k=0;
            if (count==350*n || count==nList.getLength()){
            	locStr= locStr.substring(0, locStr.length() - 1);
            	locList.add(locStr);
            	
            	ArrayList<String> eleList = new ArrayList<String>();
            	eleList =callElevationApi(locStr,mapNodes);
            	for (int i=350*(n-1);i<mapNodes.size();i++){
            			mapNodes.get(i).elevation = eleList.get(k);
            		k++;
            	}
            	n++;
            	locStr=null;
        }
      }
    }
    //System.out.println(indexIDMap); 
    System.out.println(indexIDMap.get("66619669"));
    System.out.println(indexIDMap.get("66669518"));
    return mapNodes;
    }
    
    
	private static ArrayList<String> callElevationApi(String x, ArrayList<NodeObject> mapNodes) throws Exception {
		// TODO Auto-generated method stub
	    HttpURLConnection elevationConnection = null;          
	    StringBuilder eleResult = new StringBuilder();
	    String eleBase_url = "https://maps.googleapis.com/maps/api/elevation/xml?";
	    String elekey = "&key=AIzaSyAqlZ9cPy0XZ0oPiW8p9c8t6r_8s2lWtIM";
	    String locations = "&locations="+x;
	    String eleFull_url = eleBase_url+locations+elekey;
	    //System.out.println("locations..."+locations);
	    URL eleUrl = new URL(eleFull_url);
	    //URL url = new URL("https://maps.googleapis.com/maps/api/directions/xml?origin=hadley,MA,US&destination=Umass+amherst,MA,US&mode=bicycling&alternatives=true&key=AIzaSyDbJBCyTJBUmRSrlAOfzc4AbdjBqZgoSRU");
	    elevationConnection = (HttpURLConnection) eleUrl.openConnection();
	    elevationConnection.setRequestMethod("GET");
	    
	    BufferedReader ele_rd = new BufferedReader(new InputStreamReader(elevationConnection.getInputStream()));
	    String eleLine;
	    ArrayList<String> eleList = new ArrayList<String>();
	    
	    while ((eleLine = ele_rd.readLine()) != null) 
	    {	    	     
	  	  eleResult.append(eleLine);
	  	  //System.out.println(eleLine);  
	    }
	    ele_rd.close();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        InputSource is = new InputSource(new StringReader(eleResult.toString()));
        org.w3c.dom.Document doc= builder.parse(is);
        
	    NodeList locList = doc.getDocumentElement().getElementsByTagName("result");
	    //System.out.println(locList.getLength());    
	    
	    for (int temp = 0; temp < locList.getLength(); temp++)
	    {
	    	Node nNode = locList.item(temp);
	    	if (nNode.getNodeType() == Node.ELEMENT_NODE)
	        {
	        Element ele = (Element)nNode;
	        String elevation = ele.getElementsByTagName("elevation").item(0).getTextContent();
	        eleList.add(elevation);	        
	        } 
	    }
	return(eleList);
}
    
    public static float[][] getDistances(ArrayList<NodeObject> src, ArrayList<NodeObject> dst) throws MalformedURLException, IOException, JDOMException, ParserConfigurationException, SAXException
    {
        
        HttpURLConnection connection = null;
        
        StringBuilder result = new StringBuilder();
        String base_url = "https://maps.googleapis.com/maps/api/distancematrix/xml?&origins=";
        
        for(int i=0; i<src.size();i++)
        {
            String lat = src.get(i).lat;
            String lng =src.get(i).lng;
            base_url = base_url+lat+","+lng+"|";          
        }
        base_url = base_url.substring(0, base_url.length()-1);
        base_url = base_url + "&destinations=";
        for(int i=0; i<dst.size();i++)
        {
            String lat = dst.get(i).lat;
            String lng = dst.get(i).lng;
            base_url = base_url+lat+","+lng+"|";            
        }
        
        base_url = base_url.substring(0, base_url.length()-1);
        base_url = base_url + "&key=AIzaSyCjxAN34ydTukHItnOub9EVa5kwuGb9zBI";
        //System.out.println(base_url);
        
        URL url = new URL(base_url);
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
        
        //int src_len =1;// src.size();
        //int dst_len = 2;//dst.size();
        
        int src_len = src.size();
        int dst_len = dst.size();
        
        float dis_mat[][] =  new float[src_len][dst_len];
        //parse file to get distance. 
        
        SAXBuilder saxBuilder = new SAXBuilder();
        File inputFile = new File("C:\\Users\\shrut\\Documents\\SE\\Project\\Project-Elena\\src\\javaapplication7\\test.xml"); //Replace with your location of map.osm
        Document document = saxBuilder.build(inputFile); 
        
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        //org.w3c.dom.Document doc = dBuilder.parse(inputFile);
        //doc.getDocumentElement().normalize();
        
        InputSource is = new InputSource(new StringReader(result.toString()));
         org.w3c.dom.Document doc = dBuilder.parse(is);
        //doc.getDocumentElement().normalize();
        
        //System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
        NodeList rowList = doc.getElementsByTagName("row");
        ArrayList<Float> distances = new ArrayList<Float>();
        for (int temp = 0; temp < rowList.getLength(); temp++)
	    {
	    	Node nNode = rowList.item(temp);
	    	if (nNode.getNodeType() == Node.ELEMENT_NODE)
	        {
	        Element ele = (Element)nNode;
	        NodeList element = ele.getElementsByTagName("element");
	        
	        for (int k = 0; k < element.getLength(); k++)
		    {
	        	Node eleNode = element.item(k).getLastChild();
	        	eleNode = eleNode.getPreviousSibling();
	        	//System.out.println("eleNode...."+eleNode.getNodeName());
	        	String distance =eleNode.getFirstChild().getNextSibling().getTextContent();
	        	//System.out.println(distance);
	        	distances.add(Float.parseFloat(distance));
		    }
		    }
	        //element.item(0).getChildNodes();
	        //System.out.println("elevation..."+elevation);
	        //eleList.add(elevation);	        
	        
	    }
        int distances_index =0;
        for(int row=0;row<src_len;row++)
        {
        	for(int col = 0; col<dst_len;col++)
        		{
        		dis_mat[row][col]=distances.get(distances_index);
        		distances_index+=1;
        		//System.out.println("dis_mat[row][col]"+dis_mat[row][col]);
        		//System.out.println("row"+row+"coulmn"+col);
        		}
        	
        }
        return dis_mat;
    }

    public static boolean route(ArrayList<NodeObject> mapNodes) throws MalformedURLException, IOException, JDOMException, ParserConfigurationException, SAXException
    {
        ArrayList<NodeObject> src = new ArrayList<NodeObject>();
        
        src.add(mapNodes.get(0));
        
        ArrayList<NodeObject> dst = new ArrayList<NodeObject>();
        dst.add(mapNodes.get(718));
        
        class Node
                {
            NodeObject o;
            float cost;
            Node parent;
            
            Node(NodeObject x, float y)
            {
                o=x;
                cost = y;
            }
            Node()
            {
                ;
            }
        }
        ArrayList<Node> open = new ArrayList<Node>();
        ArrayList<NodeObject> closed =  new ArrayList<NodeObject>();
        
        Node src_node;
        src_node = new Node(src.get(0),0.0f);
        src_node.parent = null;
        open.add(src_node);
        
        while(true)
        {
            Node current;
            current = open.get(0);
            for(int i=1; i<open.size();i++)
            {
                if(open.get(i).cost<current.cost)
                    current = open.get(i);
            }
            
            open.remove(current);
            closed.add(current.o);
            
                
            if(current.o.id.equals(dst.get(0).id))
            {
                while(current.parent!=null)
                {
                    //System.out.print(current.o.id+"("+current.o.lat+","+current.o.lng+")"+"<-");
                    System.out.println("("+current.o.lat+","+current.o.lng+")");
                    current = current.parent;
                }
                //System.out.print(current.o.id+"("+current.o.lat+","+current.o.lng+")");
                System.out.println("("+current.o.lat+","+current.o.lng+")");
                return true;
            }
            //if(open.isEmpty())
                //break;
            System.out.println("current node: "+current.o.id);
            ArrayList<String> sourceAdjList = new ArrayList<String>();
            ArrayList<NodeObject> adj = new ArrayList<NodeObject>();
            //sourceAdjList = readCSV(current.o);
            //NodeObject source ;
            String nodeID =current.o.id;
            sourceAdjList = adjMatrix.get(nodeID);
            for(int m=0;m<sourceAdjList.size();m++){
            	//System.out.println("hi" + sourceAdjList.get(m));
            	int index = indexIDMap.get(sourceAdjList.get(m));
            	adj.add(mapNodes.get(index));
            }
            
            float src_distance[][];
            
            ArrayList<NodeObject> cur_list = new ArrayList<NodeObject>();
            cur_list.add(current.o);
                 
            src_distance = getDistances(cur_list, adj);
            //src_distance
        
            //float dst_distance[][];
            //dst_distance = getDistances(adj,dst);
            
            for(int i=0;i<adj.size();i++)
            {
                Node neighbour = new Node(adj.get(i),src_distance[0][i]);
                System.out.println("Neighbour of " + current.o.id + " is " + neighbour.o.id);
                if(closed.contains(neighbour.o))
                    continue;

                boolean found = false;
                for(int j=0; j<open.size(); j++)
                {
                   if(open.get(j).o.equals(neighbour.o))
                           {
                               found = true;
                               if(open.get(j).cost<neighbour.cost)
                               {
                                   open.get(j).cost = neighbour.cost;
                                   open.get(j).parent = current;
                               }
                               break;
                           }
                }
                
                if(!found)
                {
                    neighbour.parent = current;
                    open.add(neighbour);
                    
                }
                
                if(open.isEmpty())
                    System.out.println("empyt");
                else
                    System.out.println("size"+open.size());
                
                
            }
        }
        
        //return false;
        
        
    }


	private static HashMap<String, ArrayList<String>> readCSV() {
		// TODO Auto-generated method stub
		CSVReader csvReader = null;
		HashMap<String,ArrayList<String>> adjMatrix = new HashMap<>();
        try
        {        	
            csvReader = new CSVReader(new FileReader("C:\\Users\\shrut\\Documents\\SE\\Project\\Project-Elena\\src\\javaapplication7\\adjacency.csv"),',','"');
            String[] adjList = null;
            while((adjList = csvReader.readNext())!=null)
            {
            	ArrayList<String> lines = new ArrayList<String>();
            	for(int i =1;i<adjList.length;i++){
            		lines.add(adjList[i]);
            	}
            	adjMatrix.put(adjList[0], lines);
            	
            }
            //System.out.println(adjMatrix.size());
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

