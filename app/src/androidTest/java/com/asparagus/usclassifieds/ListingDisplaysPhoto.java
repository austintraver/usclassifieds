package com.asparagus.usclassifieds;


import androidx.test.espresso.DataInteraction;
import androidx.test.espresso.ViewInteraction;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import static androidx.test.InstrumentationRegistry.getInstrumentation;
import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static androidx.test.espresso.action.ViewActions.*;
import static androidx.test.espresso.assertion.ViewAssertions.*;
import static androidx.test.espresso.matcher.ViewMatchers.*;

import com.asparagus.usclassifieds.R;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class ListingDisplaysPhoto {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void listingDisplaysPhoto() {
        ViewInteraction fw = onView(
allOf(withText("Sign in with Google"),
childAtPosition(
allOf(withId(R.id.sign_in_button),
childAtPosition(
withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
0)),
0),
isDisplayed()));
        fw.perform(click());
        
        ViewInteraction fw2 = onView(
allOf(withText("Sign in with Google"),
childAtPosition(
allOf(withId(R.id.sign_in_button),
childAtPosition(
withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
0)),
0),
isDisplayed()));
        fw2.perform(click());
        
        ViewInteraction fw3 = onView(
allOf(withText("Sign in with Google"),
childAtPosition(
allOf(withId(R.id.sign_in_button),
childAtPosition(
withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
0)),
0),
isDisplayed()));
        fw3.perform(click());
        
        ViewInteraction fw4 = onView(
allOf(withText("Sign in with Google"),
childAtPosition(
allOf(withId(R.id.sign_in_button),
childAtPosition(
withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
0)),
0),
isDisplayed()));
        fw4.perform(click());
        
        ViewInteraction fw5 = onView(
allOf(withText("Sign in with Google"),
childAtPosition(
allOf(withId(R.id.sign_in_button),
childAtPosition(
withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
0)),
0),
isDisplayed()));
        fw5.perform(click());
        
        ViewInteraction editText = onView(
allOf(withId(R.id.search_bar),
childAtPosition(
childAtPosition(
withId(android.R.id.content),
0),
6),
isDisplayed()));
        editText.perform(replaceText("Ab"), closeSoftKeyboard());
        
        ViewInteraction editText2 = onView(
allOf(withId(R.id.search_bar), withText("Ab"),
childAtPosition(
childAtPosition(
withId(android.R.id.content),
0),
6),
isDisplayed()));
        editText2.perform(click());
        
        ViewInteraction editText3 = onView(
allOf(withId(R.id.search_bar), withText("Ab"),
childAtPosition(
childAtPosition(
withId(android.R.id.content),
0),
6),
isDisplayed()));
        editText3.perform(replaceText("Abstract Art"));
        
        ViewInteraction editText4 = onView(
allOf(withId(R.id.search_bar), withText("Abstract Art"),
childAtPosition(
childAtPosition(
withId(android.R.id.content),
0),
6),
isDisplayed()));
        editText4.perform(closeSoftKeyboard());
        
        ViewInteraction spinner = onView(
allOf(withId(R.id.spinner),
childAtPosition(
childAtPosition(
withId(android.R.id.content),
0),
8),
isDisplayed()));
        spinner.perform(click());
        
        DataInteraction checkedTextView = onData(anything())
.inAdapterView(childAtPosition(
withClassName(is("android.widget.PopupWindow$PopupBackgroundView")),
0))
.atPosition(1);
        checkedTextView.perform(click());
        
        ViewInteraction button = onView(
allOf(withId(R.id.bTitle),
childAtPosition(
childAtPosition(
IsInstanceOf.<View>instanceOf(android.widget.TableLayout.class),
0),
0),
isDisplayed()));
        button.check(matches(isDisplayed()));
        
        ViewInteraction button2 = onView(
allOf(withId(R.id.bTitle), withText("Abstract Art"),
childAtPosition(
childAtPosition(
withClassName(is("android.widget.TableLayout")),
0),
0),
isDisplayed()));
        button2.perform(click());
        
        ViewInteraction imageView = onView(
allOf(withId(R.id.listing_image),
childAtPosition(
childAtPosition(
withId(android.R.id.content),
0),
0),
isDisplayed()));
        imageView.check(matches(isDisplayed()));
        }
    
    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup)parent).getChildAt(position));
            }
        };
    }
    }
