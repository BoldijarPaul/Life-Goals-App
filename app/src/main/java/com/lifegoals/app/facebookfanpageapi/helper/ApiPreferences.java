package com.lifegoals.app.facebookfanpageapi.helper;

import android.content.Context;
import android.content.SharedPreferences;


import com.lifegoals.app.facebookfanpageapi.entities.Feed;
import com.lifegoals.app.facebookfanpageapi.entities.MainFeed;
import com.lifegoals.app.facebookfanpageapi.entities.Post;
import com.lifegoals.app.facebookfanpageapi.entities.PostUpdate;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.List;

/**
 * Created by Paul on 4/7/2015.
 */
public class ApiPreferences {

    /* the fiels which we have the name of the items in the preferences */
    private static final String FIELD_FEEDS = Constants.PAGE_ID + "FEEDS";
    private static final String FIELD_POSTS = Constants.PAGE_ID + "POSTS";
    private static final String FIELD_FAV_POSTS = Constants.PAGE_ID + "FAVPOSTS";
    private static final String FIELD_GET_NOTIFICATIONS = Constants.PAGE_ID + "GET_NOTIFIC";
    private static final String FIELD_NEW_POSTS = Constants.PAGE_ID + "NEW_POSTS";
    private static final String FIELD_COLLAGES_COUNT = Constants.PAGE_ID + "COLLAGE_COUNT";
    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editor;


    public static void initialize(Context context) {
        if (sharedPreferences != null) return;
        sharedPreferences = context.getSharedPreferences("API", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public static void saveMainFeed(MainFeed mainFeed) {
        JSONArray feedJsonArray = FeedHelper.feedListToJsonArray(mainFeed.getFeeds());
        JSONArray postJsonArray = PostHelper.postListToJsonArray(mainFeed.getAllPosts());


        editor.putString(FIELD_FEEDS, feedJsonArray.toString());
        editor.putString(FIELD_POSTS, postJsonArray.toString());
        editor.commit();
    }

    private static List<Post> favoritePostsCache = null;

    public static boolean getNotifications() {
        return sharedPreferences.getBoolean(FIELD_GET_NOTIFICATIONS, true);
    }

    public static void setNotifications(boolean get) {
        editor.putBoolean(FIELD_GET_NOTIFICATIONS, get);
        editor.commit();
    }

    public static boolean newPostsAvailable() {
        return sharedPreferences.getBoolean(FIELD_NEW_POSTS, false);
    }

    public static void setNewPostsAvailable(boolean available) {
        editor.putBoolean(FIELD_NEW_POSTS, available);
        editor.commit();
    }

    public static List<Post> getFavoritesPosts() {
        // if we have already cached the posts get from here
        if (favoritePostsCache != null) return favoritePostsCache;

        // get the string from prefs
        String favoritesString = sharedPreferences.getString(FIELD_FAV_POSTS, null);
        // if we don'y have any return a empty list
        if (favoritesString == null) {
            favoritePostsCache = new java.util.ArrayList<Post>();
            return favoritePostsCache;
        }
        try {
            JSONArray favJsonArray = new JSONArray(favoritesString);
            List<Post> posts = PostHelper.jsonArrayToPostList(favJsonArray);
            favoritePostsCache = posts;
            return favoritePostsCache;
        } catch (JSONException e) {
            e.printStackTrace();
            favoritePostsCache = new java.util.ArrayList<Post>();
            return favoritePostsCache;
        }
    }

    /* this method will return the number of collages */
    public static int getCollagesCount() {
        return sharedPreferences.getInt(FIELD_COLLAGES_COUNT, 0);
    }

    /* this method will increment the number of collages */
    public static void addCollage() {
        editor.putInt(FIELD_COLLAGES_COUNT, getCollagesCount() + 1);
        editor.commit();
    }

    public static boolean postIsInFavorites(Post post) {
        List<Post> favorites = getFavoritesPosts();
        for (Post currentPosts : favorites) {
            if (currentPosts.getObjectId().equals(post.getObjectId())) {
                return true;
            }
        }
        return false;
    }

    public static void removePostFromFavorites(Post post) {
        List<Post> favorites = getFavoritesPosts();
        int indexToRemove = -1;
        for (int i = 0; i < favorites.size(); i++) {
            Post currentPosts = favorites.get(i);
            if (currentPosts.getObjectId().equals(post.getObjectId())) {
                indexToRemove = i;
                favorites.remove(i--);
                break;
            }
        }

        // now save new fav
        try {
            String array = PostHelper.postListToJsonArray(favorites).toString();
            editor.putString(FIELD_FAV_POSTS, array);
            editor.commit();

            // publish result

            BusHelper.bus.post(new PostUpdate(PostUpdate.Type.REMOVED, indexToRemove));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean addPostToFavorites(Post post) {
        List<Post> currentFavoritePost = getFavoritesPosts();
        if (currentFavoritePost == null) return false;
        // we could not get the favorite posts, return false
        try {
            currentFavoritePost.add(0, post);
            // added, now save result
            editor.putString(FIELD_FAV_POSTS, PostHelper.postListToJsonArray(currentFavoritePost).toString());
            editor.commit();

            BusHelper.bus.post(new PostUpdate(PostUpdate.Type.ADDED, 0));

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static MainFeed getSavedMainFeed() {
        try {
            MainFeed mainFeed = new MainFeed();
            String feedString = sharedPreferences.getString(FIELD_FEEDS, null);
            String postString = sharedPreferences.getString(FIELD_POSTS, null);
            if (feedString == null || postString == null) return null;
            JSONArray feedJsonArray = new JSONArray(feedString);
            JSONArray postJsonArray = new JSONArray(postString);
            List<Post> postList = PostHelper.jsonArrayToPostList(postJsonArray);
            List<Feed> feedList = FeedHelper.jsonArrayToFeedList(feedJsonArray);
            mainFeed.setAllPosts(postList);
            mainFeed.setFeeds(feedList);
            return mainFeed;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

}
