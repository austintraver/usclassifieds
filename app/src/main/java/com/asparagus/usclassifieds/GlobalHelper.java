package com.asparagus.usclassifieds;

import android.app.Application;
import android.app.ProgressDialog;
import android.os.AsyncTask;

import androidx.annotation.NonNull;

import com.mongodb.client.model.geojson.Point;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;

public class GlobalHelper extends Application {

    public static User user = null;
    private static String email = "";
    private static String userID = "";
    public static DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    public static Boolean userQueryDone = false;
    public static ArrayList<Listing> searchedListings = new ArrayList<>();

    public static GoogleSignInClient mGoogleSignInClient;

    public static void signOut() {
        user = null;
        email = "";
        userID = "";
        userQueryDone = false;
        mGoogleSignInClient.signOut();
        return;
    }

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
        System.out.println("User set with name: " + user.getFirstName() + " " + user.getLastName());
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
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    System.out.println("User snapshot key: " + userSnapshot.getKey());
                    System.out.println("User snapshot value: " + userSnapshot.getValue());
                }

            } else {
                System.out.println("User does not exist");
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            System.out.println("onCancelled called for event listener");
        }
    };
}
