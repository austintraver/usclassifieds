package com.asparagus.usclassifieds;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("WeakerAccess")
public class Listing implements Serializable {

    // Private UUID listingID;
    public String title, description, ownerID, ownerEmail, ownerName, storageReference, latitude, longitude;
    public double price;
    public boolean sold;

    // Default constructor is required for Firebase instantiation
    public Listing() {}

    Listing(
            String ownerID, String ownerName, String ownerEmail, String title, double price, String description,
            String storageReference, String lat, String lng
    ) {
        this.ownerID = ownerID;
        this.ownerName = ownerName;
        this.ownerEmail = ownerEmail;
        this.title = title;
        this.description = description;
        this.price = price;
        this.sold = false;
        this.storageReference = storageReference;
        this.latitude = lat;
        this.longitude = lng;
    }

    Listing(JSONObject object) {
        try {
            title = object.getString("title");
            description = object.getString("description");
            price = object.getDouble("price");
            longitude = object.getString("longitude");
            latitude = object.getString("latitude");
            ownerID = object.getString("ownerID");
            ownerEmail = object.getString("ownerEmail");
            ownerName = object.getString("ownerName");
            sold = object.getBoolean("sold");
            storageReference = object.getString("storageReference");
        } catch (JSONException je) {
            je.printStackTrace();
        }
    }

    Map<String, Object> toMap() {
        return new HashMap<String, Object>() {{
            put("title", title);
            put("description", description);
            put("price", price);
            put("ownerID", ownerID);
            put("ownerEmail", ownerEmail);
            put("ownerName", ownerName);
            put("sold", sold);
            put("storageReference", storageReference);
            put("latitude", latitude);
            put("longitude", longitude);
        }};
    }

    String getStorageReference() {
        return this.storageReference;
    }

    String getOwnerID() {
        return this.ownerID;
    }

    String getOwnerName() { return this.ownerName; }

    String getOwnerEmail() {
        return this.ownerEmail;
    }

    String getLatitude() {
        return this.latitude;
    }

    String getLongitude() {
        return this.longitude;
    }

    String getTitle() {
        return this.title;
    }

    String getDescription() {
        return this.description;
    }

    double getPrice() {
        return this.price;
    }

    boolean getSold() { return this.sold; }

    void setSold() {
        this.sold = true;
    }
}
