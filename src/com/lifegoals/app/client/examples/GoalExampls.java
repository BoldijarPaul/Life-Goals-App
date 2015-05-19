package com.lifegoals.app.client.examples;

import java.util.List;

import com.lifegoals.app.client.management.ClientGoalManagement;
import com.lifegoals.app.entities.Goal;

public class GoalExampls {

	private static void getGoalsExample() {

		List<Goal> goals = ClientGoalManagement.getAllGoals();
		for (Goal goal : goals) {
			System.out.println(goal.getText());
		}
	}

	private static void addGoalExample() {
	 
		Goal goal = new Goal();
		goal.setText("I love goals so sssmuch");
		goal.setId(3);
		goal.setUserId(1);

		Goal goal2=ClientGoalManagement.addGoal(goal);
		 
		
	}

	private static void deleteGoalExample() {
		// add a goal
	 
		
		Goal goal = new Goal();
		goal.setText("Testing delete goal");
		goal.setId(3);
		goal.setUserId(1);

		
		ClientGoalManagement.addGoal(goal);
		System.out.println("Added");
		// show all goals
		getGoalsExample();
		System.out.println("\nAll now:");

		
		// delete goal and show goals again
		ClientGoalManagement.deleteGoal(goal);
		getGoalsExample();

	}

	 
	public static void main(String[] args) {
		System.setProperty("http.proxyHost", "localhost");
	    System.setProperty("http.proxyPort", "8888");
	  
		
	    
	   
		//addGoalExample();
		 
		  getGoalsExample();
	}
}
