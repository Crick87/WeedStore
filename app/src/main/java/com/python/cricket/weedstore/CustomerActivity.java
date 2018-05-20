package com.python.cricket.weedstore;

import android.app.Activity;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.python.cricket.weedstore.services.APIStore;
import com.python.cricket.weedstore.models.Customer;
import com.python.cricket.weedstore.services.RetrofitMan;

import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CustomerActivity extends AppCompatActivity {

    public static Activity a;
    RetrofitMan rf;
    APIStore api;
    Customer customer;
    String customerID;

    @BindView(R.id.customer_name) TextView cu_name;
    @BindView(R.id.customer_email) TextView cu_email;
    @BindView(R.id.customer_phone) TextView cu_phone;
    @BindView(R.id.customer_position) TextView cu_position;
    @BindView(R.id.fab_cu_edit) FloatingActionButton fab_edit;
    @BindView(R.id.fab_send_email) FloatingActionButton fab_email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);
        ButterKnife.bind(this);
        a=this;

        Bundle extras = getIntent().getExtras();
        customerID = extras.getString("customerID");

        rf.init(this);
        api = rf.get();

        fab_edit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), CustomerEditActivity.class);
                i.putExtra("customerID", customerID);
                startActivity(i);
            }
        });

        fab_email.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_SUBJECT, "WeedStore");
                intent.putExtra(Intent.EXTRA_TEXT, "Hola "+customer.getName()+"!");
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{customer.getEmail()});
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        fillData();

    }

    private void fillData() {
        api.getCustomer(Integer.parseInt(customerID)).enqueue(new Callback<Customer>() {
            @Override
            public void onResponse(Call<Customer> call, Response<Customer> response) {
                if(response.isSuccessful()){
                    customer = response.body();
                    cu_name.setText(customer.getName());
                    cu_email.setText(customer.getEmail());
                    cu_phone.setText(customer.getPhone());
                    try{
                        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                        List<Address> addresses = geocoder.getFromLocation(
                                customer.getLatlong().getX(),
                                customer.getLatlong().getY(),
                                1);
                        Address ubic = addresses.get(0);
                        cu_position.setText(
                                ubic.getThoroughfare()+" "+
                                ubic.getSubThoroughfare()+", "+
                                ubic.getSubLocality()+", "+
                                ubic.getSubAdminArea()+"."
                        );
                    }catch (Exception e){
                        cu_position.setText(
                                "lat: "+customer.getLatlong().getX().toString()+
                                ", lng: "+customer.getLatlong().getY().toString()
                        );
                    }


                }else{
                    Toast.makeText(getApplicationContext(), "Response error", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Customer> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Server error", Toast.LENGTH_LONG).show();
            }
        });
    }

}
