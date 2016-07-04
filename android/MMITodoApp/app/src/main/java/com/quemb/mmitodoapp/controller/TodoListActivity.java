package com.quemb.mmitodoapp.controller;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.quemb.mmitodoapp.R;

public class TodoListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent formIntend = new Intent(TodoListActivity.this, TodoDetailActivity.class);
                startActivity(formIntend);

            }
        });
    }

    

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_todo_list, menu);
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

        if (id == R.id.action_order_by_favourite) {
            Fragment fragment = getFragmentManager().findFragmentByTag("fragment_todo_list");

            return true;
        }

        if (id == R.id.action_order_by_due_date) {
            Fragment fragment = getFragmentManager().findFragmentByTag("fragment_todo_list");

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
