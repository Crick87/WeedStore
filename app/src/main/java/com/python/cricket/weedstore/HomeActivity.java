package com.python.cricket.weedstore;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.python.cricket.weedstore.helpers.DataSQLiteOpenHelper;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {

    private CustomerFragment cust_fragment = new CustomerFragment();
    private ProductFragment prod_fragment = new ProductFragment();
    private AboutFragment abou_fragment = new AboutFragment();
    ViewPager mViewPager;
    BottomNavigationView navigation;
    Integer actualFragment = 0;

    @BindView(R.id.fab_home) FloatingActionButton fab_home;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        mViewPager = (ViewPager) findViewById(R.id.homeContainer);
        mViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                switch (position) {
                    case 0:
                        return cust_fragment;
                    case 1:
                        return prod_fragment;
                    case 2:
                        return abou_fragment;
                }
                return null;
            }

            @Override
            public int getCount() {
                return 3;
            }
        });
        navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                mViewPager.setCurrentItem(item.getOrder());
                return true;
            }
        });
        mViewPager.addOnPageChangeListener(this);

        fab_home.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                actualFragment = mViewPager.getCurrentItem();
                switch (actualFragment){
                    case 0:
                        Intent i0 = new Intent(getApplicationContext(), CustomerEditActivity.class);
                        i0.putExtra("customerID", "-1");
                        startActivity(i0);
                        break;
                    case 1:
                        Intent i1 = new Intent(getApplicationContext(), ProductEditActivity.class);
                        i1.putExtra("productID", "-1");
                        startActivity(i1);
                        break;
                    case 2:
                        //
                        break;
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.menu_logout:
                resetTokenInDB();
                // Start the Login activity
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.menu_about:
                AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this, R.style.AppTheme_AlertDialog);
                // Get the layout inflater
                LayoutInflater inflater = HomeActivity.this.getLayoutInflater();
                // Inflate and set the layout for the dialog
                // Pass null as the parent view because its going in the dialog layout
                builder.setView(inflater.inflate(R.layout.about, null))
                        // Add action buttons
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                //
                            }
                        }).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        navigation.getMenu().getItem(position).setChecked(true);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    // Reseteamos token in DB
    public void resetTokenInDB() {
        // Abrimos DB
        DataSQLiteOpenHelper admin = new DataSQLiteOpenHelper(this,
                "dataws", null, 1);
        SQLiteDatabase bd = admin.getWritableDatabase();

        Cursor fila = bd.rawQuery(
                "select token from DataWS where id=0", null);

        // update en la base de datos
        ContentValues registro = new ContentValues();
        registro.put("token", "$thisIsAnInvalidToken");
        if (fila.moveToFirst()) {
            bd.update("DataWS", registro, "id=0", null);
            bd.close();
        } else{
            registro.put("id", 0);
            bd.insert("DataWS", null, registro);
            bd.close();
        }
    }
}
