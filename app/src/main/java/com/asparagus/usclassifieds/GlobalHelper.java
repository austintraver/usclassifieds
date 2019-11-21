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

}
