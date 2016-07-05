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
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.quemb.mmitodoapp.R;
import com.quemb.mmitodoapp.config.Constants;
import com.quemb.mmitodoapp.model.ToDo;
import com.quemb.mmitodoapp.service.FetchAddressIntentService;
import com.quemb.mmitodoapp.util.EditTextDialogBuilder;
import com.quemb.mmitodoapp.util.ToDoIntentUtils;

import static com.quemb.mmitodoapp.config.Constants.SUCCESS_RESULT;

/**
 * Created by tonimoeckel on 01.07.16.
 */
public class ToDoListMapFragment extends MapFragment implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener{

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
    public void onMapReady(GoogleMap googleMap) {
        super.onMapReady(googleMap);

        googleMap.setOnMapLongClickListener(this);

    }


    @Override
    public void onMapLongClick(LatLng latLng) {

    }

}
