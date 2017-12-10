package javaapplication7;

public class Model extends java.util.Observable {	
	private String source;
	private String destination;

	public Model(){}
	
	public void setSource(String src) {
		source = src;
	}
	public String getSource() {
		return source;
	}
	public void setDestination(String dest) {
		destination = dest;
	}
	public String getDestination() {
		return destination;
	}
}