package com.asparagus.usclassifieds;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.*;

public class ListingTest {

    private Listing listing;

    @Before
    public void setUp() throws Exception {
        listing = new Listing("seller",  "seller seller", "seller@usc.edu", "table", 1, "big table with legs", "sref", "100", "200");
    }

    @After
    public void tearDown() throws Exception {
        listing = null;
    }

    @Test
    public void test_to_map() {
        Map<String, Object> map = listing.toMap();

        assertTrue(map.size() == 10);
        assertTrue(map.get("title").equals("table"));
        assertTrue(map.get("description").equals("big table with legs"));
        assertTrue(map.get("price").equals(1.0));
        assertTrue(map.get("ownerID").equals("seller"));
        assertTrue(map.get("ownerEmail").equals("seller@usc.edu"));
        assertTrue(map.get("ownerName").equals("seller seller"));
        assertTrue(map.get("sold").equals(false));
        assertTrue(map.get("storageReference").equals("sref"));
        assertTrue(map.get("latitude").equals("100"));
        assertTrue(map.get("longitude").equals("200"));
    }
}