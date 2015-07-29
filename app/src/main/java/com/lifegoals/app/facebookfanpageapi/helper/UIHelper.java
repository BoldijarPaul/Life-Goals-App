package com.lifegoals.app.facebookfanpageapi.helper;

import android.content.res.Resources;

/**
 * Created by Paul on 4/12/2015.
 */
public class UIHelper {

    public static int dpToPx(int dp)
    {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    public static int pxToDp(int px)
    {
        return (int) (px / Resources.getSystem().getDisplayMetrics().density);
    }
}
