package com.quemb.mmitodoapp.model;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by tonimockel on 03.07.16.
 */

public class JsonStringAdapterFactory implements TypeAdapterFactory {
    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {

        TypeAdapter typeAdapter = new TypeAdapter() {
            @Override
            public void write(JsonWriter out, Object value) throws IOException {
                out.jsonValue((String) value);
            }

            @Override
            public String read(JsonReader in) throws IOException {
                Gson gson = new Gson();
                JsonObject json = gson.toJsonTree(in).getAsJsonObject().getAsJsonObject("in").getAsJsonObject("str");
                JsonPrimitive contactJson = json.getAsJsonPrimitive("contacts");
                String result = contactJson.getAsString();
                return result;
            }
        };
        return typeAdapter;
        //Log.d("hier",type.toString());
        //return null;
    }
}
