package com.asparagus.usclassifieds;

import android.location.Location;

import com.mongodb.client.model.geojson.Point;
import com.mongodb.client.model.geojson.Position;

import java.io.Serializable;
import java.util.UUID;

public class Listing implements Serializable {

    private UUID listingID;
    private String title, description, category;
    private double price;
    private Point location;
    private boolean sold;

    /* TODO include image storage */
    public Listing (String title, String description, String category, double price, Point location) {
        this.title = title;
        this.description = description;
        this.category = category;
        this.price = price;
        this.listingID = UUID.randomUUID();
        this.location = location;
        this.sold = false;
    }

    public UUID getListingID() { return this.listingID; }
    public String getTitle() { return this.title; }
    public String getDescription() { return this.description; }
    public String getCategory() { return this.category; }
    public double getPrice() { return this.price; }
    public boolean getSold() { return this.sold; }
    public Point getLocation() { return this.location; }

    /* method for updating listing information, if sold then they'll
       update through this form, updates all at once because all data
       will be present in update form (default value is current info) */
    public void updateInfo(String title, String description, String category, double price, Point location, boolean sold) {
        this.title = title;
        this.description = description;
        this.category = category;
        this.price = price;
        this.location = location;
        this.sold = sold;
    }
}
