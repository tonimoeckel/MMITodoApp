package com.quemb.mmitodoapp.controller;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.quemb.mmitodoapp.R;
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


        if (!connectionSetting.isValid()) {
            Log.d(TAG, "No connection settings");
            startIntent(new Intent(this, ConnectionSettingsActivity.class));
        } else {

            try {
                Reachability reachability = new Reachability(this, connectionSetting.getURL());
                reachability.checkReachability(new ReachabilityListener() {
                    @Override
                    public void onReachabilityChecked(ReachabilityStatus status) {
                        if (status.equals(ReachabilityStatus.Reachable)){
                            if (!Authentication.isAuthenticated()) {
                                startIntent(new Intent(StartActivity.this, LoginActivity.class));
                            }else {
                                startIntent(new Intent(StartActivity.this, TodoListActivity.class));
                            }
                        }else {
                            showNoConnectionDialog();
                        }
                    }
                });
            } catch (MalformedURLException e) {
                Log.e(TAG, "Host not reachable", e);
                showNoConnectionDialog();
            }

        }

    }

    private void showNoConnectionDialog() {

        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.dialog_connection))
                .setMessage(R.string.dialog_proceed_without_server_sync)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        StartActivity.this.startIntent(new Intent(StartActivity.this, TodoListActivity.class));
                    }
                })
                .setNeutralButton(R.string.dialog_change_connection_settings, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        StartActivity.this.startIntent(new Intent(StartActivity.this, ConnectionSettingsActivity.class));
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();

    }

    private void startIntent(Intent intent){
        startActivity(intent);
        finish();
    }


}
