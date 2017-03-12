package com.yifan.swhacksandroid;

import android.app.Activity;
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
    private static final String HTTPS_SWHACKSFIREBASE_FIREBASEIO_COM_FEED_STATUS = "https://swhacksfirebase.firebaseio.com/FeedStatus";

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
                    Long temperature = (Long) dataSnapshot.child("value").getValue();
                    String time = (String) dataSnapshot.child("time").getValue();
                    Log.i(LOG_TAG, "temp: " + temperature + "\ntime: " + time);
                    TemperatureFragment f = (TemperatureFragment) adapter.getRegistedFragment(FISH_STATUS_FRAGMENT_POSITION);
                    f.updateTemperatureAndTime(temperature.doubleValue(), time);
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
        statusDbRef = fbdb.getReferenceFromUrl(HTTPS_SWHACKSFIREBASE_FIREBASEIO_COM_FEED_STATUS);
        statusDbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (adapter.getItem(FEED_FRAGMENT_POSITION) instanceof FeedFragment) {
                    try{
                        Boolean feed = (Boolean) dataSnapshot.child("feed").getValue();
                        if(!feed && initialPoll){ //if I flipped to true ignore, if flip to false then feed has been acknowledged
                            //have fed fish
                            String lastFeedTime = (String) dataSnapshot.child("last_feed_time").getValue();
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
    }

    public void setSwipe(boolean b){
        pager.setPagingEnabled(b);
    }
}



