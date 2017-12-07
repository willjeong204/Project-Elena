package javaapplication7;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JPanel;

import org.json.JSONException;

public class Controller implements java.awt.event.ActionListener{
	Model model;
	View view;

	Controller() {}

	//invoked when a button is pressed
	public void actionPerformed(java.awt.event.ActionEvent e){
		String action = e.getActionCommand();
		switch (action) {
		case "SOURCE":
			break;
		case "DESTINATION":
			//DOSomething
			break;	
		case "MAX":
			//DOSomething
			break;
		case "MIN":
			//DOSomething
			break;
		case "CLEAR":
			model.setSource("");
			model.setDestination("");
			view.Source.setText("");
			view.Destination.setText("");
			break;
		case "GO":
			try {
	            // TODO add your handling code here:
				model.setSource(view.Source.getText());
				model.setDestination(view.Destination.getText());
				//view.panelmap.remove(view.mapView);
				Google_Map_UI newMap = new Google_Map_UI(view.options);
				newMap.destination = model.getDestination();
				newMap.startingPoint = model.getSource();
				view.panelmap.remove(0);
				view.panelmap.add(newMap);
				
	            JavaApplication7.getData(model.getSource(), model.getDestination());
	        } catch (MalformedURLException ex) {
	            Logger.getLogger(View.class.getName()).log(Level.SEVERE, null, ex);
	        } catch (IOException ex) {
	            Logger.getLogger(View.class.getName()).log(Level.SEVERE, null, ex);
	        } catch (JSONException ex) {
	            Logger.getLogger(View.class.getName()).log(Level.SEVERE, null, ex);
	        }
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

	public void initModel(String default_adress){
		model.setAddress(default_adress);
	} 
}
