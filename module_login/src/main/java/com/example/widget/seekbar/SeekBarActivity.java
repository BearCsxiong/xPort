package com.example.widget.seekbar;

import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.login.R;
import com.example.login.databinding.ActivitySeekBarBinding;

import me.csxiong.library.base.BaseActivity;

/**
 * @Desc : Seekbar测试界面
 * @Author : csxiong - 2020-01-13
 */
@Route(path = "/login/seekbar", name = "Seekbar测试界面")
public class SeekBarActivity extends BaseActivity<ActivitySeekBarBinding> {

    @Override
    public int getLayoutId() {
        return R.layout.activity_seek_bar;
    }

    @Override
    public void initView() {
        mViewBinding.xsb.setOnProgressChangeListener(new XSeekBar.OnProgressChangeListener() {
            @Override
            public void onStartTracking(int progress) {
            }

            @Override
            public void onProgressChange(int progress, boolean fromUser) {
            }

            @Override
            public void onStopTracking(int progress, boolean fromUser) {
            }
        });

        mViewBinding.xsb.setEnableStroke(false);
        mViewBinding.xsb.setBackgroundColor(0xffE5E5E5);
        mViewBinding.xsb.setProgressColor(0xffFB5986);

        mViewBinding.xsb1.setEnableCenterPoint(false);
        mViewBinding.xsb1.setCenterPointPercent(0);
        mViewBinding.xsb1.setMinProgress(0);
        mViewBinding.xsb1.setMaxProgress(100);

        mViewBinding.xsb2.setCenterPointPercent(0.3f);
        mViewBinding.xsb2.setMinProgress(-33);
        mViewBinding.xsb2.setMaxProgress(67);

        mViewBinding.btn
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        i = 1 - i;
                        boolean isUp = i == 0;
                        if (isUp) {
                            mViewBinding.xsb.setProgress(100, true);
                        } else {
                            mViewBinding.xsb.setProgress(-80, true);
                        }
                    }
                });
    }

    private int i = 0;

    @Override
    public void initData() {

    }
}
