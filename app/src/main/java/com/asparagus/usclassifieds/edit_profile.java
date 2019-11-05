package com.asparagus.usclassifieds;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.database.FirebaseDatabase;

public class edit_profile extends AppCompatActivity {

    private static EditText first, last, phone, sNum, sName, city, state, zip;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        Intent intent = getIntent();
        first = findViewById(R.id.first_name);
        last = findViewById(R.id.last_name);
        phone = findViewById(R.id.phone_number);
        sNum = findViewById(R.id.street_number);
        sName = findViewById(R.id.street_name);
        city = findViewById(R.id.city_name);
        state = findViewById(R.id.state_code);
        zip = findViewById(R.id.zip_code);


        if(GlobalHelper.getUser() != null) {
            first.setText(GlobalHelper.getUser().getFirstName());
            last.setText(GlobalHelper.getUser().getLastName());
            phone.setText(GlobalHelper.getUser().getPhone());
            sNum.setText(GlobalHelper.getUser().getStreetNumber());
            sName.setText(GlobalHelper.getUser().getStreetName());
            city.setText(GlobalHelper.getUser().getCity());
            state.setText(GlobalHelper.getUser().getState());
            zip.setText(GlobalHelper.getUser().getZipCode());
        }
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.update:
                User updatedUser = new User(GlobalHelper.getEmail(), first.getText().toString(), last.getText().toString(), phone.getText().toString(), GlobalHelper.getUserID(), sNum.getText().toString(), sName.getText().toString(), city.getText().toString(), state.getText().toString(), zip.getText().toString());


                FirebaseDatabase.getInstance().getReference("users").child(GlobalHelper.getUserID()).setValue(updatedUser);
//                Intent update_user = new Intent();
//                setResult(105,update_user);
//                update_user.putExtra("updated_user",updatedUser);
                finish();
                break;
            case R.id.cancel:
                finish();
                break;
        }
    }
}
