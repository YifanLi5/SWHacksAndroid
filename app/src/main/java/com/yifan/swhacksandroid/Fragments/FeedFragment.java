package com.yifan.swhacksandroid.Fragments;


import android.app.Fragment;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yifan.swhacksandroid.R;


public class FeedFragment extends Fragment {


    public FeedFragment() {
        // Required empty public constructor
    }

    public static FeedFragment getInstance(){
        return new FeedFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_feed, container, false);
    }

}
