package com.lifegoals.app.adapters.goals;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.lifegoals.app.R;
import com.lifegoals.app.ui.GoalView;

public class GoalViewHolder extends RecyclerView.ViewHolder {

	public GoalView goalView;

	public GoalViewHolder(View itemView) {
		super(itemView);
		goalView = (GoalView) itemView.findViewById(R.id.item_goal_goalview);

	}

}
