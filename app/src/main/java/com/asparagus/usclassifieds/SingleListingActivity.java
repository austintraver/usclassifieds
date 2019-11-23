package com.asparagus.usclassifieds;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.PicassoProvider;

import java.io.FileNotFoundException;
import java.io.IOException;

public class SingleListingActivity extends Activity {

    Listing listing;
    Picasso picasso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_listing);
        Button sold_button = findViewById(R.id.sold_button);
        sold_button.setVisibility(View.GONE);
        Intent intent = getIntent();

        Picasso.Builder p = new Picasso.Builder(this);
        this.picasso = p.build();
        this.picasso.setLoggingEnabled(true);

        this.listing = (Listing) intent.getSerializableExtra("listing");
        if (GlobalHelper.getEmail().equals(listing.getOwnerEmail())) {
            sold_button.setVisibility(View.VISIBLE);
            sold_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listing.setSold(true);
                }
            });
        }
        System.out.println("Starting Activity: single_listing");
        System.out.println("Listing : " + listing.getTitle() + "\nDescription: " + listing.getDescription());
        populatePageData();
//        startActivityForResult(intent,RESULT_CANCELED);

    }

    private void sendToOtherPage(User u) {
        Intent intent = new Intent(this, OtherProfileActivity.class);
        intent.putExtra("other_user", u);
        startActivity(intent);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.other_user_button:
                if(!listing.getOwnerID().equals(GlobalHelper.getUserID())) {
                    Query query = FirebaseDatabase.getInstance().getReference("users").child(listing.getOwnerID());
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()) {
                                User other = dataSnapshot.getValue(User.class);
                                sendToOtherPage(other);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            System.out.println("onCancelled called for event listener");
                        }
                    });
                }
                break;

            case R.id.sold_button:
                //FirebaseDatabase.getInstance().getReference("listings").child().   ;d
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void populatePageData()
    {
        if(listing == null)
        {
            System.out.println("ERROR: Page cannot display listing data");

        }
        else
        {
            System.out.println("Loading page");

            TextView tvSTitle = findViewById(R.id.detail_title);
            TextView tvSDesc = findViewById(R.id.detail_description);
            TextView tvSPrice = findViewById(R.id.detail_price);
            Button owner = (Button) findViewById(R.id.other_user_button);
            TextView desc = findViewById(R.id.detail_description);
            final ImageView ivSListing = findViewById(R.id.listing_image);
            picasso.get().setLoggingEnabled(true);

            // Load ImageView with photo from firebase per starter code from
            // https://firebase.google.com/docs/storage/android/download-files
            FirebaseStorage.getInstance().getReference().child(listing.getStorageReference()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                        System.out.println("Loaded URI " + uri.toString());
                        picasso.get().load(uri).into(ivSListing);

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle any errors
                    System.out.println("Listing has no image. Not printing exception");
                }
            });


            tvSTitle.setText(listing.getTitle());
            tvSDesc.setText(listing.getDescription());
            tvSPrice.setText(String.format("$%.2f",listing.getPrice()));
            owner.setText(listing.getOwnerEmail());
            desc.setText(listing.getDescription());
        }
    }
}
