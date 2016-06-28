package com.quemb.mmitodoapp.controller;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.common.collect.Lists;
import com.quemb.mmitodoapp.R;
import com.quemb.mmitodoapp.adapter.ToDoArrayAdapter;
import com.quemb.mmitodoapp.adapter.ToFragmentsPagingAdapter;
import com.quemb.mmitodoapp.model.ToDo;

import java.util.Iterator;

/**
 * A placeholder fragment containing a simple view.
 */
public class TodoListFragment extends ListFragment {


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

        fetchData();

    }

    private void fetchData() {

        Iterator<ToDo> items = ToDo.findAll(ToDo.class);

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
        formIntend.putExtra(TodoDetailActivity.INTENT_EXTRA_TODO_ID, toDo.getId());
        startActivity(formIntend);

    }
}
