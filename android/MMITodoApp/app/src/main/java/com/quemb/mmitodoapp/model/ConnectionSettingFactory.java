package com.quemb.mmitodoapp.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.quemb.mmitodoapp.application.ApplicationController;

import java.net.MalformedURLException;


/**
 * Created by tonimockel on 19.06.16.
 */

public class ConnectionSettingFactory {

    private static final String SP_KEY_PROTOCOL = "SP_KEY_PROTOCOL";
    private static final String SP_KEY_HOST = "SP_KEY_HOST";
    private static final String SP_KEY_PORT = "SP_KEY_PORT";

    public static ConnectionSetting getSharedPreferencesSetting(Context context){


        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        ConnectionSetting connectionSetting = new ConnectionSetting();
        connectionSetting.protocol = sharedPref.getString(SP_KEY_PROTOCOL, "HTTP");
        connectionSetting.host = sharedPref.getString(SP_KEY_HOST, null);
        connectionSetting.port = sharedPref.getInt(SP_KEY_PORT, 80);

        return connectionSetting;
    }

    public static void putSharedPreferencesSetting(ConnectionSetting connectionSetting, Context context){

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(SP_KEY_PROTOCOL, connectionSetting.protocol);
        editor.putString(SP_KEY_HOST, connectionSetting.host);
        editor.putInt(SP_KEY_PORT, connectionSetting.port);
        editor.commit();

        try {
            ApplicationController.getSharedInstance().createRetrofit(connectionSetting.getURL().toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

    }

    public static boolean isValid(Context context) {

        ConnectionSetting connectionSetting = getSharedPreferencesSetting(context);
        return connectionSetting.isValid();

    }
}
