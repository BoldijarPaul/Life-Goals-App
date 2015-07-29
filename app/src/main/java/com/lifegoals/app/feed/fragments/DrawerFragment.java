package com.lifegoals.app.feed.fragments;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.lifegoals.app.facebookfanpageapi.helper.ApiPreferences;
import com.lifegoals.app.facebookfanpageapi.helper.Constants;
import com.lifegoals.app.R;
import com.lifegoals.app.feed.other.UIHelper;
import com.gc.materialdesign.views.CheckBox;

import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class DrawerFragment extends Fragment {


    private ScrollView rootScrollView;
    // buttons
    private View rateView;
    private View shareView;
    private View feedbackView;

    public DrawerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_drawer, container, false);

        /* load the views */
        rootScrollView = (ScrollView) view.findViewById(R.id.fragment_drawer_root);

        rateView = view.findViewById(R.id.fragment_drawer_rate);
        shareView = view.findViewById(R.id.fragment_drawer_share);
        feedbackView = view.findViewById(R.id.fragment_drawer_feedback);


        // events


        /* setting up the events for the drawer items */
        shareView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                share();
            }
        });
        rateView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rate();
            }
        });
        feedbackView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                feedback();
            }
        });


        return view;
    }




    /* method called when clicking the rate item */
    private void rate() {
        UIHelper.goToUrl(getString(R.string.msg_app_url), getActivity());
    }

    /* method called when clicking the share item */
    private void share() {
        UIHelper.shareText(getString(R.string.msg_share_text), getActivity());
    }

    /* method called when clicking the feedback item */
    private void feedback() {
        UIHelper.startEmailIntent(Constants.FEEDBACK_EMAIL, getActivity().getString(R.string.msg_feedback), getActivity());
    }

    /* this method is being called from the base app activity, when the drawer is sliding */
    public void setAlpha(float alpha) {
        if (rootScrollView != null) {
             /* method only available for API 11 + */
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                rootScrollView.setAlpha(alpha);
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
}
