package me.csxiong.camera.ui.photo;

import android.support.v4.view.ViewCompat;

import me.csxiong.camera.R;
import me.csxiong.camera.album.ImageEntity;
import me.csxiong.camera.databinding.ItemPagerImageBinding;
import me.csxiong.library.integration.adapter.XPagerAdapter;
import me.csxiong.library.integration.imageloader.ImageLoader;

/**
 * @Desc : 图片Pager
 * @Author : csxiong - 2020-01-31
 */
public class ImagePagerAdpter extends XPagerAdapter<ItemPagerImageBinding, ImageEntity> {

    @Override
    public int getLayoutId() {
        return R.layout.item_pager_image;
    }

    @Override
    public void onBindView(ItemPagerImageBinding mViewBinding, int position) {
        ImageEntity entity = dataList.get(position);
        ViewCompat.setTransitionName(mViewBinding.iv, entity.getDisplayPath());

        ImageLoader.url(entity.getDisplayPath())
                .into(mViewBinding.iv);
    }

}
