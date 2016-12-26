package com.example.namsoo.s_riding_ui.fragment_menu;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.namsoo.s_riding_ui.R;

public class BodyInfoFragment extends Fragment {


    static BodyInfoFragment bodyInfoFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bodyinfo, container, false);
    }



    public static BodyInfoFragment newinstance() {
        if (bodyInfoFragment == null) {
            bodyInfoFragment = new BodyInfoFragment();
        }
        return bodyInfoFragment;

    }

}
