package com.lifegoals.app.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.lifegoals.app.R;
import com.lifegoals.app.client.locator.context.AppContext;
import com.lifegoals.app.client.management.ClientUserManagement;
import com.lifegoals.app.entities.LoginInfo;
import com.lifegoals.app.entities.LoginResult;
import com.lifegoals.app.helper.AsyncTaskHelper;
import com.lifegoals.app.helper.AsyncTaskHelper.AsyncMethods;

public class HomeActivity extends Activity {

	private EditText mUsername, mPassword;
	private View mButton, mInputLayout, mProgressBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
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
		mButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				loginClicked();
			}
		});
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
				loginInfo.setPassword(mPassword.getText().toString());
				return ClientUserManagement.login(loginInfo);
			}

			@Override
			public void onDone(LoginResult value, long ms) {
				mInputLayout.setVisibility(View.VISIBLE);
				mProgressBar.setVisibility(View.GONE);
				if (value == null) {
					Toast.makeText(getApplicationContext(),
							"Error trying to login", Toast.LENGTH_SHORT).show();
					return;
				}
				if (!value.isSuccess()) {
					Toast.makeText(getApplicationContext(),
							"Invalid username or password", Toast.LENGTH_SHORT)
							.show();
					return;
				}

				/* successful login */
				AppContext.getContext().setToken(value.getToken().getKey());
				Toast.makeText(getApplicationContext(), "Login successful!",
						Toast.LENGTH_SHORT).show();

			}
		});

	}
}
