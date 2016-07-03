package com.quemb.mmitodoapp.application;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ContentResolver;
import android.content.Context;
import android.os.Bundle;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.orm.SugarApp;
import com.quemb.mmitodoapp.model.ConnectionSetting;
import com.quemb.mmitodoapp.model.ConnectionSettingFactory;
import com.quemb.mmitodoapp.network.ToDoService;

import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.util.Date;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by tonimoeckel on 21.06.16.
 */
public class ApplicationController extends SugarApp {

    private final String DEFAULT_BASE_URL = "http://localhost:8080";

    private static ApplicationController sharedInstance;

    private ToDoService mToDoService;

    // Constants
    // The authority for the sync adapter's content provider
    public static final String AUTHORITY = "com.quemb.mmitodoapp.sync.provider";
    // An account type, in the form of a domain name
    public static final String ACCOUNT_TYPE = "com.quemb.mmitodoapp";
    // The account name
    public static final String ACCOUNT = "dummyaccount";
    // Instance fields
    private Account mAccount;

    /**
     * Create a new dummy account for the sync adapter
     *
     * @param context The application context
     */
    public static Account CreateSyncAccount(Context context) {
        // Create the account type and default account
        Account newAccount = new Account(
                ACCOUNT, ACCOUNT_TYPE);
        // Get an instance of the Android account manager
        AccountManager accountManager =
                (AccountManager) context.getSystemService(
                        ACCOUNT_SERVICE);
        /*
         * Add the account and account type, no password or user data
         * If successful, return the Account object, otherwise report an error.
         */
        if (accountManager.addAccountExplicitly(newAccount, null, null)) {
            /*
             * If you don't set android:syncable="true" in
             * in your <provider> element in the manifest,
             * then call context.setIsSyncable(account, AUTHORITY, 1)
             * here.
             */
        } else {
            /*
             * The account exists or some other error occurred. Log this, report it,
             * or handle it internally.
             */
        }

        return newAccount;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sharedInstance = this;

        ConnectionSettingFactory.getSharedPreferencesSetting(getApplicationContext());

        mAccount = CreateSyncAccount(this);

        ContentResolver.setSyncAutomatically(mAccount, AUTHORITY, true);
        triggerSync();
    }

    public static synchronized ApplicationController getSharedInstance() {
        return sharedInstance;
    }

    public Retrofit createRetrofit(String baseUrl) {

        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create(getGsonObject()))
                .build();
    }

    public ToDoService createToDoService(String baseUrl){

        if (baseUrl == null){
            baseUrl = DEFAULT_BASE_URL;
        }
        Retrofit retrofit = createRetrofit(baseUrl);
        mToDoService = retrofit.create(ToDoService.class);
        return mToDoService;

    }

    public Account getAccount(){
        return mAccount;
    }

    public void triggerSync(){

        Bundle syncBundle = new Bundle();
        syncBundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        syncBundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        ContentResolver.requestSync(getAccount(), AUTHORITY, syncBundle);

    }

    public ToDoService getToDoService() {

        if (mToDoService == null){
            ConnectionSetting connectionSetting = ConnectionSettingFactory.getSharedPreferencesSetting(getApplicationContext());
            try {
                mToDoService = createToDoService(connectionSetting.getURL().toString());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
        return mToDoService;
    }

    public static Gson getGsonObject(){
        return new GsonBuilder()
                .registerTypeAdapter(String[].class, new JsonDeserializer<String[]>() {
                    @Override
                    public String[] deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                        return new String[0];
                    }
                })
                .registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
                    public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                        return new Date(json.getAsJsonPrimitive().getAsLong());
                    }
                })
                .registerTypeAdapter(Date.class, new JsonSerializer<Date>() {
                    @Override
                    public JsonElement serialize(Date src, Type typeOfSrc, JsonSerializationContext context) {
                        return new JsonPrimitive(src.getTime());
                    }
                })
                .create();
    }
}
