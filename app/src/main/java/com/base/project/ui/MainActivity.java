package com.base.project.ui;

import android.support.v7.widget.LinearLayoutManager;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.base.project.R;
import com.base.project.databinding.ActivityMainBinding;
import com.base.project.ui.main.MainViewModel;

import java.util.ArrayList;
import java.util.List;

import me.csxiong.library.base.XActivity;
import me.csxiong.library.integration.adapter.AdapterDataBuilder;
import me.csxiong.library.integration.adapter.XRecyclerViewAdapter;

/**
 * @Desc : 主页
 * @Author : csxiong - 2019-11-13
 */
@Route(path = "/app/main", name = "主页")
public class MainActivity extends XActivity<ActivityMainBinding, MainViewModel> {

    private XRecyclerViewAdapter mAdapter;

    private List<FeatureBean> mDataList = new ArrayList<>();

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initView() {
        mAdapter = new XRecyclerViewAdapter(this);
        mViewBinding.rv.setLayoutManager(new LinearLayoutManager(this));
        mViewBinding.rv.setAdapter(mAdapter);
    }

    @Override
    public void initData() {
        mDataList.add(new FeatureBean("多人拍照的手势控件", "/login/capture"));
        mDataList.add(new FeatureBean("新多人拍照的手势控件", "/login/new/capture"));
        mAdapter.updateItemEntities(AdapterDataBuilder.create()
                .addEntities(mDataList, FeatureViewHolder.class).build());

        mAdapter.setOnItemChildClickListener((position, item, view) -> {
            Object entity = item.getEntity();
            if (entity instanceof FeatureBean) {
                ARouter.getInstance()
                        .build(((FeatureBean) entity).getRoute())
                        .navigation(this);
            }
        });
    }

}
