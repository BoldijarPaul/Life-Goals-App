package com.lifegoals.app.adapters.owngoals;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.lifegoals.app.R;

public class OwnGoalsViewHolder extends RecyclerView.ViewHolder {

	public View icon;
	public TextView textView;
	public View root;
	public View delete;

	public OwnGoalsViewHolder(View itemView) {
		super(itemView);
		icon = itemView.findViewById(R.id.layout_small_own_goal_icon);
		textView = (TextView) itemView
				.findViewById(R.id.layout_small_own_goal_text);
		root = itemView.findViewById(R.id.layout_small_own_goal_root);
		delete = itemView.findViewById(R.id.layout_small_own_goal_delete);
	}

}
