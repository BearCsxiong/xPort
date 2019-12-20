package me.csxiong.camera.ui.album;

import android.Manifest;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;

import java.util.List;

import me.csxiong.camera.R;
import me.csxiong.camera.databinding.ActivityAlbumBinding;
import me.csxiong.ipermission.EnsureAllPermissionCallBack;
import me.csxiong.ipermission.IPermission;
import me.csxiong.library.base.XActivity;
import me.csxiong.library.integration.adapter.AdapterDataBuilder;
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
                        if (isEnable) {
                            mViewModel.loadData();
                        }
                    }

                    @Override
                    public void onPreRequest(List<String> requestList) {

                    }
                });
    }

    @Override
    public void initData() {
        mViewModel.getAlbumDataEvent().observe(this, imageEntities -> {
            mAdapter.updateItemEntities(AdapterDataBuilder.create()
                    .addEntities(imageEntities, AlbumViewHolder.class)
                    .build());
        });
    }
}
