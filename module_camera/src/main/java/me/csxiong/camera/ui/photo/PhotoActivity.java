package me.csxiong.camera.ui.photo;

import android.app.SharedElementCallback;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.transition.Transition;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;

import java.util.List;
import java.util.Map;

import me.csxiong.camera.R;
import me.csxiong.camera.album.ImageEntity;
import me.csxiong.camera.databinding.ActivityPhotoBinding;
import me.csxiong.camera.ui.album.AlbumRepository;
import me.csxiong.library.base.BaseActivity;
import me.csxiong.library.integration.imageloader.IImageLoader;
import me.csxiong.library.integration.imageloader.ImageLoader;
import me.csxiong.library.utils.gesture.SimpleTransitionAdapter;

@Route(path = "/camera/photo", name = "相片界面")
public class PhotoActivity extends BaseActivity<ActivityPhotoBinding> {

    private ImagePagerAdpter mAdapter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_photo;
    }

    @Override
    public void initView() {
        mAdapter = new ImagePagerAdpter();
        mViewBinding.vp.setAdapter(mAdapter);

        mViewBinding.vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                ImageEntity entity = mAdapter.dataList.get(i);
                AlbumRepository.getInstance().setCurrentVisiableImage(entity);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        //在过渡完成之后 需要有部分需要处理的逻辑 包括隐藏过渡imageView 注册额外的监听
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().getEnterTransition().addListener(new SimpleTransitionAdapter() {
                @Override
                public void onTransitionEnd(@NonNull Transition transition) {
                    mViewBinding.ivTransition.post(() -> {
                        //显示目标
                        mViewBinding.vp.setVisibility(View.VISIBLE);
                        mViewBinding.ivTransition.setVisibility(View.GONE);
                        mViewBinding.ivTransition.setTransitionName(null);
                        //设置Back的过渡动画的目标获取
                        setEnterSharedElementCallback(new SharedElementCallback() {
                            @Override
                            public void onMapSharedElements(List<String> names, Map<String, View> sharedElements) {
                                if (mAdapter.dataList == null || mAdapter.dataList.isEmpty()) {
                                    names.clear();
                                    sharedElements.clear();
                                } else {
                                    ImageEntity entity = AlbumRepository.getInstance().getCurrentVisiableImage();
                                    View view = mViewBinding.vp.findViewWithTag(entity);
                                    if (view != null) {
                                        sharedElements.put(entity.getDisplayPath(), view.findViewById(R.id.iv));
                                    }
                                }
                            }
                        });
                    });
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_OK);
        super.onBackPressed();
    }

    @Override
    public void initData() {
        //监听图片数据
        AlbumRepository.getInstance()
                .getAlbumDataEvent().observe(this, list -> {
            mAdapter.updateDataList(list);

            ImageEntity image = AlbumRepository.getInstance().getCurrentVisiableImage();
            int position = AlbumRepository.getInstance().getAlbumDataEvent().getValue().indexOf(image);
            mViewBinding.vp.setCurrentItem(position, false);

            mViewBinding.vp.setVisibility(View.INVISIBLE);
            mViewBinding.ivTransition.setVisibility(View.VISIBLE);
            ViewCompat.setTransitionName(mViewBinding.ivTransition, image.getDisplayPath());

            //暂停动画
            supportPostponeEnterTransition();

            ImageLoader.url(image.getDisplayPath())
                    .withLoadListener(new IImageLoader.OnLoadListener() {
                        @Override
                        public void onSuccess(int width, int height) {
                            supportStartPostponedEnterTransition();
                        }

                        @Override
                        public void onError() {
                            supportStartPostponedEnterTransition();
                        }
                    })
                    .into(mViewBinding.ivTransition);
        });
    }
}
