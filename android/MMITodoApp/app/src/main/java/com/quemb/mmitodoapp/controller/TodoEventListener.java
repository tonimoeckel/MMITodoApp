package com.quemb.mmitodoapp.controller;

import com.quemb.mmitodoapp.model.ToDo;

/**
 * Created by tonimockel on 06.07.16.
 */

public interface TodoEventListener {

    public void onTodoCreated(ToDo toDo);
    public void onTodoUpdated(ToDo toDo);
    public void onTodoRemoved(ToDo toDo);

}
