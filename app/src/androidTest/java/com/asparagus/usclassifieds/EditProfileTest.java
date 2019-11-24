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
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

@LargeTest
public class EditProfileTest {

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
    public void editProfileTest() {
        ViewInteraction fw = onView(allOf(withText("Sign in with Google"), childAtPosition(
                allOf(withId(R.id.sign_in_button),
                      childAtPosition(withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")), 0)
                ), 0), isDisplayed()));
        fw.perform(click());

        ViewInteraction imageButton = onView(allOf(withId(R.id.dashboard_button),
                                                   childAtPosition(childAtPosition(withId(android.R.id.content), 0), 5),
                                                   isDisplayed()
        ));
        imageButton.perform(click());

        ViewInteraction button = onView(allOf(withId(R.id.edit_info), withText("Edit Profile"),
                                              childAtPosition(childAtPosition(withId(android.R.id.content), 0), 3),
                                              isDisplayed()
        ));
        button.perform(click());

        ViewInteraction editText = onView(withId(R.id.edit_description));
        editText.perform(replaceText("Did this work"));

        ViewInteraction editText2 = onView(allOf(withId(R.id.edit_description), withText("Did this work"),
                                                 childAtPosition(childAtPosition(withId(android.R.id.content), 0), 10),
                                                 isDisplayed()
        ));
        editText2.perform(closeSoftKeyboard());

        ViewInteraction button2 = onView(allOf(withId(R.id.update), withText("✔"),
                                               childAtPosition(childAtPosition(withId(android.R.id.content), 0), 7),
                                               isDisplayed()
        ));
        button2.perform(click());

        ViewInteraction textView = onView(allOf(withId(R.id.description), withText("Did this work"),
                                                childAtPosition(childAtPosition(withId(android.R.id.content), 0), 10),
                                                isDisplayed()
        ));
        textView.check(matches(withText("Did this work")));
    }
}
