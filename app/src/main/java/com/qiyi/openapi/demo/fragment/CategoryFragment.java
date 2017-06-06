package com.qiyi.openapi.demo.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.qiyi.apilib.model.ChannelDetailEntity;
import com.qiyi.openapi.demo.R;
import com.qiyi.openapi.demo.adapter.CategoryAdapter;
import com.qiyi.openapi.demo.adapter.RecommendAdapter;
import com.qiyi.openapi.demo.presenter.CategoryContract;
import com.qiyi.openapi.demo.presenter.CategoryPresenter;
import com.qiyi.openapi.demo.util.Constants;


/**
 * 分类页面
 * Created by xingfeng on 2017/5/7.
 */
public class CategoryFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, CategoryContract.IView {

    private SwipeRefreshLayout mRefreshLayout;
    private RecyclerView mRecyclerView;
    private CategoryPresenter mPresenter;
    private CategoryAdapter mAdapter;

    public static CategoryFragment newInstance(int channelId) {
        CategoryFragment fragment = new CategoryFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(Constants.KEY.CHANNEL_ID, channelId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_category;
    }

    @Override
    protected void initView() {
        super.initView();

        int channelId = getArguments().getInt(Constants.KEY.CHANNEL_ID);
        mPresenter = new CategoryPresenter(this, channelId);

        mRefreshLayout = (SwipeRefreshLayout) mRootView.findViewById(R.id.swipe_refresh_layout);
        mRecyclerView = (RecyclerView) mRootView.findViewById(R.id.recycler_view);
        mRefreshLayout.setOnRefreshListener(this);
        mRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);


        GridLayoutManager layoutManager = new GridLayoutManager(mActivity, RecommendAdapter.ROW_NUM);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new CategoryAdapter(mActivity);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mAdapter);


    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }


    @Override
    protected void loadData() {
        super.loadData();
        mPresenter.loadChannelListFromServer(true);
    }

    @Override
    public void onRefresh() {
        mPresenter.loadChannelListFromServer(false);
    }

    @Override
    public void showLoadingView() {
        showLoadingBar();
    }

    @Override
    public void dismissLoadingView() {
        hideLoadingBar();
        mRefreshLayout.setRefreshing(false);
    }

    @Override
    public void showEmptyView() {
        mRefreshLayout.setRefreshing(false);
    }

    @Override
    public void showNetWorkErrorView() {
        mRefreshLayout.setRefreshing(false);
        Toast.makeText(mActivity, R.string.network_error, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void renderChannelList(ChannelDetailEntity channelDetailEntity) {
        mRefreshLayout.setRefreshing(false);
        mAdapter.setData(channelDetailEntity);
    }
}
