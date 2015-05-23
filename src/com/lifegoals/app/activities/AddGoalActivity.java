package com.lifegoals.app.activities;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.widget.EditText;
import android.widget.Toast;

import com.lifegoals.app.R;
import com.lifegoals.app.client.management.ClientGoalManagement;
import com.lifegoals.app.entities.Goal;
import com.lifegoals.app.entities.User;
import com.lifegoals.app.helper.AsyncTaskHelper;
import com.lifegoals.app.helper.AsyncTaskHelper.AsyncMethods;
import com.lifegoals.app.helper.GsonHelper;
import com.lifegoals.app.ui.FixedColorPicker;
import com.lifegoals.app.ui.FixedColorPicker.ColorClickListener;
import com.lifegoals.app.ui.GoalView;

public class AddGoalActivity extends AppActivity {

	private FixedColorPicker mColorBoxes;
	private View mColorPreview;
	private View mPublic, mPrivate;
	private EditText mText;
	private View mSubmit;
	private View mProgress;
	private GoalView mGoalView;
	private View mPreviewGoalButton;

	private boolean goalIsPublic = true;
	private boolean showingPreview = false;
	private User user;

	private Animation mScaleUpAnimation, mScaleDownAnimation,
			mSlideUpAnimation, mSlideDownAnimation;

	private final int[] mPostColors = new int[] { 0xffe74c3c, 0xff2980b9,
			0xff16a085, 0xffd35400, 0xff9b59b6, 0xffe67e22 };

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_goal);
		setActionBarText("Add a new goal");
		loadViews();

		if (savedInstanceState != null) {
			goalIsPublic = savedInstanceState.getBoolean("goalIsPublic", true);
		}
		user = GsonHelper.toObject(getIntent().getStringExtra("user"),
				User.class);

		if (user == null) {
			finish();
			/* no user, there was an error probably */

		}
		postTypeChanged(goalIsPublic);
	}

	private void setupAnimations() {
		mScaleDownAnimation = new ScaleAnimation(1f, 0.5f, 1f, 0.5f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		mScaleDownAnimation.setDuration(500);
		mScaleDownAnimation.setFillAfter(true);
		mScaleUpAnimation = new ScaleAnimation(0.5f, 1f, 0.5f, 1f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		mScaleUpAnimation.setDuration(500);
		mScaleUpAnimation.setFillAfter(true);
		mSlideDownAnimation = AnimationUtils.loadAnimation(
				getApplicationContext(), R.anim.slide_down);
		mSlideUpAnimation = AnimationUtils.loadAnimation(
				getApplicationContext(), R.anim.slide_up);
		mSlideUpAnimation.setDuration(500);
		mSlideUpAnimation.setFillAfter(true);
		mSlideDownAnimation.setDuration(500);
		mSlideDownAnimation.setFillAfter(true);

	}

	private void loadViews() {
		setupAnimations();
		mPreviewGoalButton = findViewById(R.id.activity_add_goal_preview_button);
		mGoalView = (GoalView) findViewById(R.id.activity_add_goal_preview);
		mColorBoxes = (FixedColorPicker) findViewById(R.id.activity_add_goal_colors);
		mColorPreview = findViewById(R.id.activity_add_goal_color_preview);
		mPublic = findViewById(R.id.activity_add_goal_public);
		mPrivate = findViewById(R.id.activity_add_goal_private);
		mSubmit = findViewById(R.id.activity_add_goal_submit);
		mProgress = findViewById(R.id.activity_add_goal_progress);
		mText = (EditText) findViewById(R.id.activity_add_goal_text);
		mText.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				mGoalView.setText(s.toString());

			}
		});
		mSubmit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				submitClicked();

			}
		});
		mColorPreview.setBackgroundColor(mPostColors[0]);
		mGoalView.setColor(mPostColors[0]);
		mColorBoxes.addColors(mPostColors);
		mPreviewGoalButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				switchPreviewState();

			}
		});
		mColorBoxes.setOnColorListener(new ColorClickListener() {

			@Override
			public void onClick(View view, int color) {
				mColorPreview.setBackgroundColor(color);
				mGoalView.setColor(color);

			}
		});
		mPublic.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				postTypeChanged(true);

			}
		});
		mPrivate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				postTypeChanged(false);

			}
		});
	}

	/* this method will show the current goal, or hide it */
	protected void switchPreviewState() {

		if (mGoalView.getVisibility() == View.GONE) {
			mGoalView.setVisibility(View.VISIBLE);
		}
		showingPreview = !showingPreview;
		if (showingPreview) {
			mGoalView.startAnimation(mSlideDownAnimation);
		} else {
			mSlideUpAnimation.setAnimationListener(new AnimationListener() {

				@Override
				public void onAnimationStart(Animation animation) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onAnimationRepeat(Animation animation) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onAnimationEnd(Animation animation) {
					mGoalView.setVisibility(View.GONE);
				}
			});
		}
		mGoalView.startAnimation(showingPreview ? mSlideDownAnimation
				: mSlideUpAnimation);

	}

	protected void submitClicked() {
		/* we want to submit a new goal */
		final Goal goal = new Goal();
		goal.setColor(mColorBoxes.getColor());
		goal.setText(mText.getText().toString());
		goal.setUserId(user.getId());
		goal.setPublicGoal(goalIsPublic);

		mSubmit.setVisibility(View.GONE);
		mProgress.setVisibility(View.VISIBLE);

		AsyncTaskHelper.create(new AsyncMethods<Goal>() {

			@Override
			public Goal doInBackground() {
				return ClientGoalManagement.addGoal(goal);
			}

			@Override
			public void onDone(Goal value, long ms) {
				mSubmit.setVisibility(View.VISIBLE);
				mProgress.setVisibility(View.GONE);
				if (value == null) {
					/* error */
					Toast.makeText(getApplicationContext(),
							"Error trying to submit this goal!",
							Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(getApplicationContext(),
							"Goal successfully added!", Toast.LENGTH_SHORT)
							.show();
					mText.setText(null);
				}
			}
		});
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putBoolean("goalIsPublic", goalIsPublic);
	}

	protected void postTypeChanged(boolean isPublic) {
		goalIsPublic = isPublic;

		if (isPublic) {

			mPublic.startAnimation(mScaleUpAnimation);
			mPrivate.startAnimation(mScaleDownAnimation);

		} else {

			mPublic.startAnimation(mScaleDownAnimation);
			mPrivate.startAnimation(mScaleUpAnimation);

		}

	}
}
