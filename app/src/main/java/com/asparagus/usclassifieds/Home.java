package com.asparagus.usclassifieds;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;

public class Home extends AppCompatActivity {

    private static final int CREATE_LISTING = 101;
    private static final int DASHBOARD = 102;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Intent intent = getIntent();
        populateListings();
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.create_listing:
                Intent create_listing = new Intent(this, edit_listing.class);
                startActivityForResult(create_listing,CREATE_LISTING);

            case R.id.dashboard_button:
                Intent dashboard = new Intent(this, Profile.class);
                startActivityForResult(dashboard, DASHBOARD);
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
        ListView lv = (ListView) findViewById(R.id.lvListings);
        lv.setAdapter(adapter);
    }


}
