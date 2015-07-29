package com.lifegoals.app.facebookfanpageapi.helper;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Paul on 4/2/2015.
 */
public class JsonHelper {

    public static boolean keyExists(JSONObject jsonObject, String key) {
        try {
            jsonObject.get(key);
            return true;
        } catch (JSONException e) {
            return false;
        }
    }
}
