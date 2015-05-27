package com.lifegoals.app.adapters.owngoals;

import java.util.List;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.lifegoals.app.R;
import com.lifegoals.app.entities.Goal;
import com.lifegoals.app.helper.DrawableHelper;

public class OwnGoalsAdapter extends RecyclerView.Adapter<OwnGoalsViewHolder> {

	private List<Goal> goals;
	private OwnGoalsAdapterListener listener;

	public OwnGoalsAdapter(List<Goal> goals) {
		super();
		this.goals = goals;
	}

	public OwnGoalsAdapter setOnSavedAdapterListener(
			OwnGoalsAdapterListener listener) {
		this.listener = listener;
		return this;
	}

	@Override
	public int getItemCount() {
		return goals.size();
	}

	@Override
	public void onBindViewHolder(OwnGoalsViewHolder arg0, int arg1) {
		final Goal goal = goals.get(arg1);
		arg0.textView.setText(goal.getText());
		arg0.icon.setBackgroundDrawable(DrawableHelper.createTextDrawable("",
				goal.getColor()));
		arg0.delete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (listener != null) {
					/* start the delete goal event */
					listener.onDeleteOwnGoal(goal);
				}
			}
		});
	}

	@Override
	public OwnGoalsViewHolder onCreateViewHolder(ViewGroup arg0, int arg1) {
		View v = LayoutInflater.from(arg0.getContext()).inflate(
				R.layout.layout_small_own_goal, arg0, false);
		OwnGoalsViewHolder vh = new OwnGoalsViewHolder(v);

		return vh;
	}

}
