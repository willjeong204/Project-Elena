package javaapplication7;

import com.teamdev.jxmaps.GeocoderRequest;
import com.teamdev.jxmaps.GeocoderCallback;
import com.teamdev.jxmaps.Marker;
import com.teamdev.jxmaps.MapViewOptions;
import com.teamdev.jxmaps.InfoWindow;
import com.teamdev.jxmaps.GeocoderStatus;
import com.teamdev.jxmaps.GeocoderResult;
import com.teamdev.jxmaps.Map;
import com.teamdev.jxmaps.MapReadyHandler;
import com.teamdev.jxmaps.MapStatus;
import com.teamdev.jxmaps.swing.MapView;

public class Google_Map_UI extends MapView {
	public String startingPoint;
	public String destination;
    public Google_Map_UI(MapViewOptions options) {
        super(options);
        setOnMapReadyHandler(new MapReadyHandler() {
            @Override
            public void onMapReady(MapStatus status) {
                if (status == MapStatus.MAP_STATUS_OK) {
                    final Map map = getMap();
                    map.setZoom(10.0);
                    GeocoderRequest request = new GeocoderRequest(map);
                    GeocoderRequest request1 = new GeocoderRequest(map);
                    
                    request.setAddress(startingPoint);
                    request1.setAddress(destination);
                    
                    
                    getServices().getGeocoder().geocode(request, new GeocoderCallback(map) {
                        @Override
                        public void onComplete(GeocoderResult[] result, GeocoderStatus status) {
                            if (status == GeocoderStatus.OK) {
                                map.setCenter(result[0].getGeometry().getLocation());             
                                Marker marker = new Marker(map);
                                marker.setPosition(result[0].getGeometry().getLocation());
                                
                                final InfoWindow window = new InfoWindow(map);
                                window.setContent(startingPoint);
                                window.open(map, marker);
                            }
                        }
                    });
                    
                    getServices().getGeocoder().geocode(request1, new GeocoderCallback(map) {
                        @Override
                        public void onComplete(GeocoderResult[] result, GeocoderStatus status) {
                            if (status == GeocoderStatus.OK) {
                                map.setCenter(result[0].getGeometry().getLocation());
                                Marker marker = new Marker(map);                
                                marker.setPosition(result[0].getGeometry().getLocation());                      
                                
                                final InfoWindow window = new InfoWindow(map);
                                window.setContent(destination);
                                window.open(map, marker);
                            }
                        }
                    });
                    
                }
            }
        });
    }
}