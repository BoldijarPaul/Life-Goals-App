package com.lifegoals.app.activities;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.lifegoals.app.R;
import com.lifegoals.app.adapters.savedgoals.SavedGoalAdapter;
import com.lifegoals.app.adapters.savedgoals.SavedGoalAdapterListener;
import com.lifegoals.app.client.management.ClientSavedGoalManagement;
import com.lifegoals.app.entities.SavedGoal;
import com.lifegoals.app.entities.User;
import com.lifegoals.app.helper.AsyncTaskHelper;
import com.lifegoals.app.helper.AsyncTaskHelper.AsyncMethods;
import com.lifegoals.app.helper.UIHelper;

public class YourSavedGoalsActivity extends AppActivity implements
		SavedGoalAdapterListener {

	private RecyclerView mRecycler;
	private User user;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_your_saved_goals);
		setActionBarText("Your saved goals");
		user = (User) getIntent().getSerializableExtra("user");
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

					UIHelper.showCrouton("Error trying to get the goals!",
							YourSavedGoalsActivity.this);
				} else {
					/*
					 * if a saved goal doens't have the Goal field as well ,
					 * return
					 */
					for (SavedGoal savedGoal : value) {
						if (savedGoal.getGoal() == null) {
							UIHelper.showCrouton(
									"Can't load your saved goals!",
									YourSavedGoalsActivity.this);
							return;
						}
					}
					/* everything is ok */
					gotSavedGoals(value);
				}

			}
		});
	}

	protected void gotSavedGoals(List<SavedGoal> value) {
		mRecycler.setAdapter(new SavedGoalAdapter(value)
				.setOnSavedAdapterListener(this));
	}

	@Override
	public void onSavedGoalClicked(SavedGoal savedGoal) {
		Intent intent = new Intent(this, YourSavedGoalInfoActivity.class);
		intent.putExtra("savedGoal", savedGoal);
		intent.putExtra("user", user);
		startActivityForResult(intent, 0);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 0 && resultCode == RESULT_OK) {
			loadSavedGoals();
		}
	}

	@Override
	public String getName() {
		return getClass().getCanonicalName();
	}
}
