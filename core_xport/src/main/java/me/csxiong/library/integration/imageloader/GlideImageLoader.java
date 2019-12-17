package me.csxiong.library.integration.imageloader;

import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.io.File;

import me.csxiong.library.base.APP;

/**
 * @Desc : Glide内核的图片加载器
 * @Author : csxiong create on 2019/12/17
 */
public class GlideImageLoader implements IImageLoader {

    @Override
    public void load(View view, ImageLoader.ImageRequest imageRequest) {
        if (!(view instanceof ImageView)) {
            return;
        }
        Glide.with(APP.get())
                .load(imageRequest.url)
                .into((ImageView) view);
    }

    @Override
    public void preLoad(ImageLoader.ImageRequest imageRequest) {

    }

    @Override
    public void downloadFile(String url, onDownloadListener onDownloadListener) {

    }

    @Override
    public File getCacheFile(String url) {
        return null;
    }

    @Override
    public void getBitmap(String url, OnBitmapDataSourceListener listener) {

    }
}
