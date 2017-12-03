/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplication7;

import java.io.*;
import java.util.*;
import org.jdom2.*;
import org.jdom2.input.SAXBuilder;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
/**
 *
 * @author shrut
 */
public class NewClass {
    
    public static void main(String[] args) throws JDOMException, IOException, SAXException, ParserConfigurationException
    {
        parser();
    }
    
    public static void parser() throws JDOMException, IOException, SAXException, ParserConfigurationException
    {
    
    ArrayList<NodeObject> mapNodes = new ArrayList<NodeObject>();
    
    SAXBuilder saxBuilder = new SAXBuilder();
    File inputFile = new File("C:\\Users\\shrut\\Documents\\SE\\Project\\map.osm"); //Replace with your location of map.osm
    Document document = saxBuilder.build(inputFile); 
    
    DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
    DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
    org.w3c.dom.Document doc = dBuilder.parse(inputFile);
    doc.getDocumentElement().normalize();
    
    System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
    NodeList nList = doc.getElementsByTagName("node");
    
    for (int temp = 0; temp < nList.getLength(); temp++)
    {
        Node nNode = nList.item(temp);
        System.out.println(temp+1);
        System.out.println("\nCurrent Element :" + nNode.getNodeName());
        
        if (nNode.getNodeType() == Node.ELEMENT_NODE)
        {
            NodeObject point = new NodeObject();
            NamedNodeMap pNode = nNode.getAttributes();
            
            Node lat = pNode.getNamedItem("lat");
            String latitude = lat.getNodeValue();
            point.lat=latitude;
            
            Node lng = pNode.getNamedItem("lon");
            String longitude = lng.getNodeValue();
            point.lat = longitude;
            
            Node node_id = pNode.getNamedItem("id");
            String id = node_id.getNodeValue();
            point.id = id;
            
            mapNodes.add(point);
        }
    }
    
    }

}
