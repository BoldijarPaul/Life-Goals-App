package com.lifegoals.app.ui;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lifegoals.app.R;

public class GoalView extends FrameLayout {

	/* the background and text of the goal */
	private View mBackground;
	private TextView mText;

	public GoalView(Context context) {
		super(context);
		if (!isInEditMode()) {
			initializeView();
		}

	}

	public GoalView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		if (!isInEditMode()) {
			initializeView();
		}

	}

	public GoalView(Context context, AttributeSet attrs) {
		super(context, attrs);
		if (!isInEditMode()) {
			initializeView();
		}
	}

	private void initializeView() {
		mBackground = inflate(getContext(), R.layout.layout_goal, null);
		mText = (TextView) mBackground.findViewById(R.id.layout_goal_text);
		addView(mBackground);
		mBackground.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				longClickedBackground();
				return false;
			}
		});
	}

	protected void longClickedBackground() {
		/* here we want to copy text to clipboard */
		ClipboardManager clipboard = (ClipboardManager) getContext()
				.getSystemService(Context.CLIPBOARD_SERVICE);
		ClipData clip = ClipData.newPlainText(mText.getText(), mText.getText());
		clipboard.setPrimaryClip(clip);
		/* show a toast */
		Toast.makeText(getContext(), "Goal text copied!", Toast.LENGTH_SHORT)
				.show();
		// TODO string resources

	}

	public void setText(String text) {
		mText.setText(text);
	}

	public void setColor(int color) {
		mBackground.setBackgroundColor(color);
	}

}
