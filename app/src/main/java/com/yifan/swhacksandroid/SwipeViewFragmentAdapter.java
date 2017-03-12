package com.yifan.swhacksandroid;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentPagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

import com.yifan.swhacksandroid.Fragments.FeedFragment;
import com.yifan.swhacksandroid.Fragments.TemperatureFragment;

/**
 * Created by Yifan on 3/10/2017.
 */

public class SwipeViewFragmentAdapter extends FragmentPagerAdapter {

    private double tempC;
    private String time;
    private SparseArray<Fragment> createdFragments = new SparseArray<>();

    public SwipeViewFragmentAdapter(FragmentManager fm, double tempC, String time) {
        super(fm);
        this.tempC = tempC;
        this.time = time;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case SwipeViewFragmentHolderActivity.FEED_FRAGMENT_POSITION:
                return FeedFragment.getInstance();
            case SwipeViewFragmentHolderActivity.FISH_STATUS_FRAGMENT_POSITION:
                return TemperatureFragment.getInstance(tempC, time);
            default:
                throw  new UnsupportedOperationException("invalid position");

        }
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment f = (Fragment) super.instantiateItem(container, position);
        createdFragments.put(position, f);
        return f;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        createdFragments.remove(position);
        super.destroyItem(container, position, object);
    }

    public Fragment getRegistedFragment(int position){
        return createdFragments.get(position);
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case SwipeViewFragmentHolderActivity.FEED_FRAGMENT_POSITION:
                return "Feed";
            case SwipeViewFragmentHolderActivity.FISH_STATUS_FRAGMENT_POSITION:
                return "Status";
            default:
                throw new UnsupportedOperationException("invalid position");
        }
    }
}
