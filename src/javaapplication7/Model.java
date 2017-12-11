package javaapplication7;

import com.opencsv.CSVReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import javaapplication7.OsmParsing;
import javaapplication7.NodeObject;
import org.jdom2.JDOMException;
import org.xml.sax.SAXException;

public class Model extends java.util.Observable {	
	private String source;
	private String destination;
        static HashMap<String, ArrayList<String>> adjMatrix;
        ArrayList<NodeObject> mapNodes;

	public Model() throws JDOMException, SAXException, IOException, Exception{
         
            adjMatrix = readCSV();
            
            OsmParsing osm = new OsmParsing();
            mapNodes = osm.parser();
            
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
    
}