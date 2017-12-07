/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplication7;

import java.io.*;
import java.net.HttpURLConnection;
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
/**
 *
 * @author shrut
 */
public class OsmParsing {
    
    public static void main(String[] args) throws JDOMException, IOException, SAXException, ParserConfigurationException, Exception
    {
        ArrayList<NodeObject> mapNodes = parser();
        makeWays();
        
    }
    
    public static ArrayList<NodeObject> parser() throws JDOMException, IOException, SAXException, ParserConfigurationException, Exception
    {
    
    ArrayList<NodeObject> mapNodes = new ArrayList<NodeObject>();
    
    SAXBuilder saxBuilder = new SAXBuilder();
    File inputFile = new File("C:\\Users\\Darshana\\Desktop\\map.osm"); //Replace with your location of map.osm
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
    HashMap<Integer,String> indexIDMap = new HashMap<>();
    
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
            index+=1;
                        
            indexIDMap.put(index, id);
            
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
    System.out.println(indexIDMap); 
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
    
    public static void makeWays() throws JDOMException, IOException, SAXException, ParserConfigurationException
    {
        SAXBuilder saxBuilder = new SAXBuilder();
        File inputFile = new File("C:\\Users\\Darshana\\Desktop\\map.osm"); //Replace with your location of map.osm
        Document document = saxBuilder.build(inputFile); 

        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        org.w3c.dom.Document doc = dBuilder.parse(inputFile);
        doc.getDocumentElement().normalize();

        NodeList nList = doc.getElementsByTagName("way");
        //System.out.println(nList.getLength());
        for (int temp = 0; temp < nList.getLength(); temp++)
        {
            Node nNode = nList.item(temp);
            //System.out.println(temp+1);
            //System.out.println("Current Element :" + nNode.getNodeName());
        
            if (nNode.getNodeType() == Node.ELEMENT_NODE)
            {
                NodeList children = nNode.getChildNodes();
                Node last = nNode.getLastChild();
                last = last.getPreviousSibling();
                boolean building = false;
                //System.out.println(last.getNodeName());
                while(last.getNodeName().equalsIgnoreCase("tag"))
                {
                    //System.out.println("processing last");
                    NamedNodeMap x = last.getAttributes();
                    Node k = x.getNamedItem("k");
                    
                    if (k.getNodeValue().equalsIgnoreCase("building") )
                    {
                        //System.out.println("its a building");
                        building = true;
                    } 
                    last = last.getPreviousSibling();
                }
                //System.out.println(children.getLength());
                if(building == true)
                {
                    continue;
                }
                for(int i = 0; i<children.getLength();i++)
                {
                    Node child = children.item(i);
                    //NamedNodeMap child_attr = child.get
                    //child_attr.getNamedItem("nd");
                    
                    if(child.getNodeName().equalsIgnoreCase("nd"))
                    {
                        
                        NamedNodeMap x = child.getAttributes();
                        Node ref = x.getNamedItem("ref");
                        System.out.println(ref.getNodeValue());
                    }
                }
            }
        }
    }
    
}
 


