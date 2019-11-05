package com.asparagus.usclassifieds;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
//import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.mongodb.client.model.geojson.Point;
import com.mongodb.client.model.geojson.Position;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URLEncoder;

//import static com.asparagus.usclassifieds.GlobalHelper.valueEventListener;

public class MainActivity extends AppCompatActivity {

    private static final int RC_START = 2;
    private static final int RC_STOP = 3;
    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


//       File file = new File("./firebase-admin-key.json");
//       try {
//           FileInputStream serviceAccount =
//                   new FileInputStream(file);
//           FirebaseOptions options = new FirebaseOptions.Builder()
//                   .setCredentials(GoogleCredentials.fromStream(serviceAccount))
//                   .setDatabaseUrl("https://usclassifieds-c83d5.firebaseio.com")
//                   .build();
//
//           FirebaseApp.initializeApp(options);
//       } catch (FileNotFoundException fnfe) {
//           fnfe.printStackTrace();
//       } catch (IOException ioe) {
//           ioe.printStackTrace();
//       }



        System.out.println("onCreate() MAIN ");
//
//        GlobalHelper.insert();
//        System.out.println("Inserted successefully");
//        Point tempPoint = new Point(new Position(34,-118));
//        Query query = FirebaseDatabase.getInstance().getReference("users").child(userID);
//        query.addListenerForSingleValueEvent(valueEventListener);
//
//        GlobalHelper.addNewUser("jltanner@usc.edu","John","Tanner","9498128890","12345678");
//        System.out.println("users/123456789: " + GlobalHelper.userExists("123456789"));

       //i needed to pass the context this into the user object when i was testing a different method, so right now we don't *need* the context variable.
        //however, if the async extended class ends up not working, we may need to keep it here, so i'm leaving it for safety reasons right now

        /*

        these lines of code handle geoencoding. uncomment them to test what's returned from
        different addresses or users

        String address = "1279 West 37th Place, Los Angeles, CA, 90007";
        User u = new User("cpyle@usc.edu", "Charlie", "Pyle", "1234567890", this, address, "1234312");

        //below is the async class that handles HTTP requests
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



        if(GlobalHelper.getEmail().equals("")) {

            System.out.println("start sign in intent");
            Intent signInIntent = new Intent(this, SignIn.class);
            startActivityForResult(signInIntent, RC_START);

        }
        else if(GlobalHelper.getUser() == null) {

            System.out.println("In second if statement: " + GlobalHelper.getUser());
            //TODO --> check if user is in DB, if not go to edit_profile activity and update DB, o.w. go to homepage
        }
        else {
            System.out.println("Start home page intent: " + GlobalHelper.getUser());
            Intent homePageIntent = new Intent(this, Home.class);
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

            //TODO --> check if user is in Firebase, if not go to edit_profile activity and update DB, o.w. go to homepage
            //TODO --> use GlobalHelper.setUser( *** ) here if user is found

        } else if(resultCode == Activity.RESULT_CANCELED) {
            //TODO --> Sign out and redirect back to sign in activity
            GlobalHelper.setEmail("");
            GlobalHelper.setID("");
            GlobalHelper.signOut();
        }

        //used to get client token and set that for logged in user
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult().getToken();
                        System.out.println("token: " + token);

                        /* sets the client ID on logged in user, will be used to send notifications on
                         database updates for friend requests */
                        GlobalHelper.getUser().setClientToken(token);

                        // Log and toast
                        String msg = getString(R.string.msg_token_fmt, token);
                        Log.d(TAG, msg);
                        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /* the class below is used to handle asynchronous callouts. it might make more sense to move this to the global file later,
    * but i have it here right now for convenience
    * */

}

