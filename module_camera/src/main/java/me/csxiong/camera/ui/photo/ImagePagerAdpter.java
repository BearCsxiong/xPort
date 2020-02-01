package me.csxiong.camera.ui.photo;

import android.support.v4.view.ViewCompat;
import android.view.View;

import me.csxiong.camera.R;
import me.csxiong.camera.album.ImageEntity;
import me.csxiong.library.integration.adapter.XPagerAdapter;
import me.csxiong.library.integration.imageloader.ImageLoader;
import me.csxiong.library.widget.GestureImageView;

/**
 * @Desc : 图片Pager
 * @Author : csxiong - 2020-01-31
 */
public class ImagePagerAdpter extends XPagerAdapter<ImageEntity> {

    @Override
    public int getLayoutId() {
        return R.layout.item_pager_image;
    }

    @Override
    public void onBindView(View view, int position) {
        GestureImageView iv = view.findViewById(R.id.iv);
        ImageEntity entity = dataList.get(position);
        ViewCompat.setTransitionName(iv, entity.getDisplayPath());

        ImageLoader.url(entity.getDisplayPath())
                .into(iv);
    }
}
