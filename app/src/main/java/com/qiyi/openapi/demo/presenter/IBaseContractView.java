package com.qiyi.openapi.demo.presenter;

/**
 * Created by zhouxiaming on 2017/5/5.
 */

public interface IBaseContractView {

    void showRefreshView();

    void dismiddRefreshView();

    void showLoadingView();

    void dismissLoadingView();

    void showEmptyView();

    void showNetWorkErrorView();
}
