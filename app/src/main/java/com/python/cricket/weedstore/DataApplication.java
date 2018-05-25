package com.python.cricket.weedstore;

import android.app.Application;

import com.python.cricket.weedstore.models.Latlong;
import com.python.cricket.weedstore.models.Product;

public class DataApplication extends Application {

    //public static String URLAPI = "http://192.168.43.59:8080/ventas/api/"; //phone
    //public static String URLAPI = "http://192.168.0.117:8080/ventas/api/"; //crispj
    public static String URLAPI = "http://192.168.1.76:8080/ventas/api/"; //home

    public static String lastUser = "";
    public static String token = "";
    public static int userID;

    public static Latlong tempLatlong = null;

    public static Product[] basketList = new Product[0];

    public static boolean addBasket( Product item ){
        for (int i=0; i<basketList.length; i++){
            if ( basketList[i].getId() == item.getId() ){
                basketList[i].setQuantity(item.getQuantity());
                return true;
            }
        }
        Product newBasket[] = new Product[basketList.length+1];
        for (int i=0; i<basketList.length; i++){
            newBasket[i] = basketList[i];
        }
        newBasket[newBasket.length-1] = item;
        basketList = newBasket;
        return true;
    }

    public static void removeBasket( Product item ){
        //
    }

}
