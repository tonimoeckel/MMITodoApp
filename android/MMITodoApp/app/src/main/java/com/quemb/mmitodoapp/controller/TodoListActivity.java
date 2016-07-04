package com.quemb.mmitodoapp.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.quemb.mmitodoapp.R;
import com.quemb.mmitodoapp.adapter.ToDoListFragmentsPagingAdapter;

public class TodoListActivity extends TabHostActivity {

    private static final String TAG = "TodoListActivity";

    private ToDoListFragmentsPagingAdapter mTodoPagingAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        mTodoPagingAdapter = new ToDoListFragmentsPagingAdapter(getSupportFragmentManager(), this);
        viewPager.setAdapter(mTodoPagingAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_todo_list_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_login) {
            Intent formIntend = new Intent(TodoListActivity.this, LoginActivity.class);
            startActivity(formIntend);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
