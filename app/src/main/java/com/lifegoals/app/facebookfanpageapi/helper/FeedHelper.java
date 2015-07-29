package com.lifegoals.app.facebookfanpageapi.helper;



import com.lifegoals.app.facebookfanpageapi.entities.Feed;
import com.lifegoals.app.facebookfanpageapi.entities.Post;
import com.lifegoals.app.facebookfanpageapi.service.UrlCreator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by Paul on 4/3/2015.
 */
public class FeedHelper {

    /* this method will create a Feed page from the JsonObject that we get from the Facebook API */
    public static Feed createFeedFromJson(JSONObject root) {
        try {
            Feed feed = new Feed();
            List<Post> posts = new java.util.ArrayList<>();
            JSONArray jsonArray = root.getJSONArray("data");

            String lastObjectId = ""; // some times the FB API returns 2 consecutive cloned objects, we are going to check this
            // looping through each item of the json array
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject postJsonObject = jsonArray.getJSONObject(i);
                Post post = new Post();
                String objectId = null;
                // setting the post fields
                if (JsonHelper.keyExists(postJsonObject, "message")) {
                    post.setMessage(postJsonObject.getString("message"));
                }
                post.setCreatedDateString(postJsonObject.getString("created_time"));
                if (JsonHelper.keyExists(postJsonObject, "object_id")) {
                    objectId = postJsonObject.getString("object_id");
                    post.setObjectId(objectId);
                    post.setPicture(UrlCreator.createPictureUrl(post.getObjectId()));
                } else {
                    continue;
                }
                //check for paging
                if (JsonHelper.keyExists(root, "paging")) {
                    JSONObject pagingJsonObject = root.getJSONObject("paging");
                    feed.setOlderFeedUrl(pagingJsonObject.getString("next"));
                    feed.setNewerFeedUrl(pagingJsonObject.getString("previous"));
                }

                if (objectId != null && !lastObjectId.equals(objectId)) // only add the post if this is not a clone
                    posts.add(post);

                lastObjectId = objectId;
            }
            feed.setPosts(posts);
            return feed;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    /* this method will return the urls in all sizes of a image json object */
    public static List<String> getImageUrlsFromJson(JSONObject root) {
        try {
            List<String> images = new java.util.ArrayList<>();
            JSONArray jsonArray = root.getJSONArray("images");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject imageJsonObject = jsonArray.getJSONObject(i);
                images.add(imageJsonObject.getString("source"));
            }
            return images;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    /* conversion JsonArray to List */
    public static JSONArray feedListToJsonArray(List<Feed> feedList) {
        try {
            JSONArray jsonArray = new JSONArray();
            for (Feed feed : feedList) {
                JSONObject jsonObject = feed.toJsonObject();
                jsonArray.put(jsonObject);
            }
            return jsonArray;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /* conversion List to JsonArray */
    public static List<Feed> jsonArrayToFeedList(JSONArray jsonArray) {
        try {
            List<Feed> feeds = new java.util.ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                feeds.add(Feed.fromJsonObject(jsonObject));
            }
            return feeds;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
