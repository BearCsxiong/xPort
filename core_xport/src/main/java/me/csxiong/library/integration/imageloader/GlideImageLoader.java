package me.csxiong.library.integration.imageloader;

import android.graphics.Bitmap;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

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
                .asBitmap()
                .load(imageRequest.url)
                .listener(new RequestListener<Bitmap>() {
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
                })
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
