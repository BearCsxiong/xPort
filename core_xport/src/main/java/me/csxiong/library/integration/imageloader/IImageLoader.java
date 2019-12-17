package me.csxiong.library.integration.imageloader;

import android.graphics.Bitmap;
import android.view.View;

import java.io.File;

/**
 * @Desc : 图片加载器抽象
 * @Author : csxiong create on 2019/12/17
 */
public interface IImageLoader {

    int WEBP_DEFAULT = -1;
    int WEBP_200 = 1;
    int WEBP_240 = 2;
    int WEBP_360 = 3;
    int WEBP_720 = 4;

    /**
     * 根据request加载
     *
     * @param view         目标
     * @param imageRequest 加载请求
     */
    void load(View view, ImageLoader.ImageRequest imageRequest);

    /**
     * 预加载
     *
     * @param imageRequest
     */
    void preLoad(ImageLoader.ImageRequest imageRequest);

    /**
     * 下载文件
     *
     * @param url
     * @param onDownloadListener
     */
    void downloadFile(String url, onDownloadListener onDownloadListener);

    /**
     * 获取缓存
     *
     * @param url
     * @return
     */
    File getCacheFile(String url);

    /**
     * 获取Bitmap
     *
     * @param url
     * @param listener
     */
    void getBitmap(String url, OnBitmapDataSourceListener listener);

    interface OnLoadListener {
        void onSuccess(int width, int height);

        void onError();
    }

    interface onDownloadListener {

        void onStart();

        void onSuccess(String path);

        void onError();
    }

    interface onDownloadFilesListener extends onDownloadListener {
        void onComplete();
    }

    interface OnBitmapDataSourceListener {
        void onSuccess(Bitmap bitmap);

        void onError();
    }

}
