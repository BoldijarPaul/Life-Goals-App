package com.lifegoals.app.feed.helper;

import android.content.Context;
import android.os.Vibrator;

/**
 * Created by Paul on 4/28/2015.
 */
public class Utils {
    /* vibrates for some milliseconds */
    public static void vibrate(long amount, Context context) {
        if (context == null) return;
        ((Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE)).vibrate(amount);



    }
}
