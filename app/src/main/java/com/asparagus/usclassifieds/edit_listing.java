package com.asparagus.usclassifieds;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class edit_listing extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_listing);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancel:
                Intent canceled = new Intent();
                setResult(Activity.RESULT_CANCELED, canceled);
                finish();

            case R.id.create_listing:
                TextView textView = findViewById(R.id.textView9);
                String title = (String) textView.getText();

                textView = findViewById(R.id.textView5);
                String category = (String) textView.getText();

                textView = findViewById(R.id.textView10);
                String description = (String) textView.getText();


                //Double price = findViewById(R.id.new_price);

                Intent newListingAdded = new Intent();
                setResult(Activity.RESULT_OK, newListingAdded);
                finish();
        }
    }
}
