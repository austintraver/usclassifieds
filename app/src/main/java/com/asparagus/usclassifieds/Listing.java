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
    private String image;

    /* TODO include image storage */
    public Listing (String title, String description, String category, double price) {
        this.title = title;
        this.description = description;
        this.category = category;
        this.price = price;
        this.listingID = UUID.randomUUID();
        this.sold = false;
        this.image = null;
    }

    public UUID getListingID() { return this.listingID; }
    public String getTitle() { return this.title; }
    public String getDescription() { return this.description; }
    public String getCategory() { return this.category; }
    public double getPrice() { return this.price; }
    public boolean getSold() { return this.sold; }
    public Point getLocation() { return this.location; }
}
