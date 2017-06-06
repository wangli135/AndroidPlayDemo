package com.qiyi.openapi.demo.activity;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;

import com.qiyi.openapi.demo.R;
import com.qiyi.openapi.demo.fragment.CategoryFragment;
import com.qiyi.openapi.demo.fragment.RecommendFragment;
import com.qiyi.openapi.demo.model.ChannelID;

/**
 * Created by Xingfeng on 2017-05-25.
 */

public class MainActivity extends BaseActivity {

    private TabLayout channelTabLayout;
    private ViewPager channelViewPager;
    private String[] channelTitles;
    private FragmentStatePagerAdapter mAdapter;
    private Fragment[] channelFragments;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        super.initView();

        Toolbar toolbar = (Toolbar) mRootView.findViewById(R.id.fragment_toolbar);
        toolbar.setTitle(R.string.app_name);
        setSupportActionBar(toolbar);

        channelTabLayout = (TabLayout) findViewById(R.id.tabLayout);
        channelViewPager = (ViewPager) findViewById(R.id.channelViewPager);
    }

    @Override
    protected void loadData() {
        channelTitles = getResources().getStringArray(R.array.channel_titles);
        initFragments();

        mAdapter = new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return channelFragments[position];
            }

            @Override
            public int getCount() {
                return channelFragments.length;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return channelTitles[position];
            }
        };
        channelViewPager.setAdapter(mAdapter);
        channelTabLayout.setupWithViewPager(channelViewPager);
    }

    private void initFragments() {
        channelFragments = new Fragment[channelTitles.length];
        channelFragments[0] = RecommendFragment.newInstance();
        channelFragments[1] = CategoryFragment.newInstance(ChannelID.TV);
        channelFragments[2] = CategoryFragment.newInstance(ChannelID.FILM);
        channelFragments[3] = CategoryFragment.newInstance(ChannelID.VARIETY);
        channelFragments[4] = CategoryFragment.newInstance(ChannelID.CARTOON);
        channelFragments[5] = CategoryFragment.newInstance(ChannelID.NEWS);
        channelFragments[6] = CategoryFragment.newInstance(ChannelID.ENTERTAINMENT);
        channelFragments[7] = CategoryFragment.newInstance(ChannelID.FUNNY);
        channelFragments[8] = CategoryFragment.newInstance(ChannelID.CHILD);
    }
}
