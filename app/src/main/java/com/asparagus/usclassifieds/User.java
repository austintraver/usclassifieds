package com.asparagus.usclassifieds;

import android.location.Location;

import com.mongodb.client.model.geojson.Point;
import com.mongodb.client.model.geojson.Position;

import java.util.HashSet;
import java.util.LinkedList;

public class User {

    public String firstName, lastName, email, phone;
    public LinkedList<Listing> listings, starredListings;
    public Point location;
    public HashSet<User> friends;
    public HashSet<User> outgoingFriendRequests;
    public HashSet<User> incomingFriendRequests;

    public User(String firstName, String lastName, Location location, String email, String phone) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.location = new Point(new Position(
                            location.getLatitude(),
                            location.getLongitude()));
        this.email = email;
        this.phone = phone;
        this.listings = new LinkedList<Listing>();
        this.starredListings = new LinkedList<Listing>();
        this.friends = new HashSet<User>();
        this.outgoingFriendRequests = new HashSet<User>();
        this.incomingFriendRequests = new HashSet<User>();
    }

    /* update all at once because all data will be present
      in update form (default value is current info) */
    public void updateInfo(String firstName, String lastName, String email, String phone) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        DatabaseClient.updateUser(this);
    }

    // getter methods
    public String getFirstName() { return this.firstName; }
    public String getLastName() { return this.lastName; }
    public String getEmail() { return this.email; }
    public String getPhone() { return this.phone; }
    public LinkedList<Listing> getListings() {
        return DatabaseClient.listingsBy(this);
    }
    public LinkedList<Listing> getStarredListings() { return this.starredListings; }
    public Point getLocation() { return this.location; }
    public HashSet<User> getIncomingFriendRequests() { return this.incomingFriendRequests;}
    public HashSet<User> getoutgoingFriendRequests() { return this.outgoingFriendRequests;}

    // setter methods for listings
    public void addListing(Listing l) { this.listings.add(l); }
    public void removeListing(Listing l) { this.listings.remove(l); }
    public void starListing(Listing l) { this.starredListings.add(l); }
    public void unstarListing(Listing l) { this.starredListings.remove(l); }
    public void removeFriend(User u) { this.friends.remove(u); }
    public void removeIncomingFriendRequest(User u) { this.incomingFriendRequests.remove(u); }
    public void outgoingFriendRequests(User u) { this.outgoingFriendRequests.remove(u); }
    public void setLocation(Point l) { this.location = l; }

    //add incoming friend request
    public void addIncomingRequest(User u) {
        this.incomingFriendRequests.add(u);
    }

    //add outgoing friend request
    public void addOutgoingRequest(User u) {
        this.outgoingFriendRequests.add(u);
    }

    // triggered when the person you sent a request to accepts or rejects your offer, or you cancel
    public void removeOutgoingFriendRequest(User u) {
        outgoingFriendRequests.remove(u);
    }

    // accept friend requests
    public void acceptFriendRequest(User u) {
        this.friends.add(u);
        this.incomingFriendRequests.remove(u);
    }

    // rejects a friend request
    public void rejectFriendRequest(User u) {
        this.incomingFriendRequests.remove(u);
    }

}
