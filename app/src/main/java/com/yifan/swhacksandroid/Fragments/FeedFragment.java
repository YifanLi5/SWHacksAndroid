package com.yifan.swhacksandroid.Fragments;


import android.app.Fragment;
import android.graphics.Point;
import android.os.Bundle;

import android.view.Display;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.Toast;

import com.yifan.swhacksandroid.R;

import pl.droidsonroids.gif.GifTextView;


public class FeedFragment extends Fragment {

    private ImageView mPokeballImageView;
    private GifTextView mMagikarpGifTextView;
    private float mPokeballOriginalX = -1, mPokeballOriginalY = -1;
    private View mRootView;
    private int mWindowWidth, mWindowHeight;

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
        mRootView = inflater.inflate(R.layout.fragment_feed, container, false);
        mMagikarpGifTextView = (GifTextView) mRootView.findViewById(R.id.magikarp_gtf);
        mPokeballImageView = (ImageView) mRootView.findViewById(R.id.pokeball_iv);
        mPokeballImageView.bringToFront();
        mPokeballImageView.setOnTouchListener(new OnTouchListener());

        mPokeballOriginalX = mPokeballImageView.getLeft();
        mPokeballOriginalY = mPokeballImageView.getTop();

        return mRootView;
    }

    private void triggerFishFeed() {
        // Restore to original position
        mPokeballImageView.setX(mPokeballOriginalX);
        mPokeballImageView.setY(mPokeballOriginalY);

        Toast.makeText(getActivity(), "Fish has been fed!", Toast.LENGTH_SHORT).show();
    }

    float xOffset, yOffset;

    private class OnTouchListener implements View.OnTouchListener {

        private final int DISTANCE_THRESHOLD = 400;

        @Override
        public boolean onTouch(View view, MotionEvent event) {
            switch (event.getActionMasked()) {
                case MotionEvent.ACTION_DOWN:
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
                    view.animate().x(event.getRawX() + xOffset).y(event.getRawY() + yOffset).setDuration(0).start();
                    break;
                case MotionEvent.ACTION_UP:
                    if (pokeballMagikarpDist() < DISTANCE_THRESHOLD) {
                        triggerFishFeed();
                    }
                    break;
            }
            return true;
        }

        private double pokeballMagikarpDist() {
            float magikarpCenterX = mMagikarpGifTextView.getX() + mMagikarpGifTextView.getWidth()/2;
            float magikarpCenterY = mMagikarpGifTextView.getY() + mMagikarpGifTextView.getHeight()/2;
            float pokeballCenterX = mPokeballImageView.getX() + mPokeballImageView.getWidth()/2;
            float pokeballCenterY = mPokeballImageView.getY() + mPokeballImageView.getHeight()/2;

            // Euclidean distance
            return Math.pow(Math.pow(magikarpCenterX - pokeballCenterX, 2) +
                    Math.pow(magikarpCenterY - pokeballCenterY, 2), 0.5);
        }
    }
}
