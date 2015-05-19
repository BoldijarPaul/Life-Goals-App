package com.lifegoals.app.client.examples;

import java.util.List;


 
import com.lifegoals.app.client.management.ClientUserManagement;
import com.lifegoals.app.entities.LoginInfo;
import com.lifegoals.app.entities.LoginResult;
import com.lifegoals.app.entities.User;
 

public class UserExamples {

	private static void loginExample() {
		LoginInfo info = new LoginInfo();
		info.setName("paul");
		info.setPassword("paul");

		LoginResult loginResult = ClientUserManagement.login(info);
		System.out.println(loginResult.isSuccess());
	}

	private static void showUsers() {

		 
 
		List<User> users = ClientUserManagement.getAllUsers();
		String text = "";
		for (User user : users) {
			text += user.getName() + " ";
		}
		System.out.println(text);
	}

	public static void main(String[] args) {
		showUsers();
		loginExample();
	}
}
