package com.lifegoals.app.facebookfanpageapi.fragments;


import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;

import com.lifegoals.app.R;
import com.lifegoals.app.facebookfanpageapi.entities.Post;


/**
 * A simple {@link Fragment} subclass.
 */
public class PostDialogFragment extends DialogFragment {

    /* this dialog fragment will show a post image, and we can zoom it in or out. */

    /* the post that we are going to use */
    private Post post;

    public void setPost(Post post) {
        this.post = post;
    }

    public PostDialogFragment() {
        // Required empty public constructor
    }

    /* make the dialog full screen */
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
        setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (post == null || post.getBitmap() == null) {
            /* we have no post or no bitmap , close dialog */
            this.dismiss();
        }
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        View view = inflater.inflate(R.layout.dialog_fragment_post, container, false);
        final ImageView imageView = (ImageView) view.findViewById(R.id.fragment_post_image);


        // set the post bitmap
        imageView.setImageBitmap(post.getBitmap());


        return view;
    }


}
