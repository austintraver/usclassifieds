package com.asparagus.usclassifieds;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.util.HashMap;
import java.util.Map;

import static com.google.android.gms.common.internal.safeparcel.SafeParcelable.NULL;

public class MainActivity extends Activity {

    private static final int RC_START = 2;
    private static final int RC_START2 = 4;
    private static final int RC_STOP = 3;
    private static final String TAG = MainActivity.class.getSimpleName();
    private static FirebaseAuth mAuth;
    private static FirebaseUser user;
    private static String emailError = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();

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
                        String token = task.getResult().getToken();
                        GlobalHelper.setUserToken(token);

                        // Log and toast
                        //String msg = getString(R.string.msg_token_fmt, token);
                        // Log.d(TAG, msg);
                        // Toast.makeText(HomeActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(GlobalHelper.getDebug()){
            GlobalHelper.setTestUser();
            mAuth.signInAnonymously();
            Intent homePageIntent = new Intent(this, HomeActivity.class);
            startActivityForResult(homePageIntent, RC_STOP);

        } else if (GlobalHelper.getEmail().equals("")) {
            Intent signInIntent = new Intent(this, SignInActivity.class);
            signInIntent.putExtra("emailError", emailError);
            startActivityForResult(signInIntent, RC_START);
        } else if (GlobalHelper.getUser() == null) {
            Intent createUserIntent = new Intent(this, EditProfileActivity.class);
            startActivityForResult(createUserIntent, RC_START2);
        }
        else {
            if(GlobalHelper.getUser().getNotificationTokens() == null) {
                GlobalHelper.getUser().notificationTokens = new HashMap<>();
                GlobalHelper.getUser().getNotificationTokens().put(GlobalHelper.userToken, GlobalHelper.userToken);
                Map<String, Object> userValues = GlobalHelper.getUser().toMap();
                FirebaseDatabase.getInstance().getReference("users").child(GlobalHelper.getUserID()).setValue(userValues);
            }
            else if(GlobalHelper.getUser().getNotificationTokens().get(GlobalHelper.userToken) == NULL) {

                GlobalHelper.getUser().getNotificationTokens().put(GlobalHelper.userToken, GlobalHelper.userToken);
                Map<String, Object> userValues = GlobalHelper.getUser().toMap();
                FirebaseDatabase.getInstance().getReference("users").child(GlobalHelper.getUserID()).setValue(userValues);
            }

            user = mAuth.getCurrentUser();
            if (user != null) {
                System.out.println("Firebase person is not authenticated.");
            } else {
                mAuth.signInAnonymously();
            }

            Intent homePageIntent = new Intent(this, HomeActivity.class);
            startActivityForResult(homePageIntent, RC_STOP);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == Activity.RESULT_OK) {
            System.out.println("User successfully logged in!!!");
        } else if(resultCode == Activity.RESULT_CANCELED) {
            GlobalHelper.setEmail("");
            GlobalHelper.setID("");
            GlobalHelper.signOut();
        } else if (resultCode == 4567) {
            emailError = "Please use a valid @usc.edu email to sign in.";
            GlobalHelper.signOut();
        }
    }
}

