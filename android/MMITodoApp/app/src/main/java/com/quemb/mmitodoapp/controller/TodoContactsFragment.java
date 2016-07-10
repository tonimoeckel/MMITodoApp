package com.quemb.mmitodoapp.controller;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.quemb.mmitodoapp.R;
import com.quemb.mmitodoapp.adapter.ContactsUriArrayAdapter;
import com.quemb.mmitodoapp.adapter.ToFragmentsPagingAdapter;
import com.quemb.mmitodoapp.model.ToDo;
import com.quemb.mmitodoapp.util.ContactFetcher;

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class TodoContactsFragment extends ListFragment {

    private static final String TAG = "TodoContactsFragment";
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;
    private static final int PICK_CONTACT_REQUEST = 555;

    protected static final int ACTION_SEND_EMAIL = 111;
    protected static final int ACTION_SEND_SMS = 222;

    private ToDo mTodo;

    private boolean mEditMode;

    private Menu mMenu;

    public TodoContactsFragment() {
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
        inflater.inflate(R.menu.menu_todo_contacts, menu);

        updateActionBarItems();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.action_remove_selected:
                removeSelectedContacts();
                break;
            case R.id.action_select:
                mEditMode = true;
                getListView().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
                updateActionBarItems();
                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);

    }

    private void removeSelectedContacts() {

        ArrayList<String> contacts = mTodo.getContacts();
        ArrayList<String> removeList = new ArrayList<>();

        SparseBooleanArray selection = getListView().getCheckedItemPositions();
        for (int i=0; i<selection.size();i++){
            int key = selection.keyAt(i);
            if (selection.get(key)){
                removeList.add(contacts.get(key));
            }
        }

        contacts.removeAll(removeList);
        getListView().clearChoices();
        getListView().requestLayout();
        mTodo.setContacts(contacts);
        mTodo.save(true);

        getListView().setChoiceMode(ListView.CHOICE_MODE_NONE);
        mEditMode = false;

        updateActionBarItems();

        reloadData();
    }

    private void reloadData() {

        ContactsUriArrayAdapter arrayAdapter = (ContactsUriArrayAdapter) getListAdapter();
        arrayAdapter.setArray(mTodo.getContacts());

    }

    private void updateActionBarItems() {

        mMenu.findItem(R.id.action_select).setVisible(!mEditMode);
        mMenu.findItem(R.id.action_remove_selected).setVisible(mEditMode);

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

        getListView().setChoiceMode(ListView.CHOICE_MODE_NONE);
        registerForContextMenu(getListView());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && getActivity().checkSelfPermission(
                Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
        } else {
            showContacts();
        }

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
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
            int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted
                showContacts();
            } else {
                Toast.makeText(getActivity(), "Until you grant the permission, we canot display the names", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void showContacts() {

        setListAdapter(new ContactsUriArrayAdapter(getContext(), mTodo.getContacts()));

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == PICK_CONTACT_REQUEST) {
            // Make sure the request was successful
            if (resultCode == Activity.RESULT_OK) {

                Uri uri = data.getData();
                Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
                if (cursor.moveToFirst()) {
                    int contactId = cursor.getColumnIndex(ContactsContract.Contacts._ID);
                    String contactIdString = cursor.getString(contactId);
                    ArrayList<String> contacts = mTodo.getContacts();
                    if (!contacts.contains(contactIdString)) {
                        contacts.add(contactIdString);
                        mTodo.setContacts(contacts);
                        mTodo.save(true);

                        ContactsUriArrayAdapter arrayAdapter = (ContactsUriArrayAdapter) getListAdapter();
                        arrayAdapter.add(contactIdString);
                        arrayAdapter.notifyDataSetChanged();
                    }
                }
                cursor.close();

            }
        }
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        if (!mEditMode){

            String contactId = (String) getListAdapter().getItem(position);
//            ContactsContract.QuickContact.showQuickContact(getContext(), v, Uri.parse(uriString),null, null);

            openActionDialog(contactId);

        }

    }

    private void openActionDialog(final String contactId){

        ContactFetcher contactFetcher = new ContactFetcher(getContext(), contactId);

        ArrayList<String> titles = new ArrayList<>();
        final ArrayList<Integer> actions = new ArrayList<>();

        String email = contactFetcher.getEmail();
        if (email != null){
            titles.add(getString(R.string.title_send_email));
            actions.add(ACTION_SEND_EMAIL);
        }

        String phone = contactFetcher.getPhone();
        if (phone != null){
            titles.add(getString(R.string.title_send_sms));
            actions.add(ACTION_SEND_SMS);
        }

        CharSequence[] cs = titles.toArray(new CharSequence[titles.size()]);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(contactFetcher.getName())
                .setItems(cs, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        switch (actions.get(which)){
                            case ACTION_SEND_EMAIL:
                                sendMailAction(contactId);
                                break;
                            case ACTION_SEND_SMS:
                                callPhoneAction(contactId);
                                break;
                            default:
                                break;
                        }
                    }
                })
                .setNegativeButton(R.string.button_cancel, null)
                .setCancelable(true);
        builder.create().show();

    }

    private void callPhoneAction(String contactId) {

        ContactFetcher contactFetcher = new ContactFetcher(getContext(), contactId);

        String msg = "";
        if (mTodo.title != null){
            msg = mTodo.title;
        }

        if (mTodo.text != null){
            if (msg.length() > 0){
                msg = msg + "\n";
            }
            msg = msg + mTodo.text;
        }

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", contactFetcher.getPhone(), null));
        intent.putExtra("sms_body", msg);
        startActivity(intent);

    }

    private void sendMailAction(String contactId) {

        ContactFetcher contactFetcher = new ContactFetcher(getContext(), contactId);

        String email = contactFetcher.getEmail();

        Log.d(TAG, "send mail to "+email);

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("message/rfc822");
        intent.putExtra(Intent.EXTRA_SUBJECT, mTodo.title);
        intent.putExtra(Intent.EXTRA_TEXT, mTodo.text);
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{email});

        try {
            startActivity(Intent.createChooser(intent, getString(R.string.text_send_email)));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getActivity(), getString(R.string.error_no_email_clients), Toast.LENGTH_SHORT).show();
        }



    }

}