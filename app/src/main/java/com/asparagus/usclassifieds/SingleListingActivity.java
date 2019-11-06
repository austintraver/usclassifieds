package com.asparagus.usclassifieds;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
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
            TextView tvSTitle = findViewById(R.id.tvSingleTitle);
            TextView tvSDesc = findViewById(R.id.tvSingleDesc);
            TextView tvSPrice = findViewById(R.id.tvSinglePrice);
            ImageView ivSListing = findViewById(R.id.ivSingleListing);
            // Load ImageView with photo from firebase per starter code from
            // https://firebase.google.com/docs/storage/android/download-files
//            FirebaseStorage.getInstance().getReference().child(listing.getStorageReference()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                @Override
//                public void onSuccess(Uri uri) {
//                    try {
//                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
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
            Glide.with(this).load(listing.getStorageReference()).into(ivSListing);
            tvSTitle.setText(listing.getTitle());
            tvSDesc.setText(listing.getDescription());
            tvSPrice.setText(String.format("$%.2f",listing.getPrice()));
        }
    }
}
