package com.lifegoals.app.feed.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;
import com.lifegoals.app.facebookfanpageapi.entities.Post;
import com.lifegoals.app.facebookfanpageapi.helper.ApiPreferences;
import com.lifegoals.app.facebookfanpageapi.helper.AsyncTaskHelper;
import com.lifegoals.app.facebookfanpageapi.helper.BusHelper;
import com.lifegoals.app.facebookfanpageapi.helper.ColorHelper;
import com.lifegoals.app.facebookfanpageapi.helper.PostHelper;
import com.lifegoals.app.R;
import com.lifegoals.app.feed.adapters.TabsAdapter;
import com.lifegoals.app.feed.fragments.FavoriteFragment;
import com.lifegoals.app.feed.fragments.FeedFragment;
import com.lifegoals.app.feed.fragments.ImagesCollageDialogFragment;
import com.lifegoals.app.feed.helper.Utils;
import com.lifegoals.app.feed.other.SelectingPostState;
import com.lifegoals.app.feed.other.UIHelper;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;
import com.squareup.otto.Subscribe;

import java.util.List;


public class HomeActivity extends BaseAppActivity implements FeedFragment.FeedFragmentListener, ViewPager.OnPageChangeListener {


    public HomeActivity() {
        BusHelper.bus.register(this);
    }

    /* colors that we are going to work with to change our toolbar & tabs */
    private int mFirstColor, mSecondColor, mFirstColorDark, mSecondColorDark;
    public static final String EXTRA_AUTO_LOAD = "extraaloadauto";

    /* views */
    private ViewPager mViewPager;
    private View mHomeIcon;
    private Toolbar mToolbar;
    private PagerSlidingTabStrip mTabs;
    private FloatingActionMenu mActionMenu;
    private View mRemoveSelectionsButton;
    private View mCreateCollageButton;


    /* other members */
    private FeedFragment mFeedFragment;
    private FavoriteFragment mFavoriteFragment;
    private FloatingActionButton mRootFloatingButton;


    @Override
    public void onBackPressed() {
        boolean feedStopped = mFeedFragment.stoppedSelecting();
        boolean favoriteStopped = mFavoriteFragment.stoppedSelecting();
        /* stop and don't close the activity if at least one of the tabs closed the selections */
        if (feedStopped || favoriteStopped) return;

        super.onBackPressed();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        /* load the colors */
        mFirstColor = getResources().getColor(R.color.primary);
        mSecondColor = getResources().getColor(R.color.secondary);
        mFirstColorDark = getResources().getColor(R.color.primary_dark);
        mSecondColorDark = getResources().getColor(R.color.secondary_dark);

        /* the setContentView of the BaseAppActivity will add the navigation drawer */
        setContentView(R.layout.activity_home);


        /* loading the views */
        mToolbar = (Toolbar) findViewById(R.id.my_awesome_toolbar);
        mHomeIcon = findViewById(R.id.activity_home_toolbar_home);
        mViewPager = (ViewPager) findViewById(R.id.activity_home_pager);
        mTabs = (PagerSlidingTabStrip) findViewById(R.id.activity_home_tabs);




        /* we need an instance of the feed fragment */
        mFeedFragment = new FeedFragment();
        mFavoriteFragment = new FavoriteFragment();

        /* binding the tabs with the viewpager */
        mViewPager.setAdapter(new TabsAdapter(getSupportFragmentManager(), mFeedFragment, mFavoriteFragment));
        mTabs.setViewPager(mViewPager);
        mTabs.setOnPageChangeListener(this);
        /* if we set the load at start to true, the feed will be refreshed one more time at the first load  */
        mFeedFragment.setLoadAtStart(getIntent().getBooleanExtra(EXTRA_AUTO_LOAD, false));




        /* call the initialize method from the BaseAppActivity, which will setup the events for the home icon */
        initialize();



        /* create settings buttons */
        mRootFloatingButton = UIHelper.createFloatingActionButton(R.color.primary, R.drawable.ic_plus_grey, this);
        SubActionButton.Builder subActionBuilder = new SubActionButton.Builder(this).setTheme(SubActionButton.THEME_DARKER);
        mRemoveSelectionsButton = UIHelper.createFloatingSubIcon(subActionBuilder, R.drawable.ic_remove_grey, this);
        mCreateCollageButton = UIHelper.createFloatingSubIcon(subActionBuilder, R.drawable.ic_images_grey, this);
        mActionMenu = new FloatingActionMenu.Builder(this)
                .setStartAngle(200)
                .setEndAngle(250)
                .setRadius(120)
                .addSubActionView(mRemoveSelectionsButton)
                .addSubActionView(mCreateCollageButton)
                .attachTo(mRootFloatingButton)
                .build();
        /**/
        /* events for items */
        mRemoveSelectionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearButtonClicked();
            }
        });
        mCreateCollageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                collageButtonClicked();
            }
        });
        /* hide or close the menu if we have some selected posts or not*/
        updateCollageIcon();


    }

    private void collageButtonClicked() {
        createCollage();
    }

    private void clearButtonClicked() {
        mFeedFragment.stoppedSelecting();
        mFavoriteFragment.stoppedSelecting();
    }

    /* this method will change the visibility of the buttons if we have selected or not some posts */
    private void updateCollageIcon() {
        mRootFloatingButton.post(new Runnable() {
            @Override
            public void run() {
                if (getSelectedPosts() == null) {
                    mActionMenu.close(false);
                    mRootFloatingButton.setVisibility(View.INVISIBLE);
                    mCreateCollageButton.setVisibility(View.INVISIBLE);
                    mRemoveSelectionsButton.setVisibility(View.INVISIBLE);
                } else {
                    mRootFloatingButton.setVisibility(View.VISIBLE);
                    mCreateCollageButton.setVisibility(View.VISIBLE);
                    mRemoveSelectionsButton.setVisibility(View.VISIBLE);

                }
            }
        });
    }

    /* returns the selected posts , null if not exist*/
    private List<Post> getSelectedPosts() {
        List<Post> selectedPosts = new java.util.ArrayList<>();
        if (mFeedFragment.getSelectedPosts() != null)
            selectedPosts.addAll(mFeedFragment.getSelectedPosts());
        if (mFavoriteFragment.getSelectedPosts() != null)
            selectedPosts.addAll(mFavoriteFragment.getSelectedPosts());

        if (selectedPosts.size() == 0) return null; /* no highlighted posts */

        return selectedPosts;
    }

    /* we want to make a collage*/
    private void createCollage() {
        List<Post> selectedPosts = getSelectedPosts();
        if (selectedPosts == null) {
            UIHelper.showErrorMessage(this, getString(R.string.msg_no_img_selection));
            return;
        }
        ImagesCollageDialogFragment imagesCollageDialogFragment = new ImagesCollageDialogFragment();
        imagesCollageDialogFragment.setRetainInstance(true);
        imagesCollageDialogFragment.setPosts(selectedPosts);
        imagesCollageDialogFragment.show(getSupportFragmentManager(), "");
    }

    /* method needed from the BaseAppActivity, returning our home view, which is inside our toolbar */
    @Override
    public View getHomeIcon() {
        return mHomeIcon;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        /* clear any data that is in the intent */
        if (getIntent() != null && getIntent().getExtras() != null)
            getIntent().getExtras().clear();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        /* we got a new intent because we clicked the notification, we need to load newer feed */
        super.onNewIntent(intent);
        boolean autoLoad = intent.getBooleanExtra(EXTRA_AUTO_LOAD, false);
        mFeedFragment.setLoadAtStart(autoLoad);
        /* and scroll to the first tab */
        mViewPager.setCurrentItem(0);
    }


    @Override
    public void onScroll(int state) {

    }

    /* method called when we are scrolling the tabs */
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (position == 1 && positionOffsetPixels == 0) return;
        float ratio = positionOffset;
        if (ratio < 0.1f) ratio = 0;
        if (ratio > 0.9f) ratio = 1;
        /* find out the new color for our activity */
        int newColor = ColorHelper.mixTwoColors(mFirstColorDark, mFirstColor, ratio);
        mTabs.setBackgroundColor(newColor);
        mToolbar.setBackgroundColor(newColor);
        /* set the color for the status bar */
        setStatusBarNewColor(ColorHelper.mixTwoColors(mSecondColorDark, mFirstColorDark, ratio));
    }

    /**
     * this methods sets the status bar color, only for devices with API 21 +
     *
     * @param i the color
     */
    private void setStatusBarNewColor(int i) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(i);
        }

    }

    /* we got update from the post highlight helper */
    @Subscribe
    public void updateFloatingActionButton(SelectingPostState selectingPostState) {
        updateCollageIcon();
        Utils.vibrate(70, this);
    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


}
