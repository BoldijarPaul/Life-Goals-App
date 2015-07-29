package com.lifegoals.app.feed.activities;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;

import com.lifegoals.app.R;
import com.lifegoals.app.facebookfanpageapi.helper.ApiPreferences;
import com.lifegoals.app.facebookfanpageapi.helper.GlobalStrings;
import com.lifegoals.app.facebookfanpageapi.helper.cache.BitmapCacheHelper;
import com.lifegoals.app.feed.fragments.DrawerFragment;

/**
 * Created by Paul on 4/12/2015.
 */
public class BaseAppActivity extends ActionBarActivity implements DrawerLayout.DrawerListener {

    private DrawerLayout mDrawerLayout;
    private DrawerFragment mDrawerFragment;
    private FrameLayout frameLayout;

    /* we are overriding the setContentView method in order to
       add the navigation drawer, this way we don't need to add the same drawer for every
       activity. */
    @Override
    public void setContentView(int layoutResID) {
        /* set up our drawers , first inflate the drawer layout */
        mDrawerLayout = (DrawerLayout) getLayoutInflater().inflate(R.layout.drawer, null);
        frameLayout = (FrameLayout) mDrawerLayout.findViewById(R.id.drawer_frame);
        /* here we are 'merging' the drawer layout with the current layout of the activity */
        getLayoutInflater().inflate(layoutResID, frameLayout, true);
        /* now we keep an instance of the drawer fragment and set the drawer event listener
        so we can rotate the home icon while opening or closing the drawer */
        mDrawerFragment = (DrawerFragment) getSupportFragmentManager().findFragmentById(R.id.drawer_fragment);
        mDrawerLayout.setDrawerListener(this);
        super.setContentView(mDrawerLayout);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /* in the on create method initialize everything we need in our whole app */
        BitmapCacheHelper.initialize(this);
        ApiPreferences.initialize(this);
        GlobalStrings.initialize(this);

    }

    /* this method should be called after everything in the onCreate() ,
    it will set up the home click  event*/
    public void initialize() {
    /* event for clicking the home icon, if we have one */
        if (getHomeIcon() != null) {
            getHomeIcon().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    homeClicked();
                }
            });
        }
    }

    /* this method will open / close the drawer */
    private void homeClicked() {
        if (mDrawerLayout.isDrawerOpen(Gravity.START)) {
            mDrawerLayout.closeDrawer(Gravity.START);
        } else {
            mDrawerLayout.openDrawer(Gravity.START);
        }
    }

    /* here we are going to make a simple animation with the drawer
    1) changing the transparency of the drawer while sliding
    2) rotating the home icon while sliding
     */
    @Override
    public void onDrawerSlide(View drawerView, float slideOffset) {
        if (mDrawerFragment != null)
            mDrawerFragment.setAlpha(slideOffset);

        if (getHomeIcon() != null) {
            /* method only available for API 11 + */
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                getHomeIcon().setRotation(slideOffset * 180);
            }
        }
    }

    /* this method will return the home icon. we should override this one in our activity
    and return the home icon */
    public View getHomeIcon() {
        return null;
    }

    /* now the 3 other methods from the DrawerListener interface that we don't need. */

    @Override
    public void onDrawerOpened(View drawerView) {

    }

    @Override
    public void onDrawerClosed(View drawerView) {

    }

    @Override
    public void onDrawerStateChanged(int newState) {

    }
}
