package com.example.ngocthat.ketquaxoso;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    FragmentTransaction ft;
    SelectProvince seProvince;
    Result result;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        GetData data = new GetData();

        if(data.isInternetConnection(getBaseContext())){
            JSONObject json = data.getData();
            Bundle bundle = new Bundle();
            bundle.putString("data",json.toString());
            ft = getFragmentManager().beginTransaction();
            seProvince = SelectProvince.newInstance("province");

            seProvince.setArguments(bundle);
            ft.replace(R.id.fragmentData, seProvince);
            ft.commit();

            result = new Result();

            ft = getFragmentManager().beginTransaction();
            result = result.newInstance("first-right");
            result.setArguments(bundle);
            ft.replace(R.id.fragmentResult, result);
            ft.commit();

        }else{
            startActivity(new Intent(MainActivity.this,Pop.class));
        }


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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void onMsgFromFragToMain(String city, String day) {
        try {
            // forward blue-data to redFragment using its callback method
            ft = getFragmentManager().beginTransaction();
            result.onMsgFromMainToFragment(city, day);
            ft.detach(result);
            ft.attach(result);
            ft.commit();
        } catch (Exception e) {
            Log.e("ERROR", "onStrFromFragToMain " + e.getMessage());
        }

    }
}
