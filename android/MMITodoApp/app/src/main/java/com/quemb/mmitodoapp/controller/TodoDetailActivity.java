package com.quemb.mmitodoapp.controller;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.quemb.mmitodoapp.R;
import com.quemb.mmitodoapp.adapter.ToFragmentsPagingAdapter;
import com.quemb.mmitodoapp.model.ToDo;

/**
 * Created by tonimockel on 04.06.16.
 */

public class TodoDetailActivity extends TabHostActivity {

    public static final String INTENT_EXTRA_TODO_ID = "INTENT_EXTRA_TODO_ID";
    private static final String TAG = "TodoDetailActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activixty_todo_form);

        long extraId = getIntent().getLongExtra(INTENT_EXTRA_TODO_ID, -1);

        if (extraId < 0){
            Uri data = getIntent().getData();
            String path = data.getPath();
            if (path != null && path.length()>0) {
                path = path.replace("/","");
                extraId = Long.parseLong(path);
            }
        }


        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new ToFragmentsPagingAdapter(getSupportFragmentManager(), this, extraId));

        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }
}
