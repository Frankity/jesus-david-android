package com.jedga95.reddit;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.jedga95.reddit.reddits.json.model.RedditItem;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import java.util.concurrent.CountDownLatch;

import static android.content.Intent.ACTION_VIEW;
import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.InstrumentationRegistry.getTargetContext;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.swipeDown;
import static android.support.test.espresso.action.ViewActions.swipeUp;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.Intents.times;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ListBehaviorTestPhone {

    @Rule
    public IntentsTestRule<RedditListActivity> mIntentRule = new IntentsTestRule<>(
            RedditListActivity.class);

    @Rule
    public ActivityTestRule<RedditDetailActivity> mDetailRule = new ActivityTestRule<>(
            RedditDetailActivity.class, false, false);

    @After
    public void tearDown() {
        mIntentRule.getActivity().finish();
    }

    @Before
    public void testPreconditions() {
        onView(withId(R.id.reddit_list))
                .check(matches(isDisplayed()));
    }

    @Test
    public void test_0useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        Assert.assertEquals("com.jedga95.reddit", appContext.getPackageName());
    }

    @Test
    public void test_1BasicActivityHandling() {
        onView(withId(R.id.reddit_list)).perform(swipeUp()).perform(swipeUp());
        onView(withId(R.id.reddit_list))
                .perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));
        intended(hasComponent(RedditDetailActivity.class.getName()));
        onView(withId(R.id.detail_root_view)).perform(swipeUp());
        Espresso.pressBack();
    }

    @Test
    public void test_2RotateActivityHandling() {
        rotateScreen(mIntentRule.getActivity());
        rotateScreen(mIntentRule.getActivity());
        onView(withId(R.id.reddit_list))
                .perform((RecyclerViewActions.actionOnItemAtPosition(2, click())));
        intended(hasComponent(RedditDetailActivity.class.getName()));
        rotateScreen(mIntentRule.getActivity());
        Espresso.pressBack();
        rotateScreen(mIntentRule.getActivity());
    }

    @Test
    public void test_3RefreshEntries() {
        onView(withId(R.id.reddit_list)).perform(swipeDown());
    }

    @Test
    public void test_4InjectContent() {
        final String loremIpsum = "Lorem ipsum";
        final String loremIpsumLong = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. " +
                "Vestibulum nec dolor quis massa pulvinar molestie maximus in libero. Praesent " +
                "sit amet urna id nunc efficitur elementum. Cras dignissim felis eu enim " +
                "pellentesque, in iaculis enim elementum. Praesent eleifend vel augue ut dictum. " +
                "Aliquam varius commodo purus, efficitur feugiat est hendrerit sit amet. Ut " +
                "maximus, justo nec egestas hendrerit, tellus magna convallis lectus, ut" +
                "pellentesque metus urna in ligula. Praesent sit amet augue sem. Quisque " +
                "vulputate ligula eget diam pretium posuere. Cras dapibus nulla ut ultricies " +
                "ullamcorper. Praesent id arcu ut sem consectetur finibus ut quis ex. Quisque " +
                "volutpat liberosed gravida tempor. In hac habitasse platea dictumst. Phasellus " +
                "eu fermentum velit, ut commodo elit. Phasellus tincidunt tincidunt dui ut" +
                "volutpat. Etiam tincidunt dui sed ellus venenatis mattis.";
        final RedditItem item = new RedditItem();
        item.setDisplayName(loremIpsum);
        item.setContent(loremIpsumLong);
        item.setUrl("http://google.com");
        item.setIconImgUrl("");
        item.setBannerImageUrl("");

        Bundle bundle = new Bundle();
        bundle.putSerializable(RedditDetailFragment.ARG_SERIAL_ITEM, item);

        final Intent launchIntent = new Intent();
        launchIntent.putExtras(bundle);
        mDetailRule.launchActivity(launchIntent);

        intended(hasComponent(RedditDetailActivity.class.getName()));

        onView(withId(R.id.reddit_detail)).check(matches(withText(loremIpsumLong)));
    }

    @Test
    public void test_5OpenUrl() {
        onView(withId(R.id.reddit_list))
                .perform((RecyclerViewActions.actionOnItemAtPosition(3, click())));
        intended(hasComponent(RedditDetailActivity.class.getName()));
        onView(withId(R.id.open_on_browser_fab))
                .perform(click());
        intended(allOf(hasAction(ACTION_VIEW)));
    }

    public static void rotateScreen(Activity activity) {
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        final int orientation = InstrumentationRegistry.getTargetContext()
                .getResources()
                .getConfiguration()
                .orientation;
        final int newOrientation = (orientation == Configuration.ORIENTATION_PORTRAIT) ?
                ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE :
                ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;

        activity.setRequestedOrientation(newOrientation);

        getInstrumentation().waitForIdle(new Runnable() {
            @Override
            public void run() {
                countDownLatch.countDown();
            }
        });

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException("Screen rotation failed", e);
        }
    }
}
