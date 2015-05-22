package com.lifegoals.app.activities;

import java.util.Date;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lifegoals.app.R;
import com.lifegoals.app.R.drawable;
import com.lifegoals.app.R.id;
import com.lifegoals.app.R.layout;
import com.lifegoals.app.client.management.ClientSavedGoalManagement;
import com.lifegoals.app.client.management.ClientUserManagement;
import com.lifegoals.app.entities.SavedGoal;
import com.lifegoals.app.entities.User;
import com.lifegoals.app.helper.AsyncTaskHelper;
import com.lifegoals.app.helper.AsyncTaskHelper.AsyncMethods;
import com.lifegoals.app.helper.GsonHelper;
import com.lifegoals.app.ui.GoalView;
import com.lifegoals.app.ui.TextDrawable;

public class YourGoalInfoActivity extends AppActivity {

	private SavedGoal savedGoal;
	private GoalView mGoalView;
	private View mIcon;
	private TextView mGoalOwner;
	private TextView mTimeText;
	private View mShare, mChangeState, mDelete;

	private ImageView mDoneIcon;
	private TextView mDoneText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_your_goal_info);
		setActionBarText("Your saved goal info");
		savedGoal = GsonHelper.toObject(
				getIntent().getStringExtra("savedGoal"), SavedGoal.class);
		if (savedGoal == null) {
			finish();
			/* no saved goal, there was an error probably */
		}
		if (savedGoal.getGoal() == null) {
			finish();
			/* the saved goal doesn't have the Goal entity loaded */
		}

		loadViews();
		loadUserName();

	}

	private void loadUserName() {
		/* load the username for the owner of this goal */
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

	private void setSavedGoalState(boolean done) {
		boolean oldState = savedGoal.isDone();

		/* change the saved goal state, done or not done */
		/* first let's change the variable */
		savedGoal.setDone(done);
		/* now changing the top icon */
		mIcon.setBackgroundDrawable(TextDrawable.builder().buildRound(
				getIconText(savedGoal), savedGoal.getGoal().getColor()));

		/* now changing the button icon and text */
		if (savedGoal.isDone()) {
			mDoneText.setText("cancel");
			mDoneIcon.setImageResource(R.drawable.ic_cancel);
		} else {
			mDoneText.setText("done");
			mDoneIcon.setImageResource(R.drawable.ic_done);
		}

		/* done, now update the intent */
		getIntent().putExtra("savedGoal", GsonHelper.toString(savedGoal));

		// now try to sync with the server
		if (oldState == done) /* only try if the state was changed */
			return;
		AsyncTaskHelper.create(new AsyncMethods<SavedGoal>() {

			@Override
			public SavedGoal doInBackground() {
				return ClientSavedGoalManagement.updateSavedGoal(savedGoal);
			}

			@Override
			public void onDone(SavedGoal value, long ms) {
				if (value == null) {
					Toast.makeText(getApplicationContext(),
							"Could not sync data with the server!",
							Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(getApplicationContext(),
							"Successfully synced with the server!",
							Toast.LENGTH_SHORT).show();
				}
			}
		});

	}

	private void switchState() {
		setSavedGoalState(!savedGoal.isDone());
	}

	private void loadViews() {
		mGoalView = (GoalView) findViewById(R.id.activity_your_goal_info_goal);
		mIcon = findViewById(R.id.activity_your_goal_info_icon);
		mTimeText = (TextView) findViewById(R.id.activity_your_goal_info_date);
		mGoalOwner = (TextView) findViewById(R.id.activity_your_goal_info_user);
		mShare = findViewById(R.id.layout_goal_buttons_share);
		mChangeState = findViewById(R.id.layout_goal_buttons_change_state);
		mDelete = findViewById(R.id.layout_goal_buttons_delete);
		mDoneIcon = (ImageView) findViewById(R.id.layout_goal_buttons_done_icon);
		mDoneText = (TextView) findViewById(R.id.layout_goal_buttons_done_text);

		mGoalView.setText(savedGoal.getGoal().getText());
		mGoalView.setColor(savedGoal.getGoal().getColor());
		setSavedGoalState(savedGoal.isDone());
		mTimeText.setText(new Date(savedGoal.getCreatedDate()).toString());
		mChangeState.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				switchState();
			}
		});
		mDelete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				tryDeleting();
			}
		});
	}

	protected void tryDeleting() {
		/* trying to delete the current saved goal */
		AsyncTaskHelper.create(new AsyncMethods<SavedGoal>() {

			@Override
			public SavedGoal doInBackground() {
				return ClientSavedGoalManagement.deleteSavedGoal(savedGoal);
			}

			@Override
			public void onDone(SavedGoal value, long ms) {
				if (value == null) {
					// error, nothing deleted
					Toast.makeText(getApplicationContext(),
							"Your saved goal was not deleted, error!",
							Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(getApplicationContext(),
							"Saved goal deleted!", Toast.LENGTH_SHORT).show();
					finish();
				}
			}
		});

	}

}
