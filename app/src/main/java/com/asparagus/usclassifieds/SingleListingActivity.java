package com.asparagus.usclassifieds;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;

import java.io.FileNotFoundException;
import java.io.IOException;

public class SingleListingActivity extends Activity {

    Listing listing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("Starting Activity: single_listing");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_listing);

        Intent intent =  getIntent();
        this.listing = (Listing) intent.getSerializableExtra("listing");
        System.out.println("Listing : " + listing.getTitle() + "\nDescription: " + listing.getDescription());
        populatePageData();
//        startActivityForResult(intent,RESULT_CANCELED);

    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.other_user_button:
                if(listing.getOwnerID() != GlobalHelper.getUserID()) {
                    Intent intent = new Intent(this, OtherProfileActivity.class);
                    intent.putExtra("other_user", listing.getOwnerID());
                    startActivity(intent);
                }
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
            TextView tvSTitle = findViewById(R.id.detail_title);
            TextView tvSDesc = findViewById(R.id.detail_description);
            TextView tvSPrice = findViewById(R.id.detail_price);
            Button owner = (Button) findViewById(R.id.other_user_button);
            TextView desc = findViewById(R.id.detail_description);
            final ImageView ivSListing = findViewById(R.id.listing_image);
            // Load ImageView with photo from firebase per starter code from
            // https://firebase.google.com/docs/storage/android/download-files
//            FirebaseStorage.getInstance().getReference().child(listing.getStorageReference()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                @Override
//                public void onSuccess(Uri uri) {
//                    try {
//                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
//                        ivSListing.setImageBitmap(bitmap);
//                    } catch (FileNotFoundException e) {
//                        e.printStackTrace();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }).addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception exception) {
//                    // Handle any errors
//                }
//            });
//            StorageReference sref = FirebaseStorage.getInstance().getReference().child(listing.getStorageReference());
//            GlideApp.with(this).load(sref).into(ivSListing);
            tvSTitle.setText(listing.getTitle());
            tvSDesc.setText(listing.getDescription());
            tvSPrice.setText(String.format("$%.2f",listing.getPrice()));
            owner.setText(listing.getOwnerEmail());
            desc.setText(listing.getDescription());
        }
    }
}
