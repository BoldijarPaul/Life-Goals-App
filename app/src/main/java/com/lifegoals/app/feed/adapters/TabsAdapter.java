package com.lifegoals.app.feed.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by Paul on 4/18/2015.
 */
public class TabsAdapter extends FragmentPagerAdapter {

    private Fragment[] fragments;


    public TabsAdapter(FragmentManager fm, Fragment feedFragment, Fragment favoriteFragment) {
        super(fm);
        fragments = new Fragment[]{feedFragment, favoriteFragment};
    }

    @Override
    public Fragment getItem(int position) {
        return fragments[position];
    }

    @Override
    public int getCount() {
        return fragments.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0) return "HOME";
        return "FAVORITE";
    }
}
