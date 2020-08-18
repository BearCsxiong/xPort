package com.base.project.ui;

import android.graphics.Color;
import android.view.Window;
import android.view.WindowManager;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.base.project.R;
import com.base.project.databinding.ActivityMainBinding;
import com.base.project.ui.main.MainViewModel;

import me.csxiong.library.base.BaseActivity;

/**
 * @Desc : 主页
 * @Author : csxiong - 2019-11-13
 */
@Route(path = "/app/main", name = "主页")
public class MainActivity extends BaseActivity<ActivityMainBinding> {

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initView() {
        setTranslucentStatus(true);
        // create our manager instance after the content view is set
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        // enable status bar tint
        tintManager.setStatusBarTintEnabled(true);
        // enable navigation bar tint
        tintManager.setNavigationBarTintEnabled(true);
        tintManager.setStatusBarTintColor(Color.TRANSPARENT);
    }

    @Override
    public void initData() {

    }

    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

}
