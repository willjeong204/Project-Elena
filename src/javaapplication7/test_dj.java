package javaapplication7;


import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;

import com.opencsv.CSVReader;

public class test_dj 
{
	public static void main(String args[])
    {
        CSVReader csvReader = null;
        
        try
        {
        	HashMap<String,ArrayList> adjMatrix = new HashMap<>();
            csvReader = new CSVReader(new FileReader("C:\\Users\\Darshana\\Desktop\\adjacency.csv"),',','"');
            String[] adjList = null;
            while((adjList = csvReader.readNext())!=null)
            {
            	ArrayList lines = new ArrayList();
            	for(int i =1;i<adjList.length;i++){
            		lines.add(adjList[i]);
            	}
            	adjMatrix.put(adjList[0], lines);
            	
            }
            System.out.println(adjMatrix);
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
    }
}