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

/****
 *This Method finds the best route based on user input (minimum elevation,
 * Maximum elevation or the shortest root(if not mentioned))
 */
public class FindRoute {

    public static float getScore(float g, float h, float e)
    {
        boolean minimize_elevation = true;
        float percentage = 100.0f;
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

}
