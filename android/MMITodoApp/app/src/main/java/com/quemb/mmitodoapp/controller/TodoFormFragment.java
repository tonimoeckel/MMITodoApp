package com.quemb.mmitodoapp.controller;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.quemb.mmitodoapp.R;
import com.quemb.mmitodoapp.adapter.ToFragmentsPagingAdapter;
import com.quemb.mmitodoapp.model.ToDo;
import com.quemb.qmbform.FormManager;
import com.quemb.qmbform.annotation.FormDescriptorAnnotationFactory;
import com.quemb.qmbform.descriptor.FormDescriptor;
import com.quemb.qmbform.descriptor.OnFormRowValueChangedListener;
import com.quemb.qmbform.descriptor.RowDescriptor;
import com.quemb.qmbform.descriptor.Value;

import java.lang.reflect.Field;

/**
 * A placeholder fragment containing a simple view.
 */
public class TodoFormFragment extends Fragment implements OnFormRowValueChangedListener{

    private static final String TAG = "TodoFormFragment";

    private static final String SECTION_TAG_REMOVE = "SECTION_TAG_REMOVE";
    private static final String ROW_TAG_REMOVE = "ROW_TAG_REMOVE";

    private ToDo mTodo;

    private FormManager mFormManager;

    private Menu mMenu;


    public TodoFormFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_todo_form, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        mMenu = menu;
        inflater.inflate(R.menu.menu_todo_detail, menu);

        MenuItem item = mMenu.findItem(R.id.action_remove);
        if (item != null){
            item.setVisible(!(mTodo.getId() < 0));
        }


    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        long extraId = getArguments().getLong(ToFragmentsPagingAdapter.INTENT_EXTRA_TODO_ID, -1);
        if (extraId < 0){
            mTodo = new ToDo();
        }else {
            mTodo = ToDo.findById(ToDo.class, extraId);
        }

        ListView listView = (ListView) view.findViewById(R.id.listview);

        FormDescriptorAnnotationFactory factory = new FormDescriptorAnnotationFactory(getActivity());
        FormDescriptor descriptor = factory.createFormDescriptorFromAnnotatedClass(mTodo);

        mFormManager = new FormManager();
        mFormManager.setup(descriptor, listView, getActivity());
        mFormManager.setOnFormRowValueChangedListener(this);

        Log.d(TAG, listView.toString());

    }

    private void showRemoveButton() {

        MenuItem item = mMenu.findItem(R.id.action_remove);
        item.setVisible(true);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.action_remove){
            removeTodo();
        }

        return super.onOptionsItemSelected(item);
    }

    private void removeTodo() {
        mTodo.delete();
        getActivity().finish();
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public void onValueChanged(RowDescriptor rowDescriptor, Value<?> oldValue, Value<?> newValue) {

        try {

            boolean newItem = mTodo.getId() < 0;

            Field field = mTodo.getClass().getField(rowDescriptor.getTag());
            field.set(mTodo, newValue.getValue());
            mTodo.save();

            if (newItem){
                showRemoveButton();
            }

            Activity parentActivity = getActivity();
            if (parentActivity instanceof ToDoListener){

                ToDoListener toDoListener = (ToDoListener) parentActivity;

                if (newItem){
                    toDoListener.onCreate(mTodo);
                }else {
                    toDoListener.onUpdate(mTodo);
                }

            }

        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }

    }


}
