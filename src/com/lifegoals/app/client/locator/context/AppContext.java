package com.lifegoals.app.client.locator.context;

import com.lifegoals.app.context.Context;

public class AppContext {
	private static Context context;
	
	public static Context getContext() {
		if(context==null){
			context=new Context();
			context.setRoot("http://169.254.123.149:8080/api/");
			context.setToken("Token");
		}
		return context;
	}
}
