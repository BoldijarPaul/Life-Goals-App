package com.lifegoals.app.facebookfanpageapi.helper.cache;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Paul on 4/9/2015.
 */
public class BitmapCacheHelper {
    /* this class will handle bitmap caching , inspired from StackOverflow */
    private static BitmapCacheHelper instance;

    public static void initialize(Context context) {
        instance = new BitmapCacheHelper();
        instance.setCacheDir(new File(context.getCacheDir(), "thumbnails"));
        instance.getCacheDir().mkdirs();
    }

    public static BitmapCacheHelper getInstance() {
        return instance;
    }

    private File cacheDir;

    public File getCacheDir() {
        return cacheDir;
    }

    private void setCacheDir(File cacheDir) {
        this.cacheDir = cacheDir;
    }


    /**
     * Returns a bitmap from the cache, with the specified key
     *
     * @param name the key of the cached bitmap
     * @return null if bitmap does not exist or error, otherwise the bitmap itself
     */
    public Bitmap getBitmap(String name) {
        try {
            File cacheFile = new File(cacheDir, name.replaceAll("\\W+", "") + ".png");
            FileInputStream fis = new FileInputStream(cacheFile);
// Read a bitmap from the file (which presumable contains bitmap in PNG format, since
// that's how files are created)
            return BitmapFactory.decodeStream(fis);
        } catch (FileNotFoundException e) {
            return null;
        }
    }

    /**
     * This method will cache a bitmap with a a chosen key
     * @param name the the key of the bitmap that we want to cache
     * @param bitmap the bitmap that we want to cache
     */
    public void cacheBitmap(String name, Bitmap bitmap) {
        // Create a path pointing to the system-recommended cache dir for the app, with sub-dir named
        // thumbnails
        // Create a path in that dir for a file, named by the default hash of the url
        File cacheFile = new File(cacheDir, name.replaceAll("\\W+", "") + ".png");
        try {
            // Create a file at the file path, and open it for writing obtaining the output stream
            cacheFile.createNewFile();
            FileOutputStream fos = new FileOutputStream(cacheFile);
            // Write the bitmap to the output stream (and thus the file) in PNG format (lossless compression)
            bitmap.compress(Bitmap.CompressFormat.PNG, 8, fos);
            // Flush and close the output stream
            fos.flush();
            fos.close();
        } catch (Exception e) {
            // Log anything that might go wrong with IO to file
            e.printStackTrace();
        }
    }
}
