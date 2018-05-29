package com.python.cricket.weedstore;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.auth0.android.jwt.JWT;

import com.python.cricket.weedstore.helpers.DataSQLiteOpenHelper;
import com.python.cricket.weedstore.services.APIStore;
import com.python.cricket.weedstore.models.User;
import com.python.cricket.weedstore.services.RetrofitMan;


public class SplashActivity extends AppCompatActivity {

    APIStore api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getTokenInDB();

        api = RetrofitMan.get();

        User user = new User();
        user.setUsername(DataApplication.lastUser);
        user.setToken(DataApplication.token);

        String token = DataApplication.token;
        android.content.Context mContext = this;
        if (token != null) {
            if (!(new JWT(token).isExpired(10))) {
                mContext.startActivity(new Intent(mContext, HomeActivity.class));
                finish();
            }
            else {
                Toast.makeText(mContext, "Token expirado", Toast.LENGTH_LONG).show();
                mContext.startActivity(new Intent(mContext, LoginActivity.class));
                finish();
            }
        } else {
            mContext.startActivity(new Intent(mContext, LoginActivity.class));
            finish();
        }

        /*api.getTest(user).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.isSuccessful()){
                    DataApplication.userID = response.body().getId();
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
                // On server error:
                // Start login activity
                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                // Close splash activity
                finish();
            }
        });*/
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
