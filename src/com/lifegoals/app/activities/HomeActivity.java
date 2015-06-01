package com.lifegoals.app.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

import com.lifegoals.app.R;
import com.lifegoals.app.client.management.AppContext;
import com.lifegoals.app.client.management.ClientUserManagement;
import com.lifegoals.app.entities.LoginInfo;
import com.lifegoals.app.entities.LoginResult;
import com.lifegoals.app.helper.AsyncTaskHelper;
import com.lifegoals.app.helper.CryptHelper;
import com.lifegoals.app.helper.AsyncTaskHelper.AsyncMethods;
import com.lifegoals.app.helper.UIHelper;

public class HomeActivity extends Activity {

	private EditText mUsername, mPassword;
	private View mButton, mInputLayout, mProgressBar, mRegister;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		AppContext.setRoot("http://app-leaderboards.rhcloud.com/api/");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		loadViews();

	}

	private void loadViews() {
		mUsername = (EditText) findViewById(R.id.activity_home_username);
		mPassword = (EditText) findViewById(R.id.activity_home_password);
		mButton = findViewById(R.id.activity_home_login);
		mInputLayout = findViewById(R.id.activity_home_input_layout);
		mProgressBar = findViewById(R.id.activity_home_progressbar);
		mRegister = findViewById(R.id.activity_home_register);
		mRegister.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				registerClicked();
			}
		});
		mButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				loginClicked();
			}
		});
	}

	protected void registerClicked() {
		Intent intent = new Intent(this, RegisterActivity.class);
		startActivity(intent);

	}

	protected void loginClicked() {
		/* wanting to login */
		mInputLayout.setVisibility(View.GONE);
		mProgressBar.setVisibility(View.VISIBLE);
		AsyncTaskHelper.create(new AsyncMethods<LoginResult>() {

			@Override
			public LoginResult doInBackground() {
				LoginInfo loginInfo = new LoginInfo();
				loginInfo.setName(mUsername.getText().toString());
				String password = mPassword.getText().toString();
				password = CryptHelper.encrypt(password);
				loginInfo.setPassword(password);
				return ClientUserManagement.login(loginInfo);
			}

			@Override
			public void onDone(LoginResult value, long ms) {
				mInputLayout.setVisibility(View.VISIBLE);
				mProgressBar.setVisibility(View.GONE);
				if (value == null) {
					UIHelper.showCrouton("Error trying to login!",
							HomeActivity.this);
					return;
				}
				if (!value.isSuccess()) {
					UIHelper.showCrouton("Invalid username or password!",
							HomeActivity.this);
					return;
				}

				/* successful login */
				AppContext.getContext().setToken(value.getToken().getKey());

				/* start the next activity */
				Intent intent = new Intent(HomeActivity.this,
						YourSavedGoalsActivity.class);
				intent.putExtra("user", value.getUser());
				startActivity(intent);

			}
		});

	}
}
