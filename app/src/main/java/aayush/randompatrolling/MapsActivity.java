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
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static android.location.LocationManager.NETWORK_PROVIDER;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    public static LocationListener locationListener;
    private GoogleMap mMap;
    GoogleMap.OnInfoWindowClickListener onInfoWindowClickListener;
    private LocationManager locationManager;
    placeManager placeObj = new placeManager();
    ArrayList<SelectedLocation> LocationList;
    LatLng newLocationAdded;
    private double latitude;
    private double longitude;
    private String address;
    private String minStay;
    private String maxStay;
    private String priority;
    private String checkBackOn;
    private String result;
    Location lastLocation;


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

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if (locationManager != null) {

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                Location lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if (lastLocation == null) {
                    lastLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                    if (lastLocation != null) {
                        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 300, 500, locationListener);
                        centerMapLocation(lastLocation, "Your location");
                        mMap.setMyLocationEnabled(true);
                    } else {
                        AlertDialog alertDialog = new AlertDialog.Builder(MapsActivity.this).create();
                        alertDialog.setTitle("Location Disabled");
                        alertDialog.setMessage("Please, turn your location on. This app needs location to run");
                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
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
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
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

        LocationList = placeObj.getdestinationList();

        for (SelectedLocation s : LocationList) {
            address = s.getName();
            latitude = Double.parseDouble(s.getLatitude());
            longitude = Double.parseDouble(s.getLongitude());
            newLocationAdded = new LatLng(latitude, longitude);

            minStay = s.getMinTimeToStay();
            maxStay = s.getMaxTimeTOStay();
            priority = s.getPriority();
            checkBackOn = s.getCheckBackOn();
            mMap.addMarker(new MarkerOptions().position(newLocationAdded).title(address));
        }

        onInfoWindowClickListener = new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                windowClick(marker);
            }
        };

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
}


