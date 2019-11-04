package com.asparagus.usclassifieds;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;


public class MainActivity extends AppCompatActivity {

    private static final int RC_START = 0;
    private static String email = "";
    public User user = null;

    public DatabaseClient dbClient = new DatabaseClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        System.out.println("onCreate() MAIN ");

    }

    @Override
    protected void onStart() {
        super.onStart();

        System.out.println("onStart() MAIN ");
    }

    @Override
    protected void onResume() {
        super.onResume();
        System.out.println("onResume() MAIN ");

        if(email.equals("")) {
            System.out.println("start sign in intent");
            Intent signInIntent = new Intent(this, SignIn.class);
            startActivityForResult(signInIntent, RC_START);
        } //else if(user == null) {
            //TODO --> check if user is in DB, if not go to edit_profile activity and update DB, o.w. go to homepage
        //}
        else {
            Intent homePageIntent = new Intent(this, Home.class);
            homePageIntent.putExtra("dbClient",dbClient);
            homePageIntent.putExtra("user", user);

        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        System.out.println("onPause() MAIN ");
    }

    @Override
    protected void onStop() {
        super.onStop();
        System.out.println("onStop() MAIN ");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        System.out.println("onActivityResult() MAIN ");

        if(resultCode == Activity.RESULT_OK) {
            email = data.getStringExtra("email");
            System.out.println("Signed in user in MAIN: " + email);
        } else if(resultCode == Activity.RESULT_CANCELED) {
            //TODO --> Sign out and redirect back to sign in activity
        }
    }

}
