package com.lifegoals.app.activities;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.lifegoals.app.R;
import com.lifegoals.app.adapters.goals.GoalAdapter;
import com.lifegoals.app.client.management.ClientGoalManagement;
import com.lifegoals.app.entities.Goal;
import com.lifegoals.app.helper.AsyncTaskHelper;
import com.lifegoals.app.helper.AsyncTaskHelper.AsyncMethods;
import com.lifegoals.app.helper.UIHelper;

public class AllGoalsActivity extends AppActivity {

	private RecyclerView mRecycler;
	private GoalAdapter mGoalAdapter;
	private List<Goal> mGoals;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_all_goals);
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
		mRecycler.setAdapter(mGoalAdapter);

	}

	@Override
	public String getName() {
		return getClass().getCanonicalName();
	}
}
