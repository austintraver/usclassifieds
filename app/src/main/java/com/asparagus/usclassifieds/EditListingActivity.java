package com.asparagus.usclassifieds;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.firebase.client.annotations.NotNull;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask.TaskSnapshot;


import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.asparagus.usclassifieds.R.id;
import static com.asparagus.usclassifieds.R.layout;
import static com.asparagus.usclassifieds.R.string;
import static java.lang.Double.parseDouble;

public class EditListingActivity extends Activity {
    public static final int GET_FROM_GALLERY = 101;
    private StorageReference mStorageRef;
    private ImageView product_image_view;
    private Button create_button;
    private EditText price_edit_text, title_edit_text, description_edit_text;
    private TextView upload_text_view;
    private Uri selectedImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_edit_listing);

        mStorageRef = FirebaseStorage.getInstance().getReference();
        product_image_view = findViewById(id.product_image_view);
        upload_text_view = findViewById(id.upload_text_view);
        title_edit_text = findViewById(id.title_edit_text);
        price_edit_text = findViewById(id.price_edit_text);
        description_edit_text = findViewById(id.description_edit_text);
        create_button = findViewById(id.create_button);
        Button upload_photo_button = findViewById(id.upload_photo_button);
        Button cancel_button = findViewById(id.cancel_button);
        create_button.setEnabled(false);
        OnClickListener uploadPhotoListener = new OnClickListener() {
            public void onClick(View v) {
                startActivityForResult(new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI),
                                       GET_FROM_GALLERY
                );
            }
        };
        upload_photo_button.setOnClickListener(uploadPhotoListener);
        OnClickListener cancelButtonListener = new OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        };
        cancel_button.setOnClickListener(cancelButtonListener);
        TextWatcher watcher = new TextWatcher() {
            public void onTextChanged(
                    CharSequence s, int start, int before, int count
            ) {
                checkRequiredFields();
            }

            public void beforeTextChanged(
                    CharSequence s, int start, int count, int after
            ) {}

            public void afterTextChanged(Editable s) {}
        };
        title_edit_text.addTextChangedListener(watcher);
    }


    @Override
    protected void onActivityResult(
            int requestCode, int resultCode, Intent data
    ) {
        super.onActivityResult(requestCode, resultCode, data);
        //Detects request codes
        if (requestCode == GET_FROM_GALLERY && resultCode == Activity.RESULT_OK) {
            selectedImage = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                product_image_view.setImageBitmap(bitmap);
                upload_text_view.setText(string.upload_complete);
                checkRequiredFields();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case id.cancel_button:
                Intent canceled = new Intent();
                setResult(400, canceled);
                finish();
                break;

            case id.create_button:
                String title = title_edit_text.getText().toString();
                double price = 0;
                if (!price_edit_text.getText().toString().equals("")) {
                    price = parseDouble(price_edit_text.getText().toString());
                }
                String description = description_edit_text.getText().toString();
                User user = GlobalHelper.user;
                uploadImage(user.userID, title, price, description);
                setResult(Activity.RESULT_OK, new Intent());
                finish();
                break;
        }
    }

    public boolean checkRequiredFields() {
        String upload = upload_text_view.getText().toString().trim();
        String title = title_edit_text.getText().toString().trim();
        create_button.setEnabled(!upload.isEmpty() && !title.isEmpty());
        return !upload.isEmpty() && !title.isEmpty();
    }

    private void uploadImage(
            String ownerID, String title, double price, String description
    ) {
        if (selectedImage == null) {
            Toast.makeText(EditListingActivity.this, "Please upload image", Toast.LENGTH_SHORT).show();
            return;
        }
        UUID uuid = UUID.randomUUID();
        final User user = GlobalHelper.user;
        StorageReference ref = mStorageRef.child("images/" + user.userID + "/" + uuid.toString());
        String ownerName = String.format("%s %s", user.firstName, user.lastName);
        final String key = FirebaseDatabase.getInstance().getReference("listings").child(user.userID).push().getKey();
        Listing newListing = new Listing(ownerID, ownerName, user.email, title, price, description,
                                         "images/" + user.userID + "/" + uuid.toString(), user.latitude, user.longitude, key
        );
        final Map<String, Object> listingValues = newListing.toMap();

        Map<String, Object> listingUpdates = new HashMap<String, Object>() {{
            put("/listings/" + user.userID + "/" + key, listingValues);
        }};
        GlobalHelper.mDatabase.updateChildren(listingUpdates);
        OnSuccessListener<TaskSnapshot> success = new OnSuccessListener<TaskSnapshot>() {
            public void onSuccess(TaskSnapshot taskSnapshot) {
                Toast.makeText(EditListingActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();
            }
        };
        OnFailureListener failure = new OnFailureListener() {
            public void onFailure(@NonNull Exception e) {
                String error = String.format("Failed %s", e.getLocalizedMessage());
                Toast.makeText(EditListingActivity.this, error, Toast.LENGTH_SHORT).show();
            }
        };
        OnProgressListener<TaskSnapshot> progress = new OnProgressListener<TaskSnapshot>() {
            public void onProgress(@NotNull TaskSnapshot taskSnapshot) {}
        };
        ref.putFile(selectedImage).addOnSuccessListener(success).addOnFailureListener(failure)
           .addOnProgressListener(progress);
    }
}
