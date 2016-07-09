package com.quemb.mmitodoapp.controller;

import android.database.Cursor;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.NoMatchingViewException;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.ViewAssertion;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.runner.AndroidJUnitRunner;
import android.support.v4.app.Fragment;
import android.test.ActivityInstrumentationTestCase2;

import android.content.Intent;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.support.test.espresso.intent.Intents;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.quemb.mmitodoapp.R;
import com.quemb.mmitodoapp.model.FragmentTabItem;
import com.quemb.mmitodoapp.model.ToDo;
import com.quemb.qmbform.FormManager;
import com.quemb.qmbform.descriptor.FormDescriptor;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.assertThat;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isRoot;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isA;
import static org.hamcrest.core.AllOf.allOf;
import static org.hamcrest.core.IsNull.notNullValue;

/**
 * Created by tonimockel on 09.07.16.
 */

@RunWith(AndroidJUnit4.class)
public class TodoFormFragmentTest {


    @Rule
    public final ActivityTestRule<TodoDetailActivity> rule = new ActivityTestRule<>(TodoDetailActivity.class, true, true);

    private TodoFormFragment formFragment;


    @Before
    public void setup(){

        TodoDetailActivity activity = rule.getActivity();
        formFragment = (TodoFormFragment) activity.getTodoPagingAdapter().getItem(0);

    }

    @Test
    public void should_create_fragment() throws Exception{

        assertThat(formFragment, notNullValue());

    }

    @Test
    public void should_create_form() throws Exception{

        onView(withId(R.id.listview))
                .check(matches(isDisplayed()));

    }




}
