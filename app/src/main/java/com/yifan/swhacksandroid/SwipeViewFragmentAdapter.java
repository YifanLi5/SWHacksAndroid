package com.yifan.swhacksandroid;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentPagerAdapter;

import com.yifan.swhacksandroid.Fragments.FeedFragment;
import com.yifan.swhacksandroid.Fragments.FishStatusFragment;

/**
 * Created by Yifan on 3/10/2017.
 */

public class SwipeViewFragmentAdapter extends FragmentPagerAdapter {

    public SwipeViewFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return FeedFragment.getInstance();
            case 1:
                return FishStatusFragment.getInstance();
            default:
                throw  new UnsupportedOperationException("invalid position");

        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return "Feed";
            case 1:
                return "Monitoring";
            default:
                throw new UnsupportedOperationException("invalid position");
        }
    }
}
