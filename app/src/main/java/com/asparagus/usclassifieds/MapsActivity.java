package com.asparagus.usclassifieds;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;

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



public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final int MY_LOCATION_REQUEST_CODE = 1;
    private GoogleMap mMap;
    private boolean mLocationPermissions;

    //private Location mLastLocation = null;
    private double lat, lng;
    private LatLng defaultLatLng;

    private Location mLastKnownLocation;

    // The entry point to the Fused Location Provider.
    private FusedLocationProviderClient mFusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

        lat = Double.parseDouble(GlobalHelper.getUser().getLatitude());
        lng = Double.parseDouble(GlobalHelper.getUser().getLongitude());
        defaultLatLng = new LatLng(lat,lng);

        // Construct a FusedLocationProviderClient.
        //mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


//    private void getLocationPermissions() {
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
//                == PackageManager.PERMISSION_GRANTED) {
//            mLocationPermissions = true;
//        } else {
//            ActivityCompat.requestPermissions(this,
//                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
//                    MY_LOCATION_REQUEST_CODE);
//        }
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//        mLocationPermissions = false;
//        if (requestCode == MY_LOCATION_REQUEST_CODE) {
//            if (permissions.length == 1 &&
//                    permissions[0] == Manifest.permission.ACCESS_FINE_LOCATION &&
//                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                mLocationPermissions = true;
//            } else {
//                // Permission was denied. Display an error message.
//            }
//        }
//        updateMapUI();
//    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the person will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the person has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        Intent intent = getIntent();

        //Request Location Permissions
        //getLocationPermissions();

        updateMapUI();
//        try {
//            if (mLocationPermissions) {
//
//                Task<Location> locationResult = mFusedLocationProviderClient.getLastLocation();
//                locationResult.addOnCompleteListener(this, new OnCompleteListener<Location>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Location> task) {
//                        if (task.isSuccessful() && task.getResult() != null) {
//                            // Set the map's camera position to the current location of the device.
//                            mLastKnownLocation = task.getResult();
//                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
//                                    new LatLng(mLastKnownLocation.getLatitude(),
//                                            mLastKnownLocation.getLongitude()), 14));
//                        } else {
////                            Log.d(TAG, "Current location is null. Using defaults.");
////                            Log.e(TAG, "Exception: %s", task.getException());
//                            mMap.moveCamera(CameraUpdateFactory
//                                    .newLatLngZoom(defaultLatLng, 14));
//                            mMap.getUiSettings().setMyLocationButtonEnabled(false);
//                        }
//                    }
//                });
//            }
//        } catch (SecurityException e)  {
//            //Log.e("Exception: %s", e.getMessage());
//        }
        // Add a marker in USC and move the camera

//        mMap.addMarker(new MarkerOptions().position(defaultLatLng).title("USC Campus"));
//        mMap.moveCamera(CameraUpdateFactory.zoomTo(14));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(defaultLatLng));
    }

    public void updateMapUI() {
        if(mMap == null)
            return;
        else {
//            if(mLocationPermissions == true) {
//                mMap.setMyLocationEnabled(true);
//                mMap.getUiSettings().setMyLocationButtonEnabled(true);
//            } else {
//                mMap.setMyLocationEnabled(false);
//                mMap.getUiSettings().setMyLocationButtonEnabled(false);
//                //mLastLocation = null;
//                getLocationPermissions();
//            }
            mMap.addMarker(new MarkerOptions().position(defaultLatLng).title(GlobalHelper.getUser().getFirstName() + " " + GlobalHelper.getUser().getLastName()));
//            for (Listing l : GlobalHelper.searchedListings) {
//                Double tempLat = Double.parseDouble(l.getLatitude());
//                Double tempLng = Double.parseDouble(l.getLongitude());
//                mMap.addMarker(new MarkerOptions().position(new LatLng(tempLat,tempLng)).title(l.getTitle()));
//            }
            mMap.moveCamera(CameraUpdateFactory.zoomTo(14));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(defaultLatLng));
        }
    }
}
