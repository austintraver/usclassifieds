package com.asparagus.usclassifieds;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.util.ArrayList;

public class Home extends AppCompatActivity {

    private static final int CREATE_LISTING = 101;
    private static final int DASHBOARD = 102;
    private static final String TAG = Home.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Intent intent = getIntent();
        populateListings();

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
                        Toast.makeText(Home.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.create_listing:
                Intent create_listing = new Intent(this, edit_listing.class);
                startActivityForResult(create_listing, CREATE_LISTING);
                break;

            case R.id.dashboard_button:
                Intent dashboard = new Intent(this, Profile.class);
                startActivityForResult(dashboard, DASHBOARD);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == CREATE_LISTING && resultCode == Activity.RESULT_OK) {
            //TODO --> Toast blurb of created listing success
        } else if(resultCode == Activity.RESULT_CANCELED) {
            //TODO --> user signed out from the profile page
            Intent signOut = new Intent();
            setResult(Activity.RESULT_CANCELED, signOut);
            finish();
        }
    }

    private void populateListings()
    {
        ArrayList<Listing> listings = Listing.getListings();
        ListingAdapter adapter = new ListingAdapter(this, listings);
        ListView lv = (ListView) findViewById(R.id.lvListing);
        lv.setAdapter(adapter);
    }


}
