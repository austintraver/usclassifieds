package com.asparagus.usclassifieds;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class SignIn extends AppCompatActivity {

    private static final String TAG = "";
    private static final int RC_SIGN_IN = 1;

    public GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        GlobalHelper.setGoogleClient(mGoogleSignInClient);


        findViewById(R.id.sign_in_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.sign_in_button:
                        signIn();
                        break;
                }
            }

            private void signIn() {
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
//        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
//
//        if(account != null)
//            updateUI(account);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            // Signed in successfully, show authenticated UI.
            updateUI(account);    //TODO

        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());

            updateUI(null);       //TODO
        }
    }

    public void getUser(Query query, final OnGetDataListener listener) {
        listener.onStart();
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listener.onSuccess(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("onCancelled called for event listener");
                listener.onFailure();
            }
        });
    }



    public void updateUI(GoogleSignInAccount account) {
        if (account == null) {
            startActivity(getIntent());
            return;
        }
        else {
            final Intent resultEmail = new Intent();
            GlobalHelper.setEmail(account.getEmail());
            GlobalHelper.setID(account.getId());

            Query query = FirebaseDatabase.getInstance().getReference("users").child(GlobalHelper.getUserID());
            //query.addListenerForSingleValueEvent(valueEventListener);
            getUser(query, new OnGetDataListener() {
                @Override
                public void onSuccess(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()) {
                        GlobalHelper.setUser(dataSnapshot.getValue(User.class));
                        GlobalHelper.userQueryDone = true;
//                        for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
//                            System.out.println("User snapshot key: " + userSnapshot.getKey());
//                            System.out.println("User snapshot value: " + userSnapshot.getValue());
//                        }

                    } else {
                        System.out.println("User does not exist");
                    }

                    setResult(Activity.RESULT_OK, resultEmail);
                    finish();
                }

                @Override
                public void onStart() {
                    System.out.println("Starting onStart() for Listener");
                }

                @Override
                public void onFailure() {
                    System.out.println("Calling onFailure() for Listener");

                    //TODO --> error handeling
                }
            });
        }
    }


}
