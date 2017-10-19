package aayush.randompatrolling;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    public static LocationListener locationListener;
    private GoogleMap mMap;
    GoogleMap.OnInfoWindowClickListener onInfoWindowClickListener;
    private Login login = new Login();
    String user_id;

    private placeManager placeObj = new placeManager();
    private static ArrayList<SelectedLocation> LocationList;
    private SelectedLocation selectedLocation;
    private LatLng newLocationAdded;
    private String latitude;
    private String longitude;
    private String address;
    private String minStay;
    private String maxStay;
    private String priority;
    private String checkBackOn;
    //    private String result;
    private static Location lastLocation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                if (ContextCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    centerMapLocation(location, "Your location");
                } else {
                    ActivityCompat.requestPermissions(MapsActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                }
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {
            }

            @Override
            public void onProviderEnabled(String s) {
            }

            @Override
            public void onProviderDisabled(String s) {
            }
        };

        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if (locationManager != null) {

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if (lastLocation == null) {
                    lastLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                    if (lastLocation != null) {
                        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 20, locationListener);
                        centerMapLocation(lastLocation, "Your location");
                        mMap.setMyLocationEnabled(true);
                    } else {
                        AlertDialog alertDialog = new AlertDialog.Builder(MapsActivity.this).create();
                        alertDialog.setTitle("Location Disabled");
                        alertDialog.setMessage("Allow this app to use location");
                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                        startActivity(i);
                                        dialog.dismiss();
                                    }
                                });
                        alertDialog.show();
                    }
                } else {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 300, 500, locationListener);
                    centerMapLocation(lastLocation, "Your location");
                    mMap.setMyLocationEnabled(true);
                }

            } else {
                ActivityCompat.requestPermissions(MapsActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(i);
            }
        }


        Button places = (Button) findViewById(R.id.places);
        Button alarms = (Button) findViewById(R.id.alarms);

        places.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), places.class);
                startActivity(i);
            }
        });

        alarms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), alarms.class);
                startActivity(i);
            }
        });


        onInfoWindowClickListener = new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                windowClick(marker);
            }
        };

        if (hasNetworkConnection()) {

            Response.Listener<String> responseListener = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        LocationList = new ArrayList<>();
                        boolean sucess = jsonResponse.getBoolean("sucess");
                        if (sucess) {
                            for (int i = 0; i < jsonResponse.length() - 1; i++) {
                                String j = String.valueOf(i);
                                JSONObject object = (JSONObject) jsonResponse.get(j);
                                address = (String) object.get("name");
                                latitude = (String) object.get("latitude");
                                longitude = (String) object.get("longitude");
                                minStay = (String) object.get("minStay");
                                maxStay = (String) object.get("maxStay");
                                priority = (String) object.get("priorityIndex");
                                checkBackOn = (String) object.get("placeCheckBackTime");
                                selectedLocation = new SelectedLocation();
                                selectedLocation.addPlaceInformation(address, latitude, longitude,
                                        minStay, maxStay, priority, checkBackOn);
                                LocationList.add(selectedLocation);
                            }

                            for (SelectedLocation s : LocationList) {
                                address = s.getName();
                                Double Dlatitude = Double.parseDouble(s.getLatitude());
                                Double Dlongitude = Double.parseDouble(s.getLongitude());
                                newLocationAdded = new LatLng(Dlatitude, Dlongitude);
                                Log.d("newloc", newLocationAdded.toString());
                                Log.d("addressn", address);

//                                minStay = s.getMinTimeToStay();
//                                maxStay = s.getMaxTimeTOStay();
//                                priority = s.getPriority();
//                                checkBackOn = s.getCheckBackOn();
                                if (mMap != null) {
                                    mMap.addMarker(new MarkerOptions().position(newLocationAdded).title(address));
//                                    Toast.makeText(getApplicationContext(), "Markers addeed Sucessfully", Toast.LENGTH_SHORT).show();

                                }
                            }

                        } else {
                            Toast.makeText(getApplicationContext(), "No previous locations found", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            };
            user_id = login.getUserID();
            getLocationRequest getLocationRequest = new getLocationRequest(user_id, responseListener);
            RequestQueue req_queue = Volley.newRequestQueue(MapsActivity.this);
            req_queue.add(getLocationRequest);

        } else {
            Toast.makeText(getApplicationContext(), "No internet Connection", Toast.LENGTH_SHORT).show();
        }

        mMap.setOnInfoWindowClickListener(onInfoWindowClickListener);


    }

    public void centerMapLocation(Location location, String title) {
        LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());
        try {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 15));
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    public Location getUserLocation() {
        return lastLocation;
    }

    public ArrayList<SelectedLocation> getLocationList() {
        return LocationList;
    }


    public void windowClick(Marker marker) {
        String markerAddress = marker.getTitle();
        Log.d("markerTitle", markerAddress);
        for (SelectedLocation s : LocationList) {
            if (markerAddress.equals(s.getName())) {
                Intent i = new Intent(MapsActivity.this, Display_location_info.class);
                i.putExtra("address", s.getName());
                i.putExtra("Latitude", s.getLatitude());
                i.putExtra("Longitude", s.getLongitude());
                i.putExtra("minTime", s.getMinTimeToStay());
                i.putExtra("MaxTime", s.getMaxTimeTOStay());
                i.putExtra("priority", s.getPriority());
                i.putExtra("checkBackOn", s.getCheckBackOn());
                startActivity(i);
            }
        }
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


}


