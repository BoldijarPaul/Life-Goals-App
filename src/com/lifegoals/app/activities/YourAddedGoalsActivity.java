package com.lifegoals.app.activities;

import android.os.Bundle;

import com.lifegoals.app.R;
import com.lifegoals.app.entities.User;

public class YourAddedGoalsActivity extends AppActivity {

	private User user;

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
	}

	private void loadViews() {

	}

	@Override
	public String getName() {

		return getClass().getCanonicalName();
	}

}
