package com.quemb.mmitodoapp.controller;

import com.quemb.mmitodoapp.model.ToDo;

/**
 * Created by tonimoeckel on 01.07.16.
 */
public interface ToDoListener {

    public void onCreate(ToDo toDo);
    public void onUpdate(ToDo toDo);

}
