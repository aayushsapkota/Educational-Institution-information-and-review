package com.creativeminds.stealthpatrolling;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.HashMap;


public class GetDirectionsData extends AsyncTask<Object, String, String> {

    String url;
    GoogleMap gMap;
    String googleDirectionsData;
    String duration, distance;
    LatLng latLng;

    @Override
    protected String doInBackground(Object... objects) {
        gMap = (GoogleMap) objects[0];
        url = objects[1].toString();
        latLng = (LatLng) objects[2];

        DownloadUrl downloadUrl = new DownloadUrl();

        try {
            googleDirectionsData = downloadUrl.readUrl(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d("json response", googleDirectionsData);
        return googleDirectionsData;
    }

    @Override
    protected void onPostExecute(String result) {
        HashMap<String, String> directionsList = null;
        DataParser parser = new DataParser();
        directionsList = parser.parseDirections(result);

//        Log.d("result", result);

        //duration = directionsList.get("duration");
        //distance = directionsList.get("distance");


//        gMap.clear();
////       System.out.println("the duration is "+duration +"and the distance is "+distance);
//        Log.d("duration: ", duration + " distance: " + distance);
//        MarkerOptions markerOptions = new MarkerOptions();
//        markerOptions.position(latLng);
//        markerOptions.title("Duration= " + duration);
//        markerOptions.snippet("distance = " + duration);


    }




}
