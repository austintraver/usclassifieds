package com.asparagus.usclassifieds;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class edit_profile extends AppCompatActivity {

    private Button update;

    private static EditText first, last, phone, sNum, sName, city, state, zip, desc;

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            checkRequiredFields();
        }
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        @Override
        public void afterTextChanged(Editable editable) {
        }
    };





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        Intent intent = getIntent();
        update = findViewById(R.id.update);
        first = findViewById(R.id.first_name);
        last = findViewById(R.id.last_name);
        phone = findViewById(R.id.phone_number);
        sNum = findViewById(R.id.street_number);
        sName = findViewById(R.id.street_name);
        city = findViewById(R.id.city_name);
        state = findViewById(R.id.state_code);
        zip = findViewById(R.id.zip_code);
        desc = findViewById(R.id.description);

        first.addTextChangedListener(textWatcher);
        last.addTextChangedListener(textWatcher);
        sNum.addTextChangedListener(textWatcher);
        sName.addTextChangedListener(textWatcher);
        city.addTextChangedListener(textWatcher);
        state.addTextChangedListener(textWatcher);
        zip.addTextChangedListener(textWatcher);

        update.setEnabled(false);
        if(GlobalHelper.getUser() != null) {
            first.setText(GlobalHelper.getUser().getFirstName());
            last.setText(GlobalHelper.getUser().getLastName());
            phone.setText(GlobalHelper.getUser().getPhone());
            sNum.setText(GlobalHelper.getUser().getStreetNumber());
            sName.setText(GlobalHelper.getUser().getStreetName());
            city.setText(GlobalHelper.getUser().getCity());
            state.setText(GlobalHelper.getUser().getState());
            zip.setText(GlobalHelper.getUser().getZipCode());
            desc.setText(GlobalHelper.getUser().getDescription());
        }
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.update:
                System.out.println("In Onclick for update/create user!");
                User updatedUser = new User(GlobalHelper.getEmail(), first.getText().toString(), last.getText().toString(), phone.getText().toString(), GlobalHelper.getUserID(), sNum.getText().toString(), sName.getText().toString(), city.getText().toString(), state.getText().toString(), zip.getText().toString(), desc.getText().toString());
                GlobalHelper.setUser(updatedUser);
                System.out.println("creating a new/updated user: " + updatedUser);

//                FirebaseDatabase.getInstance().getReference("users").child(GlobalHelper.getUserID()).setValue(updatedUser);
//                Intent update_user = new Intent();
//                setResult(105,update_user);
//                update_user.putExtra("updated_user",updatedUser);
                Intent newUser = new Intent();
                setResult(Activity.RESULT_OK, newUser);
                finish();
                break;
            case R.id.cancel:
                finish();
                break;
        }
    }


    private void checkRequiredFields(){

        String s1 = first.getText().toString();
        String s2 = last.getText().toString();

        String number_ = sNum.getText().toString() + " ";
        String street_ = sName.getText().toString() + " ";
        String city_ = city.getText().toString() + " ";
        String state_ = state.getText().toString() + " ";
        String zip_ = zip.getText().toString();

        String address = number_ + street_ + city_ + state_ + zip_;
        // TODO: validate address and phone number

        if(s1.trim().isEmpty() || s2.trim().isEmpty() || number_.trim().isEmpty() || street_.trim().isEmpty() || city_.trim().isEmpty() || state_.trim().isEmpty() || zip_.trim().isEmpty())
        {
            update.setEnabled(false);
        } else {
            update.setEnabled(true);
        }
    }
}
