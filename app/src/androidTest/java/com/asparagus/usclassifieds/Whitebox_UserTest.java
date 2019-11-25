package com.asparagus.usclassifieds;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.*;

public class Whitebox_UserTest {

    public User user1;
    public User user2;

    @Before
    public void setUp() throws Exception {
        user1 = new User("firstlast@usc.edu", "first", "last", "5555555555", "1", "123", "sesame street", "sesame", "NY", "00000", "puppet 1");
        user2 = new User("elmo@usc.edu", "el", "mo", "6666666666", "2", "456", "sesame street", "sesame", "NY", "00000", "puppet 2");
    }

    @After
    public void tearDown() throws Exception {
        user1 = null;
        user2 = null;
    }

    @Test
    public void test_accept_friend_request() {
        // assume add friend works
        user1.addFriend(user2);

        int user1_friends = user1.getFriends().size();
        int user2_friends = user2.getFriends().size();
        assertFalse(user1.getFriends().containsKey(user2.userID));
        assertFalse(user2.getFriends().containsKey(user1.userID));

        user2.acceptFriendRequest(user1);

        assertTrue(user1.getFriends().size() == user1_friends + 1);
        assertTrue(user2.getFriends().size() == user2_friends + 1);
        assertTrue(user1.getFriends().containsKey(user2.userID));
        assertTrue(user2.getFriends().containsKey(user1.userID));
    }

    @Test
    public void test_reject_friend_request() {
        // assume add friend works
        user1.addFriend(user2);

        int user1_friends = user1.getFriends().size();
        int user2_friends = user2.getFriends().size();
        assertFalse(user1.getFriends().containsKey(user2.userID));
        assertFalse(user2.getFriends().containsKey(user1.userID));

        user2.rejectFriendRequest(user1);

        assertTrue(user1.getFriends().size() == user1_friends);
        assertTrue(user2.getFriends().size() == user2_friends);
        assertFalse(user1.getFriends().containsKey(user2.userID));
        assertFalse(user2.getFriends().containsKey(user1.userID));
    }

    @Test
    public void test_add_friend() {
        int user1_friends = user1.getOutgoingFriendRequests().size();
        int user2_friends = user2.getIncomingFriendRequests().size();

        user1.addFriend(user2);

        assertTrue(user1.getOutgoingFriendRequests().size() == user1_friends + 1);
        assertTrue(user2.getIncomingFriendRequests().size() == user2_friends + 1);
    }

    @Test
    public void test_remove_friend() {
        user1.getFriends().put(user2.userID, "filler");
        user2.getFriends().put(user1.userID, "filler");

        int user1_friends = user1.getFriends().size();
        int user2_friends = user2.getFriends().size();

        user1.removeFriend(user2);

        assertTrue(user1.getFriends().size() == user1_friends - 1);
        assertTrue(user2.getFriends().size() == user2_friends - 1);
        assertFalse(user1.getFriends().containsKey(user2.userID));
        assertFalse(user2.getFriends().containsKey(user1.userID));
    }

    @Test
    public void test_to_map() {
        Map<String, Object> map = user1.toMap();

        assertTrue(map.size() == 17);
        assertTrue(map.get("userID").equals("1"));
        assertTrue(map.get("email").equals("firstlast@usc.edu"));
        assertTrue(map.get("firstName").equals("first"));
        assertTrue(map.get("lastName").equals("last"));
        assertTrue(map.get("streetNumber").equals("123"));
        assertTrue(map.get("streetName").equals("sesame street"));
        assertTrue(map.get("city").equals("sesame"));
        assertTrue(map.get("state").equals("NY"));
        assertTrue(map.get("zipCode").equals("00000"));
        assertTrue(map.get("phone").equals("5555555555"));
        assertTrue(map.containsKey("latitude"));
        assertTrue(map.containsKey("longitude"));
        assertTrue(map.get("description").equals("puppet 1"));

        assertTrue(map.containsKey("incomingFriendRequests"));
        assertTrue(map.containsKey("outgoingFriendRequests"));
        assertTrue(map.containsKey("friends"));
        assertTrue(map.containsKey("notificationTokens"));
    }
}