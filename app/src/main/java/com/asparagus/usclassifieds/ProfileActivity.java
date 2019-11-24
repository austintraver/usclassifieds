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
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onResume() {
        super.onResume();

        TextView textView;

        User user = GlobalHelper.user;

        textView = findViewById(R.id.first_name);
        textView.setText(user.firstName);

        textView = findViewById(R.id.last_name);
        textView.setText(user.lastName);

        textView = findViewById(R.id.phone_number);
        textView.setText(user.phone);

        textView = findViewById(R.id.street_number);
        textView.setText(user.streetNumber);

        textView = findViewById(R.id.street_name);
        textView.setText(user.streetName);

        textView = findViewById(R.id.city_name);
        textView.setText(user.city);

        textView = findViewById(R.id.state_code);
        textView.setText(user.state);

        textView = findViewById(R.id.zip_code);
        textView.setText(user.zipCode);

        textView = findViewById(R.id.description);
        textView.setText(user.description);
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
                finish();
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
        if (resultCode == Activity.RESULT_CANCELED) {
            Intent signOut = new Intent();
            setResult(Activity.RESULT_CANCELED);
            finish();
        }
    }
}
