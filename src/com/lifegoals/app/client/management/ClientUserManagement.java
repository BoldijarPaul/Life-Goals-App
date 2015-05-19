package com.lifegoals.app.client.management;

import java.util.Arrays;
import java.util.List;

 

import com.lifegoals.app.client.locator.context.AppContext;
import com.lifegoals.app.entities.LoginInfo;
import com.lifegoals.app.entities.LoginResult;
import com.lifegoals.app.entities.SavedGoal;
import com.lifegoals.app.entities.User;

public class ClientUserManagement {

	/* returns all the clients on the server */
	public static List<User> getAllUsers() {
		return Arrays.asList(AppContext.getContext().doGetRequest("users/getall",User[].class));	
	}

	/* adds a new user */
	public static User addUser(User user) {
		return  AppContext.getContext().doPostRequest("users/add",user,User.class);	
		 
	}

	/* tries to login and return a LoginResult which also has a token */
	public static LoginResult login(LoginInfo loginInfo) {
		return  AppContext.getContext().doPostRequest("users/login",loginInfo,LoginResult.class);
	}

}
