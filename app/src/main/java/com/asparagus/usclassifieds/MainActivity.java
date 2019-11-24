package com.asparagus.usclassifieds;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.util.Map;

import static com.google.android.gms.common.internal.safeparcel.SafeParcelable.NULL;

public class MainActivity extends Activity {

    private static final int RC_START = 2;
    private static final int RC_START2 = 4;
    private static final int RC_STOP = 3;
    private static final String TAG = MainActivity.class.getSimpleName();
    private static FirebaseAuth mAuth;
    private static String emailError = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.e(TAG, "onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance(); // TODO
        System.out.println("onCreate() MAIN ");

        // Used to get client token and set that for logged in person
        FirebaseInstanceId.getInstance().getInstanceId()
                          .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                              @Override
                              public void onComplete(@NonNull Task<InstanceIdResult> task) {
                                  if (!task.isSuccessful()) {
                                      Log.w(TAG, "getInstanceId failed", task.getException());
                                      return;
                                  }

                                  // Get new Instance ID token
                                  if (task.getResult() != null) {
                                      GlobalHelper.userToken = task.getResult().getToken();
                                  }
                              }
                          });

    }

    @Override
    protected void onStart() {
        Log.e(TAG, "onStart()");
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG, "onResume()");
        if (GlobalHelper.user == null) {
            System.out.println("start sign in intent");
            Intent signInIntent = new Intent(this, SignInActivity.class);
            signInIntent.putExtra("emailError", emailError);
            startActivityForResult(signInIntent, RC_START);
            return;
        }
        if (GlobalHelper.user.notificationTokens.get(GlobalHelper.userToken) == NULL) {
            GlobalHelper.user.notificationTokens.put(GlobalHelper.userToken, "true");
            Map<String, Object> userValues = GlobalHelper.user.toMap();
            FirebaseDatabase.getInstance().getReference("users").child(GlobalHelper.user.userID).setValue(userValues);
        }
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            System.out.println("Firebase person is not authenticated.");
        }
        else {
            mAuth.signInAnonymously();
        }

        System.out.println("Start home page intent: " + GlobalHelper.user);
        Intent homePageIntent = new Intent(this, HomeActivity.class);
        startActivityForResult(homePageIntent, RC_STOP);
    }

    @Override
    protected void onPause() {
        Log.e(TAG, "onPause()");
        super.onPause();
    }

    @Override
    protected void onStop() {
        Log.e(TAG, "onStop()");
        super.onStop();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.e(TAG, "onActivityResult()");

        if (resultCode == Activity.RESULT_OK) {
            System.out.println("User successfully logged in!!!");
            // TODO --> check if person is in Firebase, if not go to edit_profile activity and update DB, o.w. go to
            //  homepage
            // TODO --> use GlobalHelper.setUser( *** ) here if person is found

        } else if(resultCode == Activity.RESULT_CANCELED) {
            // TODO --> Sign out and redirect back to sign in activity
            GlobalHelper.user = null;
            GlobalHelper.signOut();
        } else if (resultCode == 4567) {
            emailError = "Please use a valid @usc.edu email to sign in.";
            GlobalHelper.signOut();
        }
    }
}

