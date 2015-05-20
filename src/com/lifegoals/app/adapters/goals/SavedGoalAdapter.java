package com.lifegoals.app.adapters.goals;

import java.util.List;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lifegoals.app.R;
import com.lifegoals.app.entities.Goal;
import com.lifegoals.app.entities.SavedGoal;
import com.lifegoals.app.helper.DrawableHelper;
import com.lifegoals.app.ui.TextDrawable;

public class SavedGoalAdapter extends RecyclerView.Adapter<SavedGoalViewHolder> {

	private List<SavedGoal> goals;

	public SavedGoalAdapter(List<SavedGoal> goals) {
		super();
		this.goals = goals;
	}

	@Override
	public int getItemCount() {
		// TODO Auto-generated method stub
		return goals.size();
	}

	private String getIconText(SavedGoal savedGoal) {
		return savedGoal.isDone() ? "✔" : "";
	}

	@Override
	public void onBindViewHolder(SavedGoalViewHolder arg0, int arg1) {
		SavedGoal goal = goals.get(arg1);
		arg0.textView.setText(goal.getGoal().getText());
		arg0.icon.setBackgroundDrawable(DrawableHelper.createTextDrawable(
				getIconText(goal), goal.getGoal().getColor()));
	}

	@Override
	public SavedGoalViewHolder onCreateViewHolder(ViewGroup arg0, int arg1) {
		View v = LayoutInflater.from(arg0.getContext()).inflate(
				R.layout.layout_small_goal, arg0, false);
		SavedGoalViewHolder vh = new SavedGoalViewHolder(v);

		return vh;
	}

}
