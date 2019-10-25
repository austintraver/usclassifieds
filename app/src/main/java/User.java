import android.location.Location;

import java.util.LinkedList;
import java.util.Vector;

public class User {

    private String firstName, lastName, email, phone;
    private LinkedList<Listing> listings, starredListings;
    private Location location;
    private Vector<User> friends;
    private Vector<FriendRequest> friendRequests;


    public User(String firstName, String lastName, String email, String phone) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        listings = new LinkedList<Listing>();
        starredListings = new LinkedList<Listing>();
        friends = new Vector<User>();
        friendRequests = new Vector<FriendRequest>();
    }

    /* update all at once because all data will be present
      in update form (default value is current info) */
    public void updateInfo(String firstName, String lastName, String email, String phone) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
    }

    // getter methods
    public String getFirstName() { return this.firstName; }
    public String getLastName() { return this.lastName; }
    public String getEmail() { return this.email; }
    public String getPhone() { return this.phone; }
    public LinkedList<Listing> getListings() { return this.listings; }
    public LinkedList<Listing> getStarredListings() { return this.starredListings; }
    public Vector<FriendRequest> getFriendRequests() { return this.friendRequests;}

    // setter methods for listings
    public void addListing(Listing l) { this.listings.add(l); }
    public void starListing(Listing l) { this.starredListings.add(l); }
    public void removeListing(Listing l) { this.listings.remove(l); }
    public void unstarListing(Listing l) { this.starredListings.remove(l); }
    public void removeFriend(User u) { this.friends.remove(u); }
    public void removeFriendRequest(FriendRequest fr) { this.friendRequests.remove(fr); }
    private void setLocation(Location l) { this.location = l; }

}
