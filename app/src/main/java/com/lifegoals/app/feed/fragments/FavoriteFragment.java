package com.lifegoals.app.feed.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lifegoals.app.facebookfanpageapi.entities.MainFeed;
import com.lifegoals.app.facebookfanpageapi.entities.Post;
import com.lifegoals.app.facebookfanpageapi.entities.PostUpdate;
import com.lifegoals.app.facebookfanpageapi.fragments.PostDialogFragment;
import com.lifegoals.app.facebookfanpageapi.helper.ApiPreferences;
import com.lifegoals.app.facebookfanpageapi.helper.BusHelper;
import com.lifegoals.app.facebookfanpageapi.helper.PopupMenuHelper;
import com.lifegoals.app.facebookfanpageapi.interfaces.PostAdapterListener;
import com.lifegoals.app.facebookfanpageapi.interfaces.PostHolderListener;
import com.lifegoals.app.R;
import com.lifegoals.app.feed.adapters.PostAdapter;
import com.lifegoals.app.feed.helper.PostHighlightHelper;
import com.squareup.otto.Subscribe;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class FavoriteFragment extends Fragment implements PostAdapterListener, PostHolderListener {


    //views

    private RecyclerView mRecyclerView;

    //


    private MainFeed mMainFeed;
    private PostAdapter mPostAdapter;
    private LinearLayoutManager linearLayoutManager;
    private static PostHighlightHelper sPostHighlightHelper;


    public FavoriteFragment() {
        // Required empty public constructor
        if (mMainFeed == null || mPostAdapter == null) {
            mMainFeed = new MainFeed();
            mMainFeed.setAllPosts(ApiPreferences.getFavoritesPosts());
            mPostAdapter = new PostAdapter(mMainFeed.getAllPosts());
        }

        mPostAdapter.setPostAdapterListener(this);
        mPostAdapter.setPostHolderListener(this);
        BusHelper.bus.register(this);

        if (sPostHighlightHelper == null)
            sPostHighlightHelper = new PostHighlightHelper(mPostAdapter);

    }

    /* returns null if we can't return the selected posts, missing or something is wrong */
    public List<Post> getSelectedPosts() {
        if (sPostHighlightHelper == null) return null;
        if (sPostHighlightHelper.getHighlightedPosts() == null) return null;
        if (sPostHighlightHelper.getHighlightedPosts().size() == 0) return null;

        return sPostHighlightHelper.getHighlightedPosts();
    }

    public boolean stoppedSelecting() {
        return sPostHighlightHelper.stopSelecting();
    }


    /* this method is called when we post a bus event with a PostUpdate entity */
    @Subscribe
    public void updateFavorites(PostUpdate postUpdate) {
        if (postUpdate.getType() == PostUpdate.Type.ADDED) {
            mPostAdapter.notifyItemInserted(postUpdate.getIndex());
            /* new post added, scroll to top only if our first visible item is the next one */
            if (linearLayoutManager.findFirstVisibleItemPosition() == 1)
                mRecyclerView.scrollToPosition(0);
        }
        if (postUpdate.getType() == PostUpdate.Type.REMOVED) {
            mPostAdapter.notifyItemRemoved(postUpdate.getIndex());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_favorite, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.fragment_favorite_recycler);
        // events
        // other stuff
        linearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(mPostAdapter);


        return view;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


    @Override
    public void onLoadOlder() {
    /* empty because we can't load older favorite feed */
    }


    @Override
    public void onImageLongClicked(View rootView) {
        int index = mRecyclerView.getChildPosition(rootView);
        Post post = mMainFeed.getAllPosts().get(index);
        sPostHighlightHelper.longClickPost(post);
    }

    @Override
    public void onImageClicked(View rootView) {
        int index = mRecyclerView.getChildPosition(rootView);
        Post post = mMainFeed.getAllPosts().get(index);

        // if we can open the image (we are not selecting anything now)
        if (sPostHighlightHelper.clickPost(post)) {
            /* no bitmap yet, return */
            if (post.getBitmap() == null) return;
            /* show the fragment with the image */
            PostDialogFragment postDialogFragment = new PostDialogFragment();
            postDialogFragment.setPost(post);
            postDialogFragment.show(getActivity().getSupportFragmentManager(), "");
        }
    }

    @Override
    public void onSettingsClicked(View view, View targetView) {
        int position = mRecyclerView.getChildPosition(view);
        Post post = mMainFeed.getAllPosts().get(position);

        PopupMenuHelper.createPostMenu(targetView, post);

    }


}
