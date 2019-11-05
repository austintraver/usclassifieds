package com.asparagus.usclassifieds;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class SingleListingActivity extends Activity {

    Listing listing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("Starting Activity: single_listing");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_listing);
        Intent intent =  getIntent();
        this.listing = (Listing) intent.getSerializableExtra("listing");
        System.out.println("Listing : " + listing.getTitle() + "\nDescription: " + listing.getDescription());
        populatePageData();
//        startActivityForResult(intent,RESULT_CANCELED);

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void populatePageData()
    {
        if(listing == null)
        {
            System.out.println("ERROR: Page cannot display listing data");

        }
        else
        {
            TextView tvSTitle = findViewById(R.id.tvSingleTitle);
            TextView tvSDesc = findViewById(R.id.tvSingleDesc);
            TextView tvSPrice = findViewById(R.id.tvSinglePrice);
            tvSTitle.setText(listing.getTitle());
            tvSDesc.setText(listing.getDescription());
            tvSPrice.setText(String.format("$%.2f",listing.getPrice()));
        }
    }
}
