package com.lifegoals.app.facebookfanpageapi.service;



import com.lifegoals.app.facebookfanpageapi.entities.Feed;
import com.lifegoals.app.facebookfanpageapi.helper.AsyncTaskHelper;
import com.lifegoals.app.facebookfanpageapi.helper.FeedHelper;
import com.lifegoals.app.facebookfanpageapi.http.HttpHelper;
import com.lifegoals.app.facebookfanpageapi.interfaces.FeedLoaderListener;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Paul on 4/1/2015.
 */
public class FacebookPageApi {

    public static String API_VERSION = "v2.3";
    public static String FILTER_PHOTO = "app_2392950137";
    private static FacebookPageApi instance;

    public static FacebookPageApi getInstance(String pageId, String accessToken) {
        if (instance == null) {
            instance = new FacebookPageApi();
        }
        instance.setAccessToken(accessToken);
        instance.setPageId(pageId);
        return instance;
    }


    private String pageId;
    private String accessToken;


    public void setPageId(String pageId) {
        this.pageId = pageId;
    }


    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }


    /**
     * @param isRoot             true if this is the root feed of the app. (the first request goes to it, after the previous and next go after it)
     * @param feedLoaderListener the interface callback
     * @param urlIfNotRoot       if we don't want to load the root, we need a URL that is the next feed url
     */
    public void newFeedLoad(final boolean isRoot, final String urlIfNotRoot, final FeedLoaderListener feedLoaderListener) {
        AsyncTaskHelper.create(new AsyncTaskHelper.AsyncMethods<Feed>() {

            @Override
            public Feed doInBackground() {
                try {
                    if (isRoot) return getMainFeed();
                    return getOtherFeed(urlIfNotRoot);
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            public void onDone(Feed value, long ms) {
                if (value != null) {
                    value.setRoot(isRoot);
                }
                feedLoaderListener.onFeedResponse(value);
            }
        });
    }

    private Feed getMainFeed() throws JSONException {
        String url = UrlCreator.createFeedUrl(pageId, accessToken);
        String postsJson = HttpHelper.getResponseFromUrl(url);
        Feed feed = FeedHelper.createFeedFromJson(new JSONObject(postsJson));
        feed.setFeedUrl(url);
        return feed;
    }

    public Feed getOtherFeed(String url) throws JSONException {
        String postsJson = HttpHelper.getResponseFromUrl(url);
        Feed feed = FeedHelper.createFeedFromJson(new JSONObject(postsJson));
        feed.setFeedUrl(url);
        return feed;
    }


}
