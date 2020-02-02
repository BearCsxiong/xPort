package me.csxiong.library.integration.imageloader;

import android.graphics.Color;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;


import java.io.File;
import java.util.ArrayList;

import javax.inject.Inject;

import me.csxiong.library.base.APP;
import me.csxiong.library.di.component.DaggerSystemComponent;

import static me.csxiong.library.integration.imageloader.IImageLoader.WEBP_200;
import static me.csxiong.library.integration.imageloader.IImageLoader.WEBP_DEFAULT;

/**
 * @Desc : ImageLoader
 * @Author : csxiong - 2020-02-02
 */
public class ImageLoader {

    @Inject
    public IImageLoader _imageLoader;

    private ImageLoader() {
        DaggerSystemComponent.builder()
                .appComponent(APP.get().getAppComponent())
                .build()
                .inject(this);
    }

    private static ImageLoader instance;

    public static IImageLoader get() {
        if (instance == null) {
            synchronized (ImageLoader.class) {
                if (instance == null) {
                    instance = new ImageLoader();
                }
            }
        }
        return instance._imageLoader;
    }

    //network image
    public static ImageRequest url(String url) {
        return instance.createRequest(url);
    }

    public static ImageRequest url(Uri uri) {
        return url(uri.toString());
    }

    public static void downloadFile(String url) {
        get().downloadFile(url, null);
    }

    public static void downloadFiles(ArrayList<String> urls, IImageLoader.onDownloadFilesListener onDownloadListener) {
        for (String url : urls) {
            downloadFile(url, onDownloadListener);
        }
        if (onDownloadListener != null)
            onDownloadListener.onComplete();
    }

    public static void downloadFile(String url, IImageLoader.onDownloadListener onDownloadListener) {
        get().downloadFile(url, onDownloadListener);
    }

    public static File getCacheFile(String url) {
        return get().getCacheFile(url);
    }

    private ImageRequest createRequest(String url) {
        return new ImageRequest(url);
    }

    public static class ImageRequest {

        String url;

        int cornerTL, cornerTR, cornerBL, cornerBR;

        boolean isCircle;

        int borderWidth = -1;

        int borderColor = Color.WHITE;

        int loadingHolderRes = -1;

        int errorHolderRes = -1;

        int width = -1;

        int height = -1;

        //是否高斯模糊
        boolean isBlur = false;

        //是否scaleType
        boolean isCenterCrop = false;

        //gif是否自动播放
        boolean isAnime = true;

        //是否支持进度条
        boolean isProgressLoading = false;

        //预取
        boolean withPrefetch = false;

        //强制静态解码
        boolean isForceStaticImage = false;

        int webpType = WEBP_200;

        IImageLoader.OnLoadListener onLoadListener;

        public ImageRequest(String url) {
            this.url = url;
        }

        public ImageRequest withCorner(int corner) {
            cornerTL = cornerTR = cornerBL = cornerBR = corner;
            return this;
        }

        public ImageRequest withCorner(int cornerTL, int cornerTR, int cornerBL, int cornerBR) {
            this.cornerTL = cornerTL;
            this.cornerTR = cornerTR;
            this.cornerBL = cornerBL;
            this.cornerBR = cornerBR;
            return this;
        }

        public ImageRequest withLoadingHolder(int placeHolderRes) {
            this.loadingHolderRes = placeHolderRes;
            return this;
        }

        public ImageRequest withErrorHolder(int errorHolder)
        {
            this.errorHolderRes = errorHolder;
            return this;
        }

        public ImageRequest withNonHolder() {
            this.loadingHolderRes = -2;
            return this;
        }

        public ImageRequest withCircle() {
            this.isCircle = true;
            return this;
        }

        public ImageRequest withBorderWidth(int width) {
            this.borderWidth = width;
            return this;
        }

        public ImageRequest withBorderColor(int color) {
            this.borderColor = color;
            return this;
        }

        public ImageRequest withArtwork() {
            //原图
            this.webpType = WEBP_DEFAULT;
            return this;
        }

        public ImageRequest withWebp(int webpType) {
            this.webpType = webpType;
            return this;
        }

        public ImageRequest withAnime(boolean isAnime) {
            this.isAnime = isAnime;
            return this;
        }

        public ImageRequest withSize(int width, int height) {
            this.width = width;
            this.height = height;
            return this;
        }

        public ImageRequest withCenterCrop() {
            this.isCenterCrop = true;
            return this;
        }

        public ImageRequest withPrefetch() {
            this.withPrefetch = true;
            return this;
        }

        public ImageRequest withForceStaticDecode() {
            this.isForceStaticImage = true;
            return this;
        }

        public ImageRequest withBlur() {
            this.isBlur = true;
            return this;
        }

        public ImageRequest withLoadListener(IImageLoader.OnLoadListener onLoadListener) {
            this.onLoadListener = onLoadListener;
            return this;
        }

        public ImageRequest withProgressLoading() {
            this.isProgressLoading = true;
            return this;
        }

        public void preLoad() {
            instance._imageLoader.preLoad(this);
        }

        public void into(View view) {
            instance._imageLoader.load(view, this);
        }
    }

    public static boolean isGif(String url) {
        if (TextUtils.isEmpty(url)) {
            return false;
        }
        return url.contains(".gif") || url.contains(".GIF");
    }
}
