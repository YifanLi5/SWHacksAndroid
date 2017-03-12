package com.yifan.swhacksandroid.Fragments;


import android.app.Fragment;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.yifan.swhacksandroid.R;
import com.yifan.swhacksandroid.SwipeViewFragmentHolderActivity;

import pl.droidsonroids.gif.GifImageButton;
import pl.droidsonroids.gif.GifImageView;


public class FeedFragment extends Fragment {
    private View mRootView;
    private static final String LOG_TAG = FeedFragment.class.getSimpleName();

    private float mPokeballOriginalX = -1, mPokeballOriginalY = -1;
    private int mWindowWidth, mWindowHeight;

    private ViewFlipper mFlipper;
    //page magikarp
    private TextView mLastFedTV;
    private ImageView mPokeballImageView;
    private GifImageView mMagikarpGifImageView;

    //page evolution
    private GifImageButton mGyaradosGifImageButton;


    public FeedFragment() {
        // Required empty public constructor
    }

    public static FeedFragment getInstance() {
        return new FeedFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mRootView = inflater.inflate(R.layout.fragment_feed, container, false);
        mFlipper = (ViewFlipper) mRootView.findViewById(R.id.flipper);
        mMagikarpGifImageView = (GifImageView) mRootView.findViewById(R.id.magikarp_gtf);
        mGyaradosGifImageButton = (GifImageButton) mRootView.findViewById(R.id.gyarados_evo_gif);
        mPokeballImageView = (ImageView) mRootView.findViewById(R.id.food_iv);
        mLastFedTV = (TextView) mRootView.findViewById(R.id.last_feed_time);
        mPokeballImageView.bringToFront();
        mPokeballImageView.setOnTouchListener(new OnTouchListener());

        mPokeballOriginalX = mPokeballImageView.getLeft();
        mPokeballOriginalY = mPokeballImageView.getTop();
        mFlipper.setDisplayedChild(0);

        mGyaradosGifImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPokeballImageView.setX(mPokeballOriginalX);
                mPokeballImageView.setY(mPokeballOriginalY);
                mFlipper.setDisplayedChild(0); //on click go back to first page
            }
        });

        return mRootView;
    }

    private void triggerFishFeed() {
        // Restore to original position
        mPokeballImageView.setX(mPokeballOriginalX);
        mPokeballImageView.setY(mPokeballOriginalY);
        mFlipper.setDisplayedChild(1);
        Log.i(LOG_TAG, "attempt to feed");
        if(getActivity() instanceof SwipeViewFragmentHolderActivity){
            ((SwipeViewFragmentHolderActivity) getActivity()).feedFish();
        }
    }

    public void setLastFedTV(String s){
        mLastFedTV.setText(s);
    }


    float xOffset, yOffset;

    private class OnTouchListener implements View.OnTouchListener {

        private final int DISTANCE_THRESHOLD = 400;

        @Override
        public boolean onTouch(View view, MotionEvent event) {
            switch (event.getActionMasked()) {
                case MotionEvent.ACTION_DOWN:

                    if(getActivity() instanceof SwipeViewFragmentHolderActivity){
                        ((SwipeViewFragmentHolderActivity) getActivity()).setSwipe(false);
                    }
                    // Set original pokeball position if not set
                    // (jank solution, idk how else to do it)
                    if (mPokeballOriginalX <= 0 && mPokeballOriginalY <= 0) {
                        mPokeballOriginalX = view.getX();
                        mPokeballOriginalY = view.getY();
                    }

                    xOffset = view.getX() - event.getRawX();
                    yOffset = view.getY() - event.getRawY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    view.animate().x(Math.max(0, event.getRawX() + xOffset)).y(Math.max(0, event.getRawY() + yOffset)).setDuration(0).start();
                    break;
                case MotionEvent.ACTION_UP:
                    if(getActivity() instanceof SwipeViewFragmentHolderActivity){
                        ((SwipeViewFragmentHolderActivity) getActivity()).setSwipe(true);
                    }
                    if (pokeballMagikarpDist() < DISTANCE_THRESHOLD) {
                        triggerFishFeed();
                    }
                    break;
            }
            return true;
        }

        private double pokeballMagikarpDist() {
            float magikarpCenterX = mMagikarpGifImageView.getX() + mMagikarpGifImageView.getWidth()/2;
            float magikarpCenterY = mMagikarpGifImageView.getY() + mMagikarpGifImageView.getHeight()/2;
            float pokeballCenterX = mPokeballImageView.getX() + mPokeballImageView.getWidth()/2;
            float pokeballCenterY = mPokeballImageView.getY() + mPokeballImageView.getHeight()/2;

            // Euclidean distance
            return Math.pow(Math.pow(magikarpCenterX - pokeballCenterX, 2) +
                    Math.pow(magikarpCenterY - pokeballCenterY, 2), 0.5);
        }
    }


}
