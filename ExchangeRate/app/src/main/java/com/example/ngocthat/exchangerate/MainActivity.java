package com.example.ngocthat.exchangerate;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    ArrayList<String> countrylist;
    ArrayList<Exchange> country;
    JSONArray jsonarray;
    Double buy;
    Double sell;
    Double inputToCountry = 0.0;
    Double inputToVND = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ExchangeRate exRate = new ExchangeRate();
        String[] data = {};
        JSONObject json = null;
        if(exRate.isInternetConnection(getBaseContext())) {
            data = exRate.getDBshow(exRate.getExchaneRate());
            json = exRate.getExchaneRate();
            SharedPreferences settings = getSharedPreferences("Data", MODE_PRIVATE);
            SharedPreferences.Editor editor = settings.edit();
            editor.putString("data", json.toString());
            editor.commit();
        }else{
            startActivity(new Intent(MainActivity.this,Pop.class));
            SharedPreferences pre = getSharedPreferences ("Data",MODE_PRIVATE);
            try {
                String result = pre.getString("data","");
                if(result != "") {
                    json = new JSONObject(result);
                    data = exRate.getDBshow(json);
                }else {
                    startActivity(new Intent(MainActivity.this,Pop.class));
                }

            }catch (Exception e){

            }

        }

        ArrayAdapter<String> da=new ArrayAdapter<String>
                (
                        this,
                        R.layout.layout_item,
                        data
                );
        ExpandableHeightGridView gridView = (ExpandableHeightGridView )findViewById(R.id.gridview);

        gridView.setNumColumns(3);
        gridView.setAdapter(da);
        gridView.setExpanded(true);

        country = new ArrayList<Exchange>();
        countrylist = new ArrayList<String>();

        try {
            json = (new JSONObject(json.getString("ExrateList").toString()));
            jsonarray = json.getJSONArray("Exrate");
            for (int i = 0; i < jsonarray.length(); i++) {
                JSONObject json_data = jsonarray.getJSONObject(i);
                if(json_data.getString("Buy").toString() != "0" && json_data.getString("Sell").toString() != "0") {
                    Exchange ex = new Exchange();
                    ex.setCountry(json_data.getString("CurrencyName"));
                    ex.setBuy(Double.parseDouble(json_data.getString("Buy")));
                    ex.setSell(Double.parseDouble(json_data.getString("Sell")));
                    country.add(ex);

                    countrylist.add(json_data.optString("CurrencyName"));
                }
            }
        }catch(Exception e){
        }
        //Convert VND to Country
        Spinner SpinnerCountry1 = (Spinner) findViewById(R.id.toConvert);
        SpinnerCountry1
                .setAdapter(new ArrayAdapter<String>(MainActivity.this,
                        android.R.layout.simple_spinner_dropdown_item,
                        countrylist));

        // Spinner on item click listener
        SpinnerCountry1
                .setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> arg0,
                                               View arg1, int position, long arg3) {
                        // TODO Auto-generated method stub
                        try {
                            sell = country.get(position).getSell();
                            if (inputToCountry != 0.0) {
                                Double result = inputToCountry / sell;
                                EditText edit = (EditText) findViewById(R.id.outputNumber);

                                edit.setText(result.toString());
                            }
                        }catch (Exception e){
                            startActivity(new Intent(MainActivity.this,Pop.class));
                        }

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {

                    }
                });
        EditText inputnumber = (EditText) findViewById(R.id.inputNumber);
        inputnumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {


                    EditText edit = (EditText) findViewById(R.id.outputNumber);
                    if(s.toString().length()!=0) {
                        inputToCountry = Double.parseDouble((s.toString()));
                        Double result = Double.parseDouble((s.toString())) / sell;
                        edit.setText(result.toString());
                    }else{
                        edit.setText("");
                    }
                }catch (Exception e){
                    startActivity(new Intent(MainActivity.this,Pop.class));
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString().length()!=0) {
                    inputToCountry = Double.parseDouble((s.toString()));
                }else{
                    inputToCountry=0.0;
                }
            }
        });

        //Convert Country to VND
        Spinner SpinnerCountry2 = (Spinner) findViewById(R.id.outConvert);
        SpinnerCountry2
                .setAdapter(new ArrayAdapter<String>(MainActivity.this,
                        android.R.layout.simple_spinner_dropdown_item,
                        countrylist));

        // Spinner on item click listener
        SpinnerCountry2
                .setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> arg0,
                                               View arg1, int position, long arg3) {
                        try {


                            // TODO Auto-generated method stub
                            buy = country.get(position).getBuy();
                            if (inputToVND != 0.0) {
                                Double result = buy * inputToVND;
                                EditText edit = (EditText) findViewById(R.id.outputNumber2);

                                edit.setText(result.toString());
                            }
                        }catch (Exception e){
                            startActivity(new Intent(MainActivity.this,Pop.class));
                        }

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {

                    }
                });

        EditText inputnumber2 = (EditText) findViewById(R.id.inputNumber2);
        inputnumber2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    EditText edit = (EditText) findViewById(R.id.outputNumber2);
                    if (s.toString().length() != 0) {
                        inputToVND = Double.parseDouble((s.toString()));
                        Double result = buy * inputToVND;
                        edit.setText(result.toString());
                    } else {

                        edit.setText("");
                    }
                }catch (Exception e){
                    startActivity(new Intent(MainActivity.this,Pop.class));
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString().length()!=0) {
                    inputToVND = Double.parseDouble((s.toString()));
                }else  inputToVND=0.0;
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
