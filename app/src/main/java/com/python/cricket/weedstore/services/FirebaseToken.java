package com.python.cricket.weedstore.services;

import android.util.Log;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.python.cricket.weedstore.DataApplication;
import com.python.cricket.weedstore.models.FireToken;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FirebaseToken extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh(); //?
        String token = FirebaseInstanceId.getInstance().getToken();
        reg_token(token);
    }
    private void reg_token(String token)
    {
        APIStore api = new Retrofit.Builder()
                .baseUrl(DataApplication.URLAPI)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(APIStore.class);

        FireToken newToken = new FireToken();
        newToken.setToken(token);

        api.addToken(newToken).enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if(response.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "Token registrado", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(getApplicationContext(), "Token no registrado", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Server error, token no registrado.", Toast.LENGTH_LONG).show();
            }
        });
    }
}