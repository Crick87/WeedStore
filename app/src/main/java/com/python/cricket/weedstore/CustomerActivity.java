package com.python.cricket.weedstore;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.python.cricket.weedstore.services.APIStore;
import com.python.cricket.weedstore.models.Customer;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CustomerActivity extends AppCompatActivity {

    APIStore api;
    Customer customer;

    @BindView(R.id.customer_name) TextView cu_name;
    @BindView(R.id.customer_email) TextView cu_email;
    @BindView(R.id.customer_phone) TextView cu_phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);
        ButterKnife.bind(this);

        Bundle extras = getIntent().getExtras();
        String customerID = extras.getString("customerID");

        api = new Retrofit.Builder()
                .baseUrl(DataApplication.URLAPI)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(APIStore.class);

        api.getCustomer(Integer.parseInt(customerID)).enqueue(new Callback<Customer>() {
            @Override
            public void onResponse(Call<Customer> call, Response<Customer> response) {
                if(response.isSuccessful()){
                    customer = response.body();
                    cu_name.setText(customer.getName());
                    cu_email.setText(customer.getEmail());
                    cu_phone.setText(customer.getPhone());

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
