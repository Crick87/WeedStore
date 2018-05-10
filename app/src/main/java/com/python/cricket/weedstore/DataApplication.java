package com.python.cricket.weedstore;

import android.app.Application;

import com.python.cricket.weedstore.models.Latlong;

public class DataApplication extends Application {

    public static String lastUser = "";
    public static String token = "";

    public static Latlong tempLatlong = null;

    //public static String URLAPI = "http://192.168.43.59:8080/ventas/api/"; //phone
    //public static String URLAPI = "http://192.168.0.117:8080/ventas/api/"; //crispj
    public static String URLAPI = "http://192.168.1.70:8080/ventas/api/"; //home

}
