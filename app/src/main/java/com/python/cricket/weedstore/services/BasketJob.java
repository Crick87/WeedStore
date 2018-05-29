package com.python.cricket.weedstore.services;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.birbit.android.jobqueue.Job;
import com.birbit.android.jobqueue.Params;
import com.birbit.android.jobqueue.RetryConstraint;
import com.python.cricket.weedstore.DataApplication;
import com.python.cricket.weedstore.models.Order;
import com.python.cricket.weedstore.models.Product;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BasketJob extends Job {

    public static final int PRIORITY = 500;
    private final Order order;

    public BasketJob(Order order) {
        super(new Params(PRIORITY).requireNetwork().persist());
        this.order = order;
    }

    @Override
    public void onAdded() {

    }

    @Override
    public void onRun() throws Throwable {
        RetrofitMan.get().createOrder(order).enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if(response.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "Orden creada", Toast.LENGTH_LONG).show();
                    DataApplication.basketList = new Product[0];
                }else{
                    Toast.makeText(getApplicationContext(), "Error al crear orden", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {}
        });
    }

    @Override
    protected void onCancel(int cancelReason, @Nullable Throwable throwable) {

    }

    @Override
    protected RetryConstraint shouldReRunOnThrowable(@NonNull Throwable throwable, int runCount, int maxRunCount) {
        return null;
    }
}