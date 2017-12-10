package javaapplication7;

public class Model extends java.util.Observable {	
	
	private String address;
	private String source;
	private String destination;

	public Model(){}
	
	public void setAddress(String newaddress) {
		address = newaddress;
		setChanged();
		notifyObservers(address);
	}
	public void setSource(String newSource) {
		source = newSource;
	}
	public String getSource() {
		return source;
	}
	public void setDestination(String newDestination) {
		destination = newDestination;
	}
	public String getDestination() {
		return destination;
	}
	
	public void updateView(){
		
	}
}