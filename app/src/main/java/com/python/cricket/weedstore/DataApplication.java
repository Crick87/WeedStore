package com.python.cricket.weedstore;

import android.app.Application;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.birbit.android.jobqueue.JobManager;
import com.birbit.android.jobqueue.config.Configuration;
import com.birbit.android.jobqueue.scheduling.FrameworkJobSchedulerService;
import com.python.cricket.weedstore.helpers.DataSQLiteOpenHelper;
import com.python.cricket.weedstore.models.Latlong;
import com.python.cricket.weedstore.models.Product;
import com.python.cricket.weedstore.services.OfflineBasket;
import com.python.cricket.weedstore.services.RetrofitMan;

public class DataApplication extends Application {

    //public static String URLAPI = "http://192.168.43.59:8080/ventas/api/"; //phone
    public static String URLAPI = "http://192.168.0.117:8080/ventas/api/"; //crispj
    //public static String URLAPI = "http://192.168.1.76:8080/ventas/api/"; //home

    public static String lastUser = "";
    public static String token = "";
    public static int userID;

    public static Latlong tempLatlong = null;

    public static Product[] basketList = new Product[0];
    private static DataApplication instance;
    JobManager jobManager;

    public DataApplication(){
        instance = this;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        getJobManager();
        getTokenInDB();
        RetrofitMan.init(this);
    }

    public static boolean addBasket(Product item ){
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

    public static DataApplication getInstance() {
        return instance;
    }

    public synchronized JobManager getJobManager() {
        if (jobManager == null) {
            configureJobManager();
        }
        return jobManager;
    }

    private void configureJobManager() {
        Configuration.Builder builder = new Configuration.Builder(this)
                .minConsumerCount(1)//always keep at least one consumer alive
                .maxConsumerCount(3)//up to 3 consumers at a time
                .loadFactor(3)//3 jobs per consumer
                .consumerKeepAlive(120);//wait 2 minute
        builder.scheduler(FrameworkJobSchedulerService.createSchedulerFor(this,
                OfflineBasket.class), true);
        jobManager = new JobManager(builder.build());
    }
}
