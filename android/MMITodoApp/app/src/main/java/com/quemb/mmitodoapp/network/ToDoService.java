package com.quemb.mmitodoapp.network;

import com.quemb.mmitodoapp.model.LoginForm;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.PUT;

/**
 * Created by tonimoeckel on 21.06.16.
 */
public interface ToDoService  {

    @PUT("api/users/auth")
    Call<Boolean> authenticate(
        @Body LoginForm loginData
    );

}
