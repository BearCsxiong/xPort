package com.base.project.ui;

import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.base.project.R;
import com.base.project.databinding.ActivityMainBinding;
import com.base.project.ui.main.FragmentGroup;
import com.base.project.ui.main.MainViewModel;

import me.csxiong.library.base.XActivity;

/**
 * @Desc : 主页
 * @Author : csxiong - 2019-11-13
 */
@Route(path = "/app/main", name = "主页")
public class MainActivity extends XActivity<ActivityMainBinding, MainViewModel> {

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initView() {
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fl, new FragmentGroup(), "group")
                .commitNowAllowingStateLoss();
    }

    @Override
    public void initData() {

    }

    public void change(View v) {
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fl, new FragmentGroup(), "group1")
                .remove(getSupportFragmentManager().findFragmentByTag("group"))
                .commitNowAllowingStateLoss();
    }

}
