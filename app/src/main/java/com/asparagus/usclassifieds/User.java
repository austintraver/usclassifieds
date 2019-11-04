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
    private HashSet<String> friends;
    private HashSet<String> outgoingFriendRequests;
    private HashSet<String> incomingFriendRequests;

    public User(String email, String firstName, String lastName, String phone, Point location) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.location = location;
        this.phone = phone;
        this.friends = new HashSet<String>();
        this.outgoingFriendRequests = new HashSet<String>();
        this.incomingFriendRequests = new HashSet<String>();
    }

    public User(String email) {
        this.email = email;
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
    public HashSet<String> getFriends() { return this.friends; }
    public HashSet<String> getIncomingFriendRequests() { return this.incomingFriendRequests;}
    public HashSet<String> getOutgoingFriendRequests() { return this.outgoingFriendRequests;}

    // add or remove from the incoming or outgoing friend request
    public void addIncomingFriendRequest(User user) { incomingFriendRequests.add(user.email); }
    public void addOutgoingFriendRequest(User user) { outgoingFriendRequests.add(user.email); }
    public void removeIncomingFriendRequest(User user) { incomingFriendRequests.remove(user.email); }
    public void removeOutgoingFriendRequest(User user) { outgoingFriendRequests.remove(user.email); }

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
        friends.add(u.email);
        if (!u.getFriends().contains(this.email)) {
            u.addFriend(this);
        }
    }
    public void removeFriend(User u) {
        friends.remove(u.email);
        if (u.getFriends().contains(this.email)) {
            u.removeFriend(this);
        }
    }
}
