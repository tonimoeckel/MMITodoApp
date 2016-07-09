package com.quemb.mmitodoapp.controller;

import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.quemb.mmitodoapp.R;
import com.quemb.mmitodoapp.util.ToDoIntentUtils;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.assertThat;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.is;

/**
 * Created by tonimockel on 09.07.16.
 */

@RunWith(AndroidJUnit4.class)
public class TodoListActivityTest {

    @Rule
    public final ActivityTestRule<TodoListActivity> mActivityRule = new ActivityTestRule<>(TodoListActivity.class, true, true);


    @Before
    public void setup(){

    }

    @Test
    public void should_create_viewpager() throws Exception{

        onView(withId(R.id.viewpager))
                .check(matches(isDisplayed()));
        onView(withId(R.id.sliding_tabs))
                .check(matches(isDisplayed()));

    }



}
