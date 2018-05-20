package com.python.cricket.weedstore.services;

import android.content.Context;

import com.python.cricket.weedstore.DataApplication;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitMan {

    private static APIStore mRetrofits;

    private RetrofitMan() {}

    public static APIStore get()
    {
        return mRetrofits;
    }

    public static void init(Context context)
    {
        String url = DataApplication.URLAPI;
        if(mRetrofits == null) {

            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

            // For token
            String token = DataApplication.token;
            httpClient.interceptors().add(chain -> {
                Request originalRequest = chain.request();
                Request.Builder builder1 = originalRequest.newBuilder().header("Authorization", String.format("Bearer %s", token));
                Request newRequest = builder1.build();
                return chain.proceed(newRequest);
            });

            mRetrofits = new Retrofit.Builder()
                    .baseUrl(url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClient.build())
                    .build()
                    .create(APIStore.class);
        }
    }

}
