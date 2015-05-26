package com.lifegoals.app.adapters.goals;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.View.OnClickListener;

import com.lifegoals.app.R;
import com.lifegoals.app.entities.Goal;
import com.lifegoals.app.ui.GoalView;

public class GoalViewHolder extends RecyclerView.ViewHolder {

	public GoalView goalView;
	public View mSave, mShare, mInfo;

	public GoalViewHolder(View itemView) {
		super(itemView);

		goalView = (GoalView) itemView.findViewById(R.id.item_goal_goalview);
		mSave = itemView.findViewById(R.id.item_goal_save);
		mShare = itemView.findViewById(R.id.item_goal_share);
		mInfo = itemView.findViewById(R.id.item_goal_info);

	}

	public static interface GoalViewHolderListener {
		public void onSaveClicked(Goal goal);

		public void onShareClicked(Goal goal);

		public void onInfoClicked(Goal goal);
	}

}
