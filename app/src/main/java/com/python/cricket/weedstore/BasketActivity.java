package com.python.cricket.weedstore;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.python.cricket.weedstore.models.Customer;
import com.python.cricket.weedstore.models.Order;
import com.python.cricket.weedstore.models.Product;
import com.python.cricket.weedstore.services.APIStore;
import com.python.cricket.weedstore.services.RetrofitMan;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BasketActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    RetrofitMan rf;
    APIStore api;
    ArrayList<Customer> customer_list = new ArrayList<>();
    int custID;

    @BindView(R.id.customer_select) Spinner customer;
    @BindView(R.id.btn_create_basket) Button btn_create;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basket);
        ButterKnife.bind(this);

        rf.init(this);
        api = rf.get();

        customer.setOnItemSelectedListener(this);

        api.getCustomers().enqueue(new Callback<ArrayList<Customer>>() {
            @Override
            public void onResponse(Call<ArrayList<Customer>> call, Response<ArrayList<Customer>> response) {
                if(response.isSuccessful()){
                    customer_list = response.body();
                    ArrayList<String> list_names = new ArrayList<>();
                    for (int i=0; i<customer_list.size(); i++){
                        list_names.add(customer_list.get(i).getName());
                    }
                    customer.setAdapter(new ArrayAdapter<>(getApplication(), android.R.layout.simple_spinner_item, list_names));
                }else{
                    Toast.makeText(getBaseContext(), "Response error", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<ArrayList<Customer>> call, Throwable t) {
                Toast.makeText(getBaseContext(), "Server error", Toast.LENGTH_LONG).show();
            }
        });

        btn_create.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if( DataApplication.basketList.length > 0 ){
                    Order order = new Order();
                    order.setCustomerId(custID);
                    order.setEmployeeId(DataApplication.userID);
                    order.setProductList(DataApplication.basketList);
                    order.setStatus(false);
                    createOrder(order);
                }
            }
        });

    }

    private void createOrder( Order order ){
        api.createOrder(order).enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if(response.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "Orden creada", Toast.LENGTH_LONG).show();
                    DataApplication.basketList = new Product[0];
                    finish();
                }else{
                    Toast.makeText(getApplicationContext(), "Error al crear orden", Toast.LENGTH_LONG).show();
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        custID = customer_list.get(position).getId();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
