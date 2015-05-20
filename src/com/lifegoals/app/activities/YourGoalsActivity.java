package com.lifegoals.app.activities;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.lifegoals.app.R;
import com.lifegoals.app.R.id;
import com.lifegoals.app.R.layout;
import com.lifegoals.app.R.menu;
import com.lifegoals.app.adapters.goals.SavedGoalAdapter;
import com.lifegoals.app.client.management.ClientSavedGoalManagement;
import com.lifegoals.app.entities.SavedGoal;
import com.lifegoals.app.entities.User;
import com.lifegoals.app.helper.AsyncTaskHelper;
import com.lifegoals.app.helper.AsyncTaskHelper.AsyncMethods;
import com.lifegoals.app.helper.GsonHelper;

public class YourGoalsActivity extends AppActivity {

	private RecyclerView mRecycler;
	private User user;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_your_goals);
		user = GsonHelper.toObject(getIntent().getStringExtra("user"),
				User.class);
		if (user == null) {
			finish();
			/* no user, there was an error probably */
		}
		loadViews();
		loadSavedGoals();

	}

	private void loadViews() {
		mRecycler = (RecyclerView) findViewById(R.id.activity_your_goals_recycler);
		final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
		layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
		mRecycler.setLayoutManager(layoutManager);

	}

	@Override
	public void onBackPressed() {
		Intent intent = new Intent(this, AddGoalActivity.class);
		intent.putExtra("user", GsonHelper.toString(user));
		startActivity(intent);
	}

	private void loadSavedGoals() {
		AsyncTaskHelper.create(new AsyncMethods<List<SavedGoal>>() {

			@Override
			public List<SavedGoal> doInBackground() {
				return ClientSavedGoalManagement.getUserSavedGoals(user);
			}

			@Override
			public void onDone(List<SavedGoal> value, long ms) {
				if (value == null) {
					/* error */
					Toast.makeText(getApplicationContext(),
							"Error trying to submit this goal!",
							Toast.LENGTH_SHORT).show();
				} else {
					gotSavedGoals(value);
				}

			}
		});
	}

	protected void gotSavedGoals(List<SavedGoal> value) {
		mRecycler.setAdapter(new SavedGoalAdapter(value));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.your_goals, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
