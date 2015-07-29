package com.lifegoals.app.facebookfanpageapi.interfaces;

import android.view.View;

/**
 * Created by Paul on 4/9/2015.
 */
public interface PostHolderListener {
    public void onImageLongClicked(View rootView);
    public void onImageClicked(View rootView);
    public void onSettingsClicked(View rootView, View targetView);
}
