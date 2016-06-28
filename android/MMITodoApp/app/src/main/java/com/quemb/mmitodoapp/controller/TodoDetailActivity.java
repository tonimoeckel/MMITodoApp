package com.quemb.mmitodoapp.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.quemb.mmitodoapp.R;
import com.quemb.mmitodoapp.adapter.ToFragmentsPagingAdapter;
import com.quemb.mmitodoapp.model.ToDo;

/**
 * Created by tonimockel on 04.06.16.
 */

public class TodoDetailActivity extends TabHostActivity {

    public static final String INTENT_EXTRA_TODO_ID = "INTENT_EXTRA_TODO_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_todo_form);

        long extraId = getIntent().getLongExtra(INTENT_EXTRA_TODO_ID, -1);


        ViewPager vpPager = (ViewPager) findViewById(R.id.vpPager);
        ToFragmentsPagingAdapter adapterViewPager = new ToFragmentsPagingAdapter(getSupportFragmentManager(), this, extraId);
        vpPager.setAdapter(adapterViewPager);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }
}
