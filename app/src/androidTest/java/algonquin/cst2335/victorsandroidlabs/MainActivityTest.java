package algonquin.cst2335.victorsandroidlabs;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.ViewInteraction;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public ActivityScenarioRule<MainActivity> mActivityScenarioRule =
            new ActivityScenarioRule<>(MainActivity.class);

    /**
     * test a password with only numbers
     */
    @Test
    public void mainActivityTest() {
        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction appCompatEditText = onView(withId(R.id.password_et));
        appCompatEditText.perform(replaceText("12345"), closeSoftKeyboard());

        ViewInteraction materialButton = onView(withId(R.id.login_btn));
        materialButton.perform(click());

        ViewInteraction textView = onView(withId(R.id.password_tv));
        textView.check(matches(withText("You shall not pass!")));
    }
    /**
     * test a password with no uppercase letters
     */
    @Test
    public void testFindMissingUpperCase() {

        ViewInteraction appCompatEditText = onView(withId(R.id.password_et));
        appCompatEditText.perform(replaceText("password123#$*"));

        ViewInteraction materialButton = onView(withId(R.id.login_btn));
        materialButton.perform(click());

        ViewInteraction textView = onView(withId(R.id.password_tv));
        textView.check(matches(withText("You shall not pass!")));
    }
    /**
     * test a password with no lowercase letters
     */
    @Test
    public void testFindMissingLowerCase() {

        ViewInteraction appCompatEditText = onView(withId(R.id.password_et));
        appCompatEditText.perform(replaceText("PASSWORD123#$*"));

        ViewInteraction materialButton = onView(withId(R.id.login_btn));
        materialButton.perform(click());

        ViewInteraction textView = onView(withId(R.id.password_tv));
        textView.check(matches(withText("You shall not pass!")));
    }
    /**
     * test a password with no numbers
     */
    @Test
    public void testFindMissingNumber() {

        ViewInteraction appCompatEditText = onView(withId(R.id.password_et));
        appCompatEditText.perform(replaceText("Password#$*"));

        ViewInteraction materialButton = onView(withId(R.id.login_btn));
        materialButton.perform(click());

        ViewInteraction textView = onView(withId(R.id.password_tv));
        textView.check(matches(withText("You shall not pass!")));
    }
    /**
     * test a password with no special characters
     */
    @Test
    public void testFindMissingSpecial() {

        ViewInteraction appCompatEditText = onView(withId(R.id.password_et));
        appCompatEditText.perform(replaceText("Password123"));

        ViewInteraction materialButton = onView(withId(R.id.login_btn));
        materialButton.perform(click());

        ViewInteraction textView = onView(withId(R.id.password_tv));
        textView.check(matches(withText("You shall not pass!")));
    }
    /**
     * test a password that meets all requirements
     */
    @Test
    public void testMeetAllRequirements() {

        ViewInteraction appCompatEditText = onView(withId(R.id.password_et));
        appCompatEditText.perform(replaceText("Password123#$*"));

        ViewInteraction materialButton = onView(withId(R.id.login_btn));
        materialButton.perform(click());

        ViewInteraction textView = onView(withId(R.id.password_tv));
        textView.check(matches(withText("Your password meets the requirements")));
    }
}
