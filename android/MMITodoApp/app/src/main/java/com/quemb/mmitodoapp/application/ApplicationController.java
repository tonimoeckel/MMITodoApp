package com.quemb.mmitodoapp.application;

import com.orm.SugarApp;
import com.quemb.mmitodoapp.model.ConnectionSetting;
import com.quemb.mmitodoapp.model.ConnectionSettingFactory;
import com.quemb.mmitodoapp.network.ToDoService;

import android.app.Application;

import java.net.MalformedURLException;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by tonimoeckel on 21.06.16.
 */
public class ApplicationController extends SugarApp {

    private final String DEFAULT_BASE_URL = "http://localhost:8080";

    private static ApplicationController sharedInstance;

    private ToDoService mToDoService;


    @Override
    public void onCreate() {
        super.onCreate();
        sharedInstance = this;

        ConnectionSettingFactory.getSharedPreferencesSetting(getApplicationContext());
    }

    public static synchronized ApplicationController getSharedInstance() {
        return sharedInstance;
    }

    public Retrofit createRetrofit(String baseUrl) {

        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public ToDoService createToDoService(String baseUrl){

        if (baseUrl == null){
            baseUrl = DEFAULT_BASE_URL;
        }
        Retrofit retrofit = createRetrofit(baseUrl);
        mToDoService = retrofit.create(ToDoService.class);
        return mToDoService;

    }


    public ToDoService getToDoService() {

        if (mToDoService == null){
            ConnectionSetting connectionSetting = ConnectionSettingFactory.getSharedPreferencesSetting(getApplicationContext());
            try {
                mToDoService = createToDoService(connectionSetting.getURL().toString());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
        return mToDoService;
    }
}
