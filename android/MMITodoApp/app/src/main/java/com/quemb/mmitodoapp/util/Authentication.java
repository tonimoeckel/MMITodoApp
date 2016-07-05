package com.quemb.mmitodoapp.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.quemb.mmitodoapp.application.ApplicationController;
import com.quemb.mmitodoapp.model.ToDo;

/**
 * Created by tonimockel on 22.06.16.
 */

public class Authentication {

    private static final String SP_KEY_IS_AUTHENTICATED = "SP_KEY_IS_AUTHENTICATED";

    public static boolean isAuthenticated(){
        return isAuthenticated(null);
    }

    public static boolean isAuthenticated(Context context){

        if (context == null){
            context = ApplicationController.getSharedInstance().getApplicationContext();
        }
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPref.getBoolean(SP_KEY_IS_AUTHENTICATED, false);
        
    }

    public static void setAuthenticated(){


        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(ApplicationController.getSharedInstance().getApplicationContext());
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(SP_KEY_IS_AUTHENTICATED, true);

        editor.apply();

        ApplicationController.getSharedInstance().triggerSync();


    }


    public static void logout() {

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(ApplicationController.getSharedInstance().getApplicationContext());
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(SP_KEY_IS_AUTHENTICATED, false);

        editor.apply();

        ToDo.deleteAll(ToDo.class);


    }
}
