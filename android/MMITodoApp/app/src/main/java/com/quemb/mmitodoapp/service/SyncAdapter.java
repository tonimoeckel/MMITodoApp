package com.quemb.mmitodoapp.service;


import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SyncResult;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.google.common.collect.Lists;
import com.quemb.mmitodoapp.application.ApplicationController;
import com.quemb.mmitodoapp.controller.TodoContactsFragment;
import com.quemb.mmitodoapp.model.ToDo;
import com.quemb.mmitodoapp.network.ToDoService;
import com.quemb.mmitodoapp.util.Authentication;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

import static com.quemb.mmitodoapp.service.SyncService.SYNC_EXTRAS_POST;

/**
 * Created by tonimockel on 02.07.16.
 */

public class SyncAdapter extends AbstractThreadedSyncAdapter {


    private static final String TAG = "TodoSyncAdapter";
    private ContentResolver mContentResolver;

    public SyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        init(context);
    }

    public SyncAdapter(Context context, boolean autoInitialize, boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);
        init(context);
    }


    private void init(Context context) {
        Log.w(TAG, "SyncAdapter Init");
        mContentResolver = context.getContentResolver();
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        Log.w(TAG, "onPerformSync");

        if (!Authentication.isAuthenticated(getContext())){
            Log.w(TAG, "Not Authenticated");
            getContext().getContentResolver().notifyChange(Uri.parse("todo://sync/uncomplete"), null, false);
            return;
        }


        if (extras.getBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED)){
            Log.w(TAG, "SYNC_EXTRAS_EXPEDITED");
            performExpiredSync();
        }else if (extras.getBoolean(SyncService.SYNC_EXTRAS_POST)){
            Log.w(TAG, SyncService.SYNC_EXTRAS_POST);
            performPostSync(extras.getLong(SyncService.SYNC_EXTRAS_DATA_ID, -1));
        }else if (extras.getBoolean(SyncService.SYNC_EXTRAS_PUT)){
            Log.w(TAG, SyncService.SYNC_EXTRAS_PUT);
            performPutSync(extras.getLong(SyncService.SYNC_EXTRAS_DATA_ID, -1));
        }else if (extras.getBoolean(SyncService.SYNC_EXTRAS_DELETE)){
            Log.w(TAG, SyncService.SYNC_EXTRAS_DELETE);
            performDeleteSync(extras.getLong(SyncService.SYNC_EXTRAS_DATA_ID, -1));
        }


        Log.w(TAG, "Sync Finished");

        getContext().getContentResolver().notifyChange(Uri.parse("todo://sync/complete"), null, false);

    }

    private void performPostSync(long aLong) {
        if (aLong > -1){
            ToDo todo = ToDo.findById(ToDo.class, aLong);
            if (todo != null){
                ToDoService todoService = ApplicationController.getSharedInstance().getToDoService();
                try {
                    todoService.postTodo(todo).execute();
                } catch (IOException e) {
                    Log.e(TAG, "Error posting local data to remote", e);
                }
            }
        }else {
            Log.e(TAG, "Unvalid Todo ID");
        }
    }

    private void performPutSync(long aLong) {
        if (aLong > -1){
            ToDo todo = ToDo.findById(ToDo.class, aLong);
            if (todo != null){
                ToDoService todoService = ApplicationController.getSharedInstance().getToDoService();
                try {
                    todoService.putTodo(aLong,todo).execute();
                } catch (IOException e) {
                    Log.e(TAG, "Error putting local data to remote", e);
                }
            }
        }else {
            Log.e(TAG, "Unvalid Todo ID");
        }
    }

    private void performDeleteSync(long aLong) {
        if (aLong > -1){
            ToDoService todoService = ApplicationController.getSharedInstance().getToDoService();
            try {
                todoService.deleteToDo(aLong).execute();
            } catch (IOException e) {
                Log.e(TAG, "Error deleting local data from remote", e);
            }
        }else {
            Log.e(TAG, "Unvalid Todo ID");
        }
    }


    private void performExpiredSync() {

        Log.d(TAG, "performExpiredSync");
        if (ToDo.count(ToDo.class)>0){
            Log.d(TAG, "Prefer local data");
            try {
                removeAllRemoteToDos();
            } catch (IOException e) {
                Log.e(TAG, "Error cleaning remote data", e);
            }

            try {
                postLocalToRemote();
            } catch (IOException e) {
                Log.e(TAG, "Error posting local data to remote", e);
            }

        }else {
            Log.d(TAG, "Prefer remote data");
            try {
                getRemoteTodos();
            } catch (IOException e) {
                Log.e(TAG, "Error fetching remote data", e);
            }

        }

    }

    private void getRemoteTodos() throws IOException {

        ToDoService todoService = ApplicationController.getSharedInstance().getToDoService();
        Call<List<ToDo>> callCloudToDoList = todoService.getTodos();
        Response<List<ToDo>> response = callCloudToDoList.execute();
        if (response.isSuccessful()){
            for (ToDo toDo : response.body()){
                toDo.save();
            }
        }

    }

    private void postLocalToRemote() throws IOException {

        ToDoService todoService = ApplicationController.getSharedInstance().getToDoService();
        Iterator<ToDo> localList = ToDo.findAll(ToDo.class);
        while (localList.hasNext()){
            ToDo toDo = localList.next();
            Response<ToDo> response = todoService.postTodo(toDo).execute();
            if (response.isSuccessful()){
                Log.d(TAG, "Created " + toDo.getId() + " on remote");
            }else {
                Log.e(TAG, response.errorBody().string());
            }
        }

    }

    private void removeAllRemoteToDos() throws IOException {

        ToDoService todoService = ApplicationController.getSharedInstance().getToDoService();
        Call<List<ToDo>> callCloudToDoList = todoService.getTodos();
        Response<List<ToDo>> response = callCloudToDoList.execute();
        if (response.isSuccessful()){
            List<ToDo> list = response.body();
            Log.d(TAG, "Found " + list.size() + " remote items to remove");
            for (ToDo toDo : list){
                Log.d(TAG, "Remove Todo: " + toDo.getId());
                Response<Boolean> deleteResponse = todoService.deleteToDo(toDo.getId()).execute();
                if (deleteResponse.isSuccessful()){
                    Log.d(TAG, "Deleted " + toDo.getId() + " on remote");
                }else {
                    Log.e(TAG, deleteResponse.errorBody().string());
                }
            }
        }else {
            Log.e(TAG, response.errorBody().string());
        }

    }

    public List<ToDo> getMissingTodos(List<ToDo> completeList, List<ToDo> uncompleteList){

        ArrayList<ToDo> result = new ArrayList<>();
        for (ToDo toDo : uncompleteList){
            if (!completeList.contains(toDo)){
                result.add(toDo);
            }
        }
        return result;

    }
}
