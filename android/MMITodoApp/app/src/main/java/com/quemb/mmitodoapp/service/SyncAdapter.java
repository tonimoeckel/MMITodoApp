package com.quemb.mmitodoapp.service;


import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;
import android.util.Log;

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
            return;
        }



        Iterator<ToDo> localTodoIterator = ToDo.findAll(ToDo.class);

        ToDoService todoService = ApplicationController.getSharedInstance().getToDoService();
        Call<List<ToDo>> callCloudToDoList = todoService.getTodos();
        try {
            Response<List<ToDo>> responseCloudToDoList = callCloudToDoList.execute();
            if (responseCloudToDoList.isSuccessful()){
                List<ToDo> remoteTodoList = responseCloudToDoList.body();
                List<ToDo> localTodoList = Lists.newArrayList(localTodoIterator);

                List<ToDo> downloadList = getMissingTodos(localTodoList, remoteTodoList);
                List<ToDo> uploadList = getMissingTodos(remoteTodoList, localTodoList );

                if (downloadList.size() > 0){
                    Log.d(TAG, "Remote changes to download");
                    for (ToDo toDo :downloadList){
                        toDo.save();
                    }
                }else {
                    Log.d(TAG, "No remote changes to download");
                }

                if (uploadList.size() > 0){
                    Log.d(TAG, "Local changes to upload");
                    for (ToDo toDo : uploadList){
                        Call<ToDo> postCall = todoService.postTodo(toDo);
                        Response<ToDo> postResponse = postCall.execute();
                        if (postResponse.isSuccessful()){
                            Log.w(TAG, "Todo post to server successfully ("+toDo.getId()+")");
                        }else {
                            Log.e(TAG, postResponse.errorBody().string());
                        }
                    }
                }else {
                    Log.d(TAG, "No local changes to upload");
                }
            }


        } catch (IOException e) {
            e.printStackTrace();
        }

        syncResult.fullSyncRequested = true;

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
