package com.lifegoals.app.helper;

import java.util.HashMap;

import com.lifegoals.app.ui.TextDrawable;

public class DrawableHelper {

	private static HashMap<String, TextDrawable> drawables = new HashMap<String, TextDrawable>();

	public static TextDrawable createTextDrawable(String text, int color) {
		TextDrawable drawable = drawables.get(color + text);
		if (drawable != null)
			return drawable;

		/* create the drawable */
		drawable = TextDrawable.builder().buildRound(text, color);
		drawables.put(color + text, drawable);
		return drawable;
	}
}
