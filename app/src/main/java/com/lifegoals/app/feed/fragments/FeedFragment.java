package com.lifegoals.app.feed.fragments;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.lifegoals.app.facebookfanpageapi.entities.Feed;
import com.lifegoals.app.facebookfanpageapi.entities.MainFeed;
import com.lifegoals.app.facebookfanpageapi.entities.Post;
import com.lifegoals.app.facebookfanpageapi.fragments.PostDialogFragment;
import com.lifegoals.app.facebookfanpageapi.helper.ApiPreferences;
import com.lifegoals.app.facebookfanpageapi.helper.Constants;
import com.lifegoals.app.facebookfanpageapi.helper.PopupMenuHelper;
import com.lifegoals.app.facebookfanpageapi.interfaces.FeedLoaderListener;
import com.lifegoals.app.facebookfanpageapi.interfaces.PostAdapterListener;
import com.lifegoals.app.facebookfanpageapi.interfaces.PostHolderListener;
import com.lifegoals.app.facebookfanpageapi.service.FacebookPageApi;
import com.lifegoals.app.R;
import com.lifegoals.app.feed.adapters.PostAdapter;
import com.lifegoals.app.feed.helper.PostHighlightHelper;
import com.lifegoals.app.feed.other.UIHelper;
import com.github.ksoichiro.android.observablescrollview.ObservableRecyclerView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * A simple {@link Fragment} subclass.
 */
public class FeedFragment extends Fragment implements PostAdapterListener, SwipeRefreshLayout.OnRefreshListener, PostHolderListener {


    //views
    @InjectView(R.id.fragment_feed_refresh)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @InjectView(R.id.fragment_feed_recycler)
    RecyclerView mRecyclerView;
    @InjectView(R.id.fragment_feed_layout)
    View mRootLayout;
    //


    private FeedFragmentListener feedFragmentListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        feedFragmentListener = (FeedFragmentListener) activity;

    }

    private MainFeed mMainFeed;
    private PostAdapter mPostAdapter;
    private static PostHighlightHelper sPostHighlightHelper;

    private boolean loading = false;
    private boolean loadAtStart = false;

    public void setLoadAtStart(boolean loadAtStart) {
        if (loadAtStart && !loading && mMainFeed.getFeeds().size() > 0) { // if we aren't loading anything load now
            onRefresh();
            return;
        }
        this.loadAtStart = loadAtStart;
    }

    public FeedFragment() {
        // Required empty public constructor
        if (mMainFeed == null) {
            mMainFeed = new MainFeed();
            mPostAdapter = new PostAdapter(mMainFeed.getAllPosts());
            mPostAdapter.setPostAdapterListener(this);
            mPostAdapter.setPostHolderListener(this);
        }
        if (sPostHighlightHelper == null)
            sPostHighlightHelper = new PostHighlightHelper(mPostAdapter);


    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_feed, container, false);
        ButterKnife.inject(this, view);
        // events
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.primary, R.color.primary_dark);
        // other stuff
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(mPostAdapter);
        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {


            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                feedFragmentListener.onScroll(newState);
            }
        });

        viewsLoaded();
        return view;
    }

    private void viewsLoaded() {


        firstFeedLoad();
    }


    /* returns null if we can't return the selected posts, missing or something is wrong */
    public List<Post> getSelectedPosts() {
        if (sPostHighlightHelper == null) return null;
        if (sPostHighlightHelper.getHighlightedPosts() == null) return null;
        if (sPostHighlightHelper.getHighlightedPosts().size() == 0) return null;

        return sPostHighlightHelper.getHighlightedPosts();
    }

    private void firstFeedLoad() {

        if (mMainFeed != null && mMainFeed.getAllPosts().size() > 0) return;

        MainFeed newMainFeed = ApiPreferences.getSavedMainFeed();
        if (newMainFeed != null) {
            mPostAdapter.getPosts().clear();
            mMainFeed = newMainFeed;
            mPostAdapter.setPosts(mMainFeed.getAllPosts());
            mPostAdapter.notifyDataSetChanged();


            if (loadAtStart) {
                loadAtStart = false;
                onRefresh();
            }
            return;
        }


        loading = true;
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(true);
            }
        });
        FacebookPageApi.getInstance(Constants.PAGE_ID, Constants.TOKEN).newFeedLoad(true, null, new FeedLoaderListener() {
            @Override
            public void onFeedResponse(Feed feed) {
                mSwipeRefreshLayout.setRefreshing(false);
                loading = false;

                if (feed == null) {
                    // error from the server
                    UIHelper.showErrorMessage(getActivity(), getActivity().getString(R.string.msg_check_internet));
                    return;
                }
                mMainFeed.getFeeds().add(feed);
                mMainFeed.getAllPosts().addAll(feed.getPosts());
                mPostAdapter.notifyDataSetChanged();

                ApiPreferences.saveMainFeed(mMainFeed);

                if (loadAtStart) {
                    loadAtStart = false;
                    onRefresh();
                }

            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @Override
    public void onLoadOlder() {
        if (loading) return;
        Feed currentFeed = mMainFeed.getLastFeed();
        if (currentFeed.isLastFeed()) return; // no more posts
        mSwipeRefreshLayout.setRefreshing(true);
        loading = true;
        FacebookPageApi.getInstance(Constants.PAGE_ID, Constants.TOKEN).newFeedLoad(false, currentFeed.getOlderFeedUrl(), new FeedLoaderListener() {
            @Override
            public void onFeedResponse(Feed feed) {
                mSwipeRefreshLayout.setRefreshing(false);
                loading = false;

                if (feed == null) {
                    // error from the server
                    UIHelper.showErrorMessage(getActivity(), getActivity().getString(R.string.msg_check_internet));
                    return;
                }
                if (feed.getPosts().size() == 0) {
                    // no posts
                    UIHelper.showErrorMessage(getActivity(), getActivity().getString(R.string.msg_no_posts));
                    return;
                }
                mMainFeed.getFeeds().add(feed);
                mMainFeed.getAllPosts().addAll(feed.getPosts());
                mPostAdapter.notifyDataSetChanged();


                ApiPreferences.saveMainFeed(mMainFeed);

                if (loadAtStart) {
                    loadAtStart = false;
                    onRefresh();
                }


            }
        });
    }


    public boolean stoppedSelecting() {
        return sPostHighlightHelper.stopSelecting();
    }

    @Override
    public void onRefresh() {

        if (mMainFeed.getFeeds().size() == 0) {
            // we want to refresh but no posts yet
            firstFeedLoad();
            return;
        }
        ApiPreferences.setNewPostsAvailable(false); // we got the new posts notifications

        if (loading) return;
        Feed currentFeed = mMainFeed.getFirstFeed();
        mSwipeRefreshLayout.setRefreshing(true);
        loading = true;

        FacebookPageApi.getInstance(Constants.PAGE_ID, Constants.TOKEN).newFeedLoad(false, currentFeed.getNewerFeedUrl(), new FeedLoaderListener() {
            @Override
            public void onFeedResponse(Feed feed) {
                mSwipeRefreshLayout.setRefreshing(false);
                loading = false;
                if (feed == null) {
                    // error from the server
                    UIHelper.showErrorMessage(getActivity(), getActivity().getString(R.string.msg_check_internet));
                    return;
                }
                if (feed.getPosts().size() == 0) {
                    // no posts
                    UIHelper.showErrorMessage(getActivity(), getActivity().getString(R.string.msg_no_new_posts));
                    return;
                }
                mMainFeed.getFeeds().add(0, feed);
                mMainFeed.getAllPosts().addAll(0, feed.getPosts());
                mPostAdapter.notifyDataSetChanged();


                ApiPreferences.saveMainFeed(mMainFeed);
                if (loadAtStart) {
                    loadAtStart = false;
                    onRefresh();
                }


            }
        });
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);


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


    public static interface FeedFragmentListener {
        public void onScroll(int state);
    }
}
