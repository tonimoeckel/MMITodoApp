package com.quemb.mmitodoapp.controller;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.quemb.mmitodoapp.R;

/**
 * Created by tonimockel on 02.07.16.
 */

public class ToDoLocationPickerFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.content_todo_location_picker, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ToDoMapFragement toDoMapFragement = (ToDoMapFragement) getChildFragmentManager().findFragmentById(R.id.fragment);

    }
}
