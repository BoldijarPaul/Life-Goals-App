package com.lifegoals.app.adapters.goals;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.lifegoals.app.R;

public class SavedGoalViewHolder extends RecyclerView.ViewHolder {

	public View icon;
	public TextView textView;
	public View root;

	public SavedGoalViewHolder(View itemView) {
		super(itemView);
		icon = itemView.findViewById(R.id.layout_small_goal_icon);
		textView = (TextView) itemView
				.findViewById(R.id.layout_small_goal_text);
		root = itemView.findViewById(R.id.layout_small_goal_root);
	}

}
