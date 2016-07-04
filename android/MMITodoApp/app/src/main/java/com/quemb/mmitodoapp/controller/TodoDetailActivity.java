package com.quemb.mmitodoapp.controller;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;

import com.quemb.mmitodoapp.R;
import com.quemb.mmitodoapp.adapter.ToFragmentsPagingAdapter;
import com.quemb.mmitodoapp.model.ToDo;
import com.quemb.mmitodoapp.util.ToDoIntentUtils;

/**
 * Created by tonimockel on 04.06.16.
 */

public class TodoDetailActivity extends TabHostActivity implements ToDoListener {


    private static final String TAG = "TodoDetailActivity";

    private ToFragmentsPagingAdapter mTodoPagingAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        long extraId = fetchToDoId();

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        mTodoPagingAdapter = new ToFragmentsPagingAdapter(getSupportFragmentManager(), this, extraId);
        viewPager.setAdapter(mTodoPagingAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }

    private long fetchToDoId() {

        long extraId = getIntent().getLongExtra(ToDoIntentUtils.INTENT_EXTRA_TODO_ID, -1);

        if (extraId < 0){
            Uri data = getIntent().getData();
            if (data != null){
                String path = data.getPath();
                if (path != null && path.length()>0) {
                    path = path.replace("/","");
                    extraId = Long.parseLong(path);
                }
            }

        }

        return extraId;
    }

    @Override
    public void onCreate(ToDo toDo) {
        if (!mTodoPagingAdapter.isExtendMode()){
            mTodoPagingAdapter.addExtendedTabs(toDo.getId());
            mTodoPagingAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onUpdate(ToDo toDo) {



    }
}
