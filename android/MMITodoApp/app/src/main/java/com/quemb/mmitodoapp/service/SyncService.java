package com.quemb.mmitodoapp.service;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by tonimockel on 02.07.16.
 */

public class SyncService extends Service {

    private static final String TAG = "SyncService";
    public static final String SYNC_EXTRAS_POST = "SYNC_EXTRAS_POST";
    public static final String SYNC_EXTRAS_PUT = "SYNC_EXTRAS_PUT";
    public static final String SYNC_EXTRAS_DELETE = "SYNC_EXTRAS_DELETE";
    public static final String SYNC_EXTRAS_DATA_ID = "SYNC_EXTRAS_DATA_ID";

    private static SyncAdapter sSyncAdapter = null;
    private static final Object sSyncAdapterLock = new Object();

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "created");
        synchronized (sSyncAdapterLock) {
            if (sSyncAdapter == null) {
                sSyncAdapter = new SyncAdapter(getApplicationContext(), true);
            }
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return sSyncAdapter.getSyncAdapterBinder();
    }
}
