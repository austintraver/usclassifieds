package com.asparagus.usclassifieds;


import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.DataInteraction;
import androidx.test.espresso.ViewInteraction;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.is;

@LargeTest
public class Blackbox_ListingDisplaysPhoto {

    @Rule
    public ActivityTestRule<HomeActivity> mActivityTestRule = new ActivityTestRule<>(HomeActivity.class);

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
    public void listingDisplaysPhoto() {

        /* Search for a listing with the title "Abstract Art" */
        ViewInteraction listingName = onView(allOf(withId(R.id.search_bar), isDisplayed()));
        listingName.perform(replaceText("Abstract Art"), closeSoftKeyboard());

        /* Click on the spinner that allows you to specify which filter to use */
        ViewInteraction filterSpinner = onView(allOf(withId(R.id.spinner), isDisplayed()));
        filterSpinner.perform(click());

        /* Filter listings by Title */
        DataInteraction checkedTextView = onData(anything())
                .inAdapterView(childAtPosition(withClassName(is("android.widget.PopupWindow$PopupBackgroundView")), 0))
                .atPosition(1);
        checkedTextView.perform(click());

        /* Click on the first listing, should be called "Abstract Art" */
        ViewInteraction result = onView(allOf(withId(R.id.bTitle), withText("Abstract Art"), isDisplayed()));
        result.perform(click());

        /* Check that the image appears
        not entirely certain this test does what I think it does */
        ViewInteraction imageView = onView(withId(R.id.listing_image));
        imageView.check(matches(isDisplayed()));
    }
}
