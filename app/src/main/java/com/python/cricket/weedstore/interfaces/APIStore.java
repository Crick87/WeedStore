package com.python.cricket.weedstore.interfaces;

import com.python.cricket.weedstore.models.LoginRequest;
import com.python.cricket.weedstore.models.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface APIStore {
    @POST("login")
    Call<User> getUser(@Body LoginRequest loginRequest);
}
