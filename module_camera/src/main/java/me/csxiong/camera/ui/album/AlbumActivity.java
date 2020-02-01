package me.csxiong.camera.ui.album;

import android.Manifest;
import android.content.Intent;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewTreeObserver;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;

import java.util.List;
import java.util.Map;

import me.csxiong.camera.R;
import me.csxiong.camera.album.ImageEntity;
import me.csxiong.camera.databinding.ActivityAlbumBinding;
import me.csxiong.ipermission.EnsureAllPermissionCallBack;
import me.csxiong.ipermission.IPermission;
import me.csxiong.library.base.XActivity;
import me.csxiong.library.integration.adapter.AdapterDataBuilder;
import me.csxiong.library.integration.adapter.OnItemChildClickListener;
import me.csxiong.library.integration.adapter.XItem;
import me.csxiong.library.integration.adapter.XRecyclerViewAdapter;
import me.csxiong.library.utils.XDisplayUtil;

/**
 * @Desc : 相册界面
 * @Author : csxiong create on 2019/12/17
 */
@Route(path = "/camera/album")
public class AlbumActivity extends XActivity<ActivityAlbumBinding, AlbumViewModel> {

    private XRecyclerViewAdapter mAdapter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_album;
    }

    @Override
    public void initView() {
        mAdapter = new XRecyclerViewAdapter(this);
        mViewBinding.rv.setLayoutManager(new GridLayoutManager(this, 3));
        mViewBinding.rv.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                int position = parent.getChildAdapterPosition(view);
                outRect.bottom = XDisplayUtil.dpToPxInt(1);
                int lift = position % 3;
                if (lift == 1) {
                    outRect.left = XDisplayUtil.dpToPxInt(1);
                } else if (lift == 2) {
                    outRect.left = XDisplayUtil.dpToPxInt(2);
                }
            }
        });
        mViewBinding.rv.setAdapter(mAdapter);
        new IPermission(this)
                .request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .excute(new EnsureAllPermissionCallBack() {
                    @Override
                    public void onAllPermissionEnable(boolean isEnable) {
                        AlbumRepository.getInstance().loadData();
                    }

                    @Override
                    public void onPreRequest(List<String> requestList) {

                    }
                });

        mAdapter.setOnItemChildClickListener(new OnItemChildClickListener() {
            @Override
            public void onItemChildClick(int position, XItem<?> item, View view) {
                AlbumRepository.getInstance().setCurrentVisiableImage((ImageEntity) item.getEntity());
                ARouter.getInstance()
                        .build("/camera/photo")
                        .withOptionsCompat(ActivityOptionsCompat.makeSceneTransitionAnimation(AlbumActivity.this, view, ViewCompat.getTransitionName(view)))
                        .navigation(AlbumActivity.this);
            }
        });

        ActivityCompat.setExitSharedElementCallback(this, new android.support.v4.app.SharedElementCallback() {
            @Override
            public void onMapSharedElements(List<String> names, Map<String, View> sharedElements) {
                //找到对应的View
                if (!names.isEmpty()) {
                    ImageEntity currentVisiableImage = AlbumRepository.getInstance().getCurrentVisiableImage();
                    if (currentVisiableImage != null) {
                        int position = AlbumRepository.getInstance().getAlbumDataEvent().getValue().indexOf(currentVisiableImage);
                        RecyclerView.ViewHolder viewHolder = mViewBinding.rv.findViewHolderForAdapterPosition(position);
                        if (viewHolder instanceof AlbumViewHolder) {
                            AlbumViewHolder avh = (AlbumViewHolder) viewHolder;
                            sharedElements.put(names.get(0), avh.mViewBinding.iv);
                        }
                    }
                }
            }
        });
    }

    @Override
    public void onActivityReenter(int resultCode, Intent data) {
        super.onActivityReenter(resultCode, data);
        supportPostponeEnterTransition();
        ImageEntity currentVisiableImage = AlbumRepository.getInstance().getCurrentVisiableImage();
        mAdapter.updateItemEntities(AdapterDataBuilder.create()
                .addEntities(AlbumRepository.getInstance().getAlbumDataEvent().getValue(), AlbumViewHolder.class)
                .build());
        if (currentVisiableImage != null) {
            int position = AlbumRepository.getInstance().getAlbumDataEvent().getValue().indexOf(currentVisiableImage);
            mViewBinding.rv.scrollToPosition(position);
            mViewBinding.getRoot().getViewTreeObserver()
                    .addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                        @Override
                        public boolean onPreDraw() {
                            mViewBinding.getRoot().getViewTreeObserver().removeOnPreDrawListener(this);
                            supportStartPostponedEnterTransition();
                            return false;
                        }
                    });
        }
    }

    @Override
    public void initData() {
        AlbumRepository.getInstance().getAlbumDataEvent().observe(this, imageEntities -> {
            mAdapter.updateItemEntities(AdapterDataBuilder.create()
                    .addEntities(imageEntities, AlbumViewHolder.class)
                    .build());
        });
    }
}
