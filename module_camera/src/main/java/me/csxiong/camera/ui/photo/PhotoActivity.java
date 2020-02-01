package me.csxiong.camera.ui.photo;

import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.SharedElementCallback;
import android.support.v4.view.ViewCompat;
import android.transition.Transition;
import android.transition.TransitionListenerAdapter;
import android.view.View;
import android.view.ViewTreeObserver;

import com.alibaba.android.arouter.facade.annotation.Route;

import java.util.List;
import java.util.Map;

import me.csxiong.camera.R;
import me.csxiong.camera.album.ImageEntity;
import me.csxiong.camera.databinding.ActivityPhotoBinding;
import me.csxiong.camera.ui.album.AlbumRepository;
import me.csxiong.library.base.BaseActivity;
import me.csxiong.library.integration.imageloader.ImageLoader;

@Route(path = "/camera/photo", name = "相片界面")
public class PhotoActivity extends BaseActivity<ActivityPhotoBinding> {

    private ImagePagerAdpter mAdapter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_photo;
    }

    @Override
    public void initView() {
        mAdapter = new ImagePagerAdpter(getSupportFragmentManager());
        mViewBinding.vp.setAdapter(mAdapter);

        supportPostponeEnterTransition();
        mViewBinding.getRoot().getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                mViewBinding.getRoot().getViewTreeObserver().removeOnPreDrawListener(this);
                supportStartPostponedEnterTransition();
                return false;
            }
        });

        ActivityCompat.setEnterSharedElementCallback(this, new SharedElementCallback() {
            @Override
            public void onMapSharedElements(List<String> names, Map<String, View> sharedElements) {
                ImageEntity image = AlbumRepository.getInstance().getCurrentVisiableImage();
                if (image != null) {
                    sharedElements.clear();
                    sharedElements.put(image.getDisplayPath(), mViewBinding.ivTransition);
                }
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                getWindow().getEnterTransition().addListener(new TransitionListenerAdapter() {
                    @Override
                    public void onTransitionEnd(Transition transition) {
                        super.onTransitionEnd(transition);
                        mViewBinding.getRoot().post(() -> {
                            mViewBinding.vp.setVisibility(View.VISIBLE);
                            mViewBinding.ivTransition.setVisibility(View.GONE);
                        });
                    }
                });
            }
        }
    }

    @Override
    public void initData() {
        AlbumRepository.getInstance()
                .getAlbumDataEvent().observe(this, list -> {
            mAdapter.updateData(list);

            ImageEntity image = AlbumRepository.getInstance().getCurrentVisiableImage();
            int position = AlbumRepository.getInstance().getAlbumDataEvent().getValue().indexOf(image);
            mViewBinding.vp.setCurrentItem(position);

            mViewBinding.vp.setVisibility(View.GONE);
            mViewBinding.ivTransition.setVisibility(View.VISIBLE);
            ImageLoader.url(image.getDisplayPath())
                    .into(mViewBinding.ivTransition);
            ViewCompat.setTransitionName(mViewBinding.ivTransition, image.getDisplayPath());
        });
    }
}
