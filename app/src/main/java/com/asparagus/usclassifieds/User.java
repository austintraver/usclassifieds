package com.asparagus.usclassifieds;

import android.app.DownloadManager;
import android.content.Context;
import android.location.Location;
import android.util.JsonReader;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.api.Response;
import com.google.android.gms.common.util.IOUtils;
import com.mongodb.client.model.geojson.Point;
import com.mongodb.client.model.geojson.Position;

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
    private String firstName, lastName, email, phone, userID, address;
    Context context; //context used for geo encoding in maps
    private Double latitude, longitude;
    private HashSet<String> friends;
    private HashSet<String> outgoingFriendRequests;
    private HashSet<String> incomingFriendRequests;

    public User(String email, String firstName, String lastName, String phone, Context context, String address, String userID) {
        this.userID = userID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.context = context;
        this.address = address;
        this.phone = phone;
        this.friends = new HashSet<String>();
        this.outgoingFriendRequests = new HashSet<String>();
        this.incomingFriendRequests = new HashSet<String>();
        //System.out.println("before setLoc");
        //setLoc();
    }

    public void setLoc() {
        //final TextView textView = (TextView) findViewById(R.id.text);
        // Instantiate the RequestQueue.l

        /* temporarily commented all of this out because I'm trying to handle setloc in the
        *** HttpDataHandler class. I've mirrored that off of some functionality online and
        * have everything including the response interpretation written up, but my emulator's internet
        * isn't working, so I'm not able to make any queries online. please disregard this method
        * for the time being and refer to http data handler


        System.out.println("in setLoc");
        RequestQueue queue = Volley.newRequestQueue(context.getApplicationContext());
        queue.start();
        //
        String base_url = "https://maps.googleapis.com/maps/api/geocode/json?address=";
        String key = "&key=AIzaSyDfuYE5Tc8sY6t42ZWr9A7K1xEhS6U9rnI";
        String middle = "";
        String url = "";
        try {
            middle = URLEncoder.encode(address,"UTF-8");
            url = base_url + middle + key;
            // url = new URL(base_url + middle + key);

        } catch(UnsupportedEncodingException exec) {
            System.out.println(exec.getMessage());
        } //catch(MalformedURLException mal) {
           //System.out.println(mal.getMessage()); }
//         catch(IOException ioe) {
//            System.out.println(ioe.getMessage());
//        }
        System.out.println("url is: " + url);

// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(com.android.volley.Request.Method.GET, url,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        //textView.setText("Response is: "+ response.substring(0,500));
                        System.out.println("Response is: "+ response.substring(0,500));
                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //textView.setText("That didn't work!");
                System.out.println("Error is: "+ error.getMessage());
            }
        });

// Add the request to the RequestQueue.
        queue.add(stringRequest);

        //System.out.println("queue size is: " + ;)


        */

    }

    public Map<String,Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("userID", userID);
        result.put("email",email);
        result.put("first",firstName);
        result.put("last",lastName);
        result.put("address",address);
        result.put("phone",phone);
        result.put("latitude", latitude);
        result.put("longitude", longitude);
        result.put("friends",friends);
        result.put("outgoing", outgoingFriendRequests);
        result.put("incoming", incomingFriendRequests);

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
        this.address = address;
        this.phone = phone;
    }

    // getter methods
    public String getAddress() { return this.address; }
    public String getUserID() { return this.userID; }
    public String getFirstName() { return this.firstName; }
    public String getLastName() { return this.lastName; }
    public String getEmail() { return this.email; }
    public String getPhone() { return this.phone; }
    public HashSet<String> getFriends() { return this.friends; }
    public HashSet<String> getIncomingFriendRequests() { return this.incomingFriendRequests;}
    public HashSet<String> getOutgoingFriendRequests() { return this.outgoingFriendRequests;}

    // add or remove from the incoming or outgoing friend request
    public void addIncomingFriendRequest(User user) { incomingFriendRequests.add(user.email); }
    public void addOutgoingFriendRequest(User user) { outgoingFriendRequests.add(user.email); }
    public void removeIncomingFriendRequest(User user) { incomingFriendRequests.remove(user.email); }
    public void removeOutgoingFriendRequest(User user) { outgoingFriendRequests.remove(user.email); }

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
}
