package com.quemb.reachability;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by tonimoeckel on 14.06.16.
 */

public class ConnectTask extends AsyncTask<URL, Void, ReachabilityStatus> {

    private static final String TAG = "ConnectTask";
    private ReachabilityListener mListener;
    private int mTimeout = 1000;

    @Override
    protected ReachabilityStatus doInBackground(URL... urls) {

        try {
            HttpURLConnection urlc = (HttpURLConnection) (urls[0].openConnection());
            urlc.setRequestProperty("Connection", "close");
            urlc.setConnectTimeout(mTimeout);
            urlc.connect();
            if (urlc.getResponseCode() < 500){
                return ReachabilityStatus.Reachable;
            }else if (urlc.getResponseCode() == 524 || urlc.getResponseCode() == 522 || urlc.getResponseCode() == 504){
                return ReachabilityStatus.Timeout;
            }else {
                return ReachabilityStatus.RemoteError;
            }
        } catch (IOException e) {
            Log.e(TAG, "Error: ", e);
            return ReachabilityStatus.Timeout;
        }

    }
    // onPostExecute displays the results of the AsyncTask.
    @Override
    protected void onPostExecute(ReachabilityStatus result) {

        if (mListener != null){
            mListener.onReachabilityChecked(result);
        }
    }

    public void setTimeout(int timeout) {
        mTimeout = timeout;
    }

    public void setListener(ReachabilityListener listener) {
        mListener = listener;
    }
}