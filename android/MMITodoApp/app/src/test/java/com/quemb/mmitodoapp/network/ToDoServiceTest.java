package com.quemb.mmitodoapp.network;


import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.quemb.mmitodoapp.application.ApplicationController;
import com.quemb.mmitodoapp.model.ToDo;

import org.junit.Test;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNull.notNullValue;

/**
 * Created by tonimockel on 03.07.16.
 */


public class ToDoServiceTest {


    @Test
    public void should_parse_todo_list(){

        String json = getToDosJsonTestString();
        Type listOfTestObject = new TypeToken<List<ToDo>>(){}.getType();

        Gson gson = ApplicationController.getGsonObject();
        List<ToDo> resultList = gson.fromJson(json, listOfTestObject);

        /**
         * Check List
         */

        assertThat(resultList, notNullValue());
        assertThat(resultList.size(), is(2));

        /**
         * Check Fist
         */

        ToDo toDo = resultList.get(0);
        assertThat(toDo.getId(), is(0L));
        assertThat(toDo.title, equalTo("Todo 1467494139111"));
        assertThat(toDo.text, equalTo("lorem ipsum dolor"));

        toDo = resultList.get(1);
        assertThat(toDo.getId(), is(1L));
        assertThat(toDo.title, equalTo("Todo 1467494139111"));
        assertThat(toDo.text, equalTo("sit amet consectetur"));

    }

    @Test
    public void should_parse_todo(){

        Gson gson = ApplicationController.getGsonObject();
        ToDo toDo = gson.fromJson(getToDoSampleString1(), ToDo.class);

        assertThat(toDo.getId(), is(1L));
        assertThat(toDo.title, equalTo("Todo 1467494139111"));
        assertThat(toDo.text, equalTo("sit amet consectetur"));
        assertThat(toDo.done, is(true));
        assertThat(toDo.favorite, is(true));

        Date date = new Date ();
        date.setTime((long)1467578801*1000);
        assertThat(toDo.getDate(), equalTo(date));
        //assertThat(toDo.getContacts().size(), is(3));

    }

    @Test
    public void should_serialize_to(){

        ToDo toDo = new ToDo();
        toDo.setId(5L);
        toDo.setTitle("Test Title");
        toDo.setText("Test Text");
        toDo.userAddress = "Test Address";
        toDo.setLatLng(new LatLng(51.050409, 13.737262));

        Gson gson = ApplicationController.getGsonObject();
        String jsonString = gson.toJson(toDo);

        JsonObject jsonObject = gson.fromJson(jsonString, JsonObject.class);
        assertThat(jsonObject.get("name").getAsString(), equalTo("Test Title"));
        assertThat(jsonObject.get("description").getAsString(), equalTo("Test Text"));

        JsonObject locationElement = jsonObject.get("location").getAsJsonObject();
        assertThat(locationElement.get("name").getAsString(), equalTo("Test Address"));
        JsonObject latlngElement = locationElement.get("latlng").getAsJsonObject();
        assertThat(latlngElement.get("lat").getAsDouble(), equalTo(51.050409));
        assertThat(latlngElement.get("lng").getAsDouble(), equalTo(13.737262));


    }

    private String getToDoSampleString1(){
        return "{\n" +
                "  \"id\": 1,\n" +
                "  \"name\": \"Todo 1467494139111\",\n" +
                "  \"description\": \"sit amet consectetur\",\n" +
                "  \"expiry\": 1467578801000,\n" +
                "  \"done\": true,\n" +
                "  \"favourite\": true,\n" +
                "  \"contacts\": [\"23849023\",\"2380948293048\",\"32849238402\"],\n" +
                "  \"location\": {\n" +
                "    \"name\": \"string\",\n" +
                "    \"latlng\": {\n" +
                "      \"lat\": 0,\n" +
                "      \"lng\": 0\n" +
                "    }\n" +
                "  }\n" +
                "}";
    }


    private String getToDosJsonTestString() {

        String testString = "[{\"id\":0,\"name\":\"Todo 1467494139111\",\"description\":\"lorem ipsum dolor\",\"expiry\":0,\"done\":false,\"favourite\":false,\"contacts\":null,\"location\":null},{\"id\":1,\"name\":\"Todo 1467494139111\",\"description\":\"sit amet consectetur\",\"expiry\":0,\"done\":false,\"favourite\":false,\"contacts\":null,\"location\":null}]";
        return testString;
    }

}
