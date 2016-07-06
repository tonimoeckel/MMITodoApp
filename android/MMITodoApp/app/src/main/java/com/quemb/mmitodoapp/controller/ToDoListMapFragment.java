package com.quemb.mmitodoapp.controller;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.quemb.mmitodoapp.R;
import com.quemb.mmitodoapp.model.ToDo;
import com.quemb.mmitodoapp.util.ToDoIntentUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by tonimoeckel on 01.07.16.
 */
public class ToDoListMapFragment extends MapFragment implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener, TodoEventListener{

    private List<ToDo> mToDos;
    private Map<Marker, ToDo> mMarkers;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        mMarkers = new HashMap<Marker, ToDo>();
    }

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

    @Override
    public void onResume() {
        super.onResume();

        if (getGoogleMap() != null){
            fetchData();
        }

    }

    private void fetchData() {
        mToDos = ToDo.find(ToDo.class, "lat NOT NULL AND lng NOT NULL", null, null, "done DESC, favorite DESC", null);

        Log.d("hier", String.valueOf(mToDos.size()));
        addTodoMarkers();
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

    private void addTodoMarkers() {

        getGoogleMap().clear();

        for (ToDo toDo : mToDos) {
           addMarker(toDo);
        }

        centerMap();
    }

    private void addMarker(ToDo toDo) {

        LatLng latLng = toDo.getLatLng();
        if (latLng != null){
            Marker marker;
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(toDo.getLatLng());
            markerOptions.title(toDo.title);
            markerOptions.snippet(toDo.text);
            marker = getGoogleMap().addMarker(markerOptions);
            marker.setDraggable(true);
            mMarkers.put(marker, toDo);
        }

    }

    private void centerMap() {

        if (mMarkers.keySet().size()>0){
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

    @Override
    public void onTodoCreated(ToDo toDo) {
        addMarker(toDo);
    }

    @Override
    public void onTodoUpdated(ToDo toDo) {
        Marker marker = findMarker(toDo);
        if (marker != null){
            marker.setTitle(toDo.getTitle());
        }else {
            addMarker(toDo);
        }
    }

    @Override
    public void onTodoRemoved(ToDo toDo) {
        Marker marker = findMarker(toDo);
        if (marker != null){
            mMarkers.remove(marker);
            marker.remove();
        }
    }

    private Marker findMarker(ToDo toDo) {

        Marker marker = null;
        for (Map.Entry<Marker, ToDo> entry : mMarkers.entrySet()) {
            if (toDo.equals(entry.getValue())){
                marker = entry.getKey();
            }
        }
        return marker;
    }
}
