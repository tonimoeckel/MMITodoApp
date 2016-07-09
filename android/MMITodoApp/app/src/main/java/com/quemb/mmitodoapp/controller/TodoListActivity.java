package com.quemb.mmitodoapp.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.quemb.mmitodoapp.R;
import com.quemb.mmitodoapp.adapter.ToDoListFragmentsPagingAdapter;
import com.quemb.mmitodoapp.model.ToDo;
import com.quemb.mmitodoapp.util.Authentication;

import java.util.ArrayList;
import java.util.List;

public class TodoListActivity extends TabHostActivity implements TodoEventListener {

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

        boolean isAuth = Authentication.isAuthenticated(this);
        menu.findItem(R.id.action_logout).setVisible(isAuth);
        menu.findItem(R.id.action_login).setVisible(!isAuth);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_logout) {
            Authentication.logout();
            Intent formIntend = new Intent(TodoListActivity.this, LoginActivity.class);
            startActivity(formIntend);
            finish();
            return true;
        }else if (id == R.id.action_login) {
            Intent formIntend = new Intent(TodoListActivity.this, LoginActivity.class);
            startActivity(formIntend);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTodoCreated(ToDo toDo) {

        List<TodoEventListener> list = findTodoEventListener();
        for (TodoEventListener listener : list){
            listener.onTodoCreated(toDo);
        }

    }

    @Override
    public void onTodoUpdated(ToDo toDo) {

        List<TodoEventListener> list = findTodoEventListener();
        for (TodoEventListener listener : list){
            listener.onTodoUpdated(toDo);
        }

    }

    @Override
    public void onTodoRemoved(ToDo toDo) {
        List<TodoEventListener> list = findTodoEventListener();
        for (TodoEventListener listener : list){
            listener.onTodoRemoved(toDo);
        }
    }

    public List<TodoEventListener> findTodoEventListener(){

        ArrayList<TodoEventListener> list = new ArrayList();
        for (int i = 0; i<mTodoPagingAdapter.getCount(); i++){
            Fragment fragment = mTodoPagingAdapter.getItem(i);
            if (fragment instanceof TodoEventListener){
                list.add((TodoEventListener) fragment);
            }
        }
        return list;

    }

    public ToDoListFragmentsPagingAdapter getTodoPagingAdapter() {
        return mTodoPagingAdapter;
    }
}
