package com.lifegoals.app.facebookfanpageapi.entities;




import com.lifegoals.app.facebookfanpageapi.helper.PostHelper;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by Paul on 4/1/2015.
 */
public class Feed {

    private String olderFeedUrl;
    private String newerFeedUrl;

    private List<Post> posts; // each feed as a list of posts
    private String feedUrl; // the url of the feed

    // other fields
    private boolean root = false;
    private boolean lastFeed = false;


    public JSONObject toJsonObject() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("olderFeedUrl", olderFeedUrl);
            jsonObject.put("newerFeedUrl", newerFeedUrl);
            jsonObject.put("feedUrl", feedUrl);
            jsonObject.put("root", root);
            jsonObject.put("lastFeed", lastFeed);
            jsonObject.put("posts", PostHelper.postListToJsonArray(posts));
            return jsonObject;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Feed fromJsonObject(JSONObject jsonObject) {
        try {
            Feed feed = new Feed();
            feed.setOlderFeedUrl(jsonObject.getString("olderFeedUrl"));
            feed.setNewerFeedUrl(jsonObject.getString("newerFeedUrl"));
            feed.setFeedUrl(jsonObject.getString("feedUrl"));
            feed.setRoot(jsonObject.getBoolean("root"));
            feed.setLastFeed(jsonObject.getBoolean("lastFeed"));
            feed.setPosts(PostHelper.jsonArrayToPostList(jsonObject.getJSONArray("posts")));
            return feed;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean isRoot() {
        return root;
    }

    public boolean isLastFeed() {
        return lastFeed;
    }

    public void setLastFeed(boolean lastFeed) {
        this.lastFeed = lastFeed;
    }

    public void setRoot(boolean root) {
        this.root = root;
    }

    public String getOlderFeedUrl() {
        return olderFeedUrl;
    }

    public void setOlderFeedUrl(String olderFeedUrl) {
        this.olderFeedUrl = olderFeedUrl;
    }

    public String getNewerFeedUrl() {
        return newerFeedUrl;
    }

    public void setNewerFeedUrl(String newerFeedUrl) {
        this.newerFeedUrl = newerFeedUrl;
    }


    public String getFeedUrl() {
        return feedUrl;
    }

    public void setFeedUrl(String feedUrl) {
        this.feedUrl = feedUrl;
    }


    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }
}
