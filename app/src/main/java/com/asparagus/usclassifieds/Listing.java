package com.asparagus.usclassifieds;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.UUID;

public class Listing implements Serializable {

    //TODO --> why UUID
    private UUID listingID;
    private String title, description, category;
    private double price;
    private boolean sold;
    private String image;
    private String ownerID;

    /* TODO include image storage */
    public Listing (String title, String description, String category, double price, String owner) {
        this.title = title;
        this.description = description;
        this.category = category;
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
    public String getCategory() { return this.category; }
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
