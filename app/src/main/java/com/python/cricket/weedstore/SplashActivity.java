package com.python.cricket.weedstore;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.python.cricket.weedstore.helpers.DataSQLiteOpenHelper;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getTokenInDB();

        // TODO: Conexion test y desicion

        // Start home activity
        startActivity(new Intent(SplashActivity.this, LoginActivity.class));
        // Close splash activity
        finish();
    }

    // Obtenemos el token de la DB
    public void getTokenInDB() {
        // Abrimos DB
        DataSQLiteOpenHelper admin = new DataSQLiteOpenHelper(this,
                "dataws", null, 1);
        SQLiteDatabase bd = admin.getWritableDatabase();

        Cursor fila = bd.rawQuery(
                "select token from DataWS where id=0", null);

        //String i = fila.getString(0); //testing

        if (fila.moveToFirst()) {
            DataApplication.token = fila.getString(0);
        }
        bd.close();
    }
}
