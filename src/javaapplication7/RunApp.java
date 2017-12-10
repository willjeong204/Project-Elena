package javaapplication7;

public class RunApp {
	public RunApp() {

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