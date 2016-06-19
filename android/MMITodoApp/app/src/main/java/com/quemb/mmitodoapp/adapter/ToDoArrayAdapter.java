package com.quemb.mmitodoapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.quemb.mmitodoapp.R;
import com.quemb.mmitodoapp.model.ToDo;

import java.util.ArrayList;

/**
 * Created by tonimockel on 18.06.16.
 */

public class ToDoArrayAdapter extends ArrayAdapter<ToDo> {

    public ToDoArrayAdapter(Context context, ArrayList<ToDo> toDos) {
        super(context, android.R.layout.simple_expandable_list_item_2, toDos);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ToDo item = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_list_item_2, parent, false);
        }

        TextView titleTextView = (TextView) convertView.findViewById(android.R.id.text1);
        TextView subtitleTextView = (TextView) convertView.findViewById(android.R.id.text2);

        titleTextView.setText(item.title);
        subtitleTextView.setText(item.text);

        return convertView;
    }

}
