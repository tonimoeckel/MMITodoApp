package com.quemb.mmitodoapp.util;

import android.location.Location;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.quemb.mmitodoapp.application.ApplicationController;
import com.quemb.mmitodoapp.model.ToDo;

import java.lang.reflect.Type;

/**
 * Created by tonimockel on 05.07.16.
 */

public class TodoJsonSerializer implements JsonSerializer<ToDo> {
    @Override
    public JsonElement serialize(ToDo src, Type typeOfSrc, JsonSerializationContext context) {


        GsonBuilder gsonBuilder = ApplicationController.baseGsonBuilder();
        Gson gson = gsonBuilder.create();
        JsonElement jsonElement = gson.toJsonTree(src, ToDo.class);

        if (src != null && src.getLatLng() != null &&  jsonElement.isJsonObject()){

            JsonObject jsonObject = (JsonObject) jsonElement;

            JsonObject locationObject = new JsonObject();
            locationObject.addProperty("name", src.getPreferredAddress());

            JsonObject latLngObject = new JsonObject();
            latLngObject.addProperty("lat", src.lat);
            latLngObject.addProperty("lng", src.lng);
            locationObject.add("latlng",latLngObject);

            jsonObject.add("location",locationObject);

        }

        return jsonElement;
    }
}
