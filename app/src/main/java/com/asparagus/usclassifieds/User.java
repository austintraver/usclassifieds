package com.asparagus.usclassifieds;

import android.location.Location;

import com.mongodb.client.model.geojson.Point;
import com.mongodb.client.model.geojson.Position;

import java.util.HashSet;
import java.util.LinkedList;

public class User {

    // email is the identifier
    private String firstName, lastName, email, phone;
    private Point location;
    private HashSet<User> friends;
    private HashSet<User> outgoingFriendRequests;
    private HashSet<User> incomingFriendRequests;

    public User(String email, String firstName, String lastName, String phone, Location location) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.location = new Point(new Position(
                location.getLatitude(),
                location.getLongitude()));
        this.email = email;
        this.phone = phone;
        this.friends = new HashSet<User>();
        this.outgoingFriendRequests = new HashSet<User>();
        this.incomingFriendRequests = new HashSet<User>();
    }

    /* update all at once because all data will be present
      in update form (default value is current info) */
    public void updateInfo(String firstName, String lastName, String phone, Location location) {
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
    public Point getLocation() { return this.location; }
    public HashSet<User> getFriends() { return this.friends; }
    public HashSet<User> getIncomingFriendRequests() { return this.incomingFriendRequests;}
    public HashSet<User> getOutgoingFriendRequests() { return this.outgoingFriendRequests;}

    // add or remove from the incoming or outgoing friend request
    public void addIncomingFriendRequest(User u) { incomingFriendRequests.add(u); }
    public void addOutgoingFriendRequest(User u) { outgoingFriendRequests.add(u); }
    public void removeIncomingFriendRequest(User u) { incomingFriendRequests.remove(u); }
    public void removeOutgoingFriendRequest(User u) { outgoingFriendRequests.remove(u); }

    //send outgoing friend request
    public void toggleFriendRequest(User u) {
        if (friends.contains(u)) {
            // remove the request
            u.removeIncomingFriendRequest(this);
            removeOutgoingFriendRequest(u);
        } else {
            // request a friend
            u.addIncomingFriendRequest(this);
            addOutgoingFriendRequest(u);
        }
    }

    // accept a friend requests
    public void acceptFriendRequest(User u) {
        addFriend(u);
        removeIncomingFriendRequest(u);
        u.removeOutgoingFriendRequest(this);
    }

    // reject a friend request
    public void rejectFriendRequest(User u) {
        removeIncomingFriendRequest(u);
        u.removeOutgoingFriendRequest(this);
    }


    public void addFriend(User u) {
        friends.add(u);
        if (!u.getFriends().contains(this)) {
            u.addFriend(this);
        }
    }
    public void removeFriend(User u) {
        friends.remove(u);
        if (u.getFriends().contains(this)) {
            u.removeFriend(this);
        }

}
