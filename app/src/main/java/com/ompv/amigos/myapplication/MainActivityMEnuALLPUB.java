package com.ompv.amigos.myapplication;

/**
 * Created by yessine on 13/03/17.
 */


import android.support.v4.app.Fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;

import java.util.ArrayList;
import java.util.List;

public class MainActivityMEnuALLPUB extends Fragment {



    private List<DataClassAtt> dataList;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       // View rootView = inflater.inflate(R.layout.fragment_main_activity_menuALLPUB, container, false);
        // TextView textView = (TextView) rootView.findViewById(R.id.section_label);
        //  textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
        //return rootView;
        String tabjson="[{\"firstName\" : \"yessine\", \"email\" : \"yessine@gmail.com\",\"image\":\"https://upload.wikimedia.org/wikipedia/commons/6/63/African_elephant_warning_raised_trunk.jpg\"},{\"firstName\" : \"morsi\", \"email\" : \"morsi@gmail.com\",\"image\":\"https://upload.wikimedia.org/wikipedia/commons/6/63/African_elephant_warning_raised_trunk.jpg\"},{\"firstName\" : \"zied\", \"email\" : \"zied@gmail.com\",\"image\":\"https://upload.wikimedia.org/wikipedia/commons/6/63/African_elephant_warning_raised_trunk.jpg\"}]";
        View rootView = inflater.inflate(R.layout.taballpub, container, false);

        RecyclerView rv = (RecyclerView) rootView.findViewById(R.id.rv_recycler_view);
        rv.setHasFixedSize(true);

        dataList=new ArrayList<>();
        try {
            JSONArray array=new JSONArray(tabjson.toString());

            for (int i=0;i<array.length();i++)
            {
                JSONObject object = array.getJSONObject(i);
                //Uri urlimage= Uri.parse(object.getString("image")) ;
                DataClassAtt data =new DataClassAtt(object.getString("firstName"),object.getString("email"),object.getString("image") );
                dataList.add(data);
            }
        } catch (JSONException e) {
            e.  printStackTrace();
        }


        MyAdapter adapter = new MyAdapter(dataList);
        rv.setAdapter(adapter);



        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(llm);

        return rootView;
    }




}
