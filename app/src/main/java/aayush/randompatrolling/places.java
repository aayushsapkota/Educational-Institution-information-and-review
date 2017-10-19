package aayush.randompatrolling;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Network;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.LocaleList;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RequestTickle;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.android.volley.toolbox.VolleyTickle;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Stack;
import java.util.concurrent.ExecutionException;


public class places extends AppCompatActivity {

//    tspCalculator tsp = new tspCalculator();

    MapsActivity mapsActivity = new MapsActivity();
    //All places arraylist
    ArrayList<SelectedLocation> placesList;
    //used for storing duration retrieved from google
    ArrayList<String> durationList = new ArrayList<>();
    //used for other inner purpose calculations and manupulations
    //like taking place out of when visited
    ArrayList<SelectedLocation> tspDurationList;
    int[][] durationMatrix;
    private Stack<Integer> stack;
    private int numberOfNodes;

    String oLatitude;
    String oLongitude;
    String dLatitude;
    String dLongitude;
    DataFromGoogleRequest dataFromGoogleRequest;
    private int time;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        durationList.clear();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_places);

        final TextView resultView = (TextView) findViewById(R.id.result);
        Button addPlace = (Button) findViewById(R.id.addPlaceButton);
        Button back = (Button) findViewById(R.id.placesBack);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), MapsActivity.class);
                startActivity(i);
            }
        });

        addPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), addPlace.class);
                startActivity(i);
            }
        });

        Log.d("placesList", "Here we areeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee");

        if (placesList != null) {
            placesList.clear();

        }
        placesList = mapsActivity.getLocationList();
        Log.d("placesList", placesList.get(0).getName());
        String userLatitude = String.valueOf(mapsActivity.getUserLocation().getLatitude());
        String userLongitude = String.valueOf(mapsActivity.getUserLocation().getLongitude());
        SelectedLocation s = new SelectedLocation();
        if (!placesList.contains(s)) {
            s.addPlaceInformation("Your Location", userLatitude, userLongitude, "0", "0", "0", "0");
            placesList.add(s);
        }
        getAllDirectionDataFromGoogle();
        int timeFactor = placesList.size();
        if (timeFactor < 7) {
            time = timeFactor * 3500;
        } else {
            time = timeFactor * timeFactor * 1000;
        }
        dataFromGoogleRequest = new DataFromGoogleRequest();

        final AlertDialog alertDialog = new AlertDialog.Builder(places.this).create();
        alertDialog.setTitle("Loading...");
        alertDialog.setMessage("Please Wait, while we generate a Best Route \npossible waiting time: " + time / 1000 + " seconds");
        alertDialog.setCancelable(false);
        alertDialog.show();


        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                try {
                    durationList = dataFromGoogleRequest.getDurationList();
                    Log.d("size", String.valueOf(durationList.size()));
                    int size = (int) Math.sqrt(durationList.size());

                    durationMatrix = new int[size][size];
                    for (int i = 0; i < durationList.size(); i++) {
                        durationMatrix[i / size][i % size] = Integer.parseInt(durationList.get(i));
                    }

                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            Log.d("durationMatrix", Arrays.deepToString(durationMatrix));

                            places instance = new places();
                            String result = "";
                            ArrayList<Integer> index = instance.tsp(durationMatrix);
                            for (int i = 0; i < index.size(); i++) {
                                int j = index.get(i);
                                result += placesList.get(j).getName() + " \n\n ";
                            }
                            resultView.setText("Places sorted by nearest distance \n\n" + result);
                            Log.d("result", result);

                            alertDialog.dismiss();
                        }
                    }, 1000);

                } catch (IndexOutOfBoundsException e) {
                    e.printStackTrace();
                    AlertDialog.Builder builder = new AlertDialog.Builder(places.this);
                    builder.setMessage("Data retrieval Failed").setNegativeButton("Retry", null).create().show();
                }
            }
        }, time);


    }

    public void getAllDirectionDataFromGoogle() {

        for (int i = 0; i < placesList.size(); i++) {
            for (int j = 0; j < placesList.size(); j++) {
                oLongitude = placesList.get(i).getLongitude();
                oLatitude = placesList.get(i).getLatitude();
                dLatitude = placesList.get(j).getLatitude();
                dLongitude = placesList.get(j).getLongitude();
                makeDurationGoogleRequest(oLatitude, oLongitude, dLatitude, dLongitude);
            }
        }


    }

    private String getDirectionsUrl(String oLatitude, String oLongitude, String dLatitude, String dLongitude) {
        String googlePlacesUrl = "https://maps.googleapis.com/maps/api/directions/json?" +
                "origin=" + oLatitude + "," + oLongitude +
                "&destination=" + dLatitude + "," + dLongitude +
                "&key=" + "AIzaSyDtJn2Q1RnH14J3ByeCdA5HDwshyqu-owY";
//        Log.d("url", googlePlacesUrl);
        return googlePlacesUrl;
    }

    private void makeDurationGoogleRequest(String oLatitude, String oLongitude, String dLatitude, String dLongitude) {

        String DURATION_REQUEST_URL = getDirectionsUrl(oLatitude, oLongitude, dLatitude, dLongitude);
        Object[] datatransfer = new Object[1];
        DataFromGoogleRequest dataFromGoogleRequest = new DataFromGoogleRequest();
        datatransfer[0] = DURATION_REQUEST_URL;
        dataFromGoogleRequest.execute(datatransfer);

    }


    public ArrayList tsp(int adjacencyMatrix[][]) {
        ArrayList<Integer> index = new ArrayList<>();
        stack = new Stack<Integer>();
        numberOfNodes = adjacencyMatrix[1].length - 1;
        Log.d("numberOfNodes", String.valueOf(numberOfNodes));
        int[] visited = new int[numberOfNodes + 1];
        visited[numberOfNodes] = 1;
        stack.push(numberOfNodes);
        int element, dur = 0, i;
        int min = Integer.MAX_VALUE;
        boolean minFlag = false;
//        System.out.print(0 + "\t");
        index.add(numberOfNodes);
        while (!stack.isEmpty()) {
            element = stack.peek();
            i = 0;
            min = Integer.MAX_VALUE;
            while (i <= numberOfNodes) {
                if (adjacencyMatrix[element][i] > 1 && visited[i] == 0) {
                    if (min > adjacencyMatrix[element][i]) {
                        min = adjacencyMatrix[element][i];
                        Log.d("min", String.valueOf(min));
                        dur = i;
                        minFlag = true;
                    }
                }
                i++;
            }
            if (minFlag) {
                visited[dur] = 1;
                stack.push(dur);
//                System.out.print(dst + "\t");
                Log.d("TSP is", String.valueOf(dur));
                index.add(dur);
                minFlag = false;
                continue;
            }
            stack.pop();
        }
        return index;
    }
}
