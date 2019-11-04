package com.asparagus.usclassifieds;

import android.app.Application;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class GlobalHelper extends Application {

    public static User user = null;
    private static String email = "";
    private static String userID = "";
    public static DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    public static GoogleSignInClient mGoogleSignInClient;


    public static void signOut() {
        mGoogleSignInClient.signOut();
        return;
    }

    public static GoogleSignInClient getGoogleClient() {
        return mGoogleSignInClient;
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
        System.out.println("User set with name: " + user.getFirstName() + user.getLastName());
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

    //public static void
    public static void insert(){

//        mDatabase.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                // This method is called once with the initial value and again
//                // whenever data at this location is updated.
//                String value = dataSnapshot.getValue(String.class);
//                System.out.println("Value is: " + value);
//            }
//            @Override
//            public void onCancelled(DatabaseError error) {
//                // Failed to read value
//                //Log.w(TAG, “Failed to read value.“, error.toException());
//            }
//        });

//        mDatabase.setValue("John Tanner");
//        mDatabase.setValue("Charlie Pyle");
        mDatabase.child("users").child("cpyle").setValue("Charlie Pyle");
        //mDatabase.child("users").child("jltanner@usc").child("name").setValue("John Tanner");
        //mDatabase.child("user").child("cpyle@usc").child("name").setValue("Charlie Pyle");

    }
}
