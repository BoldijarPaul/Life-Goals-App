package com.lifegoals.app.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

import com.lifegoals.app.R;
import com.lifegoals.app.client.management.ClientUserManagement;
import com.lifegoals.app.entities.RegisterResponse;
import com.lifegoals.app.entities.RegisterState;
import com.lifegoals.app.entities.User;
import com.lifegoals.app.helper.AsyncTaskHelper;
import com.lifegoals.app.helper.AsyncTaskHelper.AsyncMethods;
import com.lifegoals.app.helper.UIHelper;

public class RegisterActivity extends Activity {
	private EditText mUsername, mPassword, mEmail;
	private View mButton, mInputLayout, mProgressBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		loadViews();
	}

	/* load the activity views */
	private void loadViews() {
		mUsername = (EditText) findViewById(R.id.activity_register_username);
		mPassword = (EditText) findViewById(R.id.activity_register_password);
		mEmail = (EditText) findViewById(R.id.activity_register_email);
		mButton = findViewById(R.id.activity_register_register);
		mInputLayout = findViewById(R.id.activity_register_input_layout);
		mProgressBar = findViewById(R.id.activity_register_progressbar);
		mButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				registerClicked();

			}
		});
	}

	/* called when we click the register button */
	protected void registerClicked() {
		mInputLayout.setVisibility(View.GONE);
		mProgressBar.setVisibility(View.VISIBLE);

		/* the user that we want to create */
		final User user = new User();
		user.setName(mUsername.getText().toString());
		user.setPassword(mPassword.getText().toString());
		user.setEmail(mEmail.getText().toString());

		AsyncTaskHelper.create(new AsyncMethods<RegisterResponse>() {

			@Override
			public RegisterResponse doInBackground() {
				return ClientUserManagement.addUser(user);
			}

			@Override
			public void onDone(RegisterResponse value, long ms) {
				mInputLayout.setVisibility(View.VISIBLE);
				mProgressBar.setVisibility(View.GONE);
				gotRegisterResponse(value);

			}
		});

	}

	protected void gotRegisterResponse(RegisterResponse value) {
		if (value == null) {
			// TODO strings resources
			UIHelper.showCrouton("Connection error", this);
			return;
		}
		if (value.isSuccess()) {
			UIHelper.showCrouton("Register successful!", this);
			finish();
			return;
		}
		String errorText = "Please check your register data!";
		if (value.getState() == RegisterState.EMAIL_TAKEN) {
			errorText = "The email is already in use!";
		}
		if (value.getState() == RegisterState.USERNAME_TAKEN) {
			errorText = "The username is already in use!";
		}
		UIHelper.showCrouton(errorText, this);
	}

}
