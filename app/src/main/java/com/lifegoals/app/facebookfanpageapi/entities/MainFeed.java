package com.lifegoals.app.facebookfanpageapi.entities;

import java.util.List;

/**
 * Created by Paul on 4/3/2015.
 */
/*
 The main feed of the app. This one contains all the other feeds and all allPosts in one place
 */
public class MainFeed {
    private List<Feed> feeds;
    private List<Post> allPosts;

    public MainFeed() {
        feeds = new java.util.ArrayList<Feed>();
        allPosts = new java.util.ArrayList<Post>();
    }

    public List<Feed> getFeeds() {
        return feeds;
    }

    public void setFeeds(List<Feed> feeds) {
        this.feeds = feeds;
    }

    public List<Post> getAllPosts() {
        return allPosts;
    }

    public void setAllPosts(List<Post> allPosts) {
        this.allPosts = allPosts;
    }

    public Feed getLastFeed() {
        return feeds.get(feeds.size() - 1);
    }

    public Feed getFirstFeed() {
        return feeds.get(0);
    }
}
