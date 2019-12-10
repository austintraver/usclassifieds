package com.asparagus.usclassifieds;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import static android.widget.ArrayAdapter.createFromResource;
import static com.asparagus.usclassifieds.R.array.search_choices;
import static com.asparagus.usclassifieds.R.id.sort_spinner;
import static com.asparagus.usclassifieds.R.layout.spinner_item;
import static java.lang.String.format;
import static java.util.Locale.getDefault;

public class SingleListingActivity extends Activity implements OnItemSelectedListener {

    private static final String TAG = SingleListingActivity.class.getSimpleName();
    Listing listing;
    Picasso picasso;
    private Spinner userSpinner;
    private String selectedUser;
    private int userIndex;
    Button sold_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_listing);
        sold_button = findViewById(R.id.sold_button);
        sold_button.setVisibility(View.GONE);

        selectedUser = "";
        userIndex = 0;
        userSpinner = findViewById(R.id.user_spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, GlobalHelper.getUserNames());
        userSpinner.setAdapter(adapter);
        userSpinner.setBackgroundColor(Color.WHITE);
        userSpinner.setOnItemSelectedListener(this);

        userSpinner.setVisibility(View.GONE);
        Intent intent = getIntent();

        User user = GlobalHelper.user;

        Picasso.Builder p = new Picasso.Builder(this);
        this.picasso = p.build();
        this.picasso.setLoggingEnabled(true);

        this.listing = (Listing) intent.getSerializableExtra("listing");
        if (listing != null) {
            /* If the owner is looking at their own listing */
            if (user.email.equals(listing.ownerEmail) && !listing.sold) {
                /* Create a listener for the sold button */
                OnClickListener listener = new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listing.sold = true;
                    }
                };
                /* Set the sold button to visible/clickable */
                sold_button.setVisibility(View.VISIBLE);
                sold_button.setEnabled(false);
                userSpinner.setVisibility(View.VISIBLE);
                //sold_button.setOnClickListener(listener);
                Log.d(TAG, format("onCreate()\nListing: %s\n Description: %s\n", listing.title, listing.description));
            }
        }
        populatePageData();
    }

    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        // An item was selected. You can retrieve the selected item using
        selectedUser = parent.getItemAtPosition(pos).toString();
        userIndex = pos;
        if(!selectedUser.equals(""))
            sold_button.setEnabled(true);
    }

    public void onNothingSelected(AdapterView<?> parent) {}


    private void sendToOtherPage(User u) {
        Intent intent = new Intent(this, OtherProfileActivity.class);
        intent.putExtra("other_user", u);
        startActivityForResult(intent, 54321);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.other_user_button:
                Log.d(TAG, format("onClick()\nUser id: %s", listing.getOwnerID()));
                /* User clicks on someone else's profile */
                User user = GlobalHelper.user;
                if (!listing.getOwnerID().equals(user.userID)) {
                    Query query = FirebaseDatabase.getInstance().getReference("users").child(listing.getOwnerID());
                    ValueEventListener listener = new ValueEventListener() {
                        @Override
                        public void onDataChange(
                                @NonNull DataSnapshot dataSnapshot
                        ) {
                            if (dataSnapshot.exists()) {
                                Log.d(TAG, "User exists");
                                try {
                                    User other = dataSnapshot.getValue(User.class);
                                    sendToOtherPage(other);
                                } catch (DatabaseException dbe) {
                                    Log.e(TAG, format("Database exception has " + "occurred %s", dbe.getMessage()));
                                }
                            }
                            else {
                                Log.e(TAG, "User doesn't exist");
                                Toast.makeText(SingleListingActivity.this, "User doesn't exist", Toast.LENGTH_SHORT)
                                     .show();
                            }
                        }

                        @Override
                        public void onCancelled(
                                @NonNull DatabaseError databaseError
                        ) {
                            Log.d(TAG, "onCancelled()");
                        }
                    };
                    query.addListenerForSingleValueEvent(listener);
                }
                /* User clicks on their own profile, so take them to the
                Profile activity */
                else {
                    startActivity(new Intent(this, ProfileActivity.class));
                }
                break;

            case R.id.sold_button:
                FirebaseDatabase.getInstance().getReference().child("item_listings").child(listing.getOwnerID()).child(listing.getUUID()).child("sold").setValue(true);
                Integer currentSold = Integer.parseInt(GlobalHelper.getUser().sold);
                currentSold = currentSold + 1;
                String tempString = currentSold.toString();
                FirebaseDatabase.getInstance().getReference().child("users").child(GlobalHelper.getUserID()).child("sold").setValue(tempString);
                GlobalHelper.getUser().setSold(tempString);

                User other = GlobalHelper.getActiveUsers().get(userIndex);
                Integer currentBought = Integer.parseInt(other.getBought());
                currentBought = currentBought + 1;
                tempString = currentBought.toString();
                FirebaseDatabase.getInstance().getReference().child("users").child(other.userID).child("bought").setValue(tempString);


                sold_button.setVisibility(View.GONE);
                GlobalHelper.justSoldItem = true;
                GlobalHelper.soldItem = listing;
                finish();
//                Intent i = new Intent();
//                i.putExtra("changedListing",listing);
//                setResult(4444, i);
//                finish();
                break;

            default:
                Log.d(TAG, format("Button ID: %s", v.getId()));
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void populatePageData() {
        if (listing == null) {
            String error = "Sorry, unable to display listing data.";
            Toast.makeText(SingleListingActivity.this, error, Toast.LENGTH_SHORT).show();
            return;
        }
        Log.d(TAG, "Loading page");
        TextView tvSTitle = findViewById(R.id.detail_title);
        TextView tvSPrice = findViewById(R.id.detail_price);
        TextView desc = findViewById(R.id.detail_description);
        Button owner = findViewById(R.id.other_user_button);
        final ImageView ivSListing = findViewById(R.id.listing_image);
        Picasso.get().setLoggingEnabled(true);
        // Load ImageView with photo from firebase per starter code from
        // https://firebase.google.com/docs/storage/android/download-files
        OnSuccessListener<Uri> success = new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Log.d(TAG, format("Loaded URI: %s", uri.toString()));
                Picasso.get().load(uri).into(ivSListing);
            }
        };
        OnFailureListener failure = new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.e(TAG, "Listing has no image.");
            }
        };
        StorageReference child = FirebaseStorage.getInstance().getReference().child(listing.getStorageReference());
        child.getDownloadUrl().addOnSuccessListener(success).addOnFailureListener(failure);
        tvSTitle.setText(listing.getTitle());
        tvSPrice.setText(format(getDefault(), "$%.2f", listing.getPrice()));
        owner.setText(listing.getOwnerName());
        desc.setText(listing.getDescription());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 54321 && resultCode == 12345) {
            Intent otherSearch = new Intent();
            otherSearch.putExtra("otherUser",listing.getOwnerID());
            setResult(54321, otherSearch);
            GlobalHelper.otherUser = listing.getOwnerID();
            finish();
        }
    }
}
