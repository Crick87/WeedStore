package com.python.cricket.weedstore;

import android.content.DialogInterface;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.python.cricket.weedstore.models.Product;
import com.python.cricket.weedstore.services.APIStore;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProductEditActivity extends AppCompatActivity {

    APIStore api;
    Product product = new Product();

    @BindView(R.id.et_prod_name) EditText et_name;
    @BindView(R.id.et_prod_description) EditText et_description;
    @BindView(R.id.et_prod_price) EditText et_price;
    @BindView(R.id.fab_prod_save) FloatingActionButton fab_save;
    @BindView(R.id.fab_prod_delete) FloatingActionButton fab_delete;
    @BindView(R.id.btn_prod_save) Button btn_save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_edit);
        ButterKnife.bind(this);

        Bundle extras = getIntent().getExtras();
        final String productID = extras.getString("productID");

        api = new Retrofit.Builder()
                .baseUrl(DataApplication.URLAPI)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(APIStore.class);

        if(!productID.equals("-1")){
            api.getProduct(Integer.parseInt(productID)).enqueue(new Callback<Product>() {
                @Override
                public void onResponse(Call<Product> call, Response<Product> response) {
                    if(response.isSuccessful()){
                        product = response.body();
                        et_name.setText(product.getName());
                        et_description.setText(product.getDescription());
                        et_price.setText(Double.toString(product.getPrice()));

                    }else{
                        Toast.makeText(getApplicationContext(), "Response error", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<Product> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), "Server error", Toast.LENGTH_LONG).show();
                }
            });

            fab_save.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    saveProduct();
                }
            });

            btn_save.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    saveProduct();
                }
            });

            fab_delete.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    AlertDialog.Builder builder;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        builder = new AlertDialog.Builder(ProductEditActivity.this, R.style.AppTheme_AlertDialog);
                    } else {
                        builder = new AlertDialog.Builder(ProductEditActivity.this);
                    }
                    builder.setTitle("Eliminar producto")
                            .setMessage("Seguro que desea eliminar el producto '"+product.getName()+"'?")
                            .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    delProduct();
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
            fab_delete.setVisibility(View.INVISIBLE);

            fab_save.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    addProduct();
                }
            });

            btn_save.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    addProduct();
                }
            });
        }
    }

    private void saveProduct(){
        product.setName(et_name.getText().toString());
        product.setDescription(et_description.getText().toString());
        product.setPrice(Double.parseDouble(et_price.getText().toString()));

        api.updateProduct(product).enqueue(new Callback<Product>() {
            @Override
            public void onResponse(Call<Product> call, Response<Product> response) {
                if(response.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "Producto actualizado", Toast.LENGTH_LONG).show();
                    finish();
                }else{
                    Toast.makeText(getApplicationContext(), "Error al actualizar", Toast.LENGTH_LONG).show();
                    finish();
                }
            }
            @Override
            public void onFailure(Call<Product> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Server error, intente más tarde.", Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }

    private void addProduct(){
        product.setName(et_name.getText().toString());
        product.setDescription(et_description.getText().toString());
        product.setPrice(Double.parseDouble(et_price.getText().toString()));

        api.createProduct(product).enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if(response.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "Producto creado", Toast.LENGTH_LONG).show();
                    finish();
                }else{
                    Toast.makeText(getApplicationContext(), "Error al crear producto", Toast.LENGTH_LONG).show();
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

    private void delProduct(){

        api.deleteProduct(product.getId()).enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if(response.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "Producto eliminado", Toast.LENGTH_LONG).show();
                    ProductActivity.a.finish();
                    finish();
                }else{
                    Toast.makeText(getApplicationContext(), "Error al eliminar", Toast.LENGTH_LONG).show();
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
}
