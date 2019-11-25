package com.asparagus.usclassifieds;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class EditProfileActivity extends Activity {

    private Button update;
    private Boolean valid_email = true;
    private EditText first, last, phone, streetNumber, streetName, city, state, zip, desc;
    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            checkRequiredFields();
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {}

        @Override
        public void afterTextChanged(Editable editable) {}
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        update = findViewById(R.id.update);

        first = findViewById(R.id.edit_first_name);
        last = findViewById(R.id.edit_last_name);
        phone = findViewById(R.id.edit_phone_number);
        streetNumber = findViewById(R.id.edit_street_number);
        streetName = findViewById(R.id.edit_street_name);
        city = findViewById(R.id.edit_city_name);
        state = findViewById(R.id.edit_state_code);
        zip = findViewById(R.id.edit_zip_code);
        desc = findViewById(R.id.edit_description);

        first.addTextChangedListener(textWatcher);
        last.addTextChangedListener(textWatcher);
        streetNumber.addTextChangedListener(textWatcher);
        streetName.addTextChangedListener(textWatcher);
        city.addTextChangedListener(textWatcher);
        state.addTextChangedListener(textWatcher);
        zip.addTextChangedListener(textWatcher);

        User user = GlobalHelper.user;
        valid_email = GlobalHelper.isValidEmail(GlobalHelper.getEmail());

        update.setEnabled(false);
        if(user != null) {
            first.setText(user.firstName);
            last.setText(user.lastName);
            phone.setText(user.phone);
            streetNumber.setText(user.streetNumber);
            streetName.setText(user.streetName);
            city.setText(user.city);
            state.setText(user.state);
            zip.setText(user.zipCode);
            desc.setText(user.description);
        }
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.update:

                User updatedUser = new User(GlobalHelper.getEmail(), first.getText().toString(), last.getText().toString(),
                                            phone.getText().toString(), GlobalHelper.getUserID(), streetNumber.getText().toString(),
                                            streetName.getText().toString(), city.getText().toString(),
                                            state.getText().toString(), zip.getText().toString(),
                                            desc.getText().toString()
                );

                User user = GlobalHelper.getUser();
                if (user != null && user.getFriends() != null) {
                    updatedUser.setFriends(user.friends);
                    updatedUser.setIncomingFriendRequests(user.incomingFriendRequests);
                    updatedUser.setOutgoingFriendRequests(user.outgoingFriendRequests);
                    updatedUser.setNotificationTokens(user.notificationTokens);
                }
                GlobalHelper.setUser(updatedUser);

                setResult(Activity.RESULT_OK, new Intent());
                finish();
                break;
            case R.id.cancel:
                setResult(400);
                finish();
                break;
        }
    }

    public boolean hasRequiredFields() {
        String firstName, lastName, streetNumber, streetName, cityName, stateName, zipCode;
        firstName = first.getText().toString().trim();
        lastName = last.getText().toString().trim();
        streetNumber = this.streetNumber.getText().toString().trim();
        streetName = this.streetName.getText().toString().trim();
        cityName = city.getText().toString().trim();
        stateName = state.getText().toString().trim();
        zipCode = zip.getText().toString().trim();

        if (firstName.isEmpty() || lastName.isEmpty() || streetNumber.isEmpty() || streetName.isEmpty() ||
                cityName.isEmpty() || stateName.isEmpty() || zipCode.isEmpty()) {
            return false;
        }
        return true;
    }

    public void checkRequiredFields() {
        if (!(valid_email)) {
            return;
        }
        String streetNumber, streetName, cityName, stateName;
        streetNumber = this.streetNumber.getText().toString().trim();
        streetName = this.streetName.getText().toString().trim();
        cityName = city.getText().toString().trim();
        stateName = state.getText().toString().trim();

        if (!hasRequiredFields()) {
            update.setEnabled(false);
            return;
        }
        String address, APIkey, url, request;
        address = String.format("%s+%s+%s+%s", streetNumber, streetName, cityName, stateName);
        APIkey = "AIzaSyCfVnn-khp9z8ao5Sb2uESYaqmRuo2PhQ4";
        url = "https://maps.googleapis.com/maps/api/geocode/json";
        request = String.format("%s?address=%s&key=%s", url, address, APIkey);

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            public void onResponse(String response) {
                int size = 0;
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    size = ((JSONArray) jsonObject.get("results")).length();
                    update.setEnabled(size == 1);
                } catch (JSONException je) {
                    je.printStackTrace();
                }
            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                Log.e("ERROR", "REQUEST FAILED");
                update.setEnabled(false);
            }
        };
        // Send a GET request to the google maps API
        StringRequest stringRequest = new StringRequest(Request.Method.GET, request, responseListener, errorListener);
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(stringRequest);
    }
}
