package com.qiyi.openapi.demo.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.qiyi.apilib.ApiLib;
import com.qiyi.apilib.model.BaseEntity;
import com.qiyi.apilib.model.ChannelDetailEntity;
import com.qiyi.apilib.model.VideoInfo;
import com.qiyi.apilib.utils.ImageUtils;
import com.qiyi.apilib.utils.StringUtils;
import com.qiyi.apilib.utils.UiUtils;
import com.qiyi.openapi.demo.QYPlayerUtils;
import com.qiyi.openapi.demo.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Xingfeng on 2017-06-06.
 */
public class CategoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private int[] screen;
    Activity context;
    private List<BaseEntity> entityList = new ArrayList<BaseEntity>();

    public CategoryAdapter(Activity context) {
        this.context = context;
        screen = UiUtils.getScreenWidthAndHeight(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.card_video_info_item, parent, false);
        return new VideoInfoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        ((VideoInfoViewHolder) holder).setData(position);

    }

    @Override
    public int getItemCount() {
        return entityList.size();
    }

    public void setData(ChannelDetailEntity entity) {
        reAssembleData(entity);
        this.notifyDataSetChanged();
    }

    private void reAssembleData(ChannelDetailEntity entity) {
        if (entity == null) {
            return;
        }

        List<VideoInfo> videoInfos = entity.dataEntity.videoInfoList;
        if (videoInfos == null || videoInfos.size() < 1) {
            return;
        }

        entityList.clear();
        entityList.addAll(videoInfos);
    }

    class VideoInfoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView cover;
        TextView name;
        TextView playCount;
        TextView snsScore;

        public VideoInfoViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.item_image_name);
            playCount = (TextView) itemView.findViewById(R.id.item_image_description);
            snsScore = (TextView) itemView.findViewById(R.id.item_image_description2);
            cover = (ImageView) itemView.findViewById(R.id.item_image_img);
            itemView.setOnClickListener(this);
            resizeImageView(cover);
        }

        /**
         * 视频图片为竖图展示取分辨率为：_120_160
         *
         * @param cover
         */
        private void resizeImageView(ImageView cover) {
            int screenWidth = context.getResources().getDisplayMetrics().widthPixels;
            int coverWidth = (screenWidth - context.getResources().getDimensionPixelSize(R.dimen.video_card_margin_margin_horizontal) * 2 * 4) / 3;
            int coverHeight = (int) (160.0f / 120.0f * coverWidth);
            cover.setMinimumHeight(coverHeight);
            cover.setMaxHeight(coverHeight);
            cover.setMaxHeight(coverWidth);
            cover.setMinimumWidth(coverWidth);
            cover.setAdjustViewBounds(false);
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) cover.getLayoutParams();
            lp.height = coverHeight;
            lp.width = coverWidth;
            cover.setLayoutParams(lp);
        }

        public void setData(int position) {
            VideoInfo video = (VideoInfo) entityList.get(position);
            if (!StringUtils.isEmpty(video.shortTitle)) {
                name.setText(video.shortTitle);
            } else {
                name.setText("");
            }

            if (!StringUtils.isEmpty(video.playCountText)) {
                playCount.setText(context.getString(R.string.play_count, video.playCountText));
            } else {
                playCount.setText("");
            }

            if (!StringUtils.isEmpty(video.snsScore)) {
                snsScore.setText(context.getString(R.string.sns_score, video.snsScore));
            } else {
                snsScore.setText("");
            }

            Glide.clear(cover); //清除缓存
            Glide.with(ApiLib.CONTEXT).load(ImageUtils.getRegImage(video.img, ImageUtils.IMG_260_360)).animate(R.anim.alpha_on).into(cover);

        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            BaseEntity dataObj = entityList.get(position);
            if (dataObj instanceof VideoInfo) {
                VideoInfo videoInfo = (VideoInfo) dataObj;
                QYPlayerUtils.jumpToPlayerActivity(context, videoInfo.aId, videoInfo.tId);
            }
        }
    }

}