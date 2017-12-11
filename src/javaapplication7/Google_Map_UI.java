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
//                drawRoute(map);
//	            performGeocode("Amherst","Boston");
	        }
    		});
    }
    
//    public void drawRoute(Map map, Model model) {
//    		ArrayList<String> list = new ArrayList<String>();
//    		list = model.getList();
//    		
//    		LatLng[] path = new LatLng[list.size()];
//    		for(int i = 0;i<path.length;i++) {
//    			double lat, lng;
//    			String[] latlng = list.get(i).split(",");
//    			lat = Double.parseDouble(latlng[0]);
//    			lng = Double.parseDouble(latlng[1]);
//    			path[i] = new LatLng(lat, lng);
//    		}
//    		
////    		LatLng[] path = { new LatLng(42.3892763,-72.5295258),
////    	    		new LatLng(42.3892487,-72.5295732),
////    	    		new LatLng(42.3890982,-72.5296371),
////    	    		new LatLng(42.3889294,-72.5296812),
////    	    		new LatLng(42.3887905,-72.5296963),
////    	    		new LatLng(42.3887713,-72.5297763),
////    	    		new LatLng(42.3888318,-72.5298010),
////    	    		new LatLng(42.3886812,-72.5304072),
////    	    		new LatLng(42.3886114,-72.5305812),
////    	    		new LatLng(42.3884862,-72.5308317),
////    	    		new LatLng(42.3883791,-72.5310670),
////    	    		new LatLng(42.3890323,-72.5313479),
////    	    		new LatLng(42.3892154,-72.5314204),
////    	    		new LatLng(42.3894712,-72.5315248),
////    	    		new LatLng(42.3894481,-72.5316186),
////    	    		new LatLng(42.3897066,-72.5317217),
////    	    		new LatLng(42.3897394,-72.5315574),
////    	    		new LatLng(42.3897790,-72.5313590),
////    	    		new LatLng(42.3898600,-72.5307200),
////    	    		new LatLng(42.3898690,-72.5306460),
////    	    		new LatLng(42.3898780,-72.5305720),
////    	    		new LatLng(42.3898920,-72.5304650),
////    	    		new LatLng(42.3899040,-72.5303570),
////    	    		new LatLng(42.3899150,-72.5302490),
////    	    		new LatLng(42.3901960,-72.5303680),
////    	    		new LatLng(42.3902190,-72.5303790),
////    	    		new LatLng(42.3902430,-72.5303890)};
//
//        Polyline polyline = new Polyline(map);
//        polyline.setPath(path);
//        PolylineOptions options = new PolylineOptions();
//        options.setGeodesic(true);
//        options.setStrokeColor("#FF0000");
//        options.setStrokeOpacity(1.0);
//        options.setStrokeWeight(2.0);
//        polyline.setOptions(options);
//    }
    
    
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
                    
//                    //for testing purpose============================
//                    LatLng location = new LatLng(42.3892763,-72.5295258);
                    
                    // Getting a location of the result
                    LatLng location = result.getGeometry().getLocation();                  
                    // Setting the map center to result location
                    srcLatLng = location;
                    /*srcLat = srcLatLng.getLat();
                    srcLng = srcLatLng.getLng();
                    public String srcLat=null;
                	public String srcLng=null;
                	public String destLat=null;
                	public String destLng=null;*/
                    
                    System.out.println("srcLatLng" +srcLatLng.getLat());
            		System.out.println("srcLatLng" +srcLatLng.getLng());
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
                    
                  //for testing purpose============================
//                    LatLng location = new LatLng(42.3902430,-72.5303890);
                    
                    // Getting a location of the result
                    LatLng location = result.getGeometry().getLocation();
                    // Setting the map center to result location
                    destLatLng = location;
                    System.out.println("hiii...." +destLatLng.getLat());
            		System.out.println("hiii..." +destLatLng.getLng());
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
    	System.out.println("inside dest");
    	System.out.println("hiii" +destLatLng.getLat());
		System.out.println("hiii" +destLatLng.getLng());
		return destLatLng;
}
}