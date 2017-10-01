package com.creativeminds.stealthpatrolling;

import android.os.AsyncTask;

import com.google.android.gms.maps.GoogleMap;

import java.io.IOException;
import java.util.HashMap;


public class GetDirectionsData extends AsyncTask<Object, String, String> {

    String url;
    GoogleMap gMap;
    String googleDirectionsData;
    String duration , distance;

    @Override
    protected String doInBackground(Object... objects) {
        gMap = (GoogleMap) objects[0];
        url = (String) objects[1];


        DownloadUrl downloadUrl = new DownloadUrl();

        try {
            googleDirectionsData = downloadUrl.readUrl(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return googleDirectionsData;
    }

    @Override
    protected void onPostExecute(String s) {
        HashMap<String, String> directionsList = null;
        DataParser parser = new DataParser();
        directionsList = parser.parseDirections(s);

        duration = directionsList.get("duration");
        distance = directionsList.get("distance");

        gMap.clear();
        System.out.println("the duration is "+duration +"and the distance is "+distance);
    }


}
