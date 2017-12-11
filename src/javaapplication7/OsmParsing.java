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

public class OsmParsing {
    static HashMap<String,Integer> indexIDMap = new HashMap<>();
    static HashMap<String, ArrayList<String>> adjMatrix =readCSV();
    public static void main(String[] args) throws JDOMException, IOException, SAXException, ParserConfigurationException, Exception
    {
        ArrayList<NodeObject> mapNodes = parser();
        System.out.println(mapNodes.get(0).lat + mapNodes.get(0).lng);
        System.out.println(mapNodes.get(718).lat + mapNodes.get(718).lng); 
        boolean result = route(mapNodes);        
        if(result)
            System.out.println("true");
        else
            System.out.println("false");

    }
    
    
    public static ArrayList<NodeObject> parser() throws JDOMException, IOException, SAXException, ParserConfigurationException, Exception
    {
    
    ArrayList<NodeObject> mapNodes = new ArrayList<NodeObject>();
    
    File inputFile = new File("C:\\Users\\shrut\\Documents\\SE\\Project\\Project-Elena\\map.osm"); //Replace with your location of map.osm
    
    DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
    DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
    org.w3c.dom.Document doc = dBuilder.parse(inputFile);
    doc.getDocumentElement().normalize();
    
    NodeList nList = doc.getElementsByTagName("node");   
    String locStr=null;
    int count =0;
    ArrayList<String> locList = new ArrayList<String>();
    
    
    int n=1;  
    int index=0;
    for (int temp = 0; temp < nList.getLength(); temp++)
    {
        Node nNode = nList.item(temp);
        
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
    return mapNodes;
    }
    
    
	private static ArrayList<String> callElevationApi(String x, ArrayList<NodeObject> mapNodes) throws Exception {
	    HttpURLConnection elevationConnection = null;          
	    StringBuilder eleResult = new StringBuilder();
	    String eleBase_url = "https://maps.googleapis.com/maps/api/elevation/xml?";
	    String elekey = "&key=AIzaSyAqlZ9cPy0XZ0oPiW8p9c8t6r_8s2lWtIM";
	    String locations = "&locations="+x;
	    String eleFull_url = eleBase_url+locations+elekey;
	    URL eleUrl = new URL(eleFull_url);
	    elevationConnection = (HttpURLConnection) eleUrl.openConnection();
	    elevationConnection.setRequestMethod("GET");
	    
	    BufferedReader ele_rd = new BufferedReader(new InputStreamReader(elevationConnection.getInputStream()));
	    String eleLine;
	    ArrayList<String> eleList = new ArrayList<String>();	    
	    while ((eleLine = ele_rd.readLine()) != null) 
	    {	    	     
	  	  eleResult.append(eleLine);
	    }
	    ele_rd.close();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        InputSource is = new InputSource(new StringReader(eleResult.toString()));
        org.w3c.dom.Document doc= builder.parse(is);
        
	    NodeList locList = doc.getDocumentElement().getElementsByTagName("result");
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
        base_url = base_url + "&key=AIzaSyBJIhJ1NEia7je40oZbD35sV_6rbqcE9Zc";
        
        URL url = new URL(base_url);
        connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String line;
        
        while ((line = rd.readLine()) != null) 
        {     
                result.append(line);
        }
        rd.close(); 
        
        int src_len = src.size();
        int dst_len = dst.size();
        
        float dis_mat[][] =  new float[src_len][dst_len];
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        InputSource is = new InputSource(new StringReader(result.toString()));
        org.w3c.dom.Document doc = dBuilder.parse(is);
       
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
	        	String distance =eleNode.getFirstChild().getNextSibling().getTextContent();
	        	distances.add(Float.parseFloat(distance));
		    }
		    }
	    }
        int distances_index =0;
        for(int row=0;row<src_len;row++)
        {
        	for(int col = 0; col<dst_len;col++)
        		{
        		dis_mat[row][col]=distances.get(distances_index);
        		distances_index+=1;
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
                    System.out.println("("+current.o.lat+","+current.o.lng+")");
                    current = current.parent;
                }
                System.out.println("("+current.o.lat+","+current.o.lng+")");
                return true;
            }
            System.out.println("current node: "+current.o.id);
            ArrayList<String> sourceAdjList = new ArrayList<String>();
            ArrayList<NodeObject> adj = new ArrayList<NodeObject>();
            String nodeID =current.o.id;
            sourceAdjList = adjMatrix.get(nodeID);
            for(int m=0;m<sourceAdjList.size();m++){
            	int index = indexIDMap.get(sourceAdjList.get(m));
            	adj.add(mapNodes.get(index));
            }
            
            float src_distance[][];
            
            ArrayList<NodeObject> cur_list = new ArrayList<NodeObject>();
            cur_list.add(current.o);
                 
            src_distance = getDistances(cur_list, adj);
        
            float dst_distance[][];
            dst_distance = getDistances(adj,dst);
            
            for(int i=0;i<adj.size();i++)
            {
                float score = 0.0f;
                score = getScore(src_distance[0][i],+dst_distance[i][0],Float.parseFloat(adj.get(i).elevation));
                                
                Node neighbour = new Node(adj.get(i),score);
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
    }


	private static HashMap<String, ArrayList<String>> readCSV() {
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
        public static float getScore(float g, float h, float e)
        {
            boolean minimize_elevation = false;
            float percentage = 1.0f;
            float f;
            float max_e = 100.0f;
            
            if(minimize_elevation)
            {
                f = g+h+(percentage*e);
            }
            else
            {
                float compliment_e = max_e - e;
                f = g+h+(percentage*compliment_e);
            }
            return f;
        }

}

