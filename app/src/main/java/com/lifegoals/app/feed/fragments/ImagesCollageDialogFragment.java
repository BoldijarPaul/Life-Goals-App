package com.lifegoals.app.feed.fragments;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.lifegoals.app.facebookfanpageapi.entities.Post;
import com.lifegoals.app.facebookfanpageapi.helper.ApiPreferences;
import com.lifegoals.app.facebookfanpageapi.helper.StorageHelper;
import com.lifegoals.app.facebookfanpageapi.helper.cache.BitmapCacheHelper;
import com.lifegoals.app.facebookfanpageapi.service.UrlCreator;
import com.lifegoals.app.R;
import com.lifegoals.app.feed.other.UIHelper;
import com.lifegoals.app.feed.other.collage.MultiTouchListener;
import com.lifegoals.app.feed.other.collage.MultiTouchListenerNoTranslate;
import com.lifegoals.app.feed.other.collage.MultiTouchListenerToFront;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ImagesCollageDialogFragment extends DialogFragment {

    /* the collage name is for now 'Collage ' + the current count of the collages. */
    private static final String COLLAGE_BASE_NAME = "Collage";

    private RelativeLayout root;
    private List<Post> posts;

    public ImagesCollageDialogFragment() {
        // Required empty public constructor
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    @Override
    public void onDestroyView() {
        if (getDialog() != null && getRetainInstance())
            getDialog().setDismissMessage(null);
        super.onDestroyView();
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
        }
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, com.lifegoals.app.R.style.DialogStyle);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().getAttributes().windowAnimations = com.lifegoals.app.R.style.DialogAnimation;
        View view = inflater.inflate(R.layout.fragment_images_collage_dialog, container, false);


        // the root layout, we are going to capture the content of this when saving the collage */
        root = (RelativeLayout) view.findViewById(R.id.root);


          /* for each post create a new image view and add it */
        for (Post post : posts) {
            final ImageView imageView = new ImageView(getActivity());
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            /* center the image view */
            layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
            layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
            imageView.setLayoutParams(layoutParams);
            imageView.setImageBitmap(post.getBitmap());
            /* setting the on touch listener, in order to make it scale / rotate / move */
            imageView.setOnTouchListener(new MultiTouchListener());
            root.addView(imageView);

        }


        /* for now, save collage on the click of the root layout */
        root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveCollage();
            }
        });


        return view;
    }


    /* this method will save the collage */
    private void saveCollage() {
        /* the collage bitmap */
        Bitmap image = UIHelper.captureView(root);

        /* now creating the new post */
        String objectId = "COLLAGE" + System.currentTimeMillis();
        Post post = new Post();
        post.setObjectId(objectId);
        post.setPicture(UrlCreator.createPictureUrl(objectId));
        post.setMessage(COLLAGE_BASE_NAME + " " + (ApiPreferences.getCollagesCount() + 1));

        BitmapCacheHelper.getInstance().cacheBitmap(post.getPicture(), image);
        ApiPreferences.addPostToFavorites(post);

        /* update the collages count*/
        ApiPreferences.addCollage();

        /* notify the user with a toast message */
        Toast.makeText(getActivity().getApplicationContext(), getActivity().getString(R.string.msg_collage_created), Toast.LENGTH_SHORT).show();

    }

}
