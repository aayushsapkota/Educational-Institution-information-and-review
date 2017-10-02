package com.creativeminds.stealthpatrolling;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class mainMap extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener, GoogleMap.OnMarkerClickListener {

    private GoogleMap gMap;
    LocationManager locationManager;
    LocationListener locationListener;
    double latitude = -12.371869;
    double longitude = 130.870036;
    double end_latitude = -12.462759;
    double end_longitude = 130.840505;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;

        gMap.setOnMapLongClickListener((GoogleMap.OnMapLongClickListener) this);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            gMap.setMyLocationEnabled(true);

        }


        Intent intent = getIntent();
        if (intent.getIntExtra("placeNumber", 0) == 0) {

            locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
            locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    centerMapLocation(location, "Your location");
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

            getAllLocationData();

            if (Build.VERSION.SDK_INT < 23) {
                locationManager.requestLocationUpdates(locationManager.GPS_PROVIDER, 0, 0, locationListener);

            } else {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    locationManager.requestLocationUpdates(locationManager.GPS_PROVIDER, 0, 0, locationListener);
                    Location lastLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    centerMapLocation(lastLocation, "Your location");

                } else {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                }
            }
        } else {
            Location placeLocation = new Location(LocationManager.GPS_PROVIDER);
            placeLocation.setLatitude(LocationList.locations.get(intent.getIntExtra("placeNumber", 0)).latitude);
            placeLocation.setLongitude(LocationList.locations.get(intent.getIntExtra("placeNumber", 0)).longitude);

            centerMapLocation(placeLocation, LocationList.places.get(intent.getIntExtra("placeNumber", 0)));

        }
    }

    public void centerMapLocation(Location location, String title) {
        LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());

        gMap.clear();
        if (title != "Your location") {
            gMap.addMarker(new MarkerOptions().position(userLocation).title(title));
        }
        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 15));

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                if (locationManager != null) {
                    locationManager.requestLocationUpdates(locationManager.GPS_PROVIDER, 0, 0, locationListener);
                }
                Location lastLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                centerMapLocation(lastLocation, "Your location");
            }
        }
    }

    @Override
    public void onMapLongClick(LatLng latLng) {

        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        String address = "";

        try {
            List<Address> listAddresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);

            if (listAddresses != null && listAddresses.size() > 0) {
                if (listAddresses.get(0).getThoroughfare() != null) {
                    if (listAddresses.get(0).getSubThoroughfare() != null) {
                        address += listAddresses.get(0).getSubThoroughfare() + " ";
                    }
                    address += listAddresses.get(0).getThoroughfare();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        if (address == "") {

            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm yyyy-MM-dd");
            address = sdf.format(new Date());

        }

        gMap.addMarker(new MarkerOptions().position(latLng).title(address).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));

        LocationList.places.add(address);
        LocationList.locations.add(latLng);

        LocationList.arrayAdapter.notifyDataSetChanged();

        Toast.makeText(this, "Location Saved", Toast.LENGTH_SHORT).show();

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }

    public void getAllLocationData() {
        LocationList.locations.remove(new LatLng(0.0,0.0));
        for (int i = 0; i < LocationList.locations.size(); i++) {
            LatLng temp = LocationList.locations.get(i);
            latitude = temp.latitude;
            longitude = temp.longitude;
            for (int j = 0; j < LocationList.locations.size(); j++) {
                LatLng temp1 = LocationList.locations.get(j);
                end_latitude = temp1.latitude;
                end_longitude = temp1.longitude;
                getDirectionsData(latitude, longitude, end_latitude, end_longitude);
            }

        }
    }

    private void getDirectionsData(double lat, double lng, double end_lat, double end_lng) {
        String placesUrl = getUrl(lat, lng, end_lat, end_lng);

        Object dataTransfer[] = new Object[3];
        GetDirectionsData getDirectionsData = new GetDirectionsData();
        dataTransfer[0] = gMap;
        dataTransfer[1] = placesUrl;
        dataTransfer[2] = new LatLng(end_latitude, end_longitude);
        getDirectionsData.execute(dataTransfer);
    }

    private String getUrl(double lat, double lng, double end_lat, double end_lng) {
        StringBuilder placesUrl = new StringBuilder("https://maps.googleapis.com/maps/api/directions/json?");
        placesUrl.append("origin=" + latitude + "," + longitude);
        placesUrl.append("&destination=" + end_latitude + "," + end_longitude);
        placesUrl.append("&key=" + "AIzaSyA8urttD5HhLk7GMXzMQ0pU8wejrmCRJF0");

        return placesUrl.toString();
    }
}
