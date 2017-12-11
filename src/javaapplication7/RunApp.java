package javaapplication7;

import java.io.IOException;

import org.jdom2.JDOMException;
import org.xml.sax.SAXException;

public class RunApp {
	public RunApp() throws JDOMException, SAXException, IOException, Exception {

		//create Model and View
		Model myModel = new Model();
		View myView 	= new View();

		//tell Model about View. 
		myModel.addObserver(myView);

		Controller myController = new Controller();
		myController.addModel(myModel);
		myController.addView(myView);
//		myController.initModel(default_address);

		//tell View about Controller 
		myView.addController(myController);
	}
} 