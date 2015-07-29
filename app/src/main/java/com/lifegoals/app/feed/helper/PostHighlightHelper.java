package com.lifegoals.app.feed.helper;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.lifegoals.app.facebookfanpageapi.entities.Post;
import com.lifegoals.app.facebookfanpageapi.helper.BusHelper;
import com.lifegoals.app.feed.adapters.PostAdapter;
import com.lifegoals.app.feed.other.SelectingPostState;

import java.util.List;

/**
 * Created by Paul on 4/28/2015.
 */
public class PostHighlightHelper {
    /* this class will highlight the posts */
    private List<Post> highlightedPosts;
    private PostAdapter postAdapter;
    /* if we are currently choosing the posts */
    private boolean selecting = false;


    public void update(PostAdapter postAdapter) {
        this.postAdapter = postAdapter;
        highlightedPosts.clear();
        switchSelectingState(false);
        for (Post post : postAdapter.getPosts()) {
            if (post.isHighlighted()) {
                highlightedPosts.add(post);
            }
        }

        if (highlightedPosts.size() > 0) {
            switchSelectingState(true);

        }
    }

    public List<Post> getHighlightedPosts() {
        return highlightedPosts;
    }

    public PostHighlightHelper(PostAdapter postAdapter) {
        this.postAdapter = postAdapter;
        highlightedPosts = new java.util.ArrayList<>();
        update(postAdapter);


    }

    /* returns true if we can open the image, false if we are currently in selecting phase */
    public boolean clickPost(Post post) {
        if (!selecting) return true;

        /* we want to select or deselect a post */
        if (post.isHighlighted()) {
            /* deselect the post and remove it */
            post.setHighlighted(false);
            highlightedPosts.remove(post);
        } else {
            /* select the post and add it */
            post.setHighlighted(true);
            highlightedPosts.add(post);
        }
        postAdapter.notifyDataSetChanged();

        if (highlightedPosts.size() == 0) {
            /* no more posts, stop highlighting */
            switchSelectingState(false);

        }
        return false;
    }

    public void longClickPost(Post post) {
        if (!selecting) {
            /* we are not selecting anything, so here we are going to start ! */
            switchSelectingState(true);

            post.setHighlighted(true);
            highlightedPosts.add(post);
            postAdapter.notifyDataSetChanged();

        } else {
            /* we are already selecting , do the same thing as normal click */
            clickPost(post);
        }
    }

    /* returns true if it stopped selecting, using this return we can override the onbackpressed of the activity
     * and if we press back cancel selecting instead of finishing activity */
    public boolean stopSelecting() {
        /* clear the posts from the list and deselect them */
        if (selecting) {
            switchSelectingState(false);
            for (int i = 0; i < highlightedPosts.size(); i++) {
                highlightedPosts.get(i).setHighlighted(false);
                highlightedPosts.remove(i--);
            }
            postAdapter.notifyDataSetChanged();
            return true;
        }

        /* we are already not selecting anything */
        return false;
    }


    /* getters / setters */
    public boolean isSelecting() {
        return selecting;
    }

    private void switchSelectingState(boolean newSelectingState) {
        if (newSelectingState != selecting) {
            selecting = newSelectingState;
            /* notify the activity */
            BusHelper.bus.post(new SelectingPostState(selecting));
        }

    }

}
