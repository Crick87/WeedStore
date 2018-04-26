package com.python.cricket.weedstore;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.python.cricket.weedstore.helpers.DataSQLiteOpenHelper;
import com.python.cricket.weedstore.interfaces.APIStore;
import com.python.cricket.weedstore.models.LoginRequest;
import com.python.cricket.weedstore.models.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SplashActivity extends AppCompatActivity {

    APIStore api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getTokenInDB();

        api = new Retrofit.Builder()
                .baseUrl(DataApplication.URLAPI)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(APIStore.class);

        User user = new User();
        user.setUsername(DataApplication.lastUser);
        user.setToken(DataApplication.token);

        api.getTest(user).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.isSuccessful()){
                    // Start home activity
                    startActivity(new Intent(SplashActivity.this, HomeActivity.class));
                    // Close splash activity
                    finish();
                }else{
                    // Start login activity
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                    // Close splash activity
                    finish();
                }
            }
            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(getBaseContext(), "Server error", Toast.LENGTH_LONG).show();
            }
        });
    }

    // Obtenemos el token de la DB
    public void getTokenInDB() {
        // Abrimos DB
        DataSQLiteOpenHelper admin = new DataSQLiteOpenHelper(this,
                "dataws", null, 1);
        SQLiteDatabase bd = admin.getWritableDatabase();

        Cursor fila = bd.rawQuery(
                "select token, user from DataWS where id=0", null);

        //String i = fila.getString(0); //testing

        if (fila.moveToFirst()) {
            DataApplication.token = fila.getString(0);
            DataApplication.lastUser = fila.getString(1);
        }
        bd.close();
    }
}
