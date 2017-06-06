package com.qiyi.openapi.demo.presenter;

import com.qiyi.apilib.model.ChannelDetailEntity;

/**
 * Created by xingfeng on 2017/6/6.
 */

public interface CategoryContract {
    interface IView extends IBaseContractView {
        void renderChannelList(ChannelDetailEntity channelDetailEntity);
    }

    interface IPresenter extends IBaseContrackPresenter {
        void resetPageIndex();

        void loadChannelListFromServer(boolean showLoadingView);

        boolean hasMore();
    }
}
