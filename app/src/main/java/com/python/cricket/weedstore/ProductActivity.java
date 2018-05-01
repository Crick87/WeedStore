package com.python.cricket.weedstore;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.python.cricket.weedstore.models.Customer;
import com.python.cricket.weedstore.models.Product;
import com.python.cricket.weedstore.services.APIStore;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProductActivity extends AppCompatActivity {

    APIStore api;
    Product product;
    String productID;

    @BindView(R.id.product_name) TextView prod_name;
    @BindView(R.id.product_description) TextView prod_description;
    @BindView(R.id.product_price) TextView prod_price;
    @BindView(R.id.fab_cu_edit) FloatingActionButton fab_edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        ButterKnife.bind(this);

        Bundle extras = getIntent().getExtras();
        productID = extras.getString("productID");

        api = new Retrofit.Builder()
                .baseUrl(DataApplication.URLAPI)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(APIStore.class);

        fab_edit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), ProductEditActivity.class);
                i.putExtra("productID", productID);
                startActivity(i);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        fillData();

    }

    private void fillData() {
        api.getProduct(Integer.parseInt(productID)).enqueue(new Callback<Product>() {
            @Override
            public void onResponse(Call<Product> call, Response<Product> response) {
                if(response.isSuccessful()){
                    product = response.body();
                    prod_name.setText(product.getName());
                    prod_description.setText(product.getDescription());
                    prod_price.setText(Double.toString(product.getPrice()));

                }else{
                    Toast.makeText(getApplicationContext(), "Response error", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Product> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Server error", Toast.LENGTH_LONG).show();
            }
        });
    }
}
