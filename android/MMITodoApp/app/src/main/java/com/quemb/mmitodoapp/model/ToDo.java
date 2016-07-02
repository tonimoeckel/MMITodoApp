package com.quemb.mmitodoapp.model;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.orm.SugarRecord;
import com.orm.dsl.Table;
import com.quemb.mmitodoapp.R;
import com.quemb.qmbform.annotation.FormElement;
import com.quemb.qmbform.descriptor.RowDescriptor;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by tonimockel on 03.06.16.
 */

@Table
public class ToDo extends SugarRecord {


    private Long id;

    @FormElement(required = true, hint = R.string.hint_title, sortId = 0, tag = "title", label = R.string.label_title, rowDescriptorType = RowDescriptor.FormRowDescriptorTypeText)
    public String title;

    @FormElement(required = true, hint = R.string.hint_text, sortId = 5, tag = "text", label = R.string.label_text, rowDescriptorType = RowDescriptor.FormRowDescriptorTypeTextView)
    public String text;

    @FormElement(required = true, hint = R.string.hint_done, sortId = 10, tag = "done", label = R.string.label_done, rowDescriptorType = RowDescriptor.FormRowDescriptorTypeBooleanCheck, section = R.string.section_due)
    public Boolean done = false;

    @FormElement(required = true, hint = R.string.hint_due_date, sortId = 15, tag = "date", label = R.string.label_due_date, rowDescriptorType = RowDescriptor.FormRowDescriptorTypeDatePicker, section = R.string.section_due)
    public Date date = new Date();

    @FormElement(required = true, hint = R.string.hint_due_time, sortId = 15, tag = "time", label = R.string.label_due_time, rowDescriptorType = RowDescriptor.FormRowDescriptorTypeTime, section = R.string.section_due)
    public Date time;

    @FormElement(required = true, hint = R.string.hint_done, sortId = 100, tag = "favorite", label = R.string.label_favorite, rowDescriptorType = RowDescriptor.FormRowDescriptorTypeBooleanCheck, section = R.string.section_more)
    public Boolean favorite = false;

    public String userAddress;

    public String geocoderAddress;

    public double lat;

    public double lng;

    public String contactsJson;

    public ToDo(){

    }

    /**
     * GETTER and SETTER
     */

    public Long getId() {
        Long result = super.getId();
        if (result == null){
            return (long) -1;
        }
        return result;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Boolean getDone() {
        return done;
    }

    public void setDone(Boolean done) {
        this.done = done;
    }

    public Boolean getFavorite() {
        return favorite;
    }

    public void setFavorite(Boolean favorite) {
        this.favorite = favorite;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public ArrayList<String> getContacts(){

        Gson gson = new Gson();
        if (contactsJson == null){
            return new ArrayList<>();
        }
        Type listType = new TypeToken<ArrayList<String>>(){}.getType();
        return gson.fromJson(contactsJson, listType);

    }

    public void setContacts(ArrayList<String> contacts){

        Gson gson = new Gson();
        contactsJson = gson.toJson(contacts);

    }

    public LatLng getLatLng(){
        if (lat == 0 || lng == 0){
            return null;
        }
        return new LatLng(lat, lng);
    }

    public void setLatLng(LatLng latLng){
        if (latLng == null){
            lat = 0;
            lng = 0;
        }else {
            lat = latLng.latitude;
            lng = latLng.longitude;
        }

    }


    public String getPreferredAddress() {

        if (userAddress != null){
            return userAddress;
        }
        return geocoderAddress;
    }
}
