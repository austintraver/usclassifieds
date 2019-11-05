package com.asparagus.usclassifieds;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class Profile extends AppCompatActivity {

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

        //TODO 1 --> update profile after changes made
        TextView textView = findViewById(R.id.textView2);
        textView.setText(GlobalHelper.getUser().getFirstName() + " " + GlobalHelper.getUser().getLastName());

        textView = findViewById(R.id.email);
        textView.setText(GlobalHelper.getUser().getEmail());
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_out_button:
                Intent signOut = new Intent();
                setResult(Activity.RESULT_CANCELED, signOut);
                finish();       //returns to Home.java in onActivityResult() callback with resultCode = 0
                break;
            case R.id.map_button:
                Intent mapIntent = new Intent(this, MapsActivity.class);
                mapIntent.putExtra("lat",34.0224);
                mapIntent.putExtra("long",-118.2851);
                startActivity(mapIntent);
                break;
            case R.id.edit_info:
                Intent edit = new Intent(this, edit_profile.class);
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
            Intent signOut = new Intent();
            setResult(Activity.RESULT_CANCELED, signOut);
            finish();
        }
    }
}
