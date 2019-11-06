package com.asparagus.usclassifieds;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.algolia.search.saas.AlgoliaException;
import com.algolia.search.saas.CompletionHandler;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class HomeActivity extends Activity implements AdapterView.OnItemSelectedListener {

    private static final int CREATE_LISTING = 101;
    private static final int DASHBOARD = 102;
    private static final String TAG = HomeActivity.class.getSimpleName();
    private static String selection = "Username";
    private Spinner spinner;
    private EditText search_bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Intent intent = getIntent();
        //populateListings();

        // Used to get client token and set that for logged in person
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult().getToken();
                        System.out.println("token: " + token);

                        /* sets the client ID on logged in person, will be used to send notifications on
                         database updates for friend requests */
                        System.out.println("user name: " + GlobalHelper.getUser().getFirstName());
                        GlobalHelper.getUser().addNotificationToken(token);

                        // Log and toast
                        String msg = getString(R.string.msg_token_fmt, token);
                        //Log.d(TAG, msg);
                        Toast.makeText(HomeActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });

        spinner = findViewById(R.id.spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.search_choices, android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(this);

        // Add listener to text box
        search_bar = (EditText) findViewById(R.id.search_bar);
        search_bar.addTextChangedListener(textWatcher);
    }

    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        // An item was selected. You can retrieve the selected item using
        selection = parent.getItemAtPosition(pos).toString();
        System.out.println("Selection is now: " + selection);
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }

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
                System.out.println("clicking search button with value: " + selection);
                break;

            case R.id.map_view:
                Intent mapIntent = new Intent(this, MapsActivity.class);
                startActivity(mapIntent);
                break;

            case R.id.list_view:
                //TODO -->
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CREATE_LISTING && resultCode == Activity.RESULT_OK) {
            // TODO --> Toast blurb of created listing success
        } else if (resultCode == Activity.RESULT_CANCELED) {
            // TODO --> person signed out from the profile page
            Intent signOut = new Intent();
            setResult(Activity.RESULT_CANCELED, signOut);
            finish();
        } else if(requestCode == DASHBOARD && resultCode == 25) {
            fillArray("thisUser");
            populateListings();
        }
    }

    public void getListings(Query query) {
        //listener.onStart();
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //listener.onSuccess(dataSnapshot);
                for (DataSnapshot listingSnapshot : dataSnapshot.getChildren()) {

                    GlobalHelper.searchedListings.add(listingSnapshot.getValue(Listing.class));
                }
                populateListings();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("onCancelled called for event listener");
                //listener.onFailure();
            }
        });
    }


    CompletionHandler fillSearchResultCallback = new CompletionHandler() {

        @Override
        public void requestCompleted(@Nullable JSONObject jsonObject, @Nullable AlgoliaException e) {
            try{
                System.out.println("Printing Algolia Result:\n" + jsonObject.toString(2));
                JSONArray array = jsonObject.getJSONArray("hits");
                for(int i = 0; i < array.length(); i++)
                {
                    GlobalHelper.searchedListings.add(new Listing(array.getJSONObject(i)));
                }
                populateListings();

            } catch (JSONException je){
                je.printStackTrace();
            }

        }
    };

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            if(spinner== null){
                spinner = findViewById(R.id.spinner);
            }
            fillArray(spinner.toString());
        }
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        @Override
        public void afterTextChanged(Editable editable) {
        }
    };

// TODO: Add on text change listener to search bar

    private void fillArray(String select) {    //search based on different listings
        GlobalHelper.searchedListings.clear();

        String query = search_bar.getText().toString();
        System.out.println("Finding results with query string: " + query);
        if(select == "thisUser") {

            // searches Algolia client with getUserID as search query
            GlobalHelper.getAlgoliaListings(GlobalHelper.getUserID(), fillSearchResultCallback);

//            query = FirebaseDatabase.getInstance().getReference("listings").orderByChild("ownerID").equalTo(GlobalHelper.getUserID());
//            getListings(query);

        } else if (select == "Username") {

            GlobalHelper.getAlgoliaListings(query, fillSearchResultCallback);

        } else if (select == "Title") {

            GlobalHelper.getAlgoliaListings(query, fillSearchResultCallback);

        } else {        //select == "Tags"

            System.out.println("select should be Tags: select = " + select);
            GlobalHelper.getAlgoliaListings(query, fillSearchResultCallback);

        }

    }

    private void populateListings() {
        //select can be one of three things = { Username, Title, Tags }
        //These are the three options for search parameters
        ArrayList<Listing> listings = GlobalHelper.searchedListings;
        ListingAdapter adapter = new ListingAdapter(this, listings);
        ListView lv = (ListView) findViewById(R.id.lvListing);
        lv.setAdapter(adapter);
    }


}
