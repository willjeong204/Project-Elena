/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplication7;

import java.io.IOException;
import java.util.ArrayList;
import org.jdom2.JDOMException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.xml.sax.SAXException;

/**
 *
 * @author shrut
 */
public class ModelTest {
    
    public ModelTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of getisMax method, of class Model.
     */
    @Test
    public void testGetisMax() throws JDOMException, IOException, SAXException, Exception {
        System.out.println("getisMax");
        Model instance = new Model();
        boolean expResult = false;
        boolean result = instance.getisMax();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        
    }

    /**
     * Test of getisMin method, of class Model.
     */
    @Test
    public void testGetisMin() throws JDOMException, SAXException, IOException, Exception {
        System.out.println("getisMin");
        Model instance = new Model();
        boolean expResult = false;
        boolean result = instance.getisMin();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        
    }
    
    @Test
    public void hashmap_mapparsing() throws JDOMException, SAXException, IOException, Exception
    {
        System.out.println("Hashmaps testing");
        Model instance = new Model();
        
        boolean expResult = false;
        boolean result = instance.mapNodes.isEmpty();
        assertEquals(expResult, result);
    }
    
    @Test
    
    public void hashmap_index() throws JDOMException, SAXException, IOException, Exception
    {
        System.out.println("Hashmaps testing");
        Model instance = new Model();
        
        boolean expResult = false;
        boolean result = instance.indexIDMap.isEmpty();
        assertEquals(expResult, result);
        
    }
    
    @Test
    public void finalroute_initial() throws JDOMException, SAXException, IOException, Exception
    {
        System.out.println("Hashmaps testing");
        Model instance = new Model();
        
        boolean expResult = true;
        boolean result = instance.final_route.isEmpty();
        assertEquals(expResult, result);
    }
}
