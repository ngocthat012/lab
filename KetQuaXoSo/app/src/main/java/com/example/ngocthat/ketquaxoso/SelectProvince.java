package com.example.ngocthat.ketquaxoso;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.content.res.TypedArrayUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link SelectProvince#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SelectProvince extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    MainActivity main;
    JSONObject json;;
    GetData data;
    ArrayList<String> citylist;
    ArrayList<String> daylist;
    Spinner spinnerDay;
    String city = "";
    String day = "";

    // TODO: Rename and change types of parameters
    private String mParam1;


    public SelectProvince() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment SelectProvince.
     */
    // TODO: Rename and change types and number of parameters
    public static SelectProvince newInstance(String param1) {
        SelectProvince fragment = new SelectProvince();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        main = (MainActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        RelativeLayout layout = (RelativeLayout) inflater.inflate(R.layout.fragment_select_province, null);
        Spinner spinnerProvince = (Spinner) layout.findViewById(R.id.city);
        spinnerDay = (Spinner) layout.findViewById(R.id.day);
        try {
            json = new JSONObject(getArguments().getString("data"));
            data = new GetData();
            JSONArray arrJson = data.getProvince(json);
            citylist = new ArrayList<String>();
            daylist = new ArrayList<>();

            for(int i = 0; i < arrJson.length();i++){
                citylist.add(arrJson.getString(i));
            }

            spinnerProvince
                    .setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,citylist));
            // Spinner on item click listener
            spinnerProvince
                    .setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                        @Override
                        public void onItemSelected(AdapterView<?> arg0,
                                                   View arg1, int position, long arg3) {
                            // TODO Auto-generated method stub
                            try {

                                city = arg0.getSelectedItem().toString();
                                daylist.clear();
                                JSONArray arrJsonDay = data.getDaybyProvince(json,city);
                                for(int i = 0; i < arrJsonDay.length();i++) {
                                    daylist.add(arrJsonDay.getString(i));
                                }
                                showDayView(daylist);

                            }catch (Exception e){

                            }

                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> arg0) {

                        }
                    });
            spinnerDay
                    .setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                        @Override
                        public void onItemSelected(AdapterView<?> arg0,
                                                   View arg1, int position, long arg3) {
                            // TODO Auto-generated method stub
                            try {

                                day = arg0.getSelectedItem().toString();
                                main.onMsgFromFragToMain(city, day);

                            }catch (Exception e){

                            }

                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> arg0) {

                        }
                    });

        }catch (Exception e){

        }
        return layout;
    }
    public void showDayView( ArrayList<String> arrayList){
        spinnerDay
                .setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,arrayList));

    }
    // TODO: Rename method, update argument and hook method into UI event
}
