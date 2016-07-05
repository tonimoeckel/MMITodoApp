package com.quemb.mmitodoapp.controller;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.os.ResultReceiver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.common.collect.Lists;
import com.quemb.mmitodoapp.R;
import com.quemb.mmitodoapp.adapter.ToDoArrayAdapter;
import com.quemb.mmitodoapp.config.Constants;
import com.quemb.mmitodoapp.model.ToDo;
import com.quemb.mmitodoapp.service.FetchAddressIntentService;
import com.quemb.mmitodoapp.util.EditTextDialogBuilder;
import com.quemb.mmitodoapp.util.ToDoIntentUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static com.quemb.mmitodoapp.config.Constants.SUCCESS_RESULT;

/**
 * Created by tonimoeckel on 01.07.16.
 */
public class ToDoListMapFragment extends MapFragment implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener{

    private List<ToDo> mToDos;
    private Map<Marker, ToDo> mMarkers;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle bundle) {
        View mapView = super.onCreateView(inflater, viewGroup, bundle);

        LinearLayout view = (LinearLayout) inflater.inflate(R.layout.fragment_todo_list_map, null);

        LinearLayout mapContainer = (LinearLayout) view.findViewById(R.id.containerMap);
        mapContainer.addView(mapView);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void fetchData() {
        mToDos = ToDo.find(ToDo.class, "lat NOT NULL AND lng NOT NULL", null, null, "done DESC, favorite DESC", null);

        addMarker();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        super.onMapReady(googleMap);

        googleMap.setOnMapLongClickListener(this);

        fetchData();

        googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {

                ToDo toDo = (ToDo) mMarkers.get(marker);
                Intent formIntend = new Intent(getActivity(), TodoDetailActivity.class);
                formIntend.putExtra(ToDoIntentUtils.INTENT_EXTRA_TODO_ID, toDo.getId());
                startActivity(formIntend);

            }
        });
    }


    @Override
    public void onMapLongClick(LatLng latLng) {

    }

    private void addMarker() {
        mMarkers = new HashMap<Marker, ToDo>();
        getGoogleMap().clear();

        for (ToDo toDo : mToDos) {
            Marker marker;
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(toDo.getLatLng());
            markerOptions.title(toDo.title);
            markerOptions.snippet(toDo.text);
            marker = getGoogleMap().addMarker(markerOptions);
            marker.setDraggable(true);
            mMarkers.put(marker, toDo);
        }

        centerMap();
    }

    private void centerMap() {
        LatLngBounds.Builder builder = new LatLngBounds.Builder();

        for (Map.Entry<Marker, ToDo> entry : mMarkers.entrySet()) {
            Marker key = entry.getKey();
            ToDo value = entry.getValue();

            builder.include(key.getPosition());
        }

        LatLngBounds bounds = builder.build();

        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 200);
        getGoogleMap().animateCamera(cu);
    }
}
