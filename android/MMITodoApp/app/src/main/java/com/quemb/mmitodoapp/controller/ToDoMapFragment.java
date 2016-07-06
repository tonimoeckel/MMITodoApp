package com.quemb.mmitodoapp.controller;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.os.ResultReceiver;
import android.support.v7.app.AlertDialog;
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
public class ToDoMapFragment extends MapFragment implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener{


    protected Location mLastLocation;
    private AddressResultReceiver mResultReceiver;
    private Marker mMarker;
    private String mGeocoderAddress;
    private ToDo mToDo;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle bundle) {
        View mapView = super.onCreateView(inflater, viewGroup, bundle);

        LinearLayout view = (LinearLayout) inflater.inflate(R.layout.content_todo_location_picker, null);

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

        mToDo = ToDoIntentUtils.getToDoFromIntent(getArguments());

        showEmptyContainer();

        mResultReceiver = new AddressResultReceiver(new Handler());
        googleMap.setOnMapLongClickListener(this);

        if (mToDo.getLatLng() != null){
            addMarker(mToDo.getLatLng(), mToDo.getPreferredAddress());
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(mToDo.getLatLng(), 10);
            googleMap.moveCamera(cameraUpdate);
            displayAddress();
        }

//        ToDoDetailInfoWindowAdapter adapter = new ToDoDetailInfoWindowAdapter(toDo, getActivity().getLayoutInflater());
//        adapter.setMarkerActionListener(this);
//        googleMap.setInfoWindowAdapter(adapter);

        getSaveButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(getActivity())
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle(getString(R.string.label_save))
                        .setMessage(getString(R.string.dialog_would_you_like_to_save_this))
                        .setPositiveButton(getString(R.string.button_yes), new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                mToDo.geocoderAddress = mGeocoderAddress;

                                if (mMarker != null){
                                    mToDo.setLatLng(mMarker.getPosition());
                                }else {
                                    mToDo.setLatLng(null);
                                }
                                mToDo.save(true);

                                if (getActivity() instanceof TodoEventListener){
                                    TodoEventListener listener = (TodoEventListener) getActivity();
                                    listener.onTodoUpdated(mToDo);
                                }

                                updateButtons();

                            }

                        })
                        .setNegativeButton(getString(R.string.button_no), null)
                        .show();

            }
        });

        getEditButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new EditTextDialogBuilder().buildDialog(getActivity(), new EditTextDialogBuilder.EditTextDialogListener(){
                    @Override
                    public void onSuccess(String text) {
                        if (text != null && text.length() > 0){
                            mToDo.userAddress = text;
                        }else {
                            mToDo.userAddress = null;
                        }
                        mToDo.save(true);
                        displayAddress();

                        if (getActivity() instanceof TodoEventListener){
                            TodoEventListener listener = (TodoEventListener) getActivity();
                            listener.onTodoUpdated(mToDo);
                        }

                    }
                }).show();
            }
        });

        getRemoveButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(getActivity())
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle(getString(R.string.label_delete))
                        .setMessage(getString(R.string.dialog_would_you_like_to_remove_this))
                        .setPositiveButton(getString(R.string.button_yes), new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                mToDo.userAddress = null;
                                mToDo.geocoderAddress = null;
                                mToDo.setLatLng(null);
                                mMarker = null;
                                mToDo.save();
                                getGoogleMap().clear();
                                showEmptyContainer();

                                if (getActivity() instanceof TodoEventListener){
                                    TodoEventListener listener = (TodoEventListener) getActivity();
                                    listener.onTodoUpdated(mToDo);
                                }

                            }

                        })
                        .setNegativeButton(getString(R.string.button_no), null)
                        .show();

            }
        });

    }


    @Override
    public void onMapLongClick(LatLng latLng) {

        addMarker(latLng, mToDo.getPreferredAddress());

    }

    private void addMarker(LatLng latLng, String address) {

        getGoogleMap().clear();

        fetchAddress(latLng);

        MarkerOptions markerOptions = new MarkerOptions()
                .position(latLng);
        mMarker = getGoogleMap().addMarker(markerOptions);
        mMarker.setDraggable(true);
    }

    private void fetchAddress(LatLng latLng) {

        mLastLocation = new Location(LocationManager.GPS_PROVIDER);
        mLastLocation.setLatitude(latLng.latitude);
        mLastLocation.setLongitude(latLng.longitude);

        showProgressBar();

        Intent intent = new Intent(getActivity(), FetchAddressIntentService.class);
        intent.putExtra(Constants.RECEIVER, mResultReceiver);
        intent.putExtra(Constants.LOCATION_DATA_EXTRA, mLastLocation);
        getActivity().startService(intent);

    }

    private void showProgressBar() {

        getProgressBar().setVisibility(View.VISIBLE);

    }

    private void showEmptyContainer() {

        getEmptyView().setVisibility(View.VISIBLE);
        getAddressContainer().setVisibility(View.INVISIBLE);
        getProgressBar().setVisibility(View.INVISIBLE);

    }

    private void showAddressContainer() {

        getEmptyView().setVisibility(View.INVISIBLE);
        getAddressContainer().setVisibility(View.VISIBLE);

    }

    private void updateButtons(){
        if (mMarker != null){

            LatLng latLng = mToDo.getLatLng();
            getSaveButton().setVisibility(mMarker.getPosition().equals(latLng) ? View.GONE : View.VISIBLE);
            getRemoveButton().setVisibility(mMarker.getPosition().equals(latLng) ? View.VISIBLE : View.GONE);
        }
    }

    private void hideProgressBar() {

        getProgressBar().setVisibility(View.INVISIBLE);

    }

    private View getAddressContainer() {
        return getView().findViewById(R.id.container_address);
    }

    private View getSaveButton() {
        return getView().findViewById(R.id.buttonAdd);
    }

    private View getRemoveButton() {
        return getView().findViewById(R.id.buttonRemove);
    }

    private View getEditButton() {
        return getView().findViewById(R.id.buttonEdit);
    }

    private TextView getAddressTextView() {
        return (TextView) getView().findViewById(R.id.editText_address);
    }

    private View getEmptyView() {
        return getView().findViewById(android.R.id.empty);
    }


    private void displayAddress() {

        showAddressContainer();
        updateButtons();

        String address = mToDo.getPreferredAddress();
        getEmptyView().setVisibility(mMarker == null ? View.VISIBLE : View.GONE);
        getAddressContainer().setVisibility(mMarker != null ? View.VISIBLE : View.GONE);

        if (address != null){
            getAddressTextView().setText(address);
            if (mMarker != null){
                mMarker.setTitle(address);
            }
        }else {
            getAddressTextView().setText(getString(R.string.text_unknown_location));
        }



    }

    public ProgressBar getProgressBar() {
        return (ProgressBar) getView().findViewById(android.R.id.progress);
    }


    @SuppressLint("ParcelCreator")
    public class AddressResultReceiver extends ResultReceiver {

        public AddressResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {

            super.onReceiveResult(resultCode, resultData);

            hideProgressBar();

            if (resultCode == SUCCESS_RESULT){
                mGeocoderAddress= resultData.getString(Constants.RESULT_DATA_KEY);
            }else {
                mGeocoderAddress = null;
            }
            mToDo.geocoderAddress = mGeocoderAddress;
            displayAddress();

        }
    }




}
