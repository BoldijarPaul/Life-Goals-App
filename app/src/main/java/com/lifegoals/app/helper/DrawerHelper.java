package com.lifegoals.app.helper;

import android.app.Activity;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.DrawerLayout.DrawerListener;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;

import com.lifegoals.app.R;

public class DrawerHelper implements DrawerListener {
	/* this class will be used for adding drawer to activities */
	private DrawerLayout mDrawerLayout;
	private FrameLayout mContainer; /* here we are going to add the drawer view */
	private FrameLayout frameLayout;
	private Activity activity;

	public DrawerHelper(Activity context) {
		this.activity = context;
	}

	/*
	 * we are overriding the setContentView method in order to add the
	 * navigation drawer, this way we don't need to add the same drawer for
	 * every activity.
	 */

	public void setContentView(int layoutResID) {
		/* set up our drawers , first inflate the drawer layout */

		mDrawerLayout = (DrawerLayout) activity.getLayoutInflater().inflate(
				R.layout.laytout_drawer, null);
		frameLayout = (FrameLayout) mDrawerLayout
				.findViewById(R.id.drawer_frame);
		/*
		 * here we are 'merging' the drawer layout with the current layout of
		 * the activity
		 */
		activity.getLayoutInflater().inflate(layoutResID, frameLayout, true);
		/*
		 * now we keep an instance of the drawer fragment and set the drawer
		 * event listener so we can rotate the home icon while opening or
		 * closing the drawer
		 */
		mContainer = (FrameLayout) mDrawerLayout
				.findViewById(R.id.drawer_container);
		mDrawerLayout.setDrawerListener(this);
		activity.setContentView(mDrawerLayout);
	}

	public void setDrawerView(View view) {
		mContainer.addView(view);
	}

	public void closeDrawer() {
		mDrawerLayout.closeDrawer(Gravity.START);
	}

	public void openDrawer() {
		mDrawerLayout.openDrawer(Gravity.START);
	}

	public void switchDrawerState() {
		if (mDrawerLayout.isDrawerOpen(Gravity.START)) {
			closeDrawer();
		} else {
			openDrawer();
		}
	}

	@Override
	public void onDrawerClosed(View arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDrawerOpened(View arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDrawerSlide(View arg0, float arg1) {
		if (mContainer != null)
			mContainer.setAlpha(arg1);

	}

	@Override
	public void onDrawerStateChanged(int arg0) {
		// TODO Auto-generated method stub

	}
}
