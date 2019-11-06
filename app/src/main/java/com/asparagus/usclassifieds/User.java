package com.asparagus.usclassifieds;

import android.app.DownloadManager;
import android.content.Context;
import android.location.Location;
import android.os.AsyncTask;
import android.util.JsonReader;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.api.Response;
import com.google.android.gms.common.util.IOUtils;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mongodb.client.model.geojson.Point;
import com.mongodb.client.model.geojson.Position;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;

public class User implements Serializable {

    // email is the identifier
    private String firstName, lastName, email, phone, userID, streetNumber, streetName, city, state, zipCode, latitude, longitude, description;
    private HashSet<String> friends;
    private HashSet<String> outgoingFriendRequests;
    private HashSet<String> incomingFriendRequests;
    private String clientToken;

    public User(String email, String firstName, String lastName, String phone, String userID, String streetNum, String streetName, String city, String state, String zip, String description) {
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
        this.latitude = "";
        this.longitude = "";
        this.description = description;
        this.friends = new HashSet<String>();
        this.outgoingFriendRequests = new HashSet<String>();
        this.incomingFriendRequests = new HashSet<String>();
        new GetCoordinates().execute(this.streetNumber + " " + this.streetName + ", " + this.city + ", " + this.state + ", " + this.zipCode);
    }

    public Map<String,Object> toMap() {
            HashMap<String, Object> result = new HashMap<>();
            result.put("userID", userID);
            result.put("email",email);
            result.put("firstName",firstName);
            result.put("lastName",lastName);
            result.put("streetNumber",streetNumber);
            result.put("streetName",streetName);
            result.put("city",city);
            result.put("state",state);
            result.put("zipCode",zipCode);
            result.put("phone",phone);
            result.put("latitude", latitude);
            result.put("longitude", longitude);
            result.put("description", description);
//        result.put("friends",friends);
//        result.put("outgoing", outgoingFriendRequests);
//        result.put("incoming", incomingFriendRequests);

        return result;
    }

    public User() {
        //required for calls to Firebase
    }

    public User(String email) {
        this.email = email;
    }

    /* update all at once because all data will be present
      in update form (default value is current info) */
    public void updateInfo(String firstName, String lastName, String phone, String address) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
    }

    public void setLatitude(String lat) {
        this.latitude = lat;
    }
    public void setLongitude(String lng) {
        this.longitude = lng;
    }
    public void setFirstName(String fn) { this.firstName = fn; }
    public void setLastName(String ln) { this.lastName = ln; }

    // getter methods
    public String getDescription() { return this.description; }
    public String getLatitude() { return this.latitude; }
    public String getLongitude() { return this.longitude; }
    public String getStreetNumber() { return this.streetNumber; }
    public String getStreetName() { return this.streetName; }
    public String getCity() { return this.city; }
    public String getState() { return this.state; }
    public String getZipCode() { return this.zipCode; }
    public String getUserID() { return this.userID; }
    public String getFirstName() { return this.firstName; }
    public String getLastName() { return this.lastName; }
    public String getEmail() { return this.email; }
    public String getPhone() { return this.phone; }
    public HashSet<String> getFriends() { return this.friends; }
    public String getClientToken() { return this.clientToken; }
    public HashSet<String> getIncomingFriendRequests() { return this.incomingFriendRequests;}
    public HashSet<String> getOutgoingFriendRequests() { return this.outgoingFriendRequests;}

    // add or remove from the incoming or outgoing friend request
    public void addIncomingFriendRequest(User user) { incomingFriendRequests.add(user.email); }
    public void addOutgoingFriendRequest(User user) { outgoingFriendRequests.add(user.email); }
    public void removeIncomingFriendRequest(User user) { incomingFriendRequests.remove(user.email); }
    public void removeOutgoingFriendRequest(User user) { outgoingFriendRequests.remove(user.email); }
    public void setClientToken(String token) { this.clientToken = token; }

    //send outgoing friend request
    public void toggleFriendRequest(User u) {
        if (friends.contains(u)) {
            // remove the request
            u.removeIncomingFriendRequest(this);
            removeOutgoingFriendRequest(u);
        } else {
            // request a friend
            u.addIncomingFriendRequest(this);
            addOutgoingFriendRequest(u);
        }
    }

    // accept a friend requests
    public void acceptFriendRequest(User u) {
        addFriend(u);
        removeIncomingFriendRequest(u);
        u.removeOutgoingFriendRequest(this);
    }

    // reject a friend request
    public void rejectFriendRequest(User u) {
        removeIncomingFriendRequest(u);
        u.removeOutgoingFriendRequest(this);
    }


    public void addFriend(User u) {
        friends.add(u.email);
        if (!u.getFriends().contains(this.email)) {
            u.addFriend(this);
        }
    }
    public void removeFriend(User u) {
        friends.remove(u.email);
        if (u.getFriends().contains(this.email)) {
            u.removeFriend(this);
        }
    }

    public class GetCoordinates extends AsyncTask<String,Void,String> {
        //ProgressDialog dialog = new ProgressDialog(MainActivity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            dialog.setMessage("Please wait....");
//            dialog.setCanceledOnTouchOutside(false);
//            dialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            String response;
            try{
                String address = strings[0];
                String middle = URLEncoder.encode(address,"UTF-8");
                String key = "&key=AIzaSyCfVnn-khp9z8ao5Sb2uESYaqmRuo2PhQ4";
                HttpDataHandler http = new HttpDataHandler();
                String url = "https://maps.googleapis.com/maps/api/geocode/json?address=" + middle + key;
                response = http.getHTTPData(url);
                System.out.println("resp is: " + response);
                return response;
            }
            catch (Exception ex)
            {
                System.out.println("error in background exception: ");
                ex.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            try{
                JSONObject jsonObject = new JSONObject(s);

                if (jsonObject != null) {
                    String lat = ((JSONArray)jsonObject.get("results")).getJSONObject(0).getJSONObject("geometry")
                            .getJSONObject("location").get("lat").toString();
                    String lng = ((JSONArray)jsonObject.get("results")).getJSONObject(0).getJSONObject("geometry")
                            .getJSONObject("location").get("lng").toString();
                    System.out.println("latitude and longitude" + lat + " " + lng);
                    GlobalHelper.getUser().setLatitude(lat);
                    GlobalHelper.getUser().setLongitude(lng);

                    Map<String, Object> userValues = GlobalHelper.getUser().toMap();
                    FirebaseDatabase.getInstance().getReference("users").child(GlobalHelper.getUserID()).setValue(userValues);
                }



            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
