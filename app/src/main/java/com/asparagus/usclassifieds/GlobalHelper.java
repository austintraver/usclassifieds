package com.asparagus.usclassifieds;

import android.app.Application;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class GlobalHelper extends Application {

    public static User user = new User("jltanner@usc.edu");

    public static DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

    public static void insert(){

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
//                String value = dataSnapshot.getValue(String.class);
//                System.out.println("Value is: " + value);
            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                //Log.w(TAG, “Failed to read value.“, error.toException());
            }
        });
//        mDatabase.setValue("John Tanner");
//        mDatabase.setValue("Charlie Pyle");
        mDatabase.child("users").child("cpyle").setValue("Charlie Pyle");
        //mDatabase.child("users").child("jltanner@usc").child("name").setValue("John Tanner");
        //mDatabase.child("user").child("cpyle@usc").child("name").setValue("Charlie Pyle");

    }
}
