package com.yifan.swhacksandroid;

import android.app.Activity;
import android.app.Fragment;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.yifan.swhacksandroid.Fragments.FeedFragment;
import com.yifan.swhacksandroid.Fragments.FishStatusFragment;

public class SwipeViewFragmentHolderActivity extends Activity {

    private static final String LOG_TAG = SwipeViewFragmentHolderActivity.class.getSimpleName();
    public static final int FEED_FRAGMENT_POSITION = 0;
    public static final int FISH_STATUS_FRAGMENT_POSITION = 1;
    private SwipeViewFragmentAdapter adapter;
    private DisableSwipeViewPager pager;
    private FirebaseDatabase fbdb;
    private DatabaseReference dbRef;

    private static final String TO_ANDROID_LISTEN_URL = "https://swhacksfirebase.firebaseio.com/ToAndroid";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe_view_fragment_holder);
        adapter = new SwipeViewFragmentAdapter(getFragmentManager(), 0, "NULL");
        pager = (DisableSwipeViewPager) findViewById(R.id.pager);
        pager.setAdapter(adapter);

        fbdb = FirebaseDatabase.getInstance();
        dbRef = fbdb.getReferenceFromUrl(TO_ANDROID_LISTEN_URL);

        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (adapter.getItem(FISH_STATUS_FRAGMENT_POSITION) instanceof FishStatusFragment) {
                    Long temperature = (Long) dataSnapshot.child("Temperature").getValue();
                    String time = (String) dataSnapshot.child("Time").getValue();
                    Log.i(LOG_TAG, "temp: " + temperature + "\ntime: " + time);
                    FishStatusFragment f = (FishStatusFragment) adapter.getRegistedFragment(FISH_STATUS_FRAGMENT_POSITION);
                    // f.updateTemperatureAndTime(temperature.doubleValue(), time);
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
    }

    public void setSwipe(boolean b){
        pager.setPagingEnabled(b);
    }
}



