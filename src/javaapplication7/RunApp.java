package javaapplication7;

public class RunApp {

	//The order of instantiating the objects below will be important for some pairs of commands.
	//I haven't explored this in any detail, beyond that the order below works.

	private String default_address = "Amherst, MA";	//initialise model, which in turn initialises view

	public RunApp() {

		//create Model and View
		Model myModel = new Model();
		View myView 	= new View();

		//tell Model about View. 
		myModel.addObserver(myView);

		Controller myController = new Controller();
		myController.addModel(myModel);
		myController.addView(myView);
		myController.initModel(default_address);

		//tell View about Controller 
		myView.addController(myController);
	}

} 