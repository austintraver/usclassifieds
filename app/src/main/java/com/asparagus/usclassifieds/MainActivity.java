package com.asparagus.usclassifieds;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends Activity {

    private static final int RC_START = 2;
    private static final int RC_START2 = 4;
    private static final int RC_STOP = 3;
    private static final String TAG = MainActivity.class.getSimpleName();
    private static FirebaseAuth mAuth;  //TODO
    private static FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance(); //TODO

        /*
        File file = new File("./firebase_key.json");
        try {
            FileInputStream serviceAccount =
                    new FileInputStream(file);
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setDatabaseUrl("https://usclassifieds-c83d5.firebaseio.com")
                    .build();

            FirebaseApp.initializeApp(options);
        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        */

        System.out.println("onCreate() MAIN ");

        /*
        GlobalHelper.insert();
        System.out.println("Inserted successfully");
        Point tempPoint = new Point(new Position(34,-118));
        Query query = FirebaseDatabase.getInstance().getReference("users").child(userID);
        query.addListenerForSingleValueEvent(valueEventListener);

        GlobalHelper.addNewUser("jltanner@usc.edu","John","Tanner","9498128890","12345678");
        System.out.println("users/123456789: " + GlobalHelper.userExists("123456789"));
        */

        /*
        i needed to pass the context this into the person object when i was testing a different
        method, so right now we don't *need* the context variable. however, if the async extended
        class ends up not working, we may need to keep it here, so i'm leaving it for
        safety reasons right now

        these lines of code handle geo-encoding. uncomment them to test what's returned from
        different addresses or users
        */

        /*
        String address = "1279 West 37th Place, Los Angeles, CA, 90007";
        User u = new User("cpyle@usc.edu", "Charlie", "Pyle", "1234567890", this, address, "1234312");
        */

        /*
        below is the async class that handles HTTP requests
        new GetCoordinates().execute(address);
        */

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
        if (GlobalHelper.getEmail().equals("")) {
            System.out.println("start sign in intent");
            Intent signInIntent = new Intent(this, SignInActivity.class);
            startActivityForResult(signInIntent, RC_START);
        } else if (GlobalHelper.getUser() == null) {
            System.out.println("In second if statement: null");
            Intent createUserIntent = new Intent(this, EditProfileActivity.class);
            startActivityForResult(createUserIntent, RC_START2);
            // TODO --> check if person is in DB, if not go to edit_profile activity and update DB, o.w. go to homepage
        }
        else {
            user = mAuth.getCurrentUser();
            if (user != null) {
                System.out.println("Firebase person is not authenticated.");
            } else {
                mAuth.signInAnonymously();
            }

            System.out.println("Start home page intent: " + GlobalHelper.getUser());
            Intent homePageIntent = new Intent(this, HomeActivity.class);
            startActivityForResult(homePageIntent, RC_STOP);
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
            System.out.println("User successfully logged in!!!");
            // TODO --> check if person is in Firebase, if not go to edit_profile activity and update DB, o.w. go to homepage
            // TODO --> use GlobalHelper.setUser( *** ) here if person is found

        } else if(resultCode == Activity.RESULT_CANCELED) {
            // TODO --> Sign out and redirect back to sign in activity
            GlobalHelper.setEmail("");
            GlobalHelper.setID("");
            GlobalHelper.signOut();
        }
    }
}

