package javaapplication7;

import org.jdom2.JDOMException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

/****
 *This Method finds the best route based on user input (minimum elevation,
 * Maximum elevation or the shortest root(if not mentioned))
 */
public class FindRoute {

    public static float getScore(float g, float h, float e,float percentage,float max_e, boolean minimize_elevation)
    {
        //boolean minimize_elevation = true;
        //float percentage = 100.0f;
        float f;
        //float max_e = 100.0f;

        if(minimize_elevation)
        {
            f = percentage*(g+h);//+e;
        }
        else
        {
            float compliment_e = max_e - e;
            f = percentage*(g+h);//+compliment_e;
        }
        return f;
    }

    /***
     *
     * @param src : source
     * @param dst : Destination
     * @return : Distances between source and desination
     *
     */
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
        base_url = base_url + "&key=AIzaSyC09ucCu9jMeI9zP7tEp5fkMkEaKMg9UGI";

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

    /***
     * Implemeted a* algorithm to find most optimized route between source and destination
     *
     * @param source
     * @param destination
     * @param mapNodes : Arraylist that stores nodeObjects
     * @param indexIDMap : Stores mapping between index and nodeID
     * @param adjMatrix : stores adjacent nodes for the required nodeIDs
     * @return : Boolean if a route was found or not
     */
    public static ArrayList<String> route(NodeObject source, NodeObject destination, ArrayList<NodeObject> mapNodes, HashMap<String,Integer> indexIDMap, HashMap<String, ArrayList<String>> adjMatrix,float percentage,float max_e, boolean minimize_elevation) throws MalformedURLException, IOException, JDOMException, ParserConfigurationException, SAXException
    {
        ArrayList<NodeObject> src = new ArrayList<NodeObject>();

        src.add(source);

        ArrayList<NodeObject> dst = new ArrayList<NodeObject>();
        dst.add(destination);

        class Node
        {
            NodeObject o;
            float cost;
            Node parent;

            Node(NodeObject n_o, float co)
            {
                o=n_o;
                cost = co;
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
                ArrayList<String> finalpath = new ArrayList<>();
                while(current.parent!=null)
                {
                    
                    finalpath.add(current.o.lat+","+current.o.lng);
                    current = current.parent;
                }
                finalpath.add(current.o.lat+","+current.o.lng);
                return finalpath;
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
                float src_dist = src_distance[0][i];
                float dst_dist = dst_distance[i][0];
                score = getScore(src_dist,dst_dist,Float.parseFloat(adj.get(i).elevation),percentage, max_e,minimize_elevation);

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
                {
                    //return false;//Check this incase of false!!!!!!!!
                    //System.out.println("empty");
                    ArrayList<String> finalpath = new ArrayList<String>();
                    return finalpath;
                    
                }
                //else
                    //System.out.println("size"+open.size());
            }
        }
    }

}
