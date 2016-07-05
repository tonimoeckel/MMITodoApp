package com.quemb.reachability;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.concurrent.ExecutionException;

/**
 * Created by tonimoeckel on 14.06.16.
 */
public class Reachability {

    private static final String TAG = "Reachability";

    private final Context mContext;
    private URL mHost;
    private ReachabilityStatus mStatus;
    private Date mLastCheck;

    private int mTimeout;


    public Reachability(Context context, URL host){

        mContext = context;
        mStatus = ReachabilityStatus.Unknown;
        mHost = host;
        mTimeout = 1000;
    }

    private boolean hasNetworkAvailable() {

        ConnectivityManager manager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean available = false;
        if (networkInfo != null && networkInfo.isConnected()) {
            available = true;
        }
        return available;
    }

    public void checkReachability(ReachabilityListener listener){

        ReachabilityStatus status = ReachabilityStatus.Unknown;
        if (hasNetworkAvailable()) {
            ConnectTask task = new ConnectTask();
            task.setListener(listener);
            task.execute(mHost);
        } else {
            Log.d(TAG, "No network present");
            mStatus = ReachabilityStatus.NotReachable;
            listener.onReachabilityChecked(status);
        }

    }

    public ReachabilityStatus checkReachability() throws ExecutionException, InterruptedException {

        ReachabilityStatus status = ReachabilityStatus.Unknown;
        if (hasNetworkAvailable()) {
            ConnectTask task = new ConnectTask();
            return task.execute(mHost).get();
        } else {
            Log.d(TAG, "No network present");
            mStatus = ReachabilityStatus.NotReachable;
            return mStatus;
        }

    }


    /**
     * GETTER and SETTER
     */

    public ReachabilityStatus getStatus(){
        return mStatus;
    }

    public int getTimeout() {
        return mTimeout;
    }

    public void setTimeout(int timeout) {
        mTimeout = timeout;
    }
}
