package com.asparagus.usclassifieds;

import android.graphics.Bitmap;

import com.google.firebase.storage.StorageReference;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/* TODO include image storage */

public class Listing implements Serializable {

    //private UUID listingID;
    private String title, description;
    private double price;
    private boolean sold;
    private Bitmap image;
    private String ownerID, ownerEmail, ownerName;
    private String storageReference;
    private String latitude, longitude;

//    public Listing (String owner, String title, double price, String description, Bitmap image) {
//        this.title = title;
//        this.description = description;
//        this.price = price;
//        this.listingID = UUID.randomUUID();
//        this.ownerID = owner;
//        this.sold = false;
//        this.image = image;
//    }

    public Listing (String owner, String ownerE, String ownerN, String title, double price, String description, String sref, String lat, String lng) {
        this.title = title;
        this.description = description;
        this.price = price;
        this.ownerID = owner;
        this.ownerEmail = ownerE;
        this.ownerName = ownerN;
        this.sold = false;
        this.image = null;
        this.storageReference = sref;
        this.latitude = lat;
        this.longitude = lng;
    }

    public Listing(JSONObject object){
        try{
            this.title = object.getString("title");
            this.description = object.getString("description");
            this.price = object.getDouble("price");
            this.longitude = object.getString("longitude");
            this.latitude = object.getString("latitude");
            this.ownerID = object.getString("ownerID");
            this.ownerEmail = object.getString("ownerEmail");
            this.ownerName = object.getString("ownerName");
            this.sold = object.getBoolean("sold");
            this.storageReference = object.getString("storageReference");

        } catch(JSONException je){
            je.printStackTrace();
        }
    }

    public Listing() { } // Default constructor is required for Firebase instantiation

    public Map<String,Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("title", title);
        result.put("description", description);
        result.put("price", price);
        result.put("ownerID", ownerID);
        result.put("ownerEmail", ownerEmail);
        result.put("ownerName", ownerName);
        result.put("sold", sold);
        result.put("storageReference", storageReference);
        result.put("latitude", latitude);
        result.put("longitude", longitude);

        return result;
    }

    public String getStorageReference() {
        return this.storageReference;
    }

//    public UUID getListingID() {
//        return this.listingID;
//    }

    public String getOwnerID() { return this.ownerID; }

    public String getOwnerEmail() { return this.ownerEmail; }

    public String getOwnerName() { return this.ownerName; }

    public String getLatitude() {
        return this.getLatitude();
    }

    public String getLongitude() {
        return this.longitude;
    }

    public String getTitle() {
        return this.title;
    }

    public String getDescription() {
        return this.description;
    }

    public double getPrice() {
        return this.price;
    }

    public boolean getSold() {
        return this.sold;
    }

    public static ArrayList<Listing> getListings() {
        ArrayList<Listing> listings = new ArrayList<Listing>();
//        listings.add(new Listing("105390386330726279653", "Bike", 420.00, "A dirty frat boy's bike.", null));
//        listings.add(new Listing("105390386330726279653", "Another Bike", 42.00, "A dirty frat boy's bike.", null));
//        listings.add(new Listing("118050324720858569859", "Skateboard", 42.00, "A dirty frat boy's skateboard.", null));
//        listings.add(new Listing("118050324720858569859", "Bike", 42.00, "A dirty frat boy's bike.", null));
//        listings.add(new Listing("118050324720858569859", "Bike", 0.00, "We should test for strings too long", null));
        return listings;
    }
}
