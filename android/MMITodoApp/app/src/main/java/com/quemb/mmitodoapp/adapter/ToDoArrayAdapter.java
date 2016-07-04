package com.quemb.mmitodoapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.quemb.mmitodoapp.R;
import com.quemb.mmitodoapp.model.ToDo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

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

        LinearLayout listItemLinearLayout = (LinearLayout) convertView.findViewById(R.id.toDoListItem);
        TextView titleTextView = (TextView) convertView.findViewById(R.id.toDoTitle);
        TextView subtitleTextView = (TextView) convertView.findViewById(R.id.toDoText);
        CheckBox doneCheckBox = (CheckBox) convertView.findViewById(R.id.doneCheckBox);
        CheckBox favoriteCheckBox = (CheckBox) convertView.findViewById(R.id.favoriteCheckBox);
        TextView dueDateTextView = (TextView) convertView.findViewById(R.id.toDoDueDate);

        titleTextView.setText(item.title);
        subtitleTextView.setText(item.text);
        doneCheckBox.setChecked(item.done);
        favoriteCheckBox.setChecked(item.favorite);

        String dueDate = "";
        String dueTime = "";
        if (item.date != null) {
            SimpleDateFormat formatDate = new SimpleDateFormat("d/MM/yy");
            dueDate = formatDate.format(item.date);
        }
        if (item.time != null) {
            SimpleDateFormat formatTime = new SimpleDateFormat("h:mm a");
            dueTime = formatTime.format(item.time);
        }
        String formatedDueDateTime = String.format(getContext().getResources().getString(R.string.formated_due_date), dueDate, dueTime);
        dueDateTextView.setText(formatedDueDateTime);

        doneCheckBox.setOnClickListener( new View.OnClickListener() {
            public void onClick(View v) {
                item.setDone(!item.done);
                item.save(true);
                notifyDataSetChanged();
            }
        });

        favoriteCheckBox.setOnClickListener( new View.OnClickListener() {
            public void onClick(View v) {
                item.setFavorite(!item.favorite);
                item.save(true);
                notifyDataSetChanged();
            }
        });

        // Prüfen ob to do schon überfällig
        Calendar calendar = Calendar.getInstance();
        if (item.date != null) {
            calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(new SimpleDateFormat("d").format(item.date)));
            calendar.set(Calendar.MONTH, Integer.parseInt(new SimpleDateFormat("MM").format(item.date)) - 1);
            calendar.set(Calendar.YEAR, Integer.parseInt(new SimpleDateFormat("yyyy").format(item.date)));
        }
        if (item.time != null) {
            calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(new SimpleDateFormat("HH").format(item.time)));
            calendar.set(Calendar.MINUTE, Integer.parseInt(new SimpleDateFormat("mm").format(item.time)));
        }

        if (calendar.getTime().before(Calendar.getInstance().getTime()) && !item.done) {
            listItemLinearLayout.setBackgroundColor(0xFFFCE4EC);
        } else if (item.done) {
            listItemLinearLayout.setBackgroundColor(0xFFF1F8E9);
        } else {
            listItemLinearLayout.setBackgroundColor(0xFFFFFFFF);
        }

        return convertView;
    }

}
