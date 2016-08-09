package com.shariffproductions.flicks;

import org.hamcrest.beans.SamePropertyValuesAs;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;


@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class MainActivityTest {
    @Test
    public void shouldCreateActivity() throws Exception {
        MainActivity mainActivity = Robolectric.setupActivity(MainActivity.class);
    }
}