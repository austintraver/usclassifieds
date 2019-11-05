package com.asparagus.usclassifieds;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

public class ScrollableListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrollable_list);
        Intent i = getIntent();
        populateList();
    }



    @Override
    protected void onStart() {
        super.onStart();

    }

    private void populateList()
    {
        //ArrayList<Listing> listings = Listing.getListings();
        // Create adapter to convert into views
        //ListingAdapter adapter = new ListingAdapter(this,listings);
        // Attach adapter to a list of views
       // ListView listView = findViewById(R.id.lvListings);
        //listView.setAdapter(adapter);
    }
}
