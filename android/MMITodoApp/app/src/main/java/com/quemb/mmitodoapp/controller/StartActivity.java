package com.quemb.mmitodoapp.controller;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.quemb.mmitodoapp.model.ConnectionSetting;
import com.quemb.mmitodoapp.model.ConnectionSettingFactory;
import com.quemb.mmitodoapp.util.Authentication;
import com.quemb.reachability.Reachability;
import com.quemb.reachability.ReachabilityListener;
import com.quemb.reachability.ReachabilityStatus;

import java.net.MalformedURLException;

/**
 * Created by tonimockel on 22.06.16.
 */

public class StartActivity extends Activity {

    private static final String TAG = "StartActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ConnectionSetting connectionSetting = ConnectionSettingFactory.getSharedPreferencesSetting(this);

/**
        if (!connectionSetting.isValid()) {
            startIntent(new Intent(this, ConnectionSettingsActivity.class));
        } else {
            if (!Authentication.isAuthenticated(this)) {

                try {
                    Reachability reachability = new Reachability(this, connectionSetting.getURL());
                    reachability.checkReachability(new ReachabilityListener() {
                        @Override
                        public void onReachabilityChecked(ReachabilityStatus status) {
                            if (status.equals(ReachabilityStatus.Reachable)){
                                startIntent(new Intent(StartActivity.this, LoginActivity.class));
                            }else {
                                startIntent(new Intent(StartActivity.this, TodoListActivity.class));
                            }
                        }
                    });
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    startIntent(new Intent(this, LoginActivity.class));
                }

            } else {

                startIntent(new Intent(this, TodoListActivity.class));

            }

        }
         **/
        startIntent(new Intent(this, TodoListActivity.class));
    }

    private void startIntent(Intent intent){
        startActivity(intent);
        finish();
    }


}
