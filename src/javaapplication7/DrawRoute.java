package javaapplication7;

import com.teamdev.jxmaps.ControlPosition;
import com.teamdev.jxmaps.LatLng;
import com.teamdev.jxmaps.Map;
import com.teamdev.jxmaps.MapOptions;
import com.teamdev.jxmaps.MapReadyHandler;
import com.teamdev.jxmaps.MapStatus;
import com.teamdev.jxmaps.MapTypeControlOptions;
import com.teamdev.jxmaps.Polyline;
import com.teamdev.jxmaps.PolylineOptions;
import com.teamdev.jxmaps.swing.MapView;

import javax.swing.*;
import java.awt.*;

public class DrawRoute extends MapView {
    public DrawRoute() {        
        setOnMapReadyHandler(new MapReadyHandler() {
            public void onMapReady(MapStatus status) {
                if (status == MapStatus.MAP_STATUS_OK) {
                    final Map map = getMap();
                    MapOptions mapOptions = new MapOptions();
                    MapTypeControlOptions controlOptions = new MapTypeControlOptions();                    
                    controlOptions.setPosition(ControlPosition.TOP_RIGHT);
                    mapOptions.setMapTypeControlOptions(controlOptions);
                    map.setOptions(mapOptions);
                    map.setCenter(new LatLng(42.3732, -72.5199));
                    map.setZoom(10.0);
                    //42.3422506,-72.5884567|42.34222690000001,-72.5890769|42.3445823,-72.5895219|42.3666547,-72.5284185|42.373821,-72.53262260000001|42.3848517,-72.5269598|42.3857009,-72.5225062|42.385777,-72.5224442
                    LatLng[] path = {new LatLng(42.3422506,-72.5884567),
                            new LatLng(42.3445823,-72.5895219),
                            new LatLng(42.373821,-72.53262260000001),
                            new LatLng(42.385777,-72.5224442)};
                    Polyline polyline = new Polyline(map);
                    polyline.setPath(path);
                    PolylineOptions options = new PolylineOptions();
                    options.setGeodesic(true);
                    options.setStrokeColor("#FF0000");
                    options.setStrokeOpacity(1.0);
                    options.setStrokeWeight(2.0);
                    polyline.setOptions(options);
                }
            }
        });
    }

    public static void main(String[] args) {
        final DrawRoute sample = new DrawRoute();

        JFrame frame = new JFrame("Drawing route");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.add(sample, BorderLayout.CENTER);
        frame.setSize(700, 500);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

}
