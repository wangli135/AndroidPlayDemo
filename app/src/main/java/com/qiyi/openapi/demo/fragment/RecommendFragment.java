package com.qiyi.openapi.demo.fragment;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.qiyi.apilib.model.RecommendEntity;
import com.qiyi.openapi.demo.R;
import com.qiyi.openapi.demo.adapter.RecommendAdapter;
import com.qiyi.openapi.demo.presenter.RecommendContract;
import com.qiyi.openapi.demo.presenter.RecommendPresenter;


/**
 * Created by zhouxiaming on 2017/5/7.
 */

public class RecommendFragment extends BaseFragment implements RecommendContract.IView, OnRefreshListener, OnLoadMoreListener {

    private SwipeToLoadLayout mSwipeToLoadLayout;
    private RecyclerView mRecyclerView;
    private RecommendPresenter mPresenter;
    private RecommendAdapter mAdapter;

    public static RecommendFragment newInstance() {
        return new RecommendFragment();
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_recommend;
    }

    @Override
    protected void initView() {
        super.initView();
        mPresenter = new RecommendPresenter(this);

        mSwipeToLoadLayout = (SwipeToLoadLayout) mRootView.findViewById(R.id.swipeToLoadLayout);
        mSwipeToLoadLayout.setOnRefreshListener(this);
        mSwipeToLoadLayout.setOnLoadMoreListener(this);
        mRecyclerView = (RecyclerView) mRootView.findViewById(R.id.swipe_target);

        GridLayoutManager layoutManager = new GridLayoutManager(mActivity, RecommendAdapter.ROW_NUM);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new RecommendAdapter(mActivity);
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
        mPresenter.loadRecommendDetailFromServer(true);
    }

    @Override
    public void onRefresh() {
        mPresenter.loadRecommendDetailFromServer(false);
    }

    @Override
    public void showLoadingView() {
        showLoadingBar();
    }

    @Override
    public void dismissLoadingView() {
        hideLoadingBar();
        dismissRefreshAndLoadMoreView();
    }

    @Override
    public void showEmptyView() {
        dismissRefreshAndLoadMoreView();
    }

    private void dismissRefreshAndLoadMoreView() {
        mSwipeToLoadLayout.setRefreshing(false);
        mSwipeToLoadLayout.setLoadingMore(false);
    }

    @Override
    public void showNetWorkErrorView() {
        Toast.makeText(mActivity, R.string.network_error, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void renderRecommendDetail(RecommendEntity recommendEntity) {
        mAdapter.setData(recommendEntity);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            if (mAdapter.getItemCount() == 0) {
                loadData();
            }
        }
    }

    @Override
    public void showRefreshView() {
        mSwipeToLoadLayout.setRefreshing(true);
    }

    @Override
    public void dismiddRefreshView() {
        mSwipeToLoadLayout.setRefreshing(false);
    }

    @Override
    public void onLoadMore() {

    }
}
