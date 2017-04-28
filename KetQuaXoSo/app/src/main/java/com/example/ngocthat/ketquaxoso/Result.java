package com.example.ngocthat.ketquaxoso;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link Result#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Result extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String city, day;
    JSONObject json;;
    GetData data;
    ArrayList<String> resultlist;
    ExpandableHeightGridView result;
    TextView text;



    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment Result.
     */
    // TODO: Rename and change types and number of parameters
    public static Result newInstance(String param1) {
        Result fragment = new Result();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        RelativeLayout layout = (RelativeLayout) inflater.inflate(R.layout.fragment_result, container, false);
        text = (TextView) layout.findViewById(R.id.txtResult);
        result = (ExpandableHeightGridView) layout.findViewById(R.id.gridview);
        try {
            json = new JSONObject(getArguments().getString("data"));
            data = new GetData();
            text.setText("Kết quả xổ số đài " + city +" ngày " + day );
            JSONObject resultjson = data.getResultData(json, city, day);
            resultlist = new ArrayList<>();
            resultlist.clear();
            resultlist.add("Giải tám");
            resultlist.add(resultjson.getString("8").replace("\"","").replace("[","").replace("]","").replace(","," - "));
            resultlist.add("Giải bảy");
            resultlist.add(resultjson.getString("7").replace("\"","").replace("[","").replace("]","").replace(","," - "));
            resultlist.add("Giải sáu");
            resultlist.add(resultjson.getString("6").replace("\"","").replace("[","").replace("]","").replace(","," - "));
            resultlist.add("Giải năm");
            resultlist.add(resultjson.getString("5").replace("\"","").replace("[","").replace("]","").replace(","," - "));
            resultlist.add("Giải tư");
            resultlist.add(resultjson.getString("4").replace("\"","").replace("[","").replace("]","").replace(","," - "));
            resultlist.add("Giải ba");
            resultlist.add(resultjson.getString("3").replace("\"","").replace("[","").replace("]","").replace(","," - "));
            resultlist.add("Giải nhì");
            resultlist.add(resultjson.getString("2").replace("\"","").replace("[","").replace("]","").replace(","," - "));
            resultlist.add("Giải nhất");
            resultlist.add(resultjson.getString("1").replace("\"","").replace("[","").replace("]","").replace(","," - "));
            resultlist.add("Giải đặc biệt");
            resultlist.add(resultjson.getString("DB").replace("\"","").replace("[","").replace("]","").replace(","," - "));

            result.setNumColumns(2);
            result.setAdapter(new ArrayAdapter<String>
                            (getActivity(), R.layout.layout_item, resultlist));
            result.setExpanded(true);

        }catch (Exception e){

        }
        return layout;
    }
    public void onMsgFromMainToFragment(String city, String day) {
        this.city = city;
        this.day = day;
    }

}
