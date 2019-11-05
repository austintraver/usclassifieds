package com.asparagus.usclassifieds;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
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
    private ImageView imageView;
    private Button upload_photo_button, cancel_button, create_button;
    public static final int GET_FROM_GALLERY = 101;
    private Bitmap bitmap = null;
    private TextView price_text_view, title_text_view, category_text_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_listing);

        mStorageRef = FirebaseStorage.getInstance().getReference();
        imageView = (ImageView) findViewById(R.id.image);

        price_text_view = findViewById(R.id.price_text_view);
        title_text_view = findViewById(R.id.title_text_view);
        category_text_view = findViewById(R.id.category_text_view);

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

        title_text_view.addTextChangedListener(new TextWatcher() {

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
                imageView.setImageBitmap(bitmap);
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
                TextView textView = findViewById(R.id.title_text_view);
                String title = (String) textView.getText();

                textView = findViewById(R.id.category_text_view);
                String category = (String) textView.getText();

                textView = findViewById(R.id.textView10);
                String description = (String) textView.getText();


                //Double price = findViewById(R.id.new_price);

                Intent newListingAdded = new Intent();
                setResult(Activity.RESULT_OK, newListingAdded);
                finish();
                break;
        }
    }
}
