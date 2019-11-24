package com.asparagus.usclassifieds;

import android.provider.Settings;
import android.util.Log;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class GlobalHelperTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void test_distance_comparator() {

    }

    @Test
    public void test_price_comparator_desc() {
        Listing listing1 = new Listing("seller1", "seller@usc.edu", "seller seller", "table1", 1, "big table with legs", "sref1", "100", "200");
        Listing listing2 = new Listing("seller2", "seller@usc.edu", "seller seller", "table2", 2, "big table with legs", "sref2", "100", "200");
        Listing listing3 = new Listing("seller3", "seller@usc.edu", "seller seller", "table3", 3, "big table with legs", "sref3", "100", "200");

        List<Listing> listings = new ArrayList<Listing>();
        listings.add(listing1);
        listings.add(listing2);
        listings.add(listing3);
        listings.sort(GlobalHelper.priceDesc);

        assertTrue(listings.get(0).title.equals("table3"));
        assertTrue(listings.get(1).title.equals("table2"));
        assertTrue(listings.get(2).title.equals("table1"));
    }

    @Test
    public void test_price_comparator_asc() {
        Listing listing1 = new Listing("seller1", "seller@usc.edu", "seller seller", "table1", 1, "big table with legs", "sref1", "100", "200");
        Listing listing2 = new Listing("seller2", "seller@usc.edu", "seller seller", "table2", 2, "big table with legs", "sref2", "100", "200");
        Listing listing3 = new Listing("seller3", "seller@usc.edu", "seller seller", "table3", 3, "big table with legs", "sref3", "100", "200");

        List<Listing> listings = new ArrayList<Listing>();
        listings.add(listing1);
        listings.add(listing2);
        listings.add(listing3);
        listings.sort(GlobalHelper.priceAsc);

        assertTrue(listings.get(0).title.equals("table1"));
        assertTrue(listings.get(1).title.equals("table2"));
        assertTrue(listings.get(2).title.equals("table3"));
    }

    @Test
    public void test_distance() {
        Log.e("DISTANCE: ", Double.toString(GlobalHelper.distance(35.6544, 139.74477, 21.4225, 39.8261)));
    }
}