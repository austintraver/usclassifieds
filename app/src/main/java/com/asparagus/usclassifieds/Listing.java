import android.location.Location;
import com.mongodb.client.model.geojson.Point;
import com.mongodb.client.model.geojson.Position;

public class Listing {

    public String name, description, category;
    public double price;
    public Position location;
    public boolean sold;

    /* TODO include image storage */
    public Listing (String name, String description, String category, double price, Location location) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.price = price;
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
        this.location = location;
        this.sold = sold;
    }
}
