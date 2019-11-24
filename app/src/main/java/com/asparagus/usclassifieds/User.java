package com.asparagus.usclassifieds;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import static java.lang.String.format;

@SuppressWarnings({"WeakerAccess", "StaticFieldLeak"})
public class User implements Serializable {

    // Email is the identifier
    public String firstName, lastName, email, phone, userID, streetNumber, streetName, city, state, zipCode, latitude
            , longitude, description;
    public HashMap<String, String> friends, outgoingFriendRequests, incomingFriendRequests, notificationTokens;
    private String TAG = User.class.getSimpleName();

    // Required for calls to Firebase
    public User() {}

    User(
            String email, String firstName, String lastName, String phone, final String userID, String streetNum,
            String streetName, String city, String state, String zip, String description
    ) {
        this.userID = userID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.streetNumber = streetNum;
        this.streetName = streetName;
        this.city = city;
        this.state = state;
        this.zipCode = zip;
        this.phone = phone;
        this.description = description;
        this.friends = new HashMap<String, String>() {{
            put(userID, userID);
        }};
        this.outgoingFriendRequests = new HashMap<>(friends);
        this.incomingFriendRequests = new HashMap<>(friends);
        this.notificationTokens = new HashMap<String, String>() {{
            put(userID, userID);
            put(GlobalHelper.userToken, "true");
        }};
        new GetCoordinates().execute(getAddress());
    }

    Map<String, Object> toMap() {
        return new HashMap<String, Object>() {{
            put("userID", userID);
            put("email", email);
            put("firstName", firstName);
            put("lastName", lastName);
            put("streetNumber", streetNumber);
            put("streetName", streetName);
            put("city", city);
            put("state", state);
            put("zipCode", zipCode);
            put("phone", phone);
            put("latitude", latitude);
            put("longitude", longitude);
            put("description", description);
            put("friends", friends);
            put("outgoingFriendRequests", outgoingFriendRequests);
            put("incomingFriendRequests", incomingFriendRequests);
            put("notificationTokens", notificationTokens);
        }};
    }

    private String getAddress() {
        return format("%s %s, %s, %s, %s", streetNumber, streetName, city, state, zipCode);
    }

    public String getDescription() {
        return this.description;
    }

    public String getStreetNumber() { return this.streetNumber; }

    public String getStreetName() {
        return this.streetName;
    }

    public String getCity() {
        return this.city;
    }

    public String getState() {
        return this.state;
    }

    public String getZipCode() {
        return this.zipCode;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public String getPhone() {
        return this.phone;
    }

    public HashMap<String, String> getFriends() {
        return this.friends;
    }

    public void setFriends(HashMap<String, String> map) {
        this.friends = map;
    }

    public HashMap<String, String> getNotificationTokens() {
        return this.notificationTokens;
    }

    public void setNotificationTokens(HashMap<String, String> map) {
        this.notificationTokens = map;
    }

    public HashMap<String, String> getIncomingFriendRequests() {
        return this.incomingFriendRequests;
    }

    public void setIncomingFriendRequests(HashMap<String, String> map) {
        this.incomingFriendRequests = map;
    }

    public HashMap<String, String> getOutgoingFriendRequests() {
        return this.outgoingFriendRequests;
    }

    public void setOutgoingFriendRequests(HashMap<String, String> map) {
        this.outgoingFriendRequests = map;
    }

    public class GetCoordinates extends AsyncTask<String, Void, String> {

        private final String TAG = GetCoordinates.class.getSimpleName();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                String address = URLEncoder.encode(strings[0], "UTF-8");
                String key = "&key=AIzaSyCfVnn-khp9z8ao5Sb2uESYaqmRuo2PhQ4";
                HttpDataHandler http = new HttpDataHandler();
                String url = "https://maps.googleapis" + ".com/maps/api/geocode/json";
                String request = format("%s?address=%s&key=%s", url, address, key);
                return http.getHTTPData(request);
            } catch (Exception ex) {
                Log.d(TAG, format("Error in background, exception: %s", ex.getLocalizedMessage()));
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                User user = GlobalHelper.user;
                JSONObject jsonObject = new JSONObject(s);
                String latitude = ((JSONArray) jsonObject.get("results")).getJSONObject(0).getJSONObject("geometry")
                                                                         .getJSONObject("location").get("lat")
                                                                         .toString();
                String longitude = ((JSONArray) jsonObject.get("results")).getJSONObject(0).getJSONObject("geometry")
                                                                          .getJSONObject("location").get("lng")
                                                                          .toString();
                user.latitude = latitude;
                user.longitude = longitude;
            } catch (JSONException e) {
                String error = e.getLocalizedMessage();
                if (error != null) {
                    Log.d(TAG, error);
                }
            }
        }
    }
}
