package com.quemb.mmitodoapp.model;


import com.google.gson.annotations.Expose;

/**
 * Created by tonimockel on 05.07.16.
 */

public class Location {
    @Expose
    public String name;
    @Expose
    public LatLng latlng;

    public Location(){
        latlng = new LatLng();
    }
}
