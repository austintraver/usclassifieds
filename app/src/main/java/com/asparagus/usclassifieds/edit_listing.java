package com.asparagus.usclassifieds;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageView;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.FileNotFoundException;
import java.io.IOException;

public class edit_listing extends AppCompatActivity {
    private StorageReference mStorageRef;
    private ImageView product_image_view;
    private Button upload_photo_button, cancel_button, create_button;
    private EditText price_edit_text, title_edit_text, description_edit_text;
    public static final int GET_FROM_GALLERY = 101;
    private Bitmap bitmap = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_listing);

        mStorageRef = FirebaseStorage.getInstance().getReference();
        product_image_view = (ImageView) findViewById(R.id.product_image_view);
        title_edit_text = findViewById(R.id.title_edit_text);
        price_edit_text = findViewById(R.id.price_edit_text);
        description_edit_text = findViewById(R.id.description_edit_text);
        cancel_button = findViewById(R.id.cancel_button);
        create_button = findViewById(R.id.create_button);
        upload_photo_button = findViewById(R.id.upload_photo_button);

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

                if(s.toString().trim().length()==0){
                    create_button.setEnabled(false);
                } else {
                    create_button.setEnabled(true);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                create_button.setEnabled(false);

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
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
            case R.id.cancel:
                Intent canceled = new Intent();
                setResult(Activity.RESULT_CANCELED, canceled);
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
}
