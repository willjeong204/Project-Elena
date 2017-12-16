/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplication7;

import org.jdom2.JDOMException;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class OsmParsing {
    static HashMap<String,Integer> indexIDMap = new HashMap<>();
    static float max_elevation;

    OsmParsing ()
    {
        max_elevation = 0.0f;
    }

    /**
     * Takes all the information from map.osm and parses the data.
     * @return : An arrayList of all the nodeObjects formed.
     * @throws JDOMException
     * @throws IOException
     * @throws SAXException
     * @throws ParserConfigurationException
     * @throws Exception
     */
    public static ArrayList<NodeObject> parser() throws JDOMException, IOException, SAXException, ParserConfigurationException, Exception
    {

        ArrayList<NodeObject> mapNodes = new ArrayList<NodeObject>();
        String filelocation = System.getProperty("user.dir");
        File inputFile = new File(filelocation+"/map.osm"); //Replace with your location of map.osm

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
                //float max = 0.0f;
                if (count==350*n || count==nList.getLength()){
                    locStr= locStr.substring(0, locStr.length() - 1);
                    locList.add(locStr);

                    ArrayList<String> eleList = new ArrayList<String>();
                    eleList =callElevationApi(locStr,mapNodes);
                    for (int i=350*(n-1);i<mapNodes.size();i++){
                        mapNodes.get(i).elevation = eleList.get(k);
                        if(Float.parseFloat(eleList.get(k))>max_elevation)
                                max_elevation = Float.parseFloat(eleList.get(k));
                        k++;
                    }
                    n++;
                    locStr=null;
                }
            }
        }
        return mapNodes;
    }

    /***
     * Calls google elevation API to fetch elevation details for each osm node.
     * @param x
     * @param mapNodes : Arraylist storing all the osm nodes and their information in the formof
     *                  nodeObjects
     * @return :  An arrayList storing elevation information for nodes.
     * @throws Exception
     */
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

}

