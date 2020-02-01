package me.csxiong.camera.ui.photo;

import android.os.Bundle;

import me.csxiong.camera.R;
import me.csxiong.camera.album.ImageEntity;
import me.csxiong.camera.databinding.FragmentPhotoBinding;
import me.csxiong.camera.ui.album.AlbumRepository;
import me.csxiong.library.base.BaseFragment;
import me.csxiong.library.integration.imageloader.ImageLoader;

public class PhotoFragment extends BaseFragment<FragmentPhotoBinding> {

    @Override
    public int getLayoutId() {
        return R.layout.fragment_photo;
    }

    @Override
    public void initView() {
        Bundle bundle = getArguments();
        int position = bundle.getInt("position");

        ImageEntity entity = AlbumRepository.getInstance().getAlbumDataEvent().getValue().get(position);
        ImageLoader.url(entity.getDisplayPath())
                .into(mViewBinding.iv);
    }

    @Override
    public void initData() {

    }
}
