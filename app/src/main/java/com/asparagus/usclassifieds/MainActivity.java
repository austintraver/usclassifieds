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
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.mongodb.client.model.geojson.Point;
import com.mongodb.client.model.geojson.Position;

import java.net.URLEncoder;

import static com.asparagus.usclassifieds.GlobalHelper.valueEventListener;


public class MainActivity extends AppCompatActivity {

    private static final int RC_START = 2;
    private static final int RC_STOP = 3;
    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
        String address = "1279 West 37th Place, Los Angeles, CA, 90007";
        User u = new User("cpyle@usc.edu", "Charlie", "Pyle", "1234567890", this, address, "1234312");
        System.out.println("past user");

        //below is the async class that handles HTTP requests
        new GetCoordinates().execute(address);
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

                        // Log and toast
                        String msg = getString(R.string.msg_token_fmt, token);
                        Log.d(TAG, msg);
                        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });

        if(GlobalHelper.getEmail().equals("")) {
            System.out.println("start sign in intent");
            Intent signInIntent = new Intent(this, SignIn.class);
            startActivityForResult(signInIntent, RC_START);
        } else if(GlobalHelper.getUser() == null) {
            //TODO --> check if user is in DB, if not go to edit_profile activity and update DB, o.w. go to homepage
        }
        else {
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
            GlobalHelper.userExists(GlobalHelper.getUserID());
            //userID and email should already be set here for GlobalHelper
            //TODO --> check if user is in Firebase, if not go to edit_profile activity and update DB, o.w. go to homepage
            //TODO --> use GlobalHelper.setUser( *** ) here if user is found

        } else if(resultCode == Activity.RESULT_CANCELED) {
            //TODO --> Sign out and redirect back to sign in activity
            GlobalHelper.setEmail("");
            GlobalHelper.setID("");
            GlobalHelper.signOut();
        }
    }
    /* the class below is used to handle asynchronous callouts. it might make more sense to move this to the global file later,
    * but i have it here right now for convenience
    * */
    private class GetCoordinates extends AsyncTask<String,Void,String> {
        ProgressDialog dialog = new ProgressDialog(MainActivity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setMessage("Please wait....");
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            String response;
            try{
                String address = strings[0];
                String middle = URLEncoder.encode(address,"UTF-8");
                String key = "&key=AIzaSyDfuYE5Tc8sY6t42ZWr9A7K1xEhS6U9rnI";
                System.out.println("addy: " + address);
                HttpDataHandler http = new HttpDataHandler();
                String url = "https://maps.googleapis.com/maps/api/geocode/json?address=" + middle + key;
                System.out.println("full url: " + url);
                response = http.getHTTPData(url);
                System.out.println("resp is: " + response);
                return response;
            }
            catch (Exception ex)
            {

            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            try{
                JSONObject jsonObject = new JSONObject(s);

                String lat = ((JSONArray)jsonObject.get("results")).getJSONObject(0).getJSONObject("geometry")
                        .getJSONObject("location").get("lat").toString();
                String lng = ((JSONArray)jsonObject.get("results")).getJSONObject(0).getJSONObject("geometry")
                        .getJSONObject("location").get("lng").toString();
                System.out.println("latitude and longitude" + lat + " " + lng);
                //txtCoord.setText(String.format("Coordinates : %s / %s ",lat,lng));

                if(dialog.isShowing())
                    dialog.dismiss();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


}

