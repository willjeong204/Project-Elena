package javaapplication7;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.logging.Level;
import java.util.logging.Logger;

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
	            JavaApplication7.getData(model.getSource(), model.getDestination());
	        } catch (MalformedURLException ex) {
	            Logger.getLogger(NewJFrame1.class.getName()).log(Level.SEVERE, null, ex);
	        } catch (IOException ex) {
	            Logger.getLogger(NewJFrame1.class.getName()).log(Level.SEVERE, null, ex);
	        } catch (JSONException ex) {
	            Logger.getLogger(NewJFrame1.class.getName()).log(Level.SEVERE, null, ex);
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
