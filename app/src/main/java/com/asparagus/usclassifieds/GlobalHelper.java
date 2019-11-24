package com.asparagus.usclassifieds;

import android.annotation.SuppressLint;
import android.util.Log;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map;

import static java.lang.String.format;

@SuppressLint("StaticFieldLeak")
public class GlobalHelper {

    static final String ALGOLIA_ID = "VTODAQDVW5";
    static final String ALGOLIA_ADMIN_KEY = "a06f0b0003ffe4d67f6fe6d89fa05f9a";
    public static User user;
    static DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    static Boolean userQueryDone = false;
    static ArrayList<Listing> searchedListings = new ArrayList<>();
    static String userToken;
    static Comparator<Listing> priceAsc = new Comparator<Listing>() {
        @Override
        public int compare(Listing a, Listing b) {
            return (int) (b.getPrice() - a.getPrice());
        }
    };
    static Comparator<Listing> priceDesc = new Comparator<Listing>() {
        @Override
        public int compare(Listing a, Listing b) {
            return (int) (a.getPrice() - b.getPrice());
        }
    };
    private static String TAG = GlobalHelper.class.getSimpleName();
    static Comparator<Listing> distComparator = new Comparator<Listing>() {
        @Override
        public int compare(Listing a, Listing b) {
            double lat1, lon1, lat2, lon2, userLatitude, userLongitude, aDist, bDist;
            try {
                lat1 = Double.parseDouble(a.getLatitude());
                lat2 = Double.parseDouble(b.getLatitude());
                lon1 = Double.parseDouble(a.getLongitude());
                lon2 = Double.parseDouble(b.getLongitude());
                userLatitude = Double.parseDouble(user.latitude);
                userLongitude = Double.parseDouble(user.longitude);
            } catch (NumberFormatException nfe) {
                Log.d(TAG, format("NumberFormatException: %s", nfe.getMessage()));
                lat1 = 0.0f;
                lat2 = 0.0f;
                lon1 = 0.0f;
                lon2 = 0.0f;
                userLatitude = 34.0522f;
                userLongitude = -118.2437f;
            }
            // Accounting for rounding errors
            aDist = distance(lat1, lon1, userLatitude, userLongitude) * 1000.0f;
            bDist = distance(lat2, lon2, userLatitude, userLongitude) * 1000.0f;
            return (int) (bDist - aDist);
        }
    };
    private static GoogleSignInClient mGoogleSignInClient;

    static void setGoogleClient(GoogleSignInClient client) {
        mGoogleSignInClient = client;
    }

    static void setUser(User newUser) {
        if (newUser == null || newUser.userID == null) {
            return;
        }
        user = newUser;
        Map<String, Object> userValues = user.toMap();
        FirebaseDatabase.getInstance().getReference("users").child(user.userID).setValue(userValues);
    }

    static void signOut() {
        user = null;
        userQueryDone = false;
        if (mGoogleSignInClient != null) {
            mGoogleSignInClient.signOut();
        }
    }

    /*
        Distance code shamelessly copied from
        https://stackoverflow
        .com/questions/3694380/calculating-distance-between-two-points-using
        -latitude-longitude
        Until we implement GeoSearching with Algolia
     */
    private static double distance(
            double lat1, double lon1, double lat2, double lon2
    ) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) +
                      Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist) * 60 * 1.1515 * 1.609344;
        return (dist);
    }

    //  Converts decimal degrees to radians
    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    //  Converts radians to decimal degrees
    private static double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

}
