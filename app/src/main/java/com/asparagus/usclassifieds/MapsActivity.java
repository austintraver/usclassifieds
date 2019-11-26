package com.asparagus.usclassifieds;

import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import static java.lang.String.format;


public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    public static User user = GlobalHelper.user;
    private static final int MY_LOCATION_REQUEST_CODE = 1;
    private Location mLastKnownLocation;
    private String TAG = MapsActivity.class.getSimpleName();
    private GoogleMap mMap;
    private LatLng defaultLatLng = new LatLng(34.021697,-118.286704);
    private ArrayList<Listing> listings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        if (user == null) {
            return;
        }
        try {
            listings = (ArrayList<Listing>) getIntent().getSerializableExtra("listingArray");
            double latitude = Double.parseDouble(user.latitude);
            double longitude = Double.parseDouble(user.longitude);
            defaultLatLng = new LatLng(latitude, longitude);

        } catch (NumberFormatException nfe) {
            Log.d(TAG, format("Number Format Exception: %s", nfe.getLocalizedMessage()));
            // Default coordinates are for Downtown Los Angeles
            double latitude = 34.021697;
            double longitude = -118.286704;
            defaultLatLng = new LatLng(latitude, longitude);
        } catch (Exception e) {
            Log.d(TAG, format("Exception: %s", e.getLocalizedMessage()));
        }
        // Get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

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
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        updateMapUI();
    }

    public void updateMapUI() {
        if (mMap == null) {
            return;
        }
        String title = String.format("%s %s", user.firstName, user.lastName);
        mMap.addMarker(new MarkerOptions().position(defaultLatLng).title(title));
        /* TODO
         *   Collect all of the listings in the current search results
         *   For each listing, pull their latitude and longitude
         *   Add a marker for each listing
         *   */
        for (Listing l : listings) {
            double lat = Double.parseDouble(l.getLatitude());
            double lng = Double.parseDouble(l.getLongitude());
            mMap.addMarker(new MarkerOptions().position(new LatLng(lat,lng)).title(l.title));
        }
        mMap.moveCamera(CameraUpdateFactory.zoomTo(15));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(defaultLatLng));
    }
}
