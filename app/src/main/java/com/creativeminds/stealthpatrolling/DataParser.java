package com.creativeminds.stealthpatrolling;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;


public class DataParser {

    private HashMap<String, String> getDuration(JSONArray googleDirectionsJson) {
        HashMap<String, String> googleDirectionMap = new HashMap<>();

        String distance = "";
        String duration = "";

        try {
          duration = googleDirectionsJson.getJSONObject(0).getJSONArray("legs").getJSONObject(0).getJSONObject("duration").getString("value");
//          distance = googleDirectionsJson.getJSONObject(0).getJSONObject("distance").getString("text");

            googleDirectionMap.put("duration", duration);
//            googleDirectionMap.put("distance", distance);

            Log.d("duration",duration);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        //Log.d("json response", googleDirectionsJson.toString());

        return googleDirectionMap;
    }

    public HashMap<String, String> parseDirections(String jsonData) {
        JSONArray jsonArray = new JSONArray();
        JSONArray finalArray = new JSONArray();
        JSONObject jsonObject;
        JSONObject jsonObject1;

        try {
            jsonObject = new JSONObject(jsonData);
            jsonArray = jsonObject.getJSONArray("routes");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return getDuration(jsonArray);
    }


}
