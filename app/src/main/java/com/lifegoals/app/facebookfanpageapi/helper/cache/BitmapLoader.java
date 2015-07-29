package com.lifegoals.app.facebookfanpageapi.helper.cache;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

/**
 * Created by Paul on 3/30/2015.
 */
public class BitmapLoader {


    /**
     * @param imageURL the url of the image we want to download
     * @return the bitmap if success, null if error
     */
    public static Bitmap getFromURL(String imageURL) {
        try {
            // TODO when using collages, never use this method, do something that will delete the post if the cache is empty
            Bitmap cachedImage = BitmapCacheHelper.getInstance().getBitmap(imageURL);
            if (cachedImage != null) {
                return cachedImage;
            } else {

            }
            // if nothing was found in cache do the request
            if (imageURL.contains("COLLAGE")) {
                /* don't request to server for the image if this was created as a collage */
                return null;
            }

            java.net.URL url = new java.net.URL(imageURL);
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setInstanceFollowRedirects(true);
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            BitmapCacheHelper.getInstance().cacheBitmap(imageURL, myBitmap);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
