package aayush.randompatrolling;

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Aayush Sapkota on 18/10/2017.
 */

public class DataFromGoogleRequest extends AsyncTask<Object, String, String> {

    String googlePlacesData;
    GoogleMap mMap;
    String googleUrl;
    private static ArrayList durationList = new ArrayList();

    public DataFromGoogleRequest(){
        durationList.clear();
    }

    @Override
    protected String doInBackground(Object[] params) {

//        mMap = (GoogleMap) params[0];
        googleUrl = (String) params[0];


        String data = "";
        InputStream inputStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(googleUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.connect();

            inputStream = urlConnection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            data = sb.toString();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
                urlConnection.disconnect();

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return data;
    }

    @Override
    protected void onPostExecute(String s) {
        JSONObject jsonRespone = null;
        JSONArray routesArray = null;
        try {
            jsonRespone = new JSONObject(s);

            routesArray = jsonRespone.getJSONArray("routes");

            JSONObject route = routesArray.optJSONObject(0);
            JSONArray legs;
            JSONObject leg;
            JSONObject durationObj;
            String duration;

            if (route.has("legs")) {
                legs = route.getJSONArray("legs");
                leg = legs.getJSONObject(0);
                durationObj = leg.getJSONObject("duration");
                Log.d("Duration is", durationObj.toString());

                if (durationObj.has("value")) {
//                                    Log.d("Duration before", durationList.toString());
                    duration = String.valueOf(durationObj.get("value"));
                    durationList.add(duration);
//                            Log.d("Duration is", durationList.toString());
                }
            } else {
                String responseString = "not found";
                if (responseString.equals("not found")) {
                    Log.d("Response is", responseString);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public ArrayList getDurationList() {
        return durationList;
    }
}
