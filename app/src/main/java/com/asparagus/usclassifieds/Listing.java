package com.asparagus.usclassifieds;

import android.location.Location;

import com.mongodb.client.model.geojson.Point;
import com.mongodb.client.model.geojson.Position;

import java.util.UUID;

public class Listing {

    public String name, description, category;
    public double price;
    public Point location;
    public boolean sold;
    UUID id;

    /* TODO include image storage */
    public Listing (String name, String description, String category, double price, Location location) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.price = price;
        this.id = UUID.randomUUID();
        this.location = new Point(new Position(
                            location.getLatitude(),
                            location.getLongitude()));
        this.sold = false;
    }

    /* method for updating listing information, if sold then they'll
       update through this form, updates all at once because all data
       will be present in update form (default value is current info) */
    public void updateInfo(String name, String description, String category, double price, Location location, boolean sold) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.price = price;
        this.location = new Point(new Position(
                location.getLatitude(),
                location.getLongitude()));
        this.sold = sold;
    }
}
