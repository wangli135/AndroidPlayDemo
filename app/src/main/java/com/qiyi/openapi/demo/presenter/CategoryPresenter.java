package com.qiyi.openapi.demo.presenter;

import com.qiyi.apilib.ApiLib;
import com.qiyi.apilib.model.ChannelDetailEntity;
import com.qiyi.apilib.net.ApiClient;
import com.qiyi.apilib.net.ApiParamsGen;
import com.qiyi.apilib.net.ApiURL;
import com.qiyi.apilib.service.ApiService;
import com.qiyi.apilib.utils.LogUtils;
import com.qiyi.apilib.utils.NetWorkTypeUtils;
import com.qiyi.openapi.demo.model.ChannelID;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Xingfeng on 2017-06-06.
 */

public class CategoryPresenter implements CategoryContract.IPresenter {

    private CategoryContract.IView mView;
    private int mChannelId;
    private int pageIndex;
    private static final int DEFAULT_SIZE = 30;

    public CategoryPresenter(CategoryContract.IView mView, int channelId) {
        this.mView = mView;
        this.mChannelId = channelId;
    }

    @Override
    public void resetPageIndex() {
        pageIndex = 1;
    }

    @Override
    public void loadChannelListFromServer(boolean showLoadingView) {
        if (!NetWorkTypeUtils.isNetAvailable(ApiLib.CONTEXT)) {
            mView.showNetWorkErrorView();
            return;
        }
        if (showLoadingView) {
            this.mView.showLoadingView();
        } else {
            this.mView.showRefreshView();
        }

        ApiService apiService = ApiClient.getAPiService(ApiURL.API_REALTIME_HOST);
        apiService.qiyiChannelDetail(ApiParamsGen.genChannelDetailParams(String.valueOf(mChannelId), getChannelNameById(mChannelId), pageIndex, DEFAULT_SIZE))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<ChannelDetailEntity>() {

                    @Override
                    public void onNext(ChannelDetailEntity channelDetailEntity) {
                        mView.dismissLoadingView();
                        mView.dismiddRefreshView();
                        if (channelDetailEntity != null) {
                            pageIndex++;
                            mView.renderChannelList(channelDetailEntity);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.dismissLoadingView();
                    }

                    @Override
                    public void onComplete() {
                        mView.dismissLoadingView();
                    }
                });
    }


    /**
     * 根据Channel ID获取Channel Name
     *
     * @param mChannelId
     * @return
     */
    private String getChannelNameById(int mChannelId) {

        switch (mChannelId) {

            case ChannelID.TV:
                return "电视剧";
            case ChannelID.FILM:
                return "电影";
            case ChannelID.CARTOON:
                return "动漫";
            case ChannelID.VARIETY:
                return "综艺";
            case ChannelID.NEWS:
                return "资讯";
            case ChannelID.FUNNY:
                return "搞笑";
            case ChannelID.ENTERTAINMENT:
                return "娱乐";
            case ChannelID.CHILD:
                return "少儿";
            default:
                return "电视剧";
        }

    }

    @Override
    public boolean hasMore() {
        return false;
    }

    /**
     * 加载更多的Video
     */
    @Override
    public void loadMoreChannelListFromServer() {

        if (!NetWorkTypeUtils.isNetAvailable(ApiLib.CONTEXT)) {
            mView.showNetWorkErrorView();
            return;
        }

        ApiService apiService = ApiClient.getAPiService(ApiURL.API_REALTIME_HOST);
        apiService.qiyiChannelDetail(ApiParamsGen.genChannelDetailParams(String.valueOf(mChannelId), getChannelNameById(mChannelId), pageIndex, DEFAULT_SIZE))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<ChannelDetailEntity>() {

                    @Override
                    public void onNext(ChannelDetailEntity channelDetailEntity) {
                        mView.dismissLoadMoreView();
                        if (channelDetailEntity != null) {
                            LogUtils.i("TAG", channelDetailEntity.toString());
                            pageIndex++;
                            mView.renderMoreChannelList(channelDetailEntity);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.dismissLoadMoreView();
                    }

                    @Override
                    public void onComplete() {
                        mView.dismissLoadingView();
                    }
                });

    }
}
