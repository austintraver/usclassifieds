package com.asparagus.usclassifieds;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.algolia.search.saas.AlgoliaException;
import com.algolia.search.saas.Client;
import com.algolia.search.saas.CompletionHandler;
import com.algolia.search.saas.Index;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Comparator;

import static android.R.layout.simple_spinner_dropdown_item;
import static android.widget.ArrayAdapter.createFromResource;
import static com.asparagus.usclassifieds.GlobalHelper.ALGOLIA_ADMIN_KEY;
import static com.asparagus.usclassifieds.GlobalHelper.ALGOLIA_ID;
import static com.asparagus.usclassifieds.GlobalHelper.getUser;
import static com.asparagus.usclassifieds.R.array.search_choices;
import static com.asparagus.usclassifieds.R.array.sort_choices;
import static com.asparagus.usclassifieds.R.id.sort_spinner;
import static com.asparagus.usclassifieds.R.id.spinner;
import static com.asparagus.usclassifieds.R.layout.activity_home;
import static com.asparagus.usclassifieds.R.layout.spinner_item;
import static java.lang.String.format;

public class HomeActivity extends Activity implements OnItemSelectedListener {

    private static final int CREATE_LISTING = 101;
    private static final int DASHBOARD = 102;
    private static String selection = "Username";
    private static String queryString;
    private final String TAG = HomeActivity.class.getSimpleName();
    private Spinner sortSpinner;
    private EditText search_bar;
    private Comparator<Listing> comparator = GlobalHelper.priceAsc;
    private Spinner filterSpinner;
    private Index index;
    private ArrayList<Listing> listings;
    private Boolean restrictToFriendsOnly = false;
    CompletionHandler fillSearchResultCallback = new CompletionHandler() {
        @Override
        public void requestCompleted(JSONObject jsonObject, AlgoliaException ae) {
            try {
                if (jsonObject == null) {
                    return;
                }
                listings.clear();
                JSONArray array = jsonObject.getJSONArray("hits");
                for (int i = 0; i < array.length(); i++) {
                    if (!array.isNull(i) && !array.getJSONObject(i).isNull("ownerID")) {
                        Listing l = new Listing(array.getJSONObject((i)));
                        boolean isFriend = GlobalHelper.getUser().getFriends().containsKey(l.getOwnerID()) && (!l.getOwnerID().equals(GlobalHelper.getUserID()));
//                        Log.d(TAG,"Item: " + l.getTitle() + " From Friend? " + Boolean.toString(isFriend) + " restrict? " + Boolean.toString(restrictToFriendsOnly) + " adding ? " + Boolean.toString((!restrictToFriendsOnly || isFriend)));
                        if(l.sold == false && (!restrictToFriendsOnly || isFriend)) {
                            listings.add(new Listing(array.getJSONObject(i)));
                        }
                    }
                }
                listings.sort(comparator);
                populateListings();
            } catch (JSONException je) {
                je.printStackTrace();
            }
        }
    };
    private TextWatcher textWatcher = new TextWatcher() {
        public void onTextChanged(CharSequence charSequence, int a, int b, int c) {
            if (filterSpinner == null) {
                filterSpinner = findViewById(spinner);
            }
            fillArray(selection);
        }

        public void beforeTextChanged(CharSequence charSequence, int a, int b, int c) {}

        public void afterTextChanged(Editable editable) {}
    };

    // Another interface callback
    public void onNothingSelected(AdapterView<?> parent) {}

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.create_listing:
                Intent create_listing = new Intent(this, EditListingActivity.class);
                startActivityForResult(create_listing, CREATE_LISTING);
                break;
            case R.id.dashboard_button:
                Intent dashboard = new Intent(this, ProfileActivity.class);
                startActivityForResult(dashboard, DASHBOARD);
                break;
            case R.id.search_button:
                fillArray(selection);
                break;
            case R.id.map_view:
                if(listings != null && listings.size() != 0) {
                    Intent mapIntent = new Intent(this, MapsActivity.class);
                    mapIntent.putExtra("listingArray", listings);
                    startActivity(mapIntent);
                }
                break;
            case R.id.list_view:
                break;
            case R.id.friends_only:
                Log.d(TAG, "Toggling to friends only search");
                toggleFriendsOnlySearch();
                fillArray(selection);
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_home);

        filterSpinner = findViewById(spinner);
        sortSpinner = findViewById(sort_spinner);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = createFromResource(this, search_choices, spinner_item);
        ArrayAdapter<CharSequence> sortAdapter = createFromResource(this, sort_choices, spinner_item);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(simple_spinner_dropdown_item);
        sortAdapter.setDropDownViewResource(simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        filterSpinner.setAdapter(adapter);
        sortSpinner.setAdapter(sortAdapter);

        filterSpinner.setOnItemSelectedListener(this);
        sortSpinner.setOnItemSelectedListener(this);

        // Add listener to text box
        search_bar = findViewById(R.id.search_bar);
        search_bar.addTextChangedListener(textWatcher);

        listings = new ArrayList<Listing>();
        Client client = new Client(ALGOLIA_ID, ALGOLIA_ADMIN_KEY);
        index = client.getIndex("item_listings");

        // Define searchable attributes to restrict in search queries
        try {
            final JSONArray attributes = new JSONArray() {{
                put("ownerName");
                put("ownerEmail");
                put("title");
                put("description");
            }};
            JSONObject settings = new JSONObject() {{
                put("searchableAttributes", attributes);
            }};
            index.setSettingsAsync(settings, null);
        } catch (JSONException e) {
            Log.d(TAG, e.getLocalizedMessage());
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if(GlobalHelper.justSoldItem) {
            GlobalHelper.justSoldItem = false;
            listings.remove(GlobalHelper.soldItem);
            populateListings();
            GlobalHelper.soldItem = null;
            Toast.makeText(HomeActivity.this, "Item marked as sold.", Toast.LENGTH_SHORT)
                    .show();

            Query q = FirebaseDatabase.getInstance().getReference("item_listings").child(GlobalHelper.getUserID()).limitToFirst(GlobalHelper.QUERY_RESULTS_LENGTH);
            getListings(q);

        }

        if(!GlobalHelper.otherUser.equals("")) {
            String other = GlobalHelper.otherUser;
            GlobalHelper.otherUser = "";

            Query q = FirebaseDatabase.getInstance().getReference("item_listings").child(other).limitToFirst(GlobalHelper.QUERY_RESULTS_LENGTH);
            getListings(q);
        }

        GlobalHelper.getActiveUsers().clear();
        GlobalHelper.getUserNames().clear();
        GlobalHelper.updateUserList();
    }

    public void getListings(Query query) {
        listings.clear();
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot listingSnapshot : dataSnapshot.getChildren()) {
                    Listing l = listingSnapshot.getValue(Listing.class);
                    if(l.sold == false) {
                        listings.add(l);
                    }
                }
                populateListings();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG, "onCancelled()");
            }
        });
    }

    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        // An item was selected. You can retrieve the selected item using
        selection = parent.getItemAtPosition(pos).toString();
        Log.d(TAG, format("Selection is: %s\nQuery string is: %s", selection, queryString));
        selection = filterSpinner.getSelectedItem().toString();
        getAlgoliaListings(queryString, fillSearchResultCallback);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == DASHBOARD) {
            if (resultCode == RESULT_CANCELED) {
                System.out.println("Home --> Signing out");
                Intent signOut = new Intent();
                setResult(RESULT_CANCELED, signOut);
                finish();
            }
            else if (resultCode == 25) {
//                fillArray("thisUser");
//                populateListings();
                Query q = FirebaseDatabase.getInstance().getReference("item_listings").child(GlobalHelper.getUserID()).limitToFirst(GlobalHelper.QUERY_RESULTS_LENGTH);
                getListings(q);
            }

        }
        else if (resultCode == 4444) {
            System.out.println("With Res code 444");
            Toast.makeText(HomeActivity.this, "Item marked as sold.", Toast.LENGTH_SHORT)
                    .show();
            Intent temp = getIntent();
            Listing tempListing = (Listing)temp.getSerializableExtra("changedListing");
            System.out.println("new listing: " + tempListing);

            if(listings.contains(tempListing)) {
                System.out.println("Removing listing: " + tempListing);
                listings.remove(tempListing);
                populateListings();
            }
        } else if( resultCode == 54321) {
            Intent get = getIntent();
            String other = get.getStringExtra("otherUser");

            Query q = FirebaseDatabase.getInstance().getReference("item_listings").child(other).limitToFirst(GlobalHelper.QUERY_RESULTS_LENGTH);
            getListings(q);
        }
    }

    private void fillArray(String select) {
        // Search based on different listings
        GlobalHelper.searchedListings.clear();
        if (search_bar == null || search_bar.getText().toString().equals("")) {
            return;
        }
        queryString = search_bar.getText().toString();
        Log.d(TAG, format("Finding results with query string: %s - userID: %s\n", queryString, GlobalHelper.getUserID()));
        Log.d(TAG, format("select is: %s", select));
        if ("thisUser".equals(select)) {
            Log.d(TAG, "Querying user's items");
            // Searches Algolia client with getId as search query
            getAlgoliaListings(GlobalHelper.user.userID, fillSearchResultCallback);
        }
        else {
            getAlgoliaListings(queryString, fillSearchResultCallback);
        }
    }

    private void populateListings() {
        // Select can be one of three things = { Username, Title, Tags }
        // These are the three options for search parameters
        Log.d(TAG, "Populating Listings");
        // Global adapter to show list
        ListingAdapter adapter = new ListingAdapter(this, listings);
        // Global list view for page
        ListView lv = findViewById(R.id.lvListing);
        lv.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    public void getAlgoliaListings(String select, CompletionHandler completionHandler) {
        String sortSelection = sortSpinner.getSelectedItem().toString();
        switch (sortSelection) {
            case "Price ↑":
                comparator = GlobalHelper.priceDesc;
                break;
            case "Price ↓":
                comparator = GlobalHelper.priceAsc;
                break;
            case "Distance":
                comparator = GlobalHelper.distComparator;
                break;
        }
        selection = filterSpinner.getSelectedItem().toString();
        Log.d(TAG, format("Getting search attribute for %s\n", selection));
        if (selection.equals("Username")) {
            index.searchAsync(new com.algolia.search.saas.Query(select)
                            .setRestrictSearchableAttributes("ownerName", "ownerEmail")
                            .setLength(GlobalHelper.QUERY_RESULTS_LENGTH)
                            .setFilters(String.format("NOT ownerID:\"%s\"", GlobalHelper.getUserID())),
                      completionHandler)
            ;
        } else if (selection.equals("Title")) {
            index.searchAsync(new com.algolia.search.saas.Query(select)
                            .setRestrictSearchableAttributes("title")
                            .setLength(GlobalHelper.QUERY_RESULTS_LENGTH)
                            .setFilters(String.format("NOT ownerID:\"%s\"", GlobalHelper.getUserID())),
                    completionHandler
            );
        } else {
            index.searchAsync(new com.algolia.search.saas.Query(select)
                                .setLength(GlobalHelper.QUERY_RESULTS_LENGTH)
                                .setFilters(String.format("NOT ownerID:%s", GlobalHelper.getUserID())),
                    completionHandler);
        }
    }

    private void toggleFriendsOnlySearch(){
        restrictToFriendsOnly = !restrictToFriendsOnly;
        TextView friendsOnlyBtn = (TextView) findViewById(R.id.friends_only);
        if(restrictToFriendsOnly){
            friendsOnlyBtn.setText("Toggle: friends");
        } else {
            friendsOnlyBtn.setText("Toggle: all");
        }
    }
}
