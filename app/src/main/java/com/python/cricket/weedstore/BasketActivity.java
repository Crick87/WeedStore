package com.python.cricket.weedstore;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BasketActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    @BindView(R.id.customer_select) Spinner customer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basket);
        ButterKnife.bind(this);

        customer.setOnItemSelectedListener(this);

        // TODO: set customer list:
        String[] letra = {"A","B","C","D","E"};
        customer.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, letra));

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        Toast.makeText(getBaseContext(), "Position "+position, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
