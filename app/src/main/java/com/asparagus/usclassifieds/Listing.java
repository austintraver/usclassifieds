package com.asparagus.usclassifieds;

import android.graphics.Bitmap;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.UUID;

/* TODO include image storage */

public class Listing implements Serializable {

    private UUID listingID;
    private String title, description;
    private double price;
    private boolean sold;
    private Bitmap image;
    private String ownerID;

    public Listing (String owner, String title, double price, String description, Bitmap image) {
        this.title = title;
        this.description = description;
        this.price = price;
        this.listingID = UUID.randomUUID();
        this.ownerID = owner;
        this.sold = false;
        this.image = image;
    }

    public Listing() {
        // Default constructor is required for Firebase instantiation
    }

    public UUID getListingID() {
        return this.listingID;
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
        listings.add(new Listing("105390386330726279653", "Bike", 420.00, "A dirty frat boy's bike.", null));
        listings.add(new Listing("105390386330726279653", "Another Bike", 42.00, "A dirty frat boy's bike.", null));
        listings.add(new Listing("118050324720858569859", "Skateboard", 42.00, "A dirty frat boy's skateboard.", null));
        listings.add(new Listing("118050324720858569859", "Bike", 42.00, "A dirty frat boy's bike.", null));
        listings.add(new Listing("118050324720858569859", "Bike", 0.00, "We should test for strings too long", null));
        return listings;
    }
}
