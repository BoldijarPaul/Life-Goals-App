package com.lifegoals.app.activities;

import android.app.Application;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.lifegoals.app.client.management.AppContext;
import com.lifegoals.app.client.management.ContextRequestListener;

public class AppApplication extends Application implements
		ContextRequestListener {
	@Override
	public void onCreate() {
		super.onCreate();
		AppContext.setOnRequestListener(this);
	}

	@Override
	public void onGetStatusCode(int code) {
		if (code == 401) {
			/* not authorized */
			showToast("Your session expired! Please login again!");
			Intent loginIntent = new Intent(this, HomeLoginActivity.class);
			loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
					| Intent.FLAG_ACTIVITY_CLEAR_TASK);
			startActivity(loginIntent);

		}

	}

	private void showToast(final String message) {
		new Handler(Looper.getMainLooper()).post(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(getApplicationContext(), message,
						Toast.LENGTH_SHORT).show();
			}
		});
	}
}
