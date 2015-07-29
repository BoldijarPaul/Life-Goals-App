package com.lifegoals.app.facebookfanpageapi.helper;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.widget.PopupMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;


import com.lifegoals.app.R;
import com.lifegoals.app.facebookfanpageapi.entities.Post;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Paul on 4/9/2015.
 */
public class PopupMenuHelper {
    public static void createPostMenu(final View target, final Post post) {
        if (post.isHighlighted()) return; /* don't create menu if our post is highlighted */

        PopupMenu menu = new PopupMenu(target.getContext(), target);

        if (ApiPreferences.postIsInFavorites(post)) {
            menu.getMenu().add(target.getContext().getString(R.string.msg_remove_fav));
        } else {
            menu.getMenu().add(target.getContext().getString(R.string.msg_add_to_fav));
        }
        menu.getMenu().add(target.getContext().getString(R.string.msg_save_picture));
        menu.getMenu().add(target.getContext().getString(R.string.msg_share));
        // listener
        menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if (menuItem.getTitle().equals(target.getContext().getString(R.string.msg_add_to_fav))) {
                    addPostToFavorites(post, target.getContext());


                }
                if (menuItem.getTitle().equals(target.getContext().getString(R.string.msg_share))) {
                    trySharingBitmap(post, target.getContext());

                }
                if (menuItem.getTitle().equals(target.getContext().getString(R.string.msg_save_picture))) {
                    trySavingPicture(post, target.getContext());
                }

                if (menuItem.getTitle().equals(target.getContext().getString(R.string.msg_remove_fav))) {
                    removeFromFav(post, target.getContext());

                }
                return false;
            }
        });
        menu.show();
    }

    private static void trySavingPicture(final Post post, final Context context) {
        if (post.getBitmap() == null) {
            Toast.makeText(context.getApplicationContext(), context.getString(R.string.msg_wait_image_load), Toast.LENGTH_SHORT).show();
        } else {
            // try to save the picture
            AsyncTaskHelper.create(new AsyncTaskHelper.AsyncMethods<StorageHelper.StorageHelperResult>() {

                @Override
                public StorageHelper.StorageHelperResult doInBackground() {
                    return StorageHelper.trySavingBitmap(post.getBitmap(), context);
                }

                @Override
                public void onDone(StorageHelper.StorageHelperResult result, long ms) {
                    String resultMessage = null;
                    if (!result.isSuccess()) {
                        resultMessage = context.getString(R.string.msg_bitmap_not_saved);
                    } else {
                        // bitmap saved
                        resultMessage = context.getString(R.string.msg_picture_saved).replace("@1", StorageHelper.FOLDER_TO_SAVE);
                    }
                    Toast.makeText(context.getApplicationContext(), resultMessage, Toast.LENGTH_SHORT).show();
                }
            });

        }

    }


    private static void removeFromFav(Post post, Context context) {
        ApiPreferences.removePostFromFavorites(post);
        Toast.makeText(context, context.getString(R.string.msg_removed_fav), Toast.LENGTH_SHORT).show();
    }

    private static void trySharingBitmap(Post post, Context context) {
        if (post.getBitmap() == null) {
            Toast.makeText(context.getApplicationContext(), context.getString(R.string.msg_wait_image_load), Toast.LENGTH_SHORT).show();
        } else {
            shareBitmap(post.getBitmap(), context);
        }
    }

    private static void addPostToFavorites(Post post, Context context) {
        if (ApiPreferences.addPostToFavorites(post)) {
            Toast.makeText(context.getApplicationContext(), context.getString(R.string.msg_added_to_fav), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context.getApplicationContext(), context.getString(R.string.msg_not_add_fav), Toast.LENGTH_SHORT).show();
        }
    }

    private static void shareBitmap(Bitmap bitmap, Context context) {
        try {
            File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            File imageFile = new File(path, System.currentTimeMillis() + ".png");
            FileOutputStream fileOutPutStream = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 80, fileOutPutStream);

            fileOutPutStream.flush();
            fileOutPutStream.close();


            Uri imageUri = Uri.parse("file://" + imageFile.getAbsolutePath());

            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_SEND);
            intent.setType("image/*");

            intent.putExtra(Intent.EXTRA_SUBJECT, "abc");
            intent.putExtra(Intent.EXTRA_TEXT, "def");
            intent.putExtra(Intent.EXTRA_STREAM, imageUri);
            context.startActivity(Intent.createChooser(intent, context.getString(R.string.msg_share)));

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context, context.getString(R.string.msg_share_error), Toast.LENGTH_SHORT).show();

        }
    }
}
