package com.quemb.mmitodoapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.quemb.mmitodoapp.R;
import com.quemb.mmitodoapp.model.ToDo;

/**
 * Created by tonimockel on 02.07.16.
 */

public class ToDoDetailInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

    private final View mContentView;
    private final ToDo mToDo;
    private LayoutInflater mLayoutInflater;
    private OnToDoMarkerActionListener mMarkerActionListener;

    public ToDoDetailInfoWindowAdapter(ToDo toDo, LayoutInflater layoutInflater){
        mToDo = toDo;
        mLayoutInflater = layoutInflater;
        mContentView = mLayoutInflater.inflate(R.layout.marker_content_todo_detail, null);
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(final Marker marker) {

        TextView textView = (TextView) mContentView.findViewById(R.id.text1);
        textView.setText(marker.getTitle());
        textView.setVisibility(textView.getText() != null && textView.getText().length()>0 ? View.VISIBLE : View.GONE);

        LatLng latLng = mToDo.getLatLng();
        boolean equalsToDoPosition = latLng != null && marker.getPosition() != null && latLng.equals(marker.getPosition());

        Button addButton = (Button) mContentView.findViewById(R.id.buttonAdd);
        addButton.setVisibility(equalsToDoPosition ? View.GONE : View.VISIBLE);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mMarkerActionListener != null){
                    mMarkerActionListener.onAddAction(mToDo, marker);
                }

            }
        });

        Button removeButton = (Button) mContentView.findViewById(R.id.buttonRemove);
        removeButton.setVisibility(equalsToDoPosition ? View.VISIBLE : View.GONE);
        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mMarkerActionListener != null){
                    mMarkerActionListener.onRemoveAction(mToDo, marker);
                }

            }
        });

        return mContentView;
    }

    public void setMarkerActionListener(OnToDoMarkerActionListener markerActionListener) {
        this.mMarkerActionListener = markerActionListener;
    }

    public interface OnToDoMarkerActionListener {

        public void onAddAction(ToDo toDo, Marker marker);

        public void onRemoveAction(ToDo toDo, Marker marker);

    }

}
