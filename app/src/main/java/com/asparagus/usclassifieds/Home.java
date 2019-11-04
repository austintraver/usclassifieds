package com.asparagus.usclassifieds;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Home extends AppCompatActivity {

    private static final int CREATE_LISTING = 101;
    private static final int DASHBOARD = 102;
    public User user;
    public DatabaseClient dbClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Intent intent = getIntent();
        user = (User) intent.getSerializableExtra("user");
        dbClient = (DatabaseClient) intent.getSerializableExtra("dbClient");

    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.create_listing:
                Intent create_listing = new Intent(this, edit_listing.class);
                create_listing.putExtra("dbClient",dbClient);
                startActivityForResult(create_listing,CREATE_LISTING);

            case R.id.dashboard_button:
                Intent dashboard = new Intent(this, Profile.class);
                dashboard.putExtra("dbClient",dbClient);
                dashboard.putExtra("user", user);
                startActivityForResult(dashboard, DASHBOARD);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == CREATE_LISTING) {
            //TODO --> Toast blurb of created listing success
        } else if(resultCode == Activity.RESULT_CANCELED) {
            //TODO --> user signed out from the profile page
            Intent signOut = new Intent();
            setResult(Activity.RESULT_CANCELED, signOut);
            finish();
        }
    }
}
