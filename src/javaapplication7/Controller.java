package javaapplication7;

import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONException;

public class Controller implements java.awt.event.ActionListener{
	Model model;
	View view;

	Controller() {}

	//invoked when a button is pressed
	public void actionPerformed(java.awt.event.ActionEvent e){
		String action = e.getActionCommand();
		switch (action) {
		case "MAX":
			//DOSomething
			break;
		case "MIN":
			//DOSomething
			break;
		case "CLEAR":
			model.setSource("");
			model.setDestination("");
			view.clear();
			//need to figure out how to clear the map.
//			view.getMapView().getMap().dispose();
			break;
		case "GO":
			//clear the map first then add more.	
			model.setSource(view.getSrc());
			model.setDestination(view.getDest());
			view.getMapView().performGeocode(model.getSource(), model.getDestination());
			//call routing algorithm
			try {
				ArrayList<Integer> srcDst = new ArrayList<>();
				srcDst = model.getNodeId(view.getMapView());
				for (int i=0;i<srcDst.size();i++){
				System.out.println("testing..."+srcDst.get(i));
				}
				//System.out.println("hereeeeeeeeeeee"+model.mapNodes.get(index).id);
			} catch (IOException | JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			//store latlng in mode here
			//view.getMapView().drawRoute(view.getMapView().getMap());

			break;
		case "ADDFAV":
			//DOSomething
			break;
		}

	} 

	public void addModel(Model m){
		this.model = m;
	} 

	public void addView(View v){
		this.view = v;
	}
}
