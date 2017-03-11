package com.yifan.swhacksandroid.Fragments;


import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yifan.swhacksandroid.R;
import com.yifan.swhacksandroid.SwipeViewFragmentHolderActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class FishStatusFragment extends Fragment {

    private static final String LOG_TAG = FishStatusFragment.class.getSimpleName();
    private static final String TEMP_C = "TEMP_C";
    private static final String TIME = "TIME";
    private TextView celsiusTV;
    private TextView fahrenheitTV;
    private TextView timeTV;
    private View rootView;

    public FishStatusFragment() {
        // Required empty public constructor
    }

    public static FishStatusFragment getInstance(double celsius, String time){
        FishStatusFragment f = new FishStatusFragment();
        Bundle args = new Bundle();
        args.putDouble(TEMP_C, celsius);
        args.putString(TIME, time);

       f.setArguments(args);
        return f;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_fish_status, container, false);
        celsiusTV = (TextView) rootView.findViewById(R.id.celsius_tv);
        fahrenheitTV = (TextView) rootView.findViewById(R.id.fahrenheit_tv);
        timeTV = (TextView) rootView.findViewById(R.id.time_tv);
        Bundle b = getArguments();
        updateTemperatureAndTime(b.getDouble(TEMP_C), b.getString(TIME));

        return rootView;
    }
    public void updateTemperatureAndTime(final double celsius, final String time){ //GIVE CELSIUS, NOT FAHRENHEIT
        double fahrenheit = celsius * 9/5 + 32;
        String celsiusStr = String.valueOf(celsius) + " °C";
        String fahrenheitStr = String.valueOf(fahrenheit) + " °F";

        Log.d(LOG_TAG, "flag1");

        if(celsiusTV  != null && fahrenheitTV != null && timeTV != null){
            celsiusTV.setText(celsiusStr);
            fahrenheitTV.setText(fahrenheitStr);
            timeTV.setText(time);
        }

    }



}
