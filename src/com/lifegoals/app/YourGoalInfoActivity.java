package com.lifegoals.app;

import java.util.Date;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.lifegoals.app.activities.AppActivity;
import com.lifegoals.app.client.management.ClientUserManagement;
import com.lifegoals.app.entities.SavedGoal;
import com.lifegoals.app.entities.User;
import com.lifegoals.app.helper.AsyncTaskHelper;
import com.lifegoals.app.helper.AsyncTaskHelper.AsyncMethods;
import com.lifegoals.app.helper.GsonHelper;
import com.lifegoals.app.ui.TextDrawable;

public class YourGoalInfoActivity extends AppActivity {

	private SavedGoal savedGoal;
	private TextView mText;
	private View mRoot;
	private View mIcon;
	private TextView mGoalOwner;
	private TextView mTimeText;
	private View mShare, mChangeState, mDelete;

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

		loadViews();
		loadUserName();

	}

	private void loadUserName() {
		AsyncTaskHelper.create(new AsyncMethods<String>() {

			@Override
			public String doInBackground() {
				User user = new User();
				user.setId(savedGoal.getGoal().getUserId());
				return ClientUserManagement.getUsernameByUserId(user);
			}

			@Override
			public void onDone(String value, long ms) {
				mGoalOwner.setText(value);
			}
		});

	}

	private String getIconText(SavedGoal savedGoal) {
		return savedGoal.isDone() ? "✔" : "✖";
	}

	private void loadViews() {
		mText = (TextView) findViewById(R.id.activity_your_goal_info_text);
		mRoot = findViewById(R.id.activity_your_goal_info_root);
		mIcon = findViewById(R.id.activity_your_goal_info_icon);
		mTimeText = (TextView) findViewById(R.id.activity_your_goal_info_date);
		mGoalOwner = (TextView) findViewById(R.id.activity_your_goal_info_user);
		mShare = findViewById(R.id.layout_goal_buttons_share);
		mChangeState = findViewById(R.id.layout_goal_buttons_change_state);
		mDelete = findViewById(R.id.layout_goal_buttons_delete);

		mText.setText(savedGoal.getGoal().getText());
		mRoot.setBackgroundColor(savedGoal.getGoal().getColor());
		mIcon.setBackgroundDrawable(TextDrawable.builder().buildRound(
				getIconText(savedGoal), savedGoal.getGoal().getColor()));
		mTimeText.setText(new Date(savedGoal.getCreatedDate()).toString());
	}

}
