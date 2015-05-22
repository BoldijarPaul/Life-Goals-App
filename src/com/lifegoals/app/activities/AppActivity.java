package com.lifegoals.app.activities;

import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lifegoals.app.R;

public class AppActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getActionBar().setIcon(R.drawable.ic_menu);
		getActionBar().setDisplayShowTitleEnabled(false);
		getActionBar().setHomeButtonEnabled(true);
		getActionBar().setDisplayShowCustomEnabled(true);

	}

	public void setActionBarText(String text) {
		LinearLayout layout = (LinearLayout) getLayoutInflater().inflate(
				R.layout.layout_action_bar_text, null);
		TextView textView = (TextView) layout
				.findViewById(R.id.layout_action_bar_text);
		textView.setText(text);
		getActionBar().setCustomView(layout);
	}
}
