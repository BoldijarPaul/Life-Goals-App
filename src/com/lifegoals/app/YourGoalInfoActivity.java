package com.lifegoals.app;

import android.app.Activity;
import android.os.Bundle;

import com.lifegoals.app.entities.SavedGoal;
import com.lifegoals.app.helper.GsonHelper;

public class YourGoalInfoActivity extends Activity {

	private SavedGoal savedGoal;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_your_goal_info);

		savedGoal = GsonHelper.toObject(
				getIntent().getStringExtra("savedGoal"), SavedGoal.class);
		if (savedGoal == null) {
			finish();
			/* no saved goal, there was an error probably */
		}
	}

}
