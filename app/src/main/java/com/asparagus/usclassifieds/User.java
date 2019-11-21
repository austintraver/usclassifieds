package com.asparagus.usclassifieds;

import android.os.AsyncTask;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class User implements Serializable {

    // email is the identifier
    private String firstName, lastName, email, phone, userID, streetNumber, streetName, city, state, zipCode, latitude, longitude, description;
    private HashMap<String, String> friends;
    private HashMap<String, String> outgoingFriendRequests;
    private HashMap<String, String> incomingFriendRequests;
    private HashMap<String, String> notificationTokens;

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
        this.friends = new HashMap<>();
        this.outgoingFriendRequests = new HashMap<>();
        this.incomingFriendRequests = new HashMap<>();
        this.notificationTokens = new HashMap<>();
        this.notificationTokens.put(GlobalHelper.userToken,"true");

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
            Map<String,String> F = new HashMap<>(friends);
            F.put(userID, userID);
            result.put("friends",F);
            Map<String,String> O = new HashMap<>(outgoingFriendRequests);
            O.put(userID,userID);
            result.put("outgoingFriendRequests", O);
            Map<String,String> I = new HashMap<>(incomingFriendRequests);
            I.put(userID,userID);
            result.put("incomingFriendRequests", I);
            Map<String,String> N = new HashMap<>(notificationTokens);
            N.put(userID,userID);
            result.put("notificationTokens", N);

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

    public void setNotificationTokens(HashMap<String,String> map) { this.notificationTokens = map; }
    public void setFriends(HashMap<String,String> map) { this.friends = map; }
    public void setIncomingFriendRequests(HashMap<String,String> map) { this.incomingFriendRequests = map; }
    public void setOutgoingFriendRequests(HashMap<String,String> map) { this.outgoingFriendRequests = map; }

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
    public HashMap<String,String> getFriends() { return this.friends; }
    public HashMap<String,String> getNotificationTokens() { return this.notificationTokens; }
    public HashMap<String,String> getIncomingFriendRequests() { return this.incomingFriendRequests;}
    public HashMap<String,String> getOutgoingFriendRequests() { return this.outgoingFriendRequests;}

    // add or remove from the incoming or outgoing friend request
    public void addFriend(User user) { friends.put(user.getUserID(),user.getFirstName() + " " + user.getLastName()); }
    public void removeFriend(User user) { friends.remove(user.getUserID()); }
//    public void addIncomingFriendRequest(User user) { incomingFriendRequests.add(user.getUserID()); }
//    public void addOutgoingFriendRequest(User user) { outgoingFriendRequests.add(user.getUserID()); }
//    public void removeIncomingFriendRequest(User user) { incomingFriendRequests.remove(user.getUserID()); }
//    public void removeOutgoingFriendRequest(User user) { outgoingFriendRequests.remove(user.getUserID()); }

    //public void setClientToken(String token) { this.clientToken = token; }
//    public void addNotificationToken(String token) { if (notificationTokens == null) notificationTokens = new ArrayList<>(); notificationTokens.add(token); }
//    public void removeNotificationToken(String token) { notificationTokens.remove(token); }

    //send outgoing friend request
//    public void toggleFriendRequest(User u) {
//        if (friends.contains(u)) {
//            // remove the request
//            u.removeIncomingFriendRequest(this);
//            removeOutgoingFriendRequest(u);
//        } else {
//            // request a friend
//            u.addIncomingFriendRequest(this);
//            addOutgoingFriendRequest(u);
//        }
//    }


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
//                System.out.println("resp is: " + response);
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

                    GlobalHelper.getUser().setLatitude(lat);
                    GlobalHelper.getUser().setLongitude(lng);

//                    Map<String, Object> userValues = GlobalHelper.getUser().toMap();
//                    FirebaseDatabase.getInstance().getReference("users").child(GlobalHelper.getUserID()).setValue(userValues);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
