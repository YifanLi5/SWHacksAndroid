package com.yifan.swhacksandroid.Fragments;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yifan.swhacksandroid.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class FishStatusFragment extends Fragment {


    public FishStatusFragment() {
        // Required empty public constructor
    }

    public static FishStatusFragment getInstance(){
        return new FishStatusFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fish_status, container, false);
    }

}
