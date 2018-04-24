package com.python.cricket.weedstore.helpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataSQLiteOpenHelper extends SQLiteOpenHelper {

    String SQLCreate = "create table DataWS(id integer primary key autoincrement not null, token text)";

    public DataSQLiteOpenHelper(Context context, String nombre, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, nombre, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Aquí creamos la tabla
        db.execSQL(SQLCreate);
    }

    @Override

    public void onUpgrade(SQLiteDatabase db, int version1, int version2) {

        db.execSQL("drop table if exists DataWS");

        db.execSQL(SQLCreate);

    }

}
