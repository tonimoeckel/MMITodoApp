package com.quemb.mmitodoapp.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.quemb.mmitodoapp.application.ApplicationController;

import java.net.MalformedURLException;
import java.util.Iterator;


/**
 * Created by tonimockel on 19.06.16.
 */

public class ConnectionSettingFactory {


    public static ConnectionSetting getSetting(){

        Iterator<ConnectionSetting> list = ConnectionSetting.findAll(ConnectionSetting.class);
        if (list.hasNext()){
            return list.next();
        }
        return new ConnectionSetting();

    }

    public static void saveSetting(ConnectionSetting connectionSetting){

        connectionSetting.save();

        try {
            ApplicationController.getSharedInstance().createRetrofit(connectionSetting.getURL().toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

    }

    public static boolean isValid() {

        ConnectionSetting connectionSetting = getSetting();
        return connectionSetting.isValid();

    }
}
