package com.lifegoals.app.feed.other;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.lifegoals.app.R;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;

/**
 * Created by Paul on 4/6/2015.
 */
public class UIHelper {
    public static void showErrorMessage(Activity activity, String text) {
        Toast.makeText(activity, text, Toast.LENGTH_SHORT).show();
    }

    public static void goToUrl(String url, Context context) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        context.startActivity(intent);
    }

    public static void shareText(String text, Context context) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, text);
        intent.setType("text/plain");
        context.startActivity(intent);
    }

    public static void startEmailIntent(String email, String subject, Context context) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("message/rfc822");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, "");
        try {
            context.startActivity(Intent.createChooser(intent, context.getString(R.string.msg_send_feedback)));
        } catch (android.content.ActivityNotFoundException ex) {
        }
    }


    public static FloatingActionButton createFloatingActionButton(int colorId, int imageId, Activity activity) {
        final ImageView image = new ImageView(activity);
        image.setImageResource(imageId);
        FloatingActionButton floatingActionButton = new FloatingActionButton.Builder(activity)
                .setTheme(FloatingActionButton.THEME_LIGHT)
                .setContentView(image)
//                .setBackgroundDrawable(new ColorDrawable(activity.getResources().getColor(colorId)))
                .build();
        return floatingActionButton;
    }

    /**
     * This method will create a new floating action sub button
     *
     * @param builder  the builder needed .
     * @param imageId  the image id from the resources
     * @param activity the activity where we want to create
     * @return the view
     */
    public static View createFloatingSubIcon(SubActionButton.Builder builder, int imageId, Activity activity) {
        final ImageView image = new ImageView(activity);
        image.setImageResource(imageId);
        FrameLayout.LayoutParams params2 = new FrameLayout.LayoutParams(100, 100);
        View view = builder.setContentView(image).setLayoutParams(params2).build();
        return view;
    }

    /* this method will capture the view into a bitmap */
    public static Bitmap captureView(View view) {
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bm = view.getDrawingCache();
        return bm;
    }
}
