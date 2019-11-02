package com.asparagus.usclassifieds;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.asparagus.usclassifieds.R;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;

public class Profile extends AppCompatActivity {

    public GoogleSignInClient mGoogleSignInClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Intent intent = getIntent();
        //GoogleSignInAccount acct = (GoogleSignInAccount) intent.getSerializableExtra("account");
        //TODO - MUST make own serializable class --> GoogleSignInAccount is not serializable
        TextView textView = findViewById(R.id.textView2);
        textView.setText(intent.getStringExtra("name"));

        textView = findViewById(R.id.email);
        textView.setText(intent.getStringExtra("email"));

        textView = findViewById(R.id.userID);
        textView.setText(intent.getStringExtra("userID"));
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_out_button:
                finish();       //returns to MainActivity.java in onActivityResult() callback with requestCode = RC_SIGN_OUT. which is 2
                break;
            case R.id.map_button:
                Intent intent = new Intent(this, MapsActivity.class);
                intent.putExtra("lat",34.0224);
                intent.putExtra("long",-118.2851);
                startActivity(intent);

        }
    }
}
