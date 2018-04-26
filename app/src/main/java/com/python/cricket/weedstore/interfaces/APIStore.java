package com.python.cricket.weedstore.interfaces;

import com.python.cricket.weedstore.models.Customer;
import com.python.cricket.weedstore.models.LoginRequest;
import com.python.cricket.weedstore.models.User;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface APIStore {
    @POST("login")
    Call<User> getUser(@Body LoginRequest loginRequest);

    @GET("customers")
    Call<ArrayList<Customer>> getCustomers();

    @GET("customers/{id}")
    Call<Customer> getCustomer(@Path("id") int id);

    @POST("/customers")
    Call<Customer> createCustomer(@Body Customer customer);

    @PUT("/customers")
    Call<Customer> updateCustomer(@Body Customer customer);

    @DELETE("/customers")
    Call<Customer> deleteCustomer(@Body Customer customer);

}
