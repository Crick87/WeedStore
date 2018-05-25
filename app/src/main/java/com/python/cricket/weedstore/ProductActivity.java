package com.python.cricket.weedstore;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.python.cricket.weedstore.models.Customer;
import com.python.cricket.weedstore.models.Product;
import com.python.cricket.weedstore.services.APIStore;
import com.python.cricket.weedstore.services.RetrofitMan;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProductActivity extends AppCompatActivity {

    public static Activity a;
    RetrofitMan rf;
    APIStore api;
    Product product;
    int quantity=0;
    String productID;

    @BindView(R.id.product_name) TextView prod_name;
    @BindView(R.id.product_description) TextView prod_description;
    @BindView(R.id.product_price) TextView prod_price;
    @BindView(R.id.product_stock) TextView prod_stock;
    @BindView(R.id.product_quantity) TextView prod_quantity;
    @BindView(R.id.fab_cu_edit) FloatingActionButton fab_edit;
    @BindView(R.id.fab_less) FloatingActionButton fab_less;
    @BindView(R.id.fab_more) FloatingActionButton fab_more;
    @BindView(R.id.btn_basket) Button btn_basket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        ButterKnife.bind(this);
        a=this;

        Bundle extras = getIntent().getExtras();
        productID = extras.getString("productID");

        checkBasket();

        rf.init(this);
        api = rf.get();

        fab_edit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), ProductEditActivity.class);
                i.putExtra("productID", productID);
                startActivity(i);
            }
        });

        fab_less.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (quantity > 0){
                    quantity--;
                    prod_quantity.setText(Integer.toString(quantity));
                }
            }
        });

        fab_more.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (quantity < product.getStock()){
                    quantity++;
                    prod_quantity.setText(Integer.toString(quantity));
                }
            }
        });

        btn_basket.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(quantity > 0){
                    product.setQuantity(quantity);
                    DataApplication.addBasket(product);
                    Toast.makeText(getApplicationContext(), product.getName()+" agregados ("+quantity+")", Toast.LENGTH_LONG).show();
                }
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
                    prod_stock.setText(Integer.toString(product.getStock()));

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

    private void checkBasket(){
        for (int i=0; i<DataApplication.basketList.length; i++){
            if ( DataApplication.basketList[i].getId() == Integer.parseInt(productID) ){
                quantity = DataApplication.basketList[i].getQuantity();
                prod_quantity.setText(Integer.toString(quantity));
            }
        }
    }
}
