package com.quemb.mmitodoapp.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.quemb.mmitodoapp.application.ApplicationController;
import com.quemb.mmitodoapp.model.Session;
import com.quemb.mmitodoapp.model.ToDo;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Created by tonimockel on 22.06.16.
 */

public class Authentication {

    public static boolean isAuthenticated(){
        return isAuthenticated(null);
    }

    public static boolean isAuthenticated(Context context){

        List<Session> list = Session.find(Session.class, "authenticated = ?", "1");
        return list.size() > 0;
        
    }

    public static void setAuthenticated(){

        Session.deleteAll(Session.class);

        Session session = new Session();
        session.loginDate = new Date();
        session.authenticated = true;
        session.save();

        ApplicationController.getSharedInstance().triggerSync();


    }


    public static void logout() {

        Session.deleteAll(Session.class);
        ToDo.deleteAll(ToDo.class);


    }


}
