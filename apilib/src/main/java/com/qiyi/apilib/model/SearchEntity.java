package com.qiyi.apilib.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * 搜索接口
 * Created by Xingfeng on 2017-05-24.
 */
public class SearchEntity extends BaseEntity {

    @SerializedName("data")
    public List<VideoInfo> videoList = new ArrayList<>();

}
