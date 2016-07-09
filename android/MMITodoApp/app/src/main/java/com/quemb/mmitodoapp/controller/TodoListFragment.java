package com.quemb.mmitodoapp.controller;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.common.collect.Lists;
import com.quemb.mmitodoapp.R;
import com.quemb.mmitodoapp.adapter.ToDoArrayAdapter;
import com.quemb.mmitodoapp.model.ToDo;
import com.quemb.mmitodoapp.util.ToDoIntentUtils;

import java.util.Iterator;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class TodoListFragment extends ListFragment {

    private Boolean mSortByFavorite = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_todo_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        AdapterView.OnItemLongClickListener listener = new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> l, View v, int position, long id) {
                ToDo toDo = (ToDo) getListAdapter().getItem(position);
                toDo.setFavorite(!toDo.favorite);
                toDo.save(true);
                return false;
            }
        };

        getListView().setOnItemLongClickListener(listener);

        fetchData();

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent formIntend = new Intent(getActivity(), TodoDetailActivity.class);
                startActivity(formIntend);

            }
        });
    }

    private void fetchData() {
        List<ToDo> items;

        if (mSortByFavorite) {
            items = ToDo.find(ToDo.class, null, null, null, "done DESC, favorite DESC", null);
        } else {
            items = ToDo.find(ToDo.class, null, null, null, "done DESC, date DESC, time DESC", null);
        }

        if (getListAdapter() == null ){
            ArrayAdapter<ToDo> adapter = new ToDoArrayAdapter(getActivity(), Lists.newArrayList(items) );
            setListAdapter(adapter);
        }else {
            ToDoArrayAdapter arrayAdapter = (ToDoArrayAdapter) getListAdapter();
            arrayAdapter.clear();
            arrayAdapter.addAll( Lists.newArrayList(items) );
            arrayAdapter.notifyDataSetChanged();
        }

    }

    @Override
    public void onResume() {
        super.onResume();

        fetchData();
    }

    @Override
    public void onListItemClick (ListView l, View v, int position, long id) {

        ToDo toDo = (ToDo) getListAdapter().getItem(position);
        Intent formIntend = new Intent(getActivity(), TodoDetailActivity.class);
        formIntend.putExtra(ToDoIntentUtils.INTENT_EXTRA_TODO_ID, toDo.getId());
        startActivity(formIntend);

    }

    @Override
    public void onCreateOptionsMenu (Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_todo_list, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_order_by_favourite) {
            mSortByFavorite = true;
            fetchData();

            return true;
        }

        if (id == R.id.action_order_by_due_date) {
            mSortByFavorite = false;
            fetchData();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
