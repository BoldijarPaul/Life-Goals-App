package com.lifegoals.app.feed.holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.lifegoals.app.facebookfanpageapi.entities.Post;
import com.lifegoals.app.facebookfanpageapi.helper.DateHelper;
import com.lifegoals.app.facebookfanpageapi.helper.PostHelper;
import com.lifegoals.app.facebookfanpageapi.interfaces.PostHolderListener;
import com.lifegoals.app.R;

/**
 * Created by Paul on 4/3/2015.
 */
public class PostHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


    private ImageView image;
    private TextView text;
    private View settings;
    private ProgressBar progressBar;
    private TextView date;
    private View rootView;
    private View highlightView;

    private PostHolderListener listener;

    public PostHolder(View itemView, final PostHolderListener listener) {
        super(itemView);
        progressBar = (ProgressBar) itemView.findViewById(R.id.item_post_progress_bar);
        image = (ImageView) itemView.findViewById(R.id.item_post_image);
        text = (TextView) itemView.findViewById(R.id.item_post_text);
        settings = itemView.findViewById(R.id.item_post_settings);
        date = (TextView) itemView.findViewById(R.id.item_post_date);
        highlightView = itemView.findViewById(R.id.item_post_highlight_view);
        this.rootView = itemView;
        this.listener = listener;
        settings.setOnClickListener(this);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) listener.onImageClicked(rootView);
            }
        });
        image.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (listener != null) listener.onImageLongClicked(rootView);
                return false;
            }
        });
    }

    /**
     * @param post               the post that we want to bind
     * @param bitmapLoadListener the listener, we are going to call back when is loaded
     * @param position           the index from the array. we need this because we do the callback with it
     */
    public void bindPost(Post post, PostHelper.BitmapLoadListener bitmapLoadListener, int position) {
        text.setText(post.getMessage());

        /* post highlighting *////////
        if (post.isHighlighted()) {
            /* only change the visibility when needed */
            if (highlightView.getVisibility() != View.VISIBLE) {
                highlightView.setVisibility(View.VISIBLE);
            }
        } else {
            /* only change the visibility when needed */
            if (highlightView.getVisibility() != View.INVISIBLE) {
                highlightView.setVisibility(View.INVISIBLE);
            }
        }

        if (post.getBitmap() == null) {
            /// load the bitmap
            loadPostImage(post, bitmapLoadListener, position);
            image.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.VISIBLE);
        } else {
            image.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.INVISIBLE);
            image.setImageBitmap(post.getBitmap());
        }

        date.setText(DateHelper.dateToString(post.getCreatedDate()));

    }

    public void loadPostImage(Post post, PostHelper.BitmapLoadListener bitmapLoadListener, int position) {
        PostHelper.loadPostImage(post, bitmapLoadListener, position);

    }


    @Override
    public void onClick(View v) {
        // we clicked a settings button
        if (listener != null)
            listener.onSettingsClicked(rootView, v);

    }
}
