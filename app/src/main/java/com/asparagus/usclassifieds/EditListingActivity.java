package com.asparagus.usclassifieds;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.FileNotFoundException;
import java.io.IOException;

public class EditListingActivity extends Activity {
    private StorageReference mStorageRef;
    private ImageView product_image_view;
    private Button upload_photo_button, cancel_button, create_button;
    private EditText price_edit_text, title_edit_text, description_edit_text;
    private TextView upload_text_view;
    public static final int GET_FROM_GALLERY = 101;
    private Bitmap bitmap = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_listing);

        mStorageRef = FirebaseStorage.getInstance().getReference();
        product_image_view = findViewById(R.id.product_image_view);
        upload_text_view = findViewById(R.id.upload_text_view);
        title_edit_text = findViewById(R.id.title_edit_text);
        price_edit_text = findViewById(R.id.price_edit_text);
        description_edit_text = findViewById(R.id.description_edit_text);
        cancel_button = findViewById(R.id.cancel_button);
        create_button = findViewById(R.id.create_button);
        upload_photo_button = findViewById(R.id.upload_photo_button);

        create_button.setEnabled(false);
        upload_photo_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                startActivityForResult(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI), GET_FROM_GALLERY);
            }
        });

        title_edit_text.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkRequiredFields();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        //Detects request codes
        if(requestCode==GET_FROM_GALLERY && resultCode == Activity.RESULT_OK) {
            Uri selectedImage = data.getData();
            bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                product_image_view.setImageBitmap(bitmap);
                upload_text_view.setText("Upload Complete!");
                checkRequiredFields();
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancel_button:
                Intent canceled = new Intent();
                setResult(400, canceled);
                finish();
                break;

            case R.id.create_button:
                String title = title_edit_text.getText().toString();
                double price = 0;
                if (!price_edit_text.getText().toString().equals("")) {
                    price = Double.parseDouble(price_edit_text.getText().toString());
                }
                String description = description_edit_text.getText().toString();

                // TODO: Put user in
                Listing new_listing = new Listing("username", title, price, description, bitmap);
                Intent newListingAdded = new Intent();
                setResult(Activity.RESULT_OK, newListingAdded);
                finish();
                break;
        }
    }

    private void checkRequiredFields(){

        String s1 = upload_text_view.getText().toString();
        String s2 = title_edit_text.getText().toString();

        if(s1.trim().isEmpty() || s2.trim().isEmpty())
        {
            create_button.setEnabled(false);
        } else {
            create_button.setEnabled(true);
        }
    }
}
