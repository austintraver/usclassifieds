package com.asparagus.usclassifieds;

import android.graphics.Bitmap;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.UUID;

public class Listing implements Serializable {

    //TODO --> why UUID
    private UUID listingID;
    private String title, description;
    private double price;
    private boolean sold;
    private Bitmap image;
    private String ownerID;

    /* TODO include image storage */
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
        //default constructor needed for Firebase instantiation
    }

    public UUID getListingID() { return this.listingID; }
    public String getTitle() { return this.title; }
    public String getDescription() { return this.description; }
    public double getPrice() { return this.price; }
    public boolean getSold() { return this.sold; }
    public static ArrayList<Listing> getListings()
    {
        ArrayList<Listing> a = new ArrayList<Listing>();
        a.add(new Listing("Bike", "A shiny sorority girl's bike.", "Transport", 123.45, "Kelsey"));
        a.add(new Listing("Skateboard", "A dirty frat boy's bike.", "Transport", 69.69, "Chad"));
        a.add(new Listing("Bike", "A shiny sorority girl's bike.", "Transport", 123.45, "Kelsey"));
        a.add(new Listing("Skateboard", "A dirty frat boy's bike.", "Transport", 69.69, "Chad"));
        a.add(new Listing("Bike", "A shiny sorority girl's bike.", "Transport", 123.45, "Kelsey"));
        a.add(new Listing("Skateboard", "A dirty frat boy's bike.", "Transport", 69.69, "Chad"));
        a.add(new Listing("Bike", "A shiny sorority girl's bike.", "Transport", 123.45, "Kelsey"));
        a.add(new Listing("Skateboard", "A dirty frat boy's bike.", "Transport", 69.69, "Chad"));
        a.add(new Listing("Bike", "A shiny sorority girl's bike.", "Transport", 123.45, "Kelsey"));
        a.add(new Listing("Skateboard", "A dirty frat boy's bike.", "Transport", 69.69, "Chad"));
        a.add(new Listing("Bike", "A shiny sorority girl's bike.", "Transport", 123.45, "Kelsey"));
        a.add(new Listing("Skateboard", "A dirty frat boy's bike.", "Transport", 69.69, "Chad"));
        return a;
    }
}
