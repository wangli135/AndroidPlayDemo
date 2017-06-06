package com.qiyi.apilib.utils;

import android.content.Context;
import android.os.Environment;

import com.qiyi.apilib.ApiLib;

import java.io.File;

/**
 * Created by Xingfeng on 2017-06-06.
 */

public class FileUtils {

    public static File getDiskCacheDir(String fileName) {

        Context context = ApiLib.CONTEXT;
        boolean externalStorageAvailable = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);

        String cachePath;

        if (externalStorageAvailable) {
            File file = context.getExternalCacheDir();
            if (file == null) {
                cachePath = Environment.getDataDirectory().getPath();
            } else
                cachePath = context.getExternalCacheDir().getPath();
        } else {
            cachePath = context.getCacheDir().getPath();
        }

        return new File(cachePath + File.separator + fileName);
    }

}
