package com.base.project.ui;

import android.graphics.Color;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.base.project.R;
import com.base.project.databinding.ActivityMainBinding;
import com.base.project.ui.main.MainViewModel;

import me.csxiong.library.base.BaseActivity;
import me.csxiong.library.utils.XToast;

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

    public void change1(View v) {
//        XToast.error("失败");
//        ARouter.getInstance().build("/main/second")
//                .navigation(this);
        startLoading("开始下载...");
    }

    public void change2(View v) {
        XToast.success("成功");
    }

    public void change3(View v) {
        XToast.info("信息");
    }

    public void change4(View v) {
        XToast.warning("提示");
    }

    public void change5(View v) {
        XToast.show("Toast");
    }

}
