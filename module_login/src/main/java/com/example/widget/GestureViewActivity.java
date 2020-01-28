package com.example.widget;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.login.R;
import com.example.login.databinding.ActivityGestureBinding;

import me.csxiong.library.base.BaseActivity;

@Route(path = "/login/gesture", name = "手势View测试界面")
public class GestureViewActivity extends BaseActivity<ActivityGestureBinding> {

    @Override
    public int getLayoutId() {
        return R.layout.activity_gesture;
    }

    @Override
    public void initView() {
    }

    @Override
    public void initData() {

    }
}
