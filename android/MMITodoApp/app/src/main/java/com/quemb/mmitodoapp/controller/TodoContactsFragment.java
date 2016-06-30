package com.quemb.mmitodoapp.controller;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEventSource;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.quemb.mmitodoapp.R;
import com.quemb.mmitodoapp.adapter.ContactsUriArrayAdapter;
import com.quemb.mmitodoapp.adapter.ToFragmentsPagingAdapter;
import com.quemb.mmitodoapp.model.ToDo;
import com.quemb.qmbform.FormManager;
import com.quemb.qmbform.annotation.FormDescriptorAnnotationFactory;
import com.quemb.qmbform.descriptor.FormDescriptor;
import com.quemb.qmbform.descriptor.OnFormRowValueChangedListener;
import com.quemb.qmbform.descriptor.RowDescriptor;
import com.quemb.qmbform.descriptor.Value;

import java.lang.reflect.Field;
import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class TodoContactsFragment extends ListFragment {

    private static final String TAG = "TodoContactsFragment";
    private static final int PICK_CONTACT_REQUEST = 555;
    private ToDo mTodo;


    public TodoContactsFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_todo_contacts, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        long extraId = getArguments().getLong(ToFragmentsPagingAdapter.INTENT_EXTRA_TODO_ID, -1);
        if (extraId >= 0){
            mTodo = ToDo.findById(ToDo.class, extraId);
        }else {
            Log.e(TAG, "No ToDo found");
        }

        setListAdapter(new ContactsUriArrayAdapter(getContext(), mTodo.contacts));

        FloatingActionButton button = (FloatingActionButton) view.findViewById(R.id.fab);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickContact();
            }
        });

    }

    private void pickContact(){

        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(intent, PICK_CONTACT_REQUEST);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == PICK_CONTACT_REQUEST) {
            // Make sure the request was successful
            if (resultCode == Activity.RESULT_OK) {

                Uri uri = data.getData();

                Log.d("hier", uri.toString());

                if (!mTodo.contacts.contains(uri.toString())) {
                    mTodo.contacts.add(uri.toString());

                    ArrayAdapter arrayAdapter = (ArrayAdapter) getListAdapter();
                    arrayAdapter.notifyDataSetChanged();
                }

            }
        }
    }
}