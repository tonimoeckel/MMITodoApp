package com.quemb.mmitodoapp.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.quemb.mmitodoapp.R;
import com.quemb.mmitodoapp.util.ContactFetcher;

import java.util.ArrayList;

/**
 * Created by tonimockel on 01.07.16.
 */

public class ContactsUriArrayAdapter extends ArrayAdapter<String> {

    private ArrayList<String> mArray;

    public ContactsUriArrayAdapter(Context context, ArrayList<String> uris) {
        super(context, R.layout.contact_list_item, uris);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String uriString = getItem(position);

        Uri uri = Uri.parse(uriString);
        ContactFetcher contactFetcher = new ContactFetcher(getContext(), uri);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.contact_list_item, parent, false);
        }

        TextView textView = (TextView) convertView.findViewById(android.R.id.text1);
        textView.setText(contactFetcher.getName());


        return convertView;
    }

    public void setArray(ArrayList<String> array) {
        clear();
        addAll(array);
        notifyDataSetChanged();
    }
}
