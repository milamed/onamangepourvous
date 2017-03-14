package com.ompv.amigos.myapplication;

/**
 * Created by yessine on 13/03/17.
 */


import android.support.v4.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MainActivityMEnuALLPUB extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       // View rootView = inflater.inflate(R.layout.fragment_main_activity_menuALLPUB, container, false);
        // TextView textView = (TextView) rootView.findViewById(R.id.section_label);
        //  textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
        //return rootView;
        return inflater.inflate(R.layout.taballpub, container, false);
    }

}
