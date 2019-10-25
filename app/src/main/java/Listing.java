import android.location.Location;

import java.util.Vector;

public class Listing {

    private String name, description, category;
    private double price;
    private Location location;
    private boolean sold;


    public Listing (String name, String description, String category, double price, Location l) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.price = price;
        this.location = l;
        this.sold = false;
    }

    // method for updating everything, if sold then they'll update through this form
    /* updates all at once because all data will be present
      in update form (default value is current info) */
    public void updateInfo(String name, String description, String category, double price, Location l, boolean sold) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.price = price;
        this.location = l;
        this.sold = sold;
    }

    // getter methods
    public String getName() { return this.name; }
    public String getDescription() { return this.description; }
    public String getCategory() { return this.category; }
    public double getPrice() { return this.price; }
    public Location getLocation() { return this.location; }
    public boolean getSold() { return this.sold; }



}
