package com.lifegoals.app.helper;

import com.google.gson.Gson;

public class GsonHelper {

	private static Gson gson = new Gson();

	public static String toString(Object object) {
		return gson.toJson(object);
	}

	public static <T> T toObject(String json, Class<T> type) {
		if (json == null)
			return null;
		return gson.fromJson(json, type);
	}
}
