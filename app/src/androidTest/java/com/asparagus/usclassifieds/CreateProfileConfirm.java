package com.asparagus.usclassifieds;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.ViewInteraction;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

@LargeTest
public class CreateProfileConfirm {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    private static Matcher<View> childAtPosition(final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent) &&
                       view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }

    @Test
    public void createProfile() {
        ViewInteraction fw = onView(allOf(withText("Sign in with Google"), childAtPosition(
                allOf(withId(R.id.sign_in_button),
                      childAtPosition(withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")), 0)
                ), 0), isDisplayed()));
        fw.perform(click());
        /*
        UiDevice mUiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        try {
            mUiDevice.findObject(new UiSelector().text("Use another account")).click();
            mUiDevice.findObject(new UiSelector().text("Email or phone")).click();
            mUiDevice.findObject(new UiSelector().text("Email or phone")).setText("tommytrojantest");
            mUiDevice.findObject(new UiSelector().text("Next")).click();
        } catch (UiObjectNotFoundException ignored) {}
        */
        ViewInteraction editFistName = onView(allOf(withId(R.id.edit_first_name),
                                                    childAtPosition(childAtPosition(withId(android.R.id.content), 0),
                                                                    8
                                                    ), isDisplayed()
        ));
        editFistName.perform(replaceText("Tommy"), closeSoftKeyboard());

        ViewInteraction editLastName = onView(
                allOf(withId(R.id.edit_last_name), childAtPosition(childAtPosition(withId(android.R.id.content), 0), 9),
                      isDisplayed()
                ));
        editLastName.perform(replaceText("Trojan"), closeSoftKeyboard());

        ViewInteraction editPhoneNumber = onView(allOf(withId(R.id.edit_phone_number),
                                                       childAtPosition(childAtPosition(withId(android.R.id.content), 0),
                                                                       1
                                                       ), isDisplayed()
        ));
        editPhoneNumber.perform(replaceText("2137404135"), closeSoftKeyboard());

        ViewInteraction editStreetNumber = onView(allOf(withId(R.id.edit_street_number), childAtPosition(
                childAtPosition(withId(android.R.id.content), 0), 5), isDisplayed()));
        editStreetNumber.perform(replaceText("941"), closeSoftKeyboard());

        ViewInteraction editStreetName = onView(allOf(withId(R.id.edit_street_name),
                                                      childAtPosition(childAtPosition(withId(android.R.id.content), 0),
                                                                      6
                                                      ), isDisplayed()
        ));
        editStreetName.perform(replaceText("Bloom Walk"), closeSoftKeyboard());

        ViewInteraction editCityName = onView(
                allOf(withId(R.id.edit_city_name), childAtPosition(childAtPosition(withId(android.R.id.content), 0), 4),
                      isDisplayed()
                ));
        editCityName.perform(replaceText("Los Angeles"), closeSoftKeyboard());

        ViewInteraction editStateName = onView(allOf(withId(R.id.edit_state_code),
                                                     childAtPosition(childAtPosition(withId(android.R.id.content), 0),
                                                                     3
                                                     ), isDisplayed()
        ));
        editStateName.perform(replaceText("CA"), closeSoftKeyboard());

        ViewInteraction editZipCode = onView(
                allOf(withId(R.id.edit_zip_code), childAtPosition(childAtPosition(withId(android.R.id.content), 0), 2),
                      isDisplayed()
                ));
        editZipCode.perform(replaceText("90089"), closeSoftKeyboard());

        ViewInteraction editDescription = onView(allOf(withId(R.id.edit_description),
                                                       childAtPosition(childAtPosition(withId(android.R.id.content), 0),
                                                                       10
                                                       ), isDisplayed()
        ));
        editDescription.perform(replaceText("I am a robot"), closeSoftKeyboard());

        ViewInteraction clickConfirm = onView(allOf(withId(R.id.update), withText("✔"),
                                                    childAtPosition(childAtPosition(withId(android.R.id.content), 0),
                                                                    7
                                                    ), isDisplayed()
        ));
        clickConfirm.perform(click());
    }
}
