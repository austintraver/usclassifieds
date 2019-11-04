package com.asparagus.usclassifieds;

import android.app.Application;

import androidx.annotation.NonNull;

import com.google.firebase.database.Query;
import com.mongodb.client.model.geojson.Point;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mongodb.client.model.geojson.Position;

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

    public static void userExists(String userID) {
//        mDatabase.child("users").child("105390386330726279653").child("firstName").setValue("Cameron");
////        mDatabase.child("users").child("105390386330726279653").child("lastName").setValue("Durham");
////        mDatabase.child("users").child("105390386330726279653").child("email").setValue("cdurham@usc");
////        Point tempPoint = new Point(new Position(34,-118));
////        mDatabase.child("users").child("105390386330726279653").child("location").setValue(tempPoint);
////        mDatabase.child("users").child("105390386330726279653").child("userID").setValue("105390386330726279653");
            mDatabase.child("users").child("105390386330726279653").child("phone").setValue("9499119111");
            mDatabase.child("users").child("105390386330726279653").child("address").setValue("1279 W 37 Place, Los Angeles, CA, 90007");


        //System.out.println("Query result: " + mDatabase.child("users").child("12345678").child("email")); //.equalTo(userID));
        Query query = FirebaseDatabase.getInstance().getReference("users").child(userID);
        query.addListenerForSingleValueEvent(valueEventListener);
        //System.out.println("New query: " + query);
        //return mDatabase.child("users").child(userID).equals(userID);
    }

    public static ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if(dataSnapshot.exists()) {
                System.out.println(dataSnapshot.getValue(User.class));
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





    public static void addNewUser(String email, String first, String last, String phone, Point loc, String userID) {
        Boolean key = mDatabase.child("users").child(userID).child("email").toString().equals("");
        //System.out.println("users/" + userID + ": " + key);
    }


    //public static void
    //public static void insert(){

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
    //mDatabase.child("users").child("cpyle").setValue("Charlie Pyle");

    //mDatabase.child("users").child("jltanner@usc").child("name").setValue("John Tanner");
        //mDatabase.child("user").child("cpyle@usc").child("name").setValue("Charlie Pyle");

   // }
}
