package com.quemb.qmbform;

import com.quemb.qmbform.descriptor.FormDescriptor;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import android.app.Activity;
import android.widget.ListView;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;

/**
 * Created by tonimoeckel on 12.08.14.
 */
@Config(constants = BuildConfig.class)
@RunWith(RobolectricGradleTestRunner.class)
public class FormManagerTest {

    private FormManager formManager;
    private Activity activity;

    @Before
    public void setUp() {
        formManager = new FormManager();
        activity = Robolectric.buildActivity(Activity.class).create().get();
    }

    @Test
    public void shouldSetupListView(){

        ListView listView = new ListView(activity);
        FormDescriptor formDescriptor = new FormDescriptor();
        formManager.setup(formDescriptor, listView, activity);

        assertThat(listView.getAdapter(), is(notNullValue()));

    }

    @After
    public void tearDown() {

    }

}