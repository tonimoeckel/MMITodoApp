package com.quemb.mmitodoapp.controller;

import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.ViewAction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.quemb.mmitodoapp.R;
import com.quemb.mmitodoapp.util.ToDoIntentUtils;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.assertThat;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.is;

/**
 * Created by tonimockel on 09.07.16.
 */

@RunWith(AndroidJUnit4.class)
public class TodoDetailActivityTest {

    @Rule
    public final ActivityTestRule<TodoDetailActivity> mActivityRule = new ActivityTestRule<>(TodoDetailActivity.class, true, true);


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


    @Test
    public void should_create_actionbar() throws Exception{

        onView(withId(R.id.toolbar))
                .check(matches(isDisplayed()));

    }

    @Test
    public void should_get_todo_id_from_intent_extras() throws Exception{

        mActivityRule.launchActivity(createIntent(1L));
        TodoDetailActivity activity = mActivityRule.getActivity();
        long toDoId = activity.fetchToDoId();
        assertThat(toDoId,is(1L));

    }

    @Test
    public void should_get_no_todo_id_from_intent_extras() throws Exception{

        TodoDetailActivity activity = mActivityRule.getActivity();
        long toDoId = activity.fetchToDoId();
        assertThat(toDoId,is(-1L));

    }

    private Intent createIntent(long l) {
        Context targetContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        Intent result = new Intent(targetContext, TodoDetailActivity.class);
        result.putExtra(ToDoIntentUtils.INTENT_EXTRA_TODO_ID, l);
        return result;
    }

}
