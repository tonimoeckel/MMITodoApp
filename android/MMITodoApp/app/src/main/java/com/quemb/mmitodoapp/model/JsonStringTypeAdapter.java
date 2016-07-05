package com.quemb.mmitodoapp.model;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by tonimockel on 03.07.16.
 */

public class JsonStringTypeAdapter extends TypeAdapter {


    @Override
    public void write(JsonWriter out, Object value) throws IOException {

        Type listOfTestObject = new TypeToken<ArrayList<String>>(){}.getType();
        ArrayList<String> list = new Gson().fromJson((String) value,listOfTestObject);
        out.beginArray();
        for (String item : list) {
            out.value(item);
        }
        out.endArray();

    }

    @Override
    public String read(JsonReader in) throws IOException {

        ArrayList arrayList = new ArrayList();
        in.beginArray();
        while (in.hasNext()){
            arrayList.add(in.nextString());
        }
        in.endArray();
        Gson gson = new Gson();
        String result = gson.toJson(arrayList);
        return result;

    }
}
