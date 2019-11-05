package com.base.project.ui;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.WindowManager;

import com.base.project.R;
import com.base.project.databinding.ActivityMainBinding;
import com.base.project.ui.main.MainViewModel;

import java.util.Date;

import me.csxiong.library.base.XActivity;
import me.csxiong.library.utils.XDisplayUtil;
import me.csxiong.library.utils.XTimerTools;

public class MainActivity extends XActivity<ActivityMainBinding, MainViewModel> {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        mViewBinding.s1.setWidth(XDisplayUtil.dpToPxInt(4));
        mViewBinding.s2.setWidth(XDisplayUtil.dpToPxInt(4));
        mViewBinding.h1.setWidth(XDisplayUtil.dpToPxInt(12));
        mViewBinding.h2.setWidth(XDisplayUtil.dpToPxInt(12));
        mViewBinding.m1.setWidth(XDisplayUtil.dpToPxInt(12));
        mViewBinding.m2.setWidth(XDisplayUtil.dpToPxInt(12));
        //轮训
        XTimerTools.infinite(() -> {
            if (mViewBinding != null) {
                long t = System.currentTimeMillis();
                Date date = new Date(t);
                mViewBinding.h1.setNumber(date.getHours() / 10);
                mViewBinding.h2.setNumber(date.getHours() % 10);
                mViewBinding.m1.setNumber(date.getMinutes() / 10);
                mViewBinding.m2.setNumber(date.getMinutes() % 10);
                mViewBinding.s1.setNumber(date.getSeconds() / 10);
                mViewBinding.s2.setNumber(date.getSeconds() % 10);
                ObjectAnimator.ofFloat(mViewBinding.cl, "Alpha", 1, 0, 1)
                        .setDuration(1000)
                        .start();
            }
        }, 1000)
                .start();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initView() {
    }

    @Override
    public void initData() {

    }

}
