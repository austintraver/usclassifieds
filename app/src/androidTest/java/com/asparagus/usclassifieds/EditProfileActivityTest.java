package com.asparagus.usclassifieds;

import android.widget.EditText;

import androidx.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static androidx.test.internal.runner.junit4.statement.UiThreadStatement.runOnUiThread;
import static org.junit.Assert.*;

public class EditProfileActivityTest {

    private EditProfileActivity activity;

    private EditText first, last, streetNumber, streetName, city, state, zip;

    @Rule
    public ActivityTestRule<EditProfileActivity> mActivityTestRule = new ActivityTestRule<EditProfileActivity>(EditProfileActivity.class);

    @Before
    public void setUp() {
        activity = mActivityTestRule.getActivity();
        first = activity.findViewById(R.id.edit_first_name);
        last = activity.findViewById(R.id.edit_last_name);
        streetNumber = activity.findViewById(R.id.edit_street_number);
        streetName = activity.findViewById(R.id.edit_street_name);
        city = activity.findViewById(R.id.edit_city_name);
        state = activity.findViewById(R.id.edit_state_code);
        zip = activity.findViewById(R.id.edit_zip_code);
    }


    @Test
    public void test_missing_required_fields() throws Throwable {
        first.setText("a");
        last.setText("b");
        streetNumber.setText("c");
        streetName.setText("d");
        city.setText("e");
        state.setText("f");

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                assertFalse(activity.hasRequiredFields());
            }
        });
    }

    @Test
    public void test_has_required_fields() throws Throwable {
        first.setText("a");
        last.setText("b");
        streetNumber.setText("c");
        streetName.setText("d");
        city.setText("e");
        state.setText("f");
        zip.setText("g");

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                assertTrue(activity.hasRequiredFields());
            }
        });
    }

    @After
    public void tearDown() throws Exception {
        activity = null;
    }
}