package me.csxiong.library.integration.imageloader;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

import java.io.File;

import me.csxiong.library.base.APP;

/**
 * @Desc : Glide内核的图片加载器
 * @Author : csxiong create on 2019/12/17
 */
public class GlideImageLoader implements IImageLoader {

    @SuppressLint("CheckResult")
    @Override
    public void load(View view, ImageLoader.ImageRequest imageRequest) {
        if (!(view instanceof ImageView)) {
            return;
        }
        RequestBuilder<Bitmap> builder = Glide.with(APP.get())
                .asBitmap()
                //1.目标地址加载
                .load(imageRequest.url);
        //2.监听
        builder.listener(new RequestListener<Bitmap>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                if (imageRequest.onLoadListener != null) {
                    imageRequest.onLoadListener.onError();
                }
                return false;
            }

            @Override
            public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                if (imageRequest.onLoadListener != null) {
                    imageRequest.onLoadListener.onSuccess(resource.getWidth(), resource.getHeight());
                }
                return false;
            }
        });
        RequestOptions options = RequestOptions.overrideOf(imageRequest.width, imageRequest.height);
        options.placeholder(imageRequest.loadingHolderRes);
        options.error(imageRequest.errorHolderRes);
        if (imageRequest.isCenterCrop) {
            options.centerCrop();
        }
        if (!imageRequest.isAnime) {
            options.dontAnimate();
        }
        //TODO 需要支持多不同角度圆角问题
        if (imageRequest.cornerTL > 0) {
            options.transform(new RoundedCorners(imageRequest.cornerTL));
        } else if (imageRequest.isCircle) {
            options.transform(new RoundedCorners(Integer.MAX_VALUE));
        }
        builder.apply(options);
        builder.into((ImageView) view);
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
