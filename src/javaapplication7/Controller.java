package javaapplication7;

import org.jdom2.JDOMException;
import org.json.JSONException;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Controller implements java.awt.event.ActionListener{
	Model model;
	View view;

	Controller() {}

	//invoked when a button is pressed
	public void actionPerformed(java.awt.event.ActionEvent e){
		String action = e.getActionCommand();

		switch (action) {
		case "MAX":
			model.setisMax(true);
			model.setisMin(false);
			break;
		case "MIN":
			model.setisMax(false);
			model.setisMin(true);
			break;
		case "CLEAR":
			model.setSource("");
			model.setDestination("");
//			view.getMapView().getMap().get
				//need to figure out how to clear the map.
//			view.getMapView().getMap().dispose();
				break;
			case "GO":
				//clear the map first then add more.
				model.setSource(view.getSrc());
				model.setDestination(view.getDest());
				view.getMapView().performGeocode(model.getSource(), model.getDestination());
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				//call routing algorithm
				try {
					ArrayList<Integer> srcDst = new ArrayList<>();
					srcDst = model.getNodeId(view.getMapView());
					javaapplication7.FindRoute r = new javaapplication7.FindRoute();
					try {
						boolean minimize_elevation = false;
						if(model.getisMin() == true)
						{
							minimize_elevation = true;
						}
						if(model.getisMax()==true)
						{
							minimize_elevation = false;
						}


						model.final_route = r.route(model.mapNodes.get(srcDst.get(0)), model.mapNodes.get(srcDst.get(1)), model.mapNodes, model.indexIDMap,model.adjMatrix,1.0f,model.max_elevation, minimize_elevation);
					} catch (MalformedURLException ex) {
						Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
					} catch (IOException ex) {
						Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
					} catch (JDOMException ex) {
						Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
					} catch (ParserConfigurationException ex) {
						Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
					} catch (SAXException ex) {
						Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
					}

				} catch (IOException | JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				//store latlng in mode here
				view.getMapView().drawRoute(view.getMapView().getMap(), model);

				break;
			case "ADDFAV":
				//String routeName= "abc"
				String routeStr = "";
				//model.fav_source_dest.add(routeName);

				System.out.println(model.final_route);

				for (String s : model.final_route)
				{
					routeStr += s + ";";
				}

				model.writeToFavsFile(routeStr);
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
