package com.example.ngocthat.exchangerate;

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
import org.json.XML;


import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by My-PC on 3/13/2017.
 */

public class ExchangeRate {
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
    public JSONObject getExchaneRate(){
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://www.vietcombank.com.vn/exchangerates/ExrateXML.aspx")
                .build();
        try {
            Response response = client.newCall(request).execute();
            if(response.isSuccessful()) {
                String xml = response.body().string();
                JSONObject result = XML.toJSONObject(xml);
                Log.d("Test",result.toString());
                return result;
            }else {
                Log.d("Test","Loi");
            }

        }catch (Exception e){
            Log.d("Test",e.toString());
        }
        return null;
    }
    public String[] getDBshow(JSONObject data){
        ArrayList<String>result= new ArrayList<String>();
        result.add("Name");
        result.add("Buy");
        result.add("Sell");
        try {
        JSONObject json = (new JSONObject(data.getString("ExrateList").toString()));
        JSONArray arr = json.getJSONArray("Exrate");
            for (int i = 0; i < arr.length(); i++) {
                JSONObject json_data = arr.getJSONObject(i);
                if((!json_data.getString("Buy").toString().equals("0"))&& (!json_data.getString("Sell").toString().equals("0"))) {
                    result.add(json_data.getString("CurrencyName"));
                    result.add(json_data.getString("Buy"));
                    result.add(json_data.getString("Sell"));
                }
            }
        }catch(Exception e){
        }
        String[] res = new String[result.size()];
        res = result.toArray(res);
        return res;
    }
}
