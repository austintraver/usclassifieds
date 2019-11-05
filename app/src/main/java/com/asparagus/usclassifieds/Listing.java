package com.asparagus.usclassifieds;

import java.io.Serializable;
import java.util.UUID;

public class Listing implements Serializable {

    //TODO --> why UUID
    private UUID listingID;
    private String title, description;
    private double price;
    private boolean sold;
    private String image;
    private String ownerID;

    /* TODO include image storage */
    public Listing (String owner, String title, double price, String description) {
        this.title = title;
        this.description = description;
        this.price = price;
        this.listingID = UUID.randomUUID();
        this.ownerID = owner;
        this.sold = false;
        this.image = null;
    }

    public Listing() {
        //default constructor needed for Firebase instantiation
    }

    public UUID getListingID() { return this.listingID; }
    public String getTitle() { return this.title; }
    public String getDescription() { return this.description; }
    public double getPrice() { return this.price; }
    public boolean getSold() { return this.sold; }
}
