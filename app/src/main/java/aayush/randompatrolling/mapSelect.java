package aayush.randompatrolling;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class mapSelect extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener {

    private GoogleMap mMap;
    private String address = "";
    private LocationListener locationListener;
    private LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_select);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;



        mMap.setOnMapLongClickListener((GoogleMap.OnMapLongClickListener) this);

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
                        AlertDialog alertDialog = new AlertDialog.Builder(mapSelect.this).create();
                        alertDialog.setMessage("Long tap on the desired location");
                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                        alertDialog.show();

                    } else {
                        AlertDialog alertDialog = new AlertDialog.Builder(mapSelect.this).create();
                        alertDialog.setTitle("Location Disabled");
                        alertDialog.setMessage("Please, turn your location on. This app needs location to run");
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
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        }

    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

        try {
            List<Address> currnetAddressLists = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            if (currnetAddressLists != null && currnetAddressLists.size() > 0) {
                if (currnetAddressLists.get(0).getThoroughfare() != null) {
                    if (currnetAddressLists.get(0).getSubThoroughfare() != null) {
                        address += currnetAddressLists.get(0).getSubThoroughfare() + " ";
                        // Log.d("address",address[0]);
                    }
                    address += currnetAddressLists.get(0).getThoroughfare();
                    Log.d("address", address);
                }
                if (address.equals("")) {
                    address = String.valueOf(latLng.latitude) + " " + String.valueOf(latLng.longitude);
                }
                Intent i = new Intent(getApplicationContext(), addPlace.class);
                i.putExtra("addressName", address);
                i.putExtra("latitude", latLng.latitude);
                i.putExtra("longitude", latLng.longitude);
                startActivity(i);
            }


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void centerMapLocation(Location location, String title) {
        LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());
        mMap.clear();
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 15));
    }


}

