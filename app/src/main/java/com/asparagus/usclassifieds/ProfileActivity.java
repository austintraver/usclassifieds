package com.asparagus.usclassifieds;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class ProfileActivity extends Activity {

    private static final int EDIT_COMPLETE = 105;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Intent intent = getIntent();
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onResume() {
        super.onResume();

        TextView textView;

        textView = findViewById(R.id.first_name);
        textView.setText(GlobalHelper.getUser().getFirstName());

        textView = findViewById(R.id.last_name);
        textView.setText(GlobalHelper.getUser().getLastName());

        textView = findViewById(R.id.phone_number);
        textView.setText(GlobalHelper.getUser().getPhone());

        textView = findViewById(R.id.street_number);
        textView.setText(GlobalHelper.getUser().getStreetNumber());

        textView = findViewById(R.id.street_name);
        textView.setText(GlobalHelper.getUser().getStreetName());

        textView = findViewById(R.id.city_name);
        textView.setText(GlobalHelper.getUser().getCity());

        textView = findViewById(R.id.state_code);
        textView.setText(GlobalHelper.getUser().getState());

        textView = findViewById(R.id.zip_code);
        textView.setText(GlobalHelper.getUser().getZipCode());

        textView = findViewById(R.id.description);
        textView.setText(GlobalHelper.getUser().getDescription());
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_out_button:
                Intent signOut = new Intent();
                setResult(Activity.RESULT_CANCELED, signOut);
                finish();
                // returns to HomeActivity.java in onActivityResult() callback with resultCode = 0
                break;
            case R.id.my_listings_button:
                setResult(25);
                break;
            case R.id.edit_info:
                Intent edit = new Intent(this, EditProfileActivity.class);
                startActivityForResult(edit, EDIT_COMPLETE);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == EDIT_COMPLETE) {
            //TODO --> Toast blurb of updated profile information
            return;
        } else if(resultCode == Activity.RESULT_CANCELED) {
            //TODO --> user signed out from the profile page
            System.out.println("Signing out now!");
            Intent signOut = new Intent();
            setResult(Activity.RESULT_CANCELED, signOut);
            finish();
        }
    }
}
