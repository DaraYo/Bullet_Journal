package com.example.bullet_journal.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.example.bullet_journal.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback {

    private SupportMapFragment mapView;
    private GoogleMap googleMap;
    private static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";
    private FusedLocationProviderClient fusedProviderClient;

    private LatLng theLocation;
    private String locationTitle;

    private EditText searchBar;
    private ImageButton searchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }

        fusedProviderClient = LocationServices.getFusedLocationProviderClient(this);

        mapView = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.the_map);

        mapView.getMapAsync(this);

        searchBar = findViewById(R.id.map_search_bar);

        searchButton = findViewById(R.id.map_search_btn);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                geoLocate();
            }
        });

        Button okButton = findViewById(R.id.map_btn_ok);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(theLocation != null){
                    Intent intent = getIntent();
                    intent.putExtra("success", true);
                    intent.putExtra("lat", theLocation.latitude);
                    intent.putExtra("long", theLocation.longitude);
                    intent.putExtra("title", locationTitle);
                    setResult(RESULT_OK, intent);
                    finish();
                }else{
                    Intent intent = getIntent();
                    intent.putExtra("success", false);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });

        getLastDeviceLocation();
    }

    private void geoLocate(){
        String searchString = searchBar.getText().toString();

        Geocoder geocoder = new Geocoder(MapActivity.this);
        List<Address> list = new ArrayList<>();

        try {
            list = geocoder.getFromLocationName(searchString, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(list.size() > 0){
            Address address = list.get(0);
            LatLng selectedLocation = new LatLng(address.getLatitude(), address.getLongitude());
            locationTitle = address.getAddressLine(0);
            moveCamera(selectedLocation);
            fadeKeyboard();
        }else{
            Toast.makeText(MapActivity.this, R.string.maps_no_results, Toast.LENGTH_SHORT).show();
        }
    }

    private void getLastDeviceLocation() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        fusedProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                if(task.isSuccessful()){
                    Location location = task.getResult();
                    moveCamera(new LatLng(location.getLatitude(), location.getLongitude()));
                }
            }
        });
    }

    private void moveCamera(LatLng selectedLocation){
        this.theLocation = selectedLocation;
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(selectedLocation));
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(10));

        MarkerOptions markerOptions = new MarkerOptions().position(selectedLocation);
        googleMap.addMarker(markerOptions);
    }

    private void fadeKeyboard(){
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
        if (ActivityCompat.checkSelfPermission(MapActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MapActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        googleMap.setMyLocationEnabled(true);
        this.googleMap = googleMap;
    }
}
