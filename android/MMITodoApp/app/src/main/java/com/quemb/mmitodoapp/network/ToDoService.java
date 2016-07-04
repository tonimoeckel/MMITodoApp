package com.quemb.mmitodoapp.network;

import com.quemb.mmitodoapp.model.LoginForm;
import com.quemb.mmitodoapp.model.ToDo;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by tonimoeckel on 21.06.16.
 */
public interface ToDoService  {

    @PUT("api/users/auth")
    Call<Boolean> authenticate(
        @Body LoginForm loginData
    );

    @GET("api/todos")
    Call<List<ToDo>> getTodos();

    @POST("api/todos")
    Call<ToDo> postTodo(
            @Body ToDo toDo
    );

    @DELETE("api/todos/{id}")
    Call<Boolean> deleteToDo(
        @Path("id") long id
    );

    @PUT("api/todos/{id}")
    Call<ToDo> putTodo(
            @Path("id") long id,
            @Body ToDo toDo
    );
}
