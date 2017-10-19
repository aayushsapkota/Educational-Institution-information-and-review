package aayush.randompatrolling;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.renderscript.RenderScript;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;


public class SelectedLocation {

    //    private ArrayList<String> place = new ArrayList<>();
    private LocationManager locationManager;
    private String name;
    private String longitude;
    private String latitude;
    private String minTimeToStay;
    private String maxTimeTOStay;
    private String priority;
    private String checkBackOn;
    private String visited;

    double distance;

    public void addPlaceInformation(String name, String latitude, String longitude,
                                    String minTimeToStay, String maxTimeTOStay, String priority, String checkBackOn) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.maxTimeTOStay = maxTimeTOStay;
        this.minTimeToStay = minTimeToStay;
        this.priority = priority;
        this.checkBackOn = checkBackOn;


//        place.add(name);
//        place.add(longitude);
//        place.add(latitude);
//        place.add(minTimeToStay);
//        place.add(maxTimeTOStay);
//        place.add(priority);
//        place.add(checkBackOn);
    }

    public void setVisited(String visited) {
        this.visited = visited;
    }

//    public ArrayList<String> getPlace() {
//        return place;
//    }

    public String getVisited() {
        return visited;
    }


    public String getName() {
        return this.name;
    }

    public String getLongitude() {
        return this.longitude;
    }

    public String getLatitude() {
        return this.latitude;
    }

    public String getMinTimeToStay() {
        return this.minTimeToStay;
    }

    public String getMaxTimeTOStay() {
        return this.maxTimeTOStay;
    }

    public String getPriority() {
        return this.priority;
    }

    public String getCheckBackOn() {
        return this.checkBackOn;
    }

//    public double distanceTo(SelectedLocation location) {
//        try {
//            double tempLatitude = Double.parseDouble(location.latitude);
//            double tempLongitude = Double.parseDouble(location.longitude);
//            double xLatitude = Math.abs(Double.parseDouble(longitude) - tempLatitude);
//            double yLongitude = Math.abs(Double.parseDouble(latitude) - tempLongitude);
//            //pathagoras theorem
//            distance = Math.sqrt((xLatitude * xLatitude) + (yLongitude * yLongitude));
//        } catch (NullPointerException e) {
//            e.printStackTrace();
//        }
//
//
//        return distance;
//    }


}
