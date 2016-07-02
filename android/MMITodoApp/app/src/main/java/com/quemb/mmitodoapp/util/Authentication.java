package com.quemb.mmitodoapp.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by tonimockel on 22.06.16.
 */

public class Authentication {

    private static final String SP_KEY_IS_AUTHENTICATED = "SP_KEY_IS_AUTHENTICATED";

    public static boolean isAuthenticated(Context context){

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPref.getBoolean(SP_KEY_IS_AUTHENTICATED, false);
        
    }

    public static void setAuthenticated(Context context){


        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(SP_KEY_IS_AUTHENTICATED, true);

        editor.commit();


    }
}
