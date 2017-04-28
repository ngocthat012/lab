package com.example.ngocthat.ketquaxoso;

import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.content.Context;

import java.io.IOException;
import java.security.cert.Extension;
import java.util.ArrayList;
import java.util.Iterator;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.RequestBody;


import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by ngocthat on 4/28/17.
 */

public class GetData {
    public  boolean isInternetConnection(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            return true;
        }
        else
            return false;
    }
    public JSONObject getData(){

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://thanhhungqb.tk:8080/kqxsmn")
                .build();
        try {
            Response response = client.newCall(request).execute();
            if(response.isSuccessful()) {
                String result = response.body().string();
                JSONObject obj = new JSONObject(result);
                return obj;
            }else {
                Log.i("Debug","Error");
            }

        }catch (Exception e){
            Log.i("Debug",e.toString());
        }
        return null;
    }
    public JSONArray getProvince(JSONObject data){
        return data.names();
    }

    public JSONArray getDaybyProvince(JSONObject data, String province){
        try {
            return (new JSONObject(data.getString(province))).names();
        }catch (Exception e){
            return null;
        }
    }

    public JSONObject getResultData(JSONObject data, String province, String day){
        try {
            return (new JSONObject(new JSONObject(data.getString(province)).getString(day)));
        }catch (Exception e){
            return null;
        }
    }
}
