package com.quemb.mmitodoapp.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
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

    public TodoListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
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
                toDo.save();
                return false;
            }
        };

        getListView().setOnItemLongClickListener(listener);

        fetchData();
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

    public void setSortByFavorite(Boolean sortByFavorite) {
        this.mSortByFavorite = sortByFavorite;
    }

}
