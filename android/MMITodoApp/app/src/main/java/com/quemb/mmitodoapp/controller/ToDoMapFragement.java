package com.quemb.mmitodoapp.controller;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;

import android.support.v4.os.ResultReceiver;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.quemb.mmitodoapp.R;
import com.quemb.mmitodoapp.config.Constants;
import com.quemb.mmitodoapp.service.FetchAddressIntentService;

import static com.quemb.mmitodoapp.config.Constants.FAILURE_RESULT;
import static com.quemb.mmitodoapp.config.Constants.SUCCESS_RESULT;

/**
 * Created by tonimoeckel on 01.07.16.
 */
public class ToDoMapFragement extends SupportMapFragment implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener {


    private static final int PERMISSIONS_REQUEST_LOCATION = 100;
    GoogleMap mGoogleMap;
    protected Location mLastLocation;
    private AddressResultReceiver mResultReceiver;
    private String mAddressOutput;
    private Marker mMarker;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setHasOptionsMenu(true);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mGoogleMap = googleMap;

        mResultReceiver = new AddressResultReceiver(new Handler());
        mGoogleMap.setOnMapLongClickListener(this);

        initializeLocationBasedServices();

    }

    private void initializeLocationBasedServices() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSIONS_REQUEST_LOCATION);
            return;
        }
        mGoogleMap.setMyLocationEnabled(true);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSIONS_REQUEST_LOCATION){
            initializeLocationBasedServices();
        }

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.menu_todo_map, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onMapLongClick(LatLng latLng) {

        addMarker(latLng);

    }

    private void addMarker(LatLng latLng) {

        mGoogleMap.clear();

        mLastLocation = new Location(LocationManager.GPS_PROVIDER);
        mLastLocation.setLatitude(latLng.latitude);
        mLastLocation.setLongitude(latLng.longitude);

        Intent intent = new Intent(getActivity(), FetchAddressIntentService.class);
        intent.putExtra(Constants.RECEIVER, mResultReceiver);
        intent.putExtra(Constants.LOCATION_DATA_EXTRA, mLastLocation);
        getActivity().startService(intent);

        MarkerOptions markerOptions = new MarkerOptions()
                .position(latLng);
        mMarker = mGoogleMap.addMarker(markerOptions);

    }

    private void displayAddressOutput(String address) {

        if (mMarker != null && address != null){
            mMarker.setTitle(address);
            mMarker.showInfoWindow();
        }

    }

    @SuppressLint("ParcelCreator")
    public class AddressResultReceiver extends ResultReceiver {

        public AddressResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {

            super.onReceiveResult(resultCode, resultData);

            if (resultCode == SUCCESS_RESULT){
                String addressOutput = resultData.getString(Constants.RESULT_DATA_KEY);
                displayAddressOutput(addressOutput);
            }



        }
    }



}
