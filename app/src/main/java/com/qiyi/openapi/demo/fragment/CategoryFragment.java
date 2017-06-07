package com.qiyi.openapi.demo.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
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
public class CategoryFragment extends BaseFragment implements CategoryContract.IView, OnRefreshListener, OnLoadMoreListener {

    private SwipeToLoadLayout mSwipeToLoadlayout;
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

        mSwipeToLoadlayout = (SwipeToLoadLayout) mRootView.findViewById(R.id.swipeToLoadLayout);
        mSwipeToLoadlayout.setOnRefreshListener(this);
        mSwipeToLoadlayout.setOnLoadMoreListener(this);
        mRecyclerView = (RecyclerView) mRootView.findViewById(R.id.swipe_target);

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
        mPresenter.resetPageIndex();
        mPresenter.loadChannelListFromServer(false);
    }

    @Override
    public void showLoadingView() {
        showLoadingBar();
    }

    @Override
    public void dismissLoadingView() {
        hideLoadingBar();
    }

    @Override
    public void showEmptyView() {
    }

    @Override
    public void showNetWorkErrorView() {
        Toast.makeText(mActivity, R.string.network_error, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void renderChannelList(ChannelDetailEntity channelDetailEntity) {
        mAdapter.setData(channelDetailEntity);
    }

    @Override
    public void renderMoreChannelList(ChannelDetailEntity channelDetailEntity) {
        mAdapter.addData(channelDetailEntity);
    }

    @Override
    public void onLoadMore() {
        mPresenter.loadMoreChannelListFromServer();
    }

    @Override
    public void showRefreshView() {
        mSwipeToLoadlayout.setRefreshing(true);
    }

    @Override
    public void dismiddRefreshView() {
        mSwipeToLoadlayout.setRefreshing(false);
    }

    @Override
    public void dismissLoadMoreView() {
        mSwipeToLoadlayout.setLoadingMore(false);
    }
}
