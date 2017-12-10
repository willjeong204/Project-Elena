package javaapplication7;

import com.teamdev.jxmaps.ControlPosition;
import com.teamdev.jxmaps.GeocoderCallback;
import com.teamdev.jxmaps.GeocoderRequest;
import com.teamdev.jxmaps.GeocoderResult;
import com.teamdev.jxmaps.GeocoderStatus;
import com.teamdev.jxmaps.InfoWindow;
import com.teamdev.jxmaps.LatLng;
import com.teamdev.jxmaps.Map;
import com.teamdev.jxmaps.MapMouseEvent;
import com.teamdev.jxmaps.MapOptions;
import com.teamdev.jxmaps.MapReadyHandler;
import com.teamdev.jxmaps.MapStatus;
import com.teamdev.jxmaps.MapTypeControlOptions;
import com.teamdev.jxmaps.Marker;
import com.teamdev.jxmaps.MouseEvent;
import com.teamdev.jxmaps.swing.MapView;

@SuppressWarnings("serial")
public class Google_Map_UI extends MapView {

    public Google_Map_UI() {
    		// Setting of a ready handler to MapView object. onMapReady will be called when map initialization is done and
    		// the map object is ready to use. Current implementation of onMapReady customizes the map object.
    		setOnMapReadyHandler(new MapReadyHandler() {
    			@Override
    			public void onMapReady(MapStatus status) {
	            // Getting the associated map object
	            final Map map = getMap();
	            // Setting initial zoom value
	            map.setZoom(10.0);
	            // Creating a map options object
	            MapOptions options = new MapOptions();
	            // Creating a map type control options object
	            MapTypeControlOptions controlOptions = new MapTypeControlOptions();
	            // Changing position of the map type control
	            controlOptions.setPosition(ControlPosition.TOP_RIGHT);
	            // Setting map type control options
	            options.setMapTypeControlOptions(controlOptions);
	            // Setting map options
	            map.setOptions(options);
	            map.setCenter(new LatLng(42.340382, -72.496819));
	
//	            performGeocode("Amherst","Boston");
	        }
    		});
    }
    public void performGeocode(String src, String dest) {
        // Getting the associated map object
        final Map map = getMap();
        // Creating a geocode request
        GeocoderRequest requestsrc = new GeocoderRequest();
        // Setting address to the geocode request
        requestsrc.setAddress(src);

        // Geocoding position by the entered address
        getServices().getGeocoder().geocode(requestsrc, new GeocoderCallback(map) {
            @Override
            public void onComplete(GeocoderResult[] results, GeocoderStatus status) {
                // Checking operation status
                if ((status == GeocoderStatus.OK) && (results.length > 0)) {
                    // Getting the first result
                    GeocoderResult result = results[0];
                    // Getting a location of the result
                    LatLng location = result.getGeometry().getLocation();
                    // Setting the map center to result location
                    map.setCenter(location);
                    // Creating a marker object
                    Marker marker = new Marker(map);
                    // Setting position of the marker to the result location
                    marker.setPosition(location);
                    // Creating an information window
                    InfoWindow infoWindow = new InfoWindow(map);
                    // Putting the address and location to the content of the information window
                    infoWindow.setContent("<b>" + result.getFormattedAddress() + "</b><br>" + location.toString());
                    // Moving the information window to the result location
                    infoWindow.setPosition(location);
                    // Showing of the information window
                    infoWindow.open(map, marker);
                    
                    // Adding event listener that intercepts clicking on marker
                    marker.addEventListener("click", new MapMouseEvent() {
                        @Override
                        public void onEvent(MouseEvent mouseEvent) {
                            // Removing marker from the map
                            marker.remove();
                        }
                    });
                }
            }
        });
        
        GeocoderRequest requestdest = new GeocoderRequest();
        // Setting address to the geocode request
        requestdest.setAddress(dest);

        // Geocoding position by the entered address
        getServices().getGeocoder().geocode(requestdest, new GeocoderCallback(map) {
            @Override
            public void onComplete(GeocoderResult[] results, GeocoderStatus status) {
                // Checking operation status
                if ((status == GeocoderStatus.OK) && (results.length > 0)) {
                    // Getting the first result
                    GeocoderResult result = results[0];
                    // Getting a location of the result
                    LatLng location = result.getGeometry().getLocation();
                    // Setting the map center to result location
                    map.setCenter(location);
                    // Creating a marker object
                    Marker marker = new Marker(map);
                    // Setting position of the marker to the result location
                    marker.setPosition(location);
                    // Creating an information window
                    InfoWindow infoWindow = new InfoWindow(map);
                    // Putting the address and location to the content of the information window
                    infoWindow.setContent("<b>" + result.getFormattedAddress() + "</b><br>" + location.toString());
                    // Moving the information window to the result location
                    infoWindow.setPosition(location);
                    // Showing of the information window
                    infoWindow.open(map, marker);
                    
                    // Adding event listener that intercepts clicking on marker
                    marker.addEventListener("click", new MapMouseEvent() {
                        @Override
                        public void onEvent(MouseEvent mouseEvent) {
                            // Removing marker from the map
                            marker.remove();
                        }
                    });
                }
            }
        });
    }
}