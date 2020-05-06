package com.foodeze.rider.ActivitiesAndFragments.Fragments;

import android.content.Context;
import android.os.Bundle;

import com.foodeze.rider.ActivitiesAndFragments.Activities.RiderMainActivity;
import com.foodeze.rider.Adapters.RAdapterPager;
import com.foodeze.rider.Constants.Functions;
import com.foodeze.rider.Utils.CustomViewPager;
import com.foodeze.rider.Utils.RelateToFragment_OnBack.OnBackPressListener;
import com.foodeze.rider.Utils.RelateToFragment_OnBack.RootFragment;
import com.foodeze.rider.Utils.SwipeDirection;
import com.google.android.material.tabs.TabLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.foodeze.rider.R;

/**
 * Created by Dinosoftlabs on 10/18/2019.
 */

public class RPagerMainFragment extends RootFragment {
    public TabLayout tabLayout;
    public static CustomViewPager viewPager;
    RAdapterPager adapter;
    public static int tabIconColor;

    private int[] tabIcons1 = {R.drawable.h_job_not_filled,R.drawable.ic_chat_not_fill,
            R.drawable.h_profile_not_filled};

    private int[] tabIcons = {R.drawable.h_job_filled,R.drawable.ic_chat_filled,
            R.drawable.h_profile_filled};


    View v;
    Context context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.activity_pager, container, false);

        tabLayout = (TabLayout) v.findViewById(R.id.tab_layout);

        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.h_job_filled));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_chat_not_fill));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.r_profile_not_filled));


        if (tabLayout != null) {
            if (RiderMainActivity.RMAINACTIVITY) {

                for (int i = 0; i < tabIcons1.length; i++) {
                    tabLayout.getTabAt(0).setIcon(R.drawable.h_job_filled);
                    tabLayout.getTabAt(i).setIcon(tabIcons1[i]);
                    RiderMainActivity.RMAINACTIVITY = false;
                }
            } else {
                for (int i = 0; i < tabIcons1.length; i++) {
                    tabLayout.getTabAt(i).setIcon(tabIcons1[i]);
                }

            }
            for (int i = 0; i < tabLayout.getTabCount(); i++) {
                TabLayout.Tab tab = tabLayout.getTabAt(i);
                if (tab != null) tab.setCustomView(R.layout.rider_tab_icon);
            }
        }
        viewPager =  v.findViewById(R.id.pager);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RiderMainActivity.width,  RiderMainActivity.height- Functions.dpToPx(getActivity(),72));
        viewPager.setLayoutParams(params);

        selectPage(1);
        adapter = new RAdapterPager(getActivity().getSupportFragmentManager(), tabLayout.getTabCount());
        //adapter.getRegisteredFragment(viewPager.getCurrentItem());
        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));
        viewPager.setAllowedSwipeDirection(SwipeDirection.none);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener()
        {
            @Override
            public void onTabSelected(TabLayout.Tab tabSelected)
            {

                tabSelected.setIcon(tabIcons[tabSelected.getPosition()]);
                    viewPager.setCurrentItem(tabSelected.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tabSelected){


                tabSelected.setIcon(tabIcons1[tabSelected.getPosition()]);

            }

            @Override
            public void onTabReselected(TabLayout.Tab tabSelected){

            }
        });

        return v;
    }


    void selectPage(int pageIndex)
    {
        viewPager.setCurrentItem(pageIndex);

    }


    @Override
    public void onResume() {
        super.onResume();

        if(RiderMainActivity.CHAT_FLAG) {
          selectPage(1);
            RiderMainActivity.CHAT_FLAG = false;
        }

    }


    public boolean onBackPressed() {
        // currently visible tab Fragment
        OnBackPressListener currentFragment = (OnBackPressListener) adapter.getRegisteredFragment(viewPager.getCurrentItem());

        if (currentFragment != null) {
            return currentFragment.onBackPressed();
        }

        // this Fragment couldn't handle the onBackPressed call
        return false;
    }


    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onStop() {
        super.onStop();
    }

}
