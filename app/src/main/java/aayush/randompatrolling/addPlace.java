package aayush.randompatrolling;

import android.content.Intent;
import android.database.sqlite.SQLiteException;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class addPlace extends AppCompatActivity {


    private ArrayList<SelectedLocation> selectedLocations = new ArrayList<>();


    private SelectedLocation selectedLocationObj = new SelectedLocation();
    private double latitude;
    private double longitude;
    private String placeCheckBackTime;
    private String minTimeStay;
    private String maxTimeStay;
    private String priorityIndex;
    private String addressName;
    private double distance = 0;
    private double fitness = 0;


    public addPlace() {
        for (int i = 0; i < placeManager.numberOfLocations(); i++) {
            selectedLocations.add(null);
        }
    }

    public addPlace(ArrayList<SelectedLocation> selectedLocationA) {
        this.selectedLocations = selectedLocationA;
    }


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
                addressName = address.getText().toString();
                minTimeStay = minTime.getText().toString();
                maxTimeStay = maxTime.getText().toString();
                priorityIndex = priority.getText().toString();
                placeCheckBackTime = checkBackTime.getText().toString();

                selectedLocationObj.addPlaceInformation(addressName, String.valueOf(longitude),
                        String.valueOf(latitude), minTimeStay, maxTimeStay, priorityIndex, placeCheckBackTime);
                Log.d("selectLocationObj", selectedLocationObj.getName());
             placeManager.addCity(selectedLocationObj);
                Intent i = new Intent(addPlace.this, MapsActivity.class);
                startActivity(i);
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

//    public SelectedLocation returnSelectedLocation(){
//        return selectedLocationObj;
//    }


    public void generateIndividual() {
        for (int i = 0; i < placeManager.numberOfLocations(); i++) {
            setLocation(i, placeManager.getCity(i));
        }
        //randomly reorder the tour
        Collections.shuffle(selectedLocations);
    }

    public SelectedLocation getLocation(int tourPosition) {
        return (SelectedLocation) selectedLocations.get(tourPosition);
    }

    public void setLocation(int tourPosition, SelectedLocation location) {
        selectedLocations.set(tourPosition, location);

        fitness = 0;
        distance = 0;
    }

    public boolean hasLocation(SelectedLocation location) {
        return selectedLocations.contains(location);
    }

    //get the tour fitness
    public double getFitness() {
        if (fitness == 0) {
            fitness = 1 / getTourDistance();
        }
        return fitness;
    }

    public int tourSize(){
        return selectedLocations.size();
    }


    public double getTourDistance() {
        if (distance == 0) {
            double tourDistance = 0;
            //loop through all cities
            for (int i = 0; i < tourSize(); i++) {
                //origin city
                SelectedLocation origin = getLocation(i);
                //destination city
                SelectedLocation destination;
                //check we are not on last city
                //tour final destination set to starting city
                if ((i + 1) < tourSize()) {
                    destination = getLocation(i+1);
                } else {
                    destination = getLocation(0);
                }
                //get the distance between the two cities
                tourDistance += origin.distanceTo(destination);
            }
            distance = tourDistance;
        }
        return distance;
    }



    @Override
    public String toString() {
        String geneString = "|";
        for (int i = 0; i < selectedLocations.size(); i++) {
            geneString += getLocation(i).getName() + " --> ";
        }
        return geneString;
    }
}
