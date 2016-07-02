package com.quemb.mmitodoapp.util;

import android.os.Bundle;

import com.quemb.mmitodoapp.model.ToDo;

/**
 * Created by tonimockel on 02.07.16.
 */

public class ToDoIntentUtils {

    public static final String INTENT_EXTRA_TODO_ID = "INTENT_EXTRA_TODO_ID";

    public static long getToDoIdFromIntent(Bundle bundle){

        if (bundle == null){
            return -1;
        }
        long extraId = bundle.getLong(INTENT_EXTRA_TODO_ID, -1);

        return extraId;
    }


    public static ToDo getToDoFromIntent(Bundle bundle){

        long extraId = getToDoIdFromIntent(bundle);

        if (extraId > -1){
            return ToDo.findById(ToDo.class, extraId);
        }

        return null;
    }
}
