package com.example.widget;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.login.R;
import com.example.login.databinding.ActivityCaptureBinding;
import me.csxiong.library.base.BaseActivity;

@Route(path = "/login/capture",name = "多人拍照手势控件")
public class CaptureViewActivity extends BaseActivity<ActivityCaptureBinding> {

    @Override
    public int getLayoutId() {
        return R.layout.activity_capture;
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {

    }
}
