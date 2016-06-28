package com.quemb.mmitodoapp.controller;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
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
    private ToDo mTodo;


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

        FormManager formManager = new FormManager();
        formManager.setup(descriptor, listView, getActivity());
        formManager.setOnFormRowValueChangedListener(this);

        Log.d(TAG, listView.toString());

    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public void onValueChanged(RowDescriptor rowDescriptor, Value<?> oldValue, Value<?> newValue) {

        try {
            Field field = mTodo.getClass().getField(rowDescriptor.getTag());
            field.set(mTodo, newValue.getValue());
            mTodo.save();
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }

    }


}
