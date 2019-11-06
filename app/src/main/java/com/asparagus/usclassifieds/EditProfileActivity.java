package com.asparagus.usclassifieds;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static android.webkit.ConsoleMessage.MessageLevel.LOG;

public class EditProfileActivity extends Activity {

    private Button update;
    private Boolean valid_email = true;
    private EditText first, last, phone, sNum, sName, city, state, zip, desc;
    private TextView email_warning;
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

        first = findViewById(R.id.edit_first_name);
        last = findViewById(R.id.edit_last_name);
        phone = findViewById(R.id.edit_phone_number);
        sNum = findViewById(R.id.edit_street_number);
        sName = findViewById(R.id.edit_street_name);
        city = findViewById(R.id.edit_city_name);
        state = findViewById(R.id.edit_state_code);
        zip = findViewById(R.id.edit_zip_code);
        desc = findViewById(R.id.edit_description);

        first.addTextChangedListener(textWatcher);
        last.addTextChangedListener(textWatcher);
        sNum.addTextChangedListener(textWatcher);
        sName.addTextChangedListener(textWatcher);
        city.addTextChangedListener(textWatcher);
        state.addTextChangedListener(textWatcher);
        zip.addTextChangedListener(textWatcher);

        email_warning = findViewById(R.id.invalid_email_text_view);
        if (!GlobalHelper.getEmail().endsWith("@usc.edu")) {
            email_warning.setText("ERROR: PLEASE USE YOUR USC EMAIL ADDRESS");
            valid_email = false;
        }

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
                System.out.println("In Onclick for update/create person!");
                User updatedUser = new User(
                    GlobalHelper.getEmail(),
                    first.getText().toString(),
                    last.getText().toString(),
                    phone.getText().toString(),
                    GlobalHelper.getUserID(),
                    sNum.getText().toString(),
                    sName.getText().toString(),
                    city.getText().toString(),
                    state.getText().toString(),
                    zip.getText().toString(),
                    desc.getText().toString()
                );
                GlobalHelper.setUser(updatedUser);
                System.out.println("creating a new/updated person: " + updatedUser);

//                FirebaseDatabase.getInstance().getReference("users").child(GlobalHelper.getUserID()).setValue(updatedUser);
//                Intent update_user = new Intent();
//                setResult(105,update_user);
//                update_user.putExtra("updated_user",updatedUser);
                Intent newUser = new Intent();
                setResult(Activity.RESULT_OK, newUser);
                finish();
                break;
            case R.id.cancel:
                setResult(400);
                finish();
                break;
        }
    }

    private void checkRequiredFields(){

        if (!valid_email) {
            return;
        }
        
        String s1 = first.getText().toString();
        String s2 = last.getText().toString();

        String number_ = sNum.getText().toString();
        String street_ = sName.getText().toString();
        String city_ = city.getText().toString();
        String state_ = state.getText().toString();
        String zip_ = zip.getText().toString();



        if(s1.trim().isEmpty() || s2.trim().isEmpty() || number_.trim().isEmpty() || street_.trim().isEmpty() || city_.trim().isEmpty() || state_.trim().isEmpty() || zip_.trim().isEmpty())
        {
            update.setEnabled(false);
        } else {
            try {
                String address = number_ + " " + street_ + " " + city_ + " " + state_;
                address = address.replaceAll(" ", "+");
                String key = "&key=AIzaSyCfVnn-khp9z8ao5Sb2uESYaqmRuo2PhQ4";
                String request = "https://maps.googleapis.com/maps/api/geocode/json?address=" + address + key;


                // Request a string response from the provided URL.
                StringRequest stringRequest = new StringRequest(Request.Method.GET, request,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.i("RESPONSE", response);

                                // TODO: CHECK IF RESPONSE SIZE IS 1 AND DELETE LOGS
                                // TODO: Turn the response into JSON obj and ensure size == 1


                                int size = 0;

                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    if (jsonObject != null) {
                                        size = ((JSONArray) jsonObject.get("results")).length();
                                    }
                                } catch (JSONException je) {
                                    return;
                                }

                                update.setEnabled(size == 1);

                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e("ERROR", "REQUEST FAILED");
                                update.setEnabled(false);
                            }
                        });

                RequestQueue queue = Volley.newRequestQueue(this);
                queue.add(stringRequest);

            } catch (Exception e) {
                update.setEnabled(false);
            }


        }
    }
}
