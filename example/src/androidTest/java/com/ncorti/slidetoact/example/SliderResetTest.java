package com.ncorti.slidetoact.example;

import android.content.Context;
import android.content.Intent;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import android.view.WindowManager;

import com.ncorti.slidetoact.SlideToActView;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.swipeRight;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class SliderResetTest {

    @Rule
    public final ActivityTestRule<SampleActivity> mActivityRule =
            new ActivityTestRule<SampleActivity>(SampleActivity.class) {
                @Override
                protected Intent getActivityIntent() {
                    Context targetContext = InstrumentationRegistry.getInstrumentation()
                            .getTargetContext();
                    Intent result = new Intent(targetContext, SampleActivity.class);
                    result.putExtra(SampleActivity.EXTRA_PRESSED_BUTTON, R.id.button_area_margin);
                    return result;
                }
            };

    @Before
    public void setUp() {
        // Force wake up of device for Circle CI test execution.
        final SampleActivity activity = mActivityRule.getActivity();
        Runnable wakeUpDevice = new Runnable() {
            public void run() {
                activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON |
                        WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                        WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            }
        };
        activity.runOnUiThread(wakeUpDevice);
    }

    @Test
    public void testSlideToActView_withSwipeRight_isCompleted() throws InterruptedException {
        onView(withId(R.id.slide_1)).perform(swipeRight());
        Thread.sleep(1500);
        assertTrue(((SlideToActView) mActivityRule.getActivity().findViewById(R.id.slide_1)).isCompleted());
    }

    @Test
    public void testSlideToActView_withSwipeAndReset_isNotCompleted() throws InterruptedException {
        onView(withId(R.id.slide_1)).perform(swipeRight());
        Thread.sleep(1200);
        onView(withId(R.id.reset)).perform(click());
        Thread.sleep(700);
        assertFalse(((SlideToActView) mActivityRule.getActivity().findViewById(R.id.slide_1)).isCompleted());
    }
}