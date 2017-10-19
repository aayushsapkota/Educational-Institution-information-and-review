package aayush.randompatrolling;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

public class addPlace extends AppCompatActivity {


    private ArrayList<SelectedLocation> selectedLocations = new ArrayList<>();


    private SelectedLocation selectedLocationObj = new SelectedLocation();
    private Login login = new Login();

    private double latitude;
    private double longitude;
    private String placeCheckBackTime;
    private String minTimeStay;
    private String maxTimeStay;
    private String priorityIndex;
    private String addressName;
    private double distance = 0;
    private double fitness = 0;
    String user_id;


//    public addPlace() {
//        for (int i = 0; i < placeManager.numberOfLocations(); i++) {
//            selectedLocations.add(null);
//        }
//    }
//
//    public addPlace(ArrayList<SelectedLocation> selectedLocationA) {
//        this.selectedLocations = selectedLocationA;
//    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_place);


        final EditText address = (EditText) findViewById(R.id.address);
        final EditText minTime = (EditText) findViewById(R.id.minTimeStay);
        final EditText maxTime = (EditText) findViewById(R.id.maxTimeStay);
        final EditText priority = (EditText) findViewById(R.id.placePriority);
        final EditText checkBackTime = (EditText) findViewById(R.id.checkBackDuration);
        final Button addPlace = (Button) findViewById(R.id.addPlaceButton);
        Button back = (Button) findViewById(R.id.placesBack);

        Intent getPlaces = getIntent();
        if (getPlaces.getStringExtra("addressName") != null) {
            String addressname = getPlaces.getStringExtra("addressName");
            Log.d("addressName", addressname);
            address.setText(addressname);
        }

        if (getPlaces.getDoubleExtra("latitude", 0) != 0) {
            this.latitude = getPlaces.getDoubleExtra("latitude", 0);
            Log.d("latitude", String.valueOf(latitude));
        }
        if (getPlaces.getDoubleExtra("longitude", 0) != 0) {
            this.longitude = getPlaces.getDoubleExtra("longitude", 0);
            Log.d("longitude", String.valueOf(longitude));
        }

        addPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hasNetworkConnection()) {
                    addressName = address.getText().toString();
                    minTimeStay = minTime.getText().toString();
                    maxTimeStay = maxTime.getText().toString();
                    priorityIndex = priority.getText().toString();
                    placeCheckBackTime = checkBackTime.getText().toString();

                    final Response.Listener<String> responseListener = new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            try {
                                Log.d("rsp", response);
                                JSONObject jsonResponse = new JSONObject(response);
                                boolean sucess = jsonResponse.getBoolean("sucess");
                                if (sucess) {
                                    Intent i = new Intent(addPlace.this, MapsActivity.class);
                                    startActivity(i);
                                    Toast.makeText(getApplicationContext(), "Place added sucessfully", Toast.LENGTH_SHORT).show();
                                } else {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(addPlace.this);
                                    builder.setMessage("Adding the place failed").setNegativeButton("Retry", null).create().show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    };
                  user_id = login.getUserID();
//                    Log.d("idtosend", id);
                    AddPlaceRequest addPlaceRequest = new AddPlaceRequest(addressName, String.valueOf(latitude), String.valueOf(longitude),
                            minTimeStay, maxTimeStay, priorityIndex, placeCheckBackTime, user_id, responseListener);

                    RequestQueue add_queue = Volley.newRequestQueue(addPlace.getContext());
                    add_queue.add(addPlaceRequest);

                } else {
                    Toast.makeText(addPlace.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                }

            }
        });

        address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), mapSelect.class);
                startActivity(i);
            }

        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), places.class);
                startActivity(i);
            }
        });
    }

    public boolean hasNetworkConnection() {
        boolean connected = false;
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netActive = connectivityManager.getActiveNetworkInfo();
            connected = netActive != null && netActive.isAvailable() && netActive.isConnected();
            return connected;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return connected;
    }

//    public SelectedLocation returnSelectedLocation(){
//        return selectedLocationObj;
//    }

//
//    public void generateIndividual() {
//        for (int i = 0; i < placeManager.numberOfLocations(); i++) {
//            setLocation(i, placeManager.getCity(i));
//        }
//        //randomly reorder the tour
//        Collections.shuffle(selectedLocations);
//    }
//
//    public SelectedLocation getLocation(int tourPosition) {
//        return (SelectedLocation) selectedLocations.get(tourPosition);
//    }
//
//    public void setLocation(int tourPosition, SelectedLocation location) {
//        selectedLocations.set(tourPosition, location);
//
//        fitness = 0;
//        distance = 0;
//    }
//
//    public boolean hasLocation(SelectedLocation location) {
//        return selectedLocations.contains(location);
//    }
//
//    //get the tour fitness
//    public double getFitness() {
//        if (fitness == 0) {
//            fitness = 1 / getTourDistance();
//        }
//        return fitness;
//    }
//
//    public int tourSize(){
//        return selectedLocations.size();
//    }
//
//
//    public double getTourDistance() {
//        if (distance == 0) {
//            double tourDistance = 0;
//            //loop through all cities
//            for (int i = 0; i < tourSize(); i++) {
//                //origin city
//                SelectedLocation origin = getLocation(i);
//                //destination city
//                SelectedLocation destination;
//                //check we are not on last city
//                //tour final destination set to starting city
//                if ((i + 1) < tourSize()) {
//                    destination = getLocation(i+1);
//                } else {
//                    destination = getLocation(0);
//                }
//                //get the distance between the two cities
//                tourDistance += origin.distanceTo(destination);
//            }
//            distance = tourDistance;
//        }
//        return distance;
//    }
//
//
//
//    @Override
//    public String toString() {
//        String geneString = "|";
//        for (int i = 0; i < selectedLocations.size(); i++) {
//            geneString += getLocation(i).getName() + " --> ";
//        }
//        return geneString;
//    }
}
