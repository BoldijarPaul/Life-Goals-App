package com.lifegoals.app.activities;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.lifegoals.app.R;
import com.lifegoals.app.adapters.goals.GoalAdapter;
import com.lifegoals.app.adapters.goals.GoalViewHolder.GoalViewHolderListener;
import com.lifegoals.app.client.management.ClientGoalManagement;
import com.lifegoals.app.client.management.ClientSavedGoalManagement;
import com.lifegoals.app.entities.Goal;
import com.lifegoals.app.entities.SavedGoal;
import com.lifegoals.app.entities.User;
import com.lifegoals.app.helper.AsyncTaskHelper;
import com.lifegoals.app.helper.AsyncTaskHelper.AsyncMethods;
import com.lifegoals.app.helper.UIHelper;

public class AllGoalsActivity extends AppActivity implements
		GoalViewHolderListener {

	private RecyclerView mRecycler;
	private GoalAdapter mGoalAdapter;
	private List<Goal> mGoals;
	private User user;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_all_goals);

		user = (User) getIntent().getSerializableExtra("user");

		if (user == null) {
			finish();
			/* no user, there was an error probably */

		}
		setActionBarText("Find new goals");
		loadViews();
		loadGoals();

	}

	private void loadGoals() {
		AsyncTaskHelper.create(new AsyncMethods<List<Goal>>() {

			@Override
			public List<Goal> doInBackground() {
				return ClientGoalManagement.getAllGoals();
			}

			@Override
			public void onDone(List<Goal> value, long ms) {
				gotGoals(value);
			}
		});
	}

	protected void gotGoals(List<Goal> value) {
		if (value == null) {
			UIHelper.showCrouton("Can't get the goals from the server!",
					AllGoalsActivity.this);
		}
		mGoals = value;
		mGoalAdapter.setGoals(mGoals);
		mGoalAdapter.notifyDataSetChanged();

	}

	private void loadViews() {
		mRecycler = (RecyclerView) findViewById(R.id.activity_all_goals_recycler);
		LinearLayoutManager layoutManager = new LinearLayoutManager(this,
				LinearLayoutManager.HORIZONTAL, false);
		mRecycler.setLayoutManager(layoutManager);
		mGoals = new ArrayList<Goal>();
		mGoalAdapter = new GoalAdapter(mGoals);
		mGoalAdapter.setListener(this);
		mRecycler.setAdapter(mGoalAdapter);

	}

	@Override
	public String getName() {
		return getClass().getCanonicalName();
	}

	/* this is called when a goal is clicked */
	@Override
	public void onSaveClicked(Goal goal) {
		final SavedGoal savedGoal = new SavedGoal();
		savedGoal.setUserId(user.getId());
		savedGoal.setGoalId(goal.getId());
		savedGoal.setDone(false);

		AsyncTaskHelper.create(new AsyncMethods<SavedGoal>() {

			@Override
			public SavedGoal doInBackground() {
				return ClientSavedGoalManagement.addSavedGoal(savedGoal);
			}

			@Override
			public void onDone(SavedGoal value, long ms) {
				if (value == null) {
					UIHelper.showCrouton("Goal can't be saved!",
							AllGoalsActivity.this);
				} else {
					UIHelper.showCrouton("Goal saved!", AllGoalsActivity.this);
				}

			}
		});

	}

	@Override
	public void onShareClicked(Goal goal) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onInfoClicked(Goal goal) {
		// TODO Auto-generated method stub

	}
}
