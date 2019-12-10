package com.asparagus.usclassifieds;

import android.annotation.SuppressLint;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map;

import static java.lang.String.format;

@SuppressLint("StaticFieldLeak")
public class GlobalHelper {

    static final String ALGOLIA_ID = "VTODAQDVW5";
    static final String ALGOLIA_ADMIN_KEY = "a06f0b0003ffe4d67f6fe6d89fa05f9a";
    public static User user;
    public static String otherUser = "";
    public static final int QUERY_RESULTS_LENGTH = 25;

    static DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    static Boolean userQueryDone = false;
    static ArrayList<Listing> searchedListings = new ArrayList<>();
    static String userToken;
    private static String email = "";
    private static String userID = "";
    private static ArrayList<User> activeUsers = new ArrayList<>();
    private static ArrayList<String> userNames = new ArrayList<>();

    public static Boolean justSoldItem = false;
    public static Listing soldItem = null;

    public static ArrayList<String> getUserNames() { return userNames; }
    public static ArrayList<User> getActiveUsers() { return activeUsers; }

    public static User getUser() { return user; }

    public static String getEmail() {
        return email;
    }

    public static String getUserID() {
        return userID;
    }

    public static void setUserToken(String token) { userToken = token; }

    public static void setEmail(String newEmail) {
        email = newEmail;
        return;
    }

    public static void setID(String id) {
        userID = id;
        return;
    }

    static Comparator<Listing> priceAsc = new Comparator<Listing>() {
        @Override
        public int compare(Listing a, Listing b) {
            return (int) (a.getPrice() - b.getPrice());
        }
    };
    static Comparator<Listing> priceDesc = new Comparator<Listing>() {
        @Override
        public int compare(Listing a, Listing b) {
            return (int) (b.getPrice() - a.getPrice());
        }
    };

    private static boolean debug = false;
    public static void setDebug(boolean b){debug = b;}
    public static boolean getDebug(){return debug;}

    public static User getTestUser(){
        User u = new User(
                "tommytrojan@usc.edu",
                "Tommy",
                "Trojan",
                "9999999999",
                "999243817665695620666",
                "123",
                "Sesame Street",
                "Los Angeles",
                "CA",
                "90007",
                "An absolute unit");
        u.latitude = "34.021697";
        u.longitude = "-118.286704";
        return u;
    }

    public static void setTestUser(){
        user = getTestUser();
    }

    public static boolean isValidEmail(String email) {
        if (email != null) {
            if (email.endsWith("@usc.edu")) {
                return true;
            }
        }
        return false;
    }

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
            } catch (Exception e) {
                Log.d(TAG, format("Exception: %s", e.getMessage()));
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
            return (int) (aDist - bDist);
        }
    };
    private static GoogleSignInClient mGoogleSignInClient;

    static void setGoogleClient(GoogleSignInClient client) {
        mGoogleSignInClient = client;
    }

    static void setUser(User newUser) {
        if (newUser == null || newUser.userID == null || GlobalHelper.getDebug()) {
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
    public static double distance(
            double lat1, double lon1, double lat2, double lon2
    ) {
        DecimalFormat df = new DecimalFormat("#.##");

        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) +
                      Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist) * 60 * 1.1515 * 1.609344;
        return (Double.parseDouble(df.format(dist)));
    }

    //  Converts decimal degrees to radians
    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    //  Converts radians to decimal degrees
    private static double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

    public static void startUserFill(Query query, final OnGetDataListener listener) {
        listener.onStart();
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listener.onSuccess(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("ValueEventListener", "onCancelled()");
                listener.onFailure();
            }
        };
        query.addListenerForSingleValueEvent(eventListener);
    }

    public static void updateUserList() {

        activeUsers.clear();
        userNames.clear();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        Query query = databaseReference.child("users");
        OnGetDataListener listener = new OnGetDataListener() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    for(DataSnapshot userData: dataSnapshot.getChildren()) {
                        User u = userData.getValue(User.class);
                        if(!activeUsers.contains(u) && !u.userID.equals(user.userID)) {
                            activeUsers.add(u);
                            userNames.add(u.firstName + " " + u.lastName);
                        }
                    }

                }
                else {
                    Log.w("OnGetDataListener", "Error with updating list of active users.");
                }
            }
            @Override
            public void onStart() {
                Log.d("onGetDataListener", "onStart()");
            }
            @Override
            public void onFailure() {
                Log.d("OnGetDataListener", "onFailure()");
            }
        };
        startUserFill(query, listener);
    }
}
