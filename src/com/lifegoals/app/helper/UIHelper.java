package com.lifegoals.app.helper;

import android.view.View;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.LinearLayout;

import com.lifegoals.app.R;

public class UIHelper {

	/* adds color boxes to a horizontal scroll view */
	public static void fillScrollViewWithColorBlocks(LinearLayout linearLayout,
			int... colors) {
		int colorIndex = 0;
		for (int color : colors) {
			View child = new View(linearLayout.getContext());
			child.setBackgroundColor(color);
			linearLayout.addView(child);
			child.getLayoutParams().height = (int) linearLayout.getContext()
					.getResources().getDimension(R.dimen.color_box_size);
			child.getLayoutParams().width = (int) linearLayout.getContext()
					.getResources().getDimension(R.dimen.color_box_size);
			if (colorIndex > 0) {
				MarginLayoutParams params = (MarginLayoutParams) child
						.getLayoutParams();
				params.setMargins((int) linearLayout.getContext()
						.getResources().getDimension(R.dimen.color_box_margin),
						0, 0, 0);
			}
			colorIndex++;
		}

	}
}
