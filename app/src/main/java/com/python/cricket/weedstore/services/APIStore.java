package com.python.cricket.weedstore.services;

import com.python.cricket.weedstore.models.Customer;
import com.python.cricket.weedstore.models.FireToken;
import com.python.cricket.weedstore.models.LoginRequest;
import com.python.cricket.weedstore.models.Order;
import com.python.cricket.weedstore.models.Product;
import com.python.cricket.weedstore.models.User;
import com.python.cricket.weedstore.models.Route;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface APIStore {

    // Login and test
    @POST("login")
    Call<User> getUser(@Body LoginRequest loginRequest);

    @POST("test")
    Call<User> getTest(@Body User user);

    // Customers
    @GET("customers")
    Call<ArrayList<Customer>> getCustomers();

    @GET("customers/{id}")
    Call<Customer> getCustomer(@Path("id") int id);

    @POST("customers")
    Call<Boolean> createCustomer(@Body Customer customer);

    @PUT("customers")
    Call<Customer> updateCustomer(@Body Customer customer);

    @DELETE("customers/{id}")
    Call<Boolean> deleteCustomer(@Path("id") int id);

    // Products
    @GET("products")
    Call<ArrayList<Product>> getProducts();

    @GET("products/{id}")
    Call<Product> getProduct(@Path("id") int id);

    @POST("products")
    Call<Boolean> createProduct(@Body Product product);

    @PUT("products")
    Call<Product> updateProduct(@Body Product product);

    @DELETE("products/{id}")
    Call<Boolean> deleteProduct(@Path("id") int id);

    // Orders
    @GET("orders")
    Call<ArrayList<Order>> getOrders();

    @GET("orders/{id}")
    Call<Order> getOrder(@Path("id") int id);

    @POST("orders")
    Call<Order> createOrder(@Body Order order);

    @PUT("orders")
    Call<Order> updateOrder(@Body Order order);

    @DELETE("orders")
    Call<Order> deleteOrder(@Body Order order);

    // Map routes
    @GET("users/routes/{id}")
    Call<ArrayList<Route>> getEmployeeRoute(@Path("id") int id);

    //Set Firebase token
    @POST("token")
    Call<Boolean> addToken(@Body FireToken token);

}
