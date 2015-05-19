package com.lifegoals.app.context;

import java.net.HttpURLConnection;

public class Context {
	/* this class will handle all requests and (de)serializing of the objects */

	/* the token that we need for some requets */
	private String token;

	public void setToken(String token) {
		this.token = token;
	}

	/* the root adress of the api */
	public String root ;

	public void setRoot(String root) {
		this.root = root;
	}

	/* for the get requets you can't set a body, for the others you can */
	public  <T> T doGetRequest(String path, Class<T> type) {
		try {
			HttpURLConnection urlConnection = HttpHelper
					.createHttpUrlConnection(root+path, "GET", null, token);
			String response = HttpHelper
					.readHttpUrlConnectionResponse(urlConnection);
			return HttpHelper.stringToObject(response, type);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public  <T> T doPostRequest(String path, Object body, Class<T> type) {
		try {
			HttpURLConnection urlConnection = HttpHelper
					.createHttpUrlConnection(root+path, "POST", body, token);
			String response = HttpHelper
					.readHttpUrlConnectionResponse(urlConnection);
			return HttpHelper.stringToObject(response, type);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public  <T> T doDeleteRequest(String path, Object body, Class<T> type) {
		try {
			HttpURLConnection urlConnection = HttpHelper
					.createHttpUrlConnection(root+path, "DELETE", body, token);
			String response = HttpHelper
					.readHttpUrlConnectionResponse(urlConnection);
			return HttpHelper.stringToObject(response, type);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public  <T> T doPutRequest(String path, Object body, Class<T> type) {
		try {
			HttpURLConnection urlConnection = HttpHelper
					.createHttpUrlConnection(root+path, "PUT", body, token);
			String response = HttpHelper
					.readHttpUrlConnectionResponse(urlConnection);
			return HttpHelper.stringToObject(response, type);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
