package com.lifegoals.app.facebookfanpageapi.helper;

import android.graphics.Bitmap;


import com.lifegoals.app.facebookfanpageapi.entities.Post;
import com.lifegoals.app.facebookfanpageapi.helper.cache.BitmapLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Paul on 4/4/2015.
 */
public class PostHelper {

    public static void loadPostImage(final Post post, final BitmapLoadListener bitmapLoadListener, final int position) {
        if (post.isLoadingImage()) return; // don't try to get the image if we already have it
        if (post.getObjectId() != null) {
            // if is a picture
            final String url = post.getPicture();
            post.setLoadingImage(true);

            AsyncTaskHelper.create(new AsyncTaskHelper.AsyncMethods<Bitmap>() {

                @Override
                public Bitmap doInBackground() {
                    return BitmapLoader.getFromURL(url);
                }

                @Override
                public void onDone(Bitmap value, long ms) {
                    post.setLoadingImage(false);
                    post.setBitmap(value);

                    if (value != null) {
                        bitmapLoadListener.onBitmapLoaded(position);
                    }
                }
            });

        }
    }

    public static JSONArray postListToJsonArray(List<Post> postList) {
        try {
            JSONArray jsonArray = new JSONArray();
            for (Post post : postList) {
                jsonArray.put(post.toJsonObject());
            }
            return jsonArray;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List<Post> jsonArrayToPostList(JSONArray jsonArray) {
        try {
            List<Post> posts = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                posts.add(Post.fromJsonObject(jsonObject));
            }
            return posts;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static interface BitmapLoadListener {
        public void onBitmapLoaded(int position);

    }

}
