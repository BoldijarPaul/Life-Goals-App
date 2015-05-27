package com.lifegoals.app.activities;

import java.util.List;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.lifegoals.app.R;
import com.lifegoals.app.adapters.owngoals.OwnGoalsAdapter;
import com.lifegoals.app.adapters.owngoals.OwnGoalsAdapterListener;
import com.lifegoals.app.client.management.ClientGoalManagement;
import com.lifegoals.app.entities.Goal;
import com.lifegoals.app.entities.User;
import com.lifegoals.app.helper.AsyncTaskHelper;
import com.lifegoals.app.helper.AsyncTaskHelper.AsyncMethods;
import com.lifegoals.app.helper.UIHelper;

public class YourAddedGoalsActivity extends AppActivity implements
		OwnGoalsAdapterListener {

	private User user;
	private RecyclerView mRecycler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_your_added_goals);
		setActionBarText("Your submited goals");

		user = (User) getIntent().getSerializableExtra("user");
		if (user == null) {
			finish();
			/* no user, there was an error probably */
		}
		loadViews();
		loadOwnGoals();
	}

	private void loadViews() {
		mRecycler = (RecyclerView) findViewById(R.id.activity_your_added_goals_recycler);
		final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
		layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
		mRecycler.setLayoutManager(layoutManager);
	}

	private void loadOwnGoals() {
		AsyncTaskHelper.create(new AsyncMethods<List<Goal>>() {

			@Override
			public List<Goal> doInBackground() {
				return ClientGoalManagement.getUserGoals(user);
			}

			@Override
			public void onDone(List<Goal> value, long ms) {
				if (value == null) {
					/* error */
					UIHelper.showCrouton("Error trying to get the goals!",
							YourAddedGoalsActivity.this);
				} else {

					gotSavedGoals(value);
				}

			}
		});
	}

	public void gotSavedGoals(List<Goal> goals) {
		mRecycler.setAdapter(new OwnGoalsAdapter(goals)
				.setOnSavedAdapterListener(this));

	}

	@Override
	public String getName() {
		return getClass().getCanonicalName();
	}

	@Override
	public void onDeleteOwnGoal(final Goal goal) {
		AsyncTaskHelper.create(new AsyncMethods<Goal>() {

			@Override
			public Goal doInBackground() {
				return ClientGoalManagement.deleteGoal(goal);
			}

			@Override
			public void onDone(Goal value, long ms) {
				deletedGoalResult(value);
			}
		});
	}

	protected void deletedGoalResult(Goal value) {
		if (value == null) {
			UIHelper.showCrouton("Goal was not deleted because of an error!",
					this);
			return;
		}
		UIHelper.showCrouton("Goal successfully deleted!!", this);
		loadOwnGoals();

	}

}
