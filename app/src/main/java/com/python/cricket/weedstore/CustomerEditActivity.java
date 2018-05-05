package com.python.cricket.weedstore;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.python.cricket.weedstore.models.Customer;
import com.python.cricket.weedstore.models.Latlong;
import com.python.cricket.weedstore.models.User;
import com.python.cricket.weedstore.services.APIStore;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CustomerEditActivity extends AppCompatActivity {

    APIStore api;
    Customer customer = new Customer();
    Latlong latlong = new Latlong();

    @BindView(R.id.et_cus_name) EditText et_name;
    @BindView(R.id.et_cus_email) EditText et_email;
    @BindView(R.id.et_cus_phone) EditText et_phone;
    @BindView(R.id.customer_position) TextView cu_position;
    @BindView(R.id.fab_cu_save) FloatingActionButton fab_save;
    @BindView(R.id.fab_cu_delete) FloatingActionButton fab_delete;
    @BindView(R.id.btn_cus_position) Button btn_position;
    @BindView(R.id.btn_cus_save) Button btn_save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_edit);
        ButterKnife.bind(this);

        Bundle extras = getIntent().getExtras();
        final String customerID = extras.getString("customerID");

        api = new Retrofit.Builder()
                .baseUrl(DataApplication.URLAPI)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(APIStore.class);

        if(!customerID.equals("-1")){
            api.getCustomer(Integer.parseInt(customerID)).enqueue(new Callback<Customer>() {
                @Override
                public void onResponse(Call<Customer> call, Response<Customer> response) {
                    if(response.isSuccessful()){
                        customer = response.body();
                        et_name.setText(customer.getName());
                        et_email.setText(customer.getEmail());
                        et_phone.setText(customer.getPhone());

                        if (DataApplication.tempLatlong==null){
                            DataApplication.tempLatlong = new Latlong();
                            DataApplication.tempLatlong.setX(customer.getLatlong().getX());
                            DataApplication.tempLatlong.setY(customer.getLatlong().getY());
                        }
                        cu_position.setText(
                                "lat: "+DataApplication.tempLatlong.getX()+
                                ", lng: "+DataApplication.tempLatlong.getY()
                        );

                    }else{
                        Toast.makeText(getApplicationContext(), "Response error", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<Customer> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), "Server error", Toast.LENGTH_LONG).show();
                }
            });

            fab_save.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    saveCustomer();
                }
            });

            btn_position.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent intMap = new Intent(getApplicationContext(), MapsActivity.class);
                    intMap.putExtra("actualLat", DataApplication.tempLatlong.getX());
                    intMap.putExtra("actualLong", DataApplication.tempLatlong.getY());
                    startActivity(intMap);
                }
            });

            btn_save.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    saveCustomer();
                }
            });

            fab_delete.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    AlertDialog.Builder builder;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        builder = new AlertDialog.Builder(CustomerEditActivity.this, R.style.AppTheme_AlertDialog);
                    } else {
                        builder = new AlertDialog.Builder(CustomerEditActivity.this);
                    }
                    builder.setTitle("Eliminar cliente")
                            .setMessage("Seguro que desea eliminar a "+customer.getName()+"?")
                            .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(getApplicationContext(), "TODO Eliminar", Toast.LENGTH_SHORT).show();
                                    //delCustomer();
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // nothing
                                }
                            })
                            .show();
                }
            });

        }else{

            btn_position.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent intMap = new Intent(getApplicationContext(), MapsActivity.class);
                    intMap.putExtra("actualLat", 0);
                    intMap.putExtra("actualLong", 0);
                    startActivity(intMap);
                }
            });

            fab_delete.setVisibility(View.INVISIBLE);

            fab_save.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    addCustomer();
                }
            });

            btn_save.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    addCustomer();
                }
            });
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        if(DataApplication.tempLatlong!=null){
            cu_position.setText(
                    "lat: "+DataApplication.tempLatlong.getX()+
                            ", lng: "+DataApplication.tempLatlong.getY()
            );
        }else {
            cu_position.setText("Sin asignar");
        }

    }

    @Override
    public void onBackPressed() {
        DataApplication.tempLatlong = null;
        finish();
    }

    private void saveCustomer(){
        customer.setName(et_name.getText().toString());
        customer.setEmail(et_email.getText().toString());
        customer.setPhone(et_phone.getText().toString());
        Latlong newLatlong = new Latlong();
        newLatlong.setX(DataApplication.tempLatlong.getX());
        newLatlong.setY(DataApplication.tempLatlong.getY());
        customer.setLatlong(newLatlong);
        DataApplication.tempLatlong = null;

        api.updateCustomer(customer).enqueue(new Callback<Customer>() {
            @Override
            public void onResponse(Call<Customer> call, Response<Customer> response) {
                if(response.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "Cliente actualizado", Toast.LENGTH_LONG).show();
                    finish();
                }else{
                    Toast.makeText(getApplicationContext(), "Error al actualizar", Toast.LENGTH_LONG).show();
                    finish();
                }
            }
            @Override
            public void onFailure(Call<Customer> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Server error, intente más tarde.", Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }

    private void addCustomer(){
        customer.setName(et_name.getText().toString());
        customer.setEmail(et_email.getText().toString());
        customer.setPhone(et_phone.getText().toString());
        Latlong newLatlong = new Latlong();
        newLatlong.setX(DataApplication.tempLatlong.getX());
        newLatlong.setY(DataApplication.tempLatlong.getY());
        customer.setLatlong(newLatlong);
        DataApplication.tempLatlong = null;

        api.createCustomer(customer).enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if(response.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "Cliente creado", Toast.LENGTH_LONG).show();
                    finish();
                }else{
                    Toast.makeText(getApplicationContext(), "Error al crear cliente", Toast.LENGTH_LONG).show();
                    finish();
                }
            }
            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Server error, intente más tarde.", Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }

    private void delCustomer(){

        api.deleteCustomer(customer.getId()).enqueue(new Callback<Customer>() {
            @Override
            public void onResponse(Call<Customer> call, Response<Customer> response) {
                if(response.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "Cliente eliminado", Toast.LENGTH_LONG).show();
                    finish();
                }else{
                    Toast.makeText(getApplicationContext(), "Error al eliminar", Toast.LENGTH_LONG).show();
                    finish();
                }
            }
            @Override
            public void onFailure(Call<Customer> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Server error, intente más tarde.", Toast.LENGTH_LONG).show();
                finish();
            }
        });

    }
}
