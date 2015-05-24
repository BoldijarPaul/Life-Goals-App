package com.lifegoals.app.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lifegoals.app.R;
import com.lifegoals.app.helper.DrawerHelper;

public class AppActivity extends Activity {

	private DrawerHelper drawerHelper;
	private View drawerView;
	private View mAddGoal;
	private View mViewSavedGoals;
	private View mLogout;
	private View mSearchGoals;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		 
		getActionBar().setIcon(R.drawable.ic_menu);
		getActionBar().setDisplayShowTitleEnabled(false);
		getActionBar().setHomeButtonEnabled(true);
		getActionBar().setDisplayShowCustomEnabled(true);
		drawerHelper = new DrawerHelper(this);

		drawerView = getLayoutInflater().inflate(
				R.layout.layout_drawer_container, null);
		mAddGoal = drawerView
				.findViewById(R.id.layout_drawer_container_add_goal);
		mViewSavedGoals = drawerView
				.findViewById(R.id.layout_drawer_container_view_saved_goal);
		mSearchGoals = drawerView
				.findViewById(R.id.layout_drawer_container_search_goals);
		mLogout = drawerView.findViewById(R.id.layout_drawer_container_logout);
		/* events */

		mAddGoal.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				changeActivity(AddGoalActivity.class);
			}
		});

		mLogout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				changeActivity(HomeActivity.class);
			}
		});
		mViewSavedGoals.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				changeActivity(YourSavedGoalsActivity.class);
			}
		});
		mSearchGoals.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				changeActivity(AllGoalsActivity.class);
			}
		});
	}

	protected <T> void changeActivity(Class<T> class1) {
		Intent intent = new Intent(this, class1);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
				| Intent.FLAG_ACTIVITY_CLEAR_TASK);
		intent.putExtra("user", getIntent().getSerializableExtra("user"));
		startActivity(intent);
		drawerHelper.closeDrawer();
	}

	@Override
	public void onBackPressed() {
		if (isTaskRoot()) {
			Intent intent = new Intent(this, HomeActivity.class);
			startActivity(intent);
			finish();
			return;
		}
		super.onBackPressed();
	}

	@Override
	public void setContentView(int layoutResID) {
		drawerHelper.setContentView(layoutResID);
		drawerHelper.setDrawerView(drawerView);
	}

	public void setActionBarText(String text) {
		LinearLayout layout = (LinearLayout) getLayoutInflater().inflate(
				R.layout.layout_action_bar_text, null);
		TextView textView = (TextView) layout
				.findViewById(R.id.layout_action_bar_text);
		textView.setText(text);
		getActionBar().setCustomView(layout);
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			drawerHelper.switchDrawerState();
		}
		return super.onMenuItemSelected(featureId, item);
	}
}
