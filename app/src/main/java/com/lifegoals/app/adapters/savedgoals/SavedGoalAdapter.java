package com.lifegoals.app.adapters.savedgoals;

import java.util.List;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.lifegoals.app.R;
import com.lifegoals.app.entities.SavedGoal;
import com.lifegoals.app.helper.DrawableHelper;

public class SavedGoalAdapter extends RecyclerView.Adapter<SavedGoalViewHolder> {

	private List<SavedGoal> goals;
	private SavedGoalAdapterListener listener;

	public SavedGoalAdapter(List<SavedGoal> goals) {
		super();
		this.goals = goals;
	}

	public SavedGoalAdapter setOnSavedAdapterListener(
			SavedGoalAdapterListener listener) {
		this.listener = listener;
		return this;
	}

	@Override
	public int getItemCount() {
		return goals.size();
	}

	private String getIconText(SavedGoal savedGoal) {
		return savedGoal.isDone() ? "✔" : "✖";
	}

	@Override
	public void onBindViewHolder(SavedGoalViewHolder arg0, int arg1) {
		final SavedGoal goal = goals.get(arg1);
		arg0.textView.setText(goal.getGoal().getText());
		arg0.icon.setBackgroundDrawable(DrawableHelper.createTextDrawable(
				getIconText(goal), goal.getGoal().getColor()));
		arg0.root.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (listener != null) {
					listener.onSavedGoalClicked(goal);
				}
			}
		});
	}

	@Override
	public SavedGoalViewHolder onCreateViewHolder(ViewGroup arg0, int arg1) {
		View v = LayoutInflater.from(arg0.getContext()).inflate(
				R.layout.layout_small_goal, arg0, false);
		SavedGoalViewHolder vh = new SavedGoalViewHolder(v);

		return vh;
	}

}
