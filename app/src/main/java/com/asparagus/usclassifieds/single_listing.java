package com.asparagus.usclassifieds;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class single_listing extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_listing);
        Intent intent =  getIntent();
//        Listing listing = intent.getSerializableExtra("listing");
    }

    @Override
    protected void onStart() {
        super.onStart();

    }
}
