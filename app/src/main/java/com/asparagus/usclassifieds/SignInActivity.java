package com.asparagus.usclassifieds;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import static com.asparagus.usclassifieds.R.id.sign_in_button;

public class SignInActivity extends Activity {

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

        Intent intent = getIntent();
        String emailError = intent.getStringExtra("emailError");
        if(!emailError.equals("")) {
            Toast.makeText(SignInActivity.this, emailError, Toast.LENGTH_SHORT).show();
        }

        findViewById(R.id.sign_in_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == sign_in_button) {
                    signIn();
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
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInClient .getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            // Signed in successfully, show authenticated UI.
            updateUI(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("handleSignInResult", String.format("Failed code: %s", e.getStatusCode()));
            updateUI(null);
        }
    }

    public void getUser(Query query, final OnGetDataListener listener) {
        listener.onStart();
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listener.onSuccess(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("ValueEventListener", "onCancelled()");
                listener.onFailure();
            }
        };
        query.addListenerForSingleValueEvent(eventListener);
    }

    public void updateUI(GoogleSignInAccount account) {
        if (account == null || account.getEmail() == null || account.getId() == null) {
            startActivity(getIntent());
            return;
        }
        final Intent resultEmail = new Intent();
        if (!(account.getEmail().endsWith("@usc.edu"))) {
            setResult(4567, resultEmail);
            finish();
            return;
        }
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users");
        Query query = databaseReference.child(account.getId());
        OnGetDataListener listener = new OnGetDataListener() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    GlobalHelper.setUser(dataSnapshot.getValue(User.class));
                    GlobalHelper.userQueryDone = true;
                }
                else {
                    Log.w("OnGetDataListener", "User does not exist");
                }
                setResult(Activity.RESULT_OK, resultEmail);
                finish();
            }

            @Override
            public void onStart() {
                Log.d("onGetDataListener", "onStart()");
            }

            @Override
            public void onFailure() {
                Log.d("OnGetDataListener", "onFailure()");
            }
        };
        getUser(query, listener);
    }
}


