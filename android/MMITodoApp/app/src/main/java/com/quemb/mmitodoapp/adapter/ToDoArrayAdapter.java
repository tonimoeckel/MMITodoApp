package com.quemb.mmitodoapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
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

        final ToDo item = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.todo_list_item, parent, false);
        }

        TextView titleTextView = (TextView) convertView.findViewById(R.id.toDoTitle);
        TextView subtitleTextView = (TextView) convertView.findViewById(R.id.toDoText);
        CheckBox doneCheckBox = (CheckBox) convertView.findViewById(R.id.doneCheckBox);

        titleTextView.setText(item.title);
        subtitleTextView.setText(item.text);
        doneCheckBox.setChecked(item.done);

        doneCheckBox.setOnClickListener( new View.OnClickListener() {
            public void onClick(View v) {
                item.setDone(!item.done);
                item.save();
            }
        });

        return convertView;
    }

}
