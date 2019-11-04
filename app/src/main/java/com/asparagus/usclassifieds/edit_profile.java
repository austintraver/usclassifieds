package com.asparagus.usclassifieds;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class edit_profile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        Intent intent = getIntent();
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.update:
//                User updatedUser = new User();
//
//                Intent update_user = new Intent();
//                setResult(105,update_user);
//                update_user.putExtra("updated_user",updatedUser);
                finish();
            case R.id.cancel:
                finish();
        }
    }
}
