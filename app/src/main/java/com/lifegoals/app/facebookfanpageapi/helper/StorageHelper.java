package com.lifegoals.app.facebookfanpageapi.helper;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by Paul on 4/18/2015.
 */
public class StorageHelper {
    public static final String FOLDER_TO_SAVE = "saved";

    /* Checks if external storage is available for read and write */
    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /* Checks if external storage is available to at least read */
    public static boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

    /* saves a bitmap external if possible, if not internal */
    public static StorageHelperResult trySavingBitmap(Bitmap bitmap, Context context) {
        if (isExternalStorageReadable() && isExternalStorageWritable()) {
            // we should be able to save the bitmap
            StorageHelperResult result = saveBitmapToExternal(bitmap);
            if (result.isSuccess())
                return result;
        }
        return saveBitmapToInternal(bitmap, context);
    }

    /* saves the bitmap to the internal storage. a context instance is needed */
    public static StorageHelperResult saveBitmapToInternal(Bitmap image, Context context) {
        try {
            FileOutputStream fos = context.openFileOutput(FOLDER_TO_SAVE + "/" + getBitmapName(), Context.MODE_PRIVATE);
            image.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();
            return new StorageHelperResult(true, true);
        } catch (Exception e) {
            e.printStackTrace();
            return new StorageHelperResult(false, true);
        }
    }

    /* returns a bitmap name. Format: PIC_ and the current time in milliseconds.*/
    private static String getBitmapName() {
        String imageName = "PIC_" + System.currentTimeMillis();
        String fname = imageName + ".png";
        return fname;
    }

    /* saves the bitmap in the external storage*/
    public static StorageHelperResult saveBitmapToExternal(Bitmap bitmap) {
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/" + FOLDER_TO_SAVE);
        myDir.mkdirs();

        String fname = getBitmapName();

        File file = new File(myDir, fname);
        if (file.exists()) file.delete();
        try {
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
            return new StorageHelperResult(true, false);

        } catch (Exception e) {
            e.printStackTrace();
            return new StorageHelperResult(false, false);
        }
    }

    /* a class which holds some info that we are going to return when trying to save bitmaps */
    public static class StorageHelperResult {
        private boolean success = false;
        private boolean isInternal = false;

        public StorageHelperResult(boolean success, boolean isInternal) {
            this.success = success;
            this.isInternal = isInternal;
        }

        public boolean isSuccess() {
            return success;
        }

        public void setSuccess(boolean success) {
            this.success = success;
        }

        public boolean isInternal() {
            return isInternal;
        }

        public void setInternal(boolean isInternal) {
            this.isInternal = isInternal;
        }
    }
}
