package com.asparagus.usclassifieds;

import android.app.Application;

import androidx.annotation.NonNull;

import com.algolia.search.saas.Client;
import com.algolia.search.saas.CompletionHandler;
import com.algolia.search.saas.Index;
import com.algolia.search.saas.Query;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map;

public class GlobalHelper extends Application {

    public static User user = null;
    private static String email = "";
    private static String userID = "";
    public static DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    public static Boolean userQueryDone = false;
    public static ArrayList<Listing> searchedListings = new ArrayList<>();
    public static MyAppGlideModule GlideApp;
    public static String userToken = "";

    public static GoogleSignInClient mGoogleSignInClient;
    public static Client mAlgoliaClient;
    public static Index mAlgoliaIndex;
    public static final String ALGOLIA_ID = "VTODAQDVW5";
    public static final String ALGOLIA_ADMIN_KEY = "a06f0b0003ffe4d67f6fe6d89fa05f9a";

    public static void signOut() {
        user = null;
        email = "";
        userID = "";
        userQueryDone = false;
        mGoogleSignInClient.signOut();
        return;
    }

    public static void setUserToken(String token) { userToken = token; }
    public static void setGoogleClient(GoogleSignInClient client) {
        mGoogleSignInClient = client;
    }

    public static User getUser() {
        return user;
    }

    public static String getEmail() {
        return email;
    }

    public static String getUserID() {
        return userID;
    }

    public static void setUser(User newUser) {
        user = newUser;
        Map<String, Object> userValues = GlobalHelper.getUser().toMap();
        FirebaseDatabase.getInstance().getReference("users").child(GlobalHelper.getUserID()).setValue(userValues);
        return;
    }

    public static void setEmail(String newEmail) {
        email = newEmail;
        System.out.println("Email set to: " + email);
        return;
    }

    public static void setID(String id) {
        userID = id;
        System.out.println("UserID set to: " + userID);
        return;
    }

    public static ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if(dataSnapshot.exists()) {
                GlobalHelper.setUser(dataSnapshot.getValue(User.class));

//                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
//                    System.out.println("User snapshot key: " + userSnapshot.getKey());
//                    System.out.println("User snapshot value: " + userSnapshot.getValue());
//                }

            } else {
                System.out.println("User does not exist");
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            System.out.println("onCancelled called for event listener");
        }
    };

    // Starts Algolia Client to begin file searches
    public static void initAlgoliaListings(){
        if(mAlgoliaIndex == null){
            mAlgoliaClient = new Client(GlobalHelper.ALGOLIA_ID, GlobalHelper.ALGOLIA_ADMIN_KEY);
        }

        mAlgoliaIndex = mAlgoliaClient.getIndex("listings");
    }

    public static void getAlgoliaListings(String select, CompletionHandler ch){
        initAlgoliaListings();
        mAlgoliaIndex.searchAsync(new Query(select), ch);

    }

    public static Comparator<Listing> priceAsc = new Comparator<Listing>(){
        @Override
        public int compare(Listing a, Listing b)
        {
            return (int) (b.getPrice() - a.getPrice());
        }
    };
    public static Comparator<Listing> priceDesc = new Comparator<Listing>(){
        @Override
        public int compare(Listing a, Listing b)
        {
            return (int) (a.getPrice() - b.getPrice());
        }
    };
    public static Comparator<Listing> distComparator = new Comparator<Listing>(){
        @Override
        public int compare(Listing a, Listing b)
        {
            double lat1, lon1, lat2, lon2, userLat, userLon, aDist, bDist;

            try {
                lat1 = Double.parseDouble(a.getLatitude());
                lat2 = Double.parseDouble(b.getLatitude());
                lon1 = Double.parseDouble(a.getLongitude());
                lon2 = Double.parseDouble(b.getLongitude());
                userLat = Double.parseDouble(getUser().getLatitude());
                userLon = Double.parseDouble(getUser().getLongitude());

            } catch(NumberFormatException nfe){
                System.out.println("NFE " + nfe.getMessage());
                lat1 = 0.0f;
                lat2 = 0.0f;
                lon1 = 0.0f;
                lon2 = 0.0f;
                userLat = 34.0522f;
                userLon = -118.2437f;
            }

            aDist = distance(lat1, lon1, userLat, userLon);
            bDist = distance(lat2, lon2, userLat, userLon);

            // account for rounding errors
            aDist *= 1000.0f;
            bDist *= 1000.0f;
            return (int) (bDist - aDist);
        }
    };

    /*
        Distance code shamelessly copied from https://stackoverflow.com/questions/3694380/calculating-distance-between-two-points-using-latitude-longitude
        Until we implement GeoSearching with Algolia
     */
    private static double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;

        dist = dist * 1.609344;

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
