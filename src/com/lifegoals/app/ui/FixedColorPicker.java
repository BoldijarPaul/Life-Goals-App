package com.lifegoals.app.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.LinearLayout;

import com.lifegoals.app.R;

public class FixedColorPicker extends LinearLayout {

	private ColorClickListener colorListener;
	private Animation mRotateAnimation;
	private int mLastColor = 0;
	private int[] colors;

	public int getColor() {
		return mLastColor;
	}

	public FixedColorPicker(Context context, AttributeSet attrs) {
		super(context, attrs);
		if (!isInEditMode()) {
			init();
		}
	}

	public FixedColorPicker(Context context, AttributeSet attrs,
			int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		if (!isInEditMode()) {
			init();
		}
	}

	public FixedColorPicker(Context context) {
		super(context);
		if (!isInEditMode()) {
			init();
		}
	}

	public void setOnColorListener(ColorClickListener colorListener) {
		this.colorListener = colorListener;
	}

	/* this method will initialize the layout settings */
	private void init() {
		setOrientation(LinearLayout.HORIZONTAL);
		mRotateAnimation = new RotateAnimation(0, 180,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		mRotateAnimation.setDuration(300);

	}

	public void addColors(int... colors) {
		this.colors = colors;
		mLastColor = colors[0];
		for (final int color : colors) {
			View child = new View(getContext());
			child.setBackgroundColor(color);
			addView(child);
			child.getLayoutParams().height = (int) getContext().getResources()
					.getDimension(R.dimen.color_box_size);
			child.getLayoutParams().width = (int) getContext().getResources()
					.getDimension(R.dimen.color_box_size);
			MarginLayoutParams params = (MarginLayoutParams) child
					.getLayoutParams();
			params.setMargins(
					(int) getContext().getResources().getDimension(
							R.dimen.color_box_margin), 0, 0, 0);
			child.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					v.startAnimation(mRotateAnimation);
					mLastColor = color;
					if (colorListener != null) {
						colorListener.onClick(v, color);
					}
				}
			});
		}
	}

	public static interface ColorClickListener {
		public void onClick(View view, int color);
	}

}
