package javaapplication7;

import java.util.ArrayList;

import com.teamdev.jxmaps.ControlPosition;
import com.teamdev.jxmaps.ElevationCallback;
import com.teamdev.jxmaps.ElevationResult;
import com.teamdev.jxmaps.ElevationService;
import com.teamdev.jxmaps.ElevationStatus;
import com.teamdev.jxmaps.GeocoderCallback;
import com.teamdev.jxmaps.GeocoderRequest;
import com.teamdev.jxmaps.GeocoderResult;
import com.teamdev.jxmaps.GeocoderStatus;
import com.teamdev.jxmaps.InfoWindow;
import com.teamdev.jxmaps.LatLng;
import com.teamdev.jxmaps.LocationElevationRequest;
import com.teamdev.jxmaps.Map;
import com.teamdev.jxmaps.MapMouseEvent;
import com.teamdev.jxmaps.MapOptions;
import com.teamdev.jxmaps.MapReadyHandler;
import com.teamdev.jxmaps.MapStatus;
import com.teamdev.jxmaps.MapTypeControlOptions;
import com.teamdev.jxmaps.MapTypeId;
import com.teamdev.jxmaps.Marker;
import com.teamdev.jxmaps.MouseEvent;
import com.teamdev.jxmaps.Polyline;
import com.teamdev.jxmaps.PolylineOptions;
import com.teamdev.jxmaps.swing.MapView;

@SuppressWarnings("serial")
public class Google_Map_UI extends MapView {
	InfoWindow infoWindow;
	static public LatLng srcLatLng;
	static public LatLng destLatLng;
	
    public Google_Map_UI() {
    		// Setting of a ready handler to MapView object. onMapReady will be called when map initialization is done and
    		// the map object is ready to use. Current implementation of onMapReady customizes the map object.
    		setOnMapReadyHandler(new MapReadyHandler() {
    			@Override
    			public void onMapReady(MapStatus status) {
	            // Getting the associated map object
	            final Map map = getMap();
	            map.setCenter(new LatLng(42.389813,-72.528250));
	            // Setting initial zoom value
	            map.setZoom(17.0);
	            // Setting initial map type
                map.setMapTypeId(MapTypeId.ROADMAP);
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
	            
	            showElevationInfo(map, map.getCenter(), true);
	            // Adding of event listener to the click event
                map.addEventListener("click", new MapMouseEvent() {
                    @Override
                    public void onEvent(final MouseEvent mouseEvent) {
                        showElevationInfo(map, mouseEvent.latLng(), false);
                    }
                });
	        }
    		});
    }
    
    public void drawRoute(Map map, Model model) {
    		ArrayList<String> list = model.final_route;
    		
    		LatLng[] path = new LatLng[list.size()];
    		for(int i = 0;i<path.length;i++) {
    			double lat, lng;
    			String[] latlng = list.get(i).split(",");
    			lat = Double.parseDouble(latlng[0]);
    			lng = Double.parseDouble(latlng[1]);
    			path[i] = new LatLng(lat, lng);
    		}
 
        Polyline polyline = new Polyline(map);
        polyline.setPath(path);
        PolylineOptions options = new PolylineOptions();
        options.setGeodesic(true);
        options.setStrokeColor("#FF0000");
        options.setStrokeOpacity(1.0);
        options.setStrokeWeight(2.0);
        polyline.setOptions(options);
    }
    
    
    private void showElevationInfo(final Map map, final LatLng latLng, final boolean initial) {
        // Getting the elevation service instance
        final ElevationService elevationService = getServices().getElevationService();

        // Checking if info window has already opened
        if (infoWindow != null) {
            // Close info window
            infoWindow.close();
        }

        // Creating an elevation request
        LocationElevationRequest request = new LocationElevationRequest();
        LatLng[] locations = {latLng};
        // Setting location to the elevation request
        request.setLocations(locations);

        // Evaluating of the elevation for a location
        elevationService.getElevationForLocations(request, new ElevationCallback(map) {
            @Override
            public void onComplete(ElevationResult[] result, ElevationStatus status) {
                // Checking operation status
                if (status == ElevationStatus.OK) {
                    // Creating an info window
                    infoWindow = new InfoWindow(map);
                    String content = "The elevation at this point is <b>" + (int) result[0].getElevation() + "</b> meters. ";
                    if (initial)
                        content += "<br><br>Click anywhere on the map to get the elevation data at that point.";
                    // Setting content of the info window
                    infoWindow.setContent(content);
                    // Moving the info window to the source location
                    infoWindow.setPosition(latLng);
                    // Showing the info window
                    infoWindow.open(map);
                }
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
                    srcLatLng = location;           
                    map.setCenter(location);
                    // Creating a marker object
                    final Marker marker = new Marker(map);
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
                    destLatLng = location;
                    map.setCenter(location);
                    // Creating a marker object
                    final Marker marker = new Marker(map);
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
    
    public LatLng getsrcLatLng() {
    		return srcLatLng;
    }
    public LatLng getdestLatLng() {
		return destLatLng;
    }
}