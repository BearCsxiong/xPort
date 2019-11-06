package com.base.project.ui;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Window;
import android.view.WindowManager;

import com.base.project.R;
import com.base.project.databinding.ActivityMainBinding;
import com.base.project.ui.main.MainViewModel;

import java.util.Date;

import me.csxiong.library.base.XActivity;
import me.csxiong.library.utils.XDisplayUtil;
import me.csxiong.library.utils.XTimerTools;

public class MainActivity extends XActivity<ActivityMainBinding, MainViewModel> {

    private Date time = new Date();

    private ObjectAnimator animator;

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
        animator = ObjectAnimator.ofFloat(mViewBinding.cl, "Alpha", 1, 0, 0, 0, 0, 0, 0, 1)
                .setDuration(1000);
        //轮训
        XTimerTools.infinite(() -> {
            if (mViewBinding != null) {
                long t = System.currentTimeMillis();
                time.setTime(t);
                mViewBinding.h1.setNumber(time.getHours() / 10);
                mViewBinding.h2.setNumber(time.getHours() % 10);
                mViewBinding.m1.setNumber(time.getMinutes() / 10);
                mViewBinding.m2.setNumber(time.getMinutes() % 10);
                mViewBinding.s1.setNumber(time.getSeconds() / 10);
                mViewBinding.s2.setNumber(time.getSeconds() % 10);
                animator.start();
            }
        }, 1000)
                .start();


        setWindowBrightness(255);
    }

    private void setWindowBrightness(int brightness) {
        Window window = getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.screenBrightness = brightness / 255.0f;
        window.setAttributes(lp);
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
