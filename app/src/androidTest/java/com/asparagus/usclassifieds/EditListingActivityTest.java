package com.asparagus.usclassifieds;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.test.annotation.UiThreadTest;
import androidx.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.*;

public class EditListingActivityTest {

    private EditListingActivity activity;

    @Rule
    public ActivityTestRule<EditListingActivity> mActivityTestRule = new ActivityTestRule<EditListingActivity>(EditListingActivity.class);

    @Before
    public void setUp() throws Exception {
        activity = mActivityTestRule.getActivity();
    }

    @Test
    public void invalid_missing_photo() {
        EditText title_edit_text = activity.findViewById(R.id.title_edit_text);
        title_edit_text.setText("ASDF");
        Button create_button = activity.findViewById(R.id.create_button);
        assertTrue(!create_button.isEnabled());
    }
    @Test
    public void invalid_missing_title() {
        TextView upload_text_view = activity.findViewById(R.id.upload_text_view);
        upload_text_view.setText("ASDF");
        Button create_button = activity.findViewById(R.id.create_button);
        assertTrue(!create_button.isEnabled());
    }
    @Test
    public void valid_listing() {
        TextView upload_text_view = activity.findViewById(R.id.upload_text_view);
        EditText title_edit_text = activity.findViewById(R.id.title_edit_text);
        title_edit_text.setText("ASDF");
        upload_text_view.setText("ASDF");
        Button create_button = activity.findViewById(R.id.create_button);
        assertTrue(create_button.isEnabled());
    }

    @After
    public void tearDown() throws Exception {
        activity = null;
    }
}