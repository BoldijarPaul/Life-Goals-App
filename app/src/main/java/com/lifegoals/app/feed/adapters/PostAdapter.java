package com.lifegoals.app.feed.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lifegoals.app.facebookfanpageapi.entities.Post;
import com.lifegoals.app.facebookfanpageapi.helper.PostHelper;
import com.lifegoals.app.facebookfanpageapi.interfaces.PostAdapterListener;
import com.lifegoals.app.facebookfanpageapi.interfaces.PostHolderListener;
import com.lifegoals.app.R;
import com.lifegoals.app.feed.holders.PostHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Paul on 4/3/2015.
 */
public class PostAdapter extends RecyclerView.Adapter<PostHolder> implements PostHelper.BitmapLoadListener {


    /* the listeners that we use to communicate */
    private PostAdapterListener postAdapterListener;
    private PostHolderListener postHolderListener;


    public void setPostAdapterListener(PostAdapterListener postAdapterListener) {
        this.postAdapterListener = postAdapterListener;
    }

    public void setPostHolderListener(PostHolderListener postHolderListener) {
        this.postHolderListener = postHolderListener;
    }

    /* how many posts to load their images after the last visible one */
    private static final int POSTS_TO_LOAD_AFTER_VISIBLE = 3;
    /* how many posts to recycle before and after the first and last visible post */
    private static final int POSTS_TO_RECYCLE_BITMAP = 6;


    private List<Post> posts;

    public PostAdapter(List<Post> posts) {
        this.posts = posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    public List<Post> getPosts() {
        return posts;
    }

    @Override
    public PostHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_post, parent, false);
        return new PostHolder(view, postHolderListener);
    }


    @Override
    public void onBindViewHolder(PostHolder holder, int position) {
        Post postToBind = posts.get(position);
        holder.bindPost(postToBind, this, position);
        /* recycle the bitmaps outside of the view bounds */
        tryRecyclingBitmaps(position);

        /* go to the next posts, and load the image */
        for (int i = position + 1; i < position + POSTS_TO_LOAD_AFTER_VISIBLE; i++) {
            if (i < getItemCount()) {
                Post postToLoad = posts.get(i);
                holder.loadPostImage(postToLoad, this, i);
            } else {
                /* we loaded all of our images, there are no more posts, we need to load more */
                loadOlderFeed();
                break;
            }
        }
    }


    /* recycling bitmaps */
    public void tryRecyclingBitmaps(int position) {
        for (int i = 0; i < position - POSTS_TO_RECYCLE_BITMAP && i < posts.size(); i++) {
            posts.get(i).recycleBitmap();
        }
    }


    /* load older feed because we reached the end */
    private void loadOlderFeed() {
        // we need to load more feed
        if (postAdapterListener != null) {
            postAdapterListener.onLoadOlder();
        }
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    @Override
    public void onBitmapLoaded(int position) {
        notifyItemChanged(position);
    }


}
