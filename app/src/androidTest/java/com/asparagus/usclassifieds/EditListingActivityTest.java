package com.asparagus.usclassifieds;

import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static androidx.test.internal.runner.junit4.statement.UiThreadStatement.runOnUiThread;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class EditListingActivityTest {

    private EditListingActivity activity;

    @Rule
    public ActivityTestRule<EditListingActivity> mActivityTestRule = new ActivityTestRule<EditListingActivity>(EditListingActivity.class);

    @Before
    public void setUp() {
        activity = mActivityTestRule.getActivity();
    }

    @Test
    public void invalid_missing_photo() {
        EditText title_edit_text = activity.findViewById(R.id.title_edit_text);
        title_edit_text.setText("Sample Title");
        Button create_button = activity.findViewById(R.id.create_button);
        assertFalse(create_button.isEnabled());
    }

    @Test
    public void invalid_missing_title() {
        TextView upload_text_view = activity.findViewById(R.id.upload_text_view);
        upload_text_view.setText("Sample Title");
        Button create_button = activity.findViewById(R.id.create_button);
        assertFalse(create_button.isEnabled());
    }

    @Test
    public void valid_listing() throws Throwable {
        EditText title_edit_text = activity.findViewById(R.id.title_edit_text);
        title_edit_text.setText("Sample Title");
        TextView upload_text_view = activity.findViewById(R.id.upload_text_view);
        upload_text_view.setText("Sample Title");
        Button create_button = activity.findViewById(R.id.create_button);


        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                activity.checkRequiredFields();

            }

        });
        assertTrue(create_button.isEnabled());
    }

    @After
    public void tearDown() throws Exception {
        activity = null;
    }
}