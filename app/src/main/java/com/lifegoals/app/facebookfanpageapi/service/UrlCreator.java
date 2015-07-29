package com.lifegoals.app.facebookfanpageapi.service;

/**
 * Created by Paul on 4/2/2015.
 */
public class UrlCreator {
    public static String createFeedUrl(String pageId, String accessToken) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("https://graph.facebook.com/");
        stringBuilder.append(FacebookPageApi.API_VERSION);
        stringBuilder.append("/");
        stringBuilder.append(pageId);
        stringBuilder.append("/feed?filter=");
        stringBuilder.append(FacebookPageApi.FILTER_PHOTO);
        stringBuilder.append("&fields=message,object_id&access_token=");
        stringBuilder.append(accessToken);
        return stringBuilder.toString();
    }



    public static String createPictureUrl(String objectId) {
        return new StringBuilder()
                .append("https://graph.facebook.com/")
                .append(objectId)
                .append("/picture?type=normal").toString();
    }


}
