package com.yifan.swhacksandroid;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.yifan.swhacksandroid.Fragments.FeedFragment;
import com.yifan.swhacksandroid.Fragments.TemperatureFragment;

public class SwipeViewFragmentHolderActivity extends Activity {

    private boolean initialPoll = false;
    private static final String LOG_TAG = SwipeViewFragmentHolderActivity.class.getSimpleName();
    public static final int FEED_FRAGMENT_POSITION = 0;
    public static final int FISH_STATUS_FRAGMENT_POSITION = 1;
    private SwipeViewFragmentAdapter adapter;
    private DisableSwipeViewPager pager;
    private FirebaseDatabase fbdb;
    private DatabaseReference tempDbRef;
    private DatabaseReference statusDbRef;

    private static final String HTTPS_SWHACKSFIREBASE_FIREBASEIO_COM_TEMPERATURE = "https://swhacksfirebase.firebaseio.com/Temperature";
    private static final String HTTPS_SWHACKSFIREBASE_FIREBASEIO_COM_FEED_STATUS = "https://swhacksfirebase.firebaseio.com/FeedStatus/feed";

    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private ShakeDetector mShakeDetector;

    public void feedFish(){
        statusDbRef.setValue(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe_view_fragment_holder);
        adapter = new SwipeViewFragmentAdapter(getFragmentManager(), 0, "NULL");
        pager = (DisableSwipeViewPager) findViewById(R.id.pager);
        pager.setAdapter(adapter);

        fbdb = FirebaseDatabase.getInstance();
        tempDbRef = fbdb.getReferenceFromUrl(HTTPS_SWHACKSFIREBASE_FIREBASEIO_COM_TEMPERATURE);
        statusDbRef = fbdb.getReferenceFromUrl(HTTPS_SWHACKSFIREBASE_FIREBASEIO_COM_FEED_STATUS);
        tempDbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (adapter.getItem(FISH_STATUS_FRAGMENT_POSITION) instanceof TemperatureFragment) {
                    Double temperature = (Double) dataSnapshot.child("value").getValue();
                    String time = (String) dataSnapshot.child("time").getValue();
                    Log.i(LOG_TAG, "temp: " + temperature + "\ntime: " + time);
                    TemperatureFragment f = (TemperatureFragment) adapter.getRegistedFragment(FISH_STATUS_FRAGMENT_POSITION);
                    f.updateTemperatureAndTime(temperature, time);
                } else {
                    throw new UnsupportedOperationException("wrong fragment returned");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Failed to read value
                Log.w(LOG_TAG, "Failed to read value.", databaseError.toException());
            }
        });

        statusDbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (adapter.getItem(FEED_FRAGMENT_POSITION) instanceof FeedFragment) {
                    try{
                        Boolean feed = (Boolean) dataSnapshot.getValue();
                        if(feed && initialPoll){ //if I flipped to true ignore, if flip to false then feed has been acknowledged
                            //have fed fish
                            String lastFeedTime = (String) dataSnapshot.getValue();
                            FeedFragment f = (FeedFragment) adapter.getRegistedFragment(FEED_FRAGMENT_POSITION);
                            f.setLastFedTV(lastFeedTime);
                            Toast.makeText(getApplicationContext(), "Fish Fed", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            initialPoll = true;
                        }
                    }
                    catch (NullPointerException|ClassCastException e){
                        Log.e(LOG_TAG, e.toString());
                    }



                } else {
                    throw new UnsupportedOperationException("wrong fragment returned");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Failed to read value
                Log.w(LOG_TAG, "Failed to read value.", databaseError.toException());
            }
        });

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager
                .getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mShakeDetector = new ShakeDetector();
        mShakeDetector.setOnShakeListener(new ShakeDetector.OnShakeListener() {

            @Override
            public void onShake(int count) {
				/*
				 * The following method, "handleShakeEvent(count):" is a stub //
				 * method you would use to setup whatever you want done once the
				 * device has been shook.
				 */
                handleShakeEvent(count);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        // Add the following line to register the Session Manager Listener onResume
        mSensorManager.registerListener(mShakeDetector, mAccelerometer,	SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    public void onPause() {
        // Add the following line to unregister the Sensor Manager onPause
        mSensorManager.unregisterListener(mShakeDetector);
        super.onPause();
    }

    private void handleShakeEvent(int count){
        feedFish();
    }

    public void setSwipe(boolean b){
        pager.setPagingEnabled(b);
    }
}



