package com.example.widget;

import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.login.R;
import com.example.login.databinding.ActivityPolygonBinding;

import me.csxiong.library.base.BaseActivity;

/**
 * @Desc : 多边形界面
 * @Author : csxiong - 2019-12-23
 */
@Route(path = "/login/polygon")
public class PolygonActivity extends BaseActivity<ActivityPolygonBinding> {

    private int lineSize = 1;

    @Override
    public int getLayoutId() {
        return R.layout.activity_polygon;
    }

    @Override
    public void initView() {
        mViewBinding.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lineSize < 20) {
                    lineSize++;
                } else {
                    lineSize = 1;
                }
                mViewBinding.btn.setText(lineSize + "");
                mViewBinding.pv.setLineSize(lineSize);
                mViewBinding.pv.invalidate();
            }
        });
    }

    @Override
    public void initData() {

    }
}
