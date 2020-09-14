package com.base.project.ui;

import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.base.project.R;
import com.base.project.databinding.ActivityMainBinding;
import com.base.project.ui.main.MainViewModel;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import me.csxiong.library.base.BaseActivity;
import me.csxiong.library.integration.http.ResponseListener;
import me.csxiong.library.integration.http.XHttp;
import me.csxiong.library.integration.process.DelegateFragment;
import me.csxiong.library.integration.process.DelegateProcess;
import me.csxiong.library.integration.process.XProcesser;
import me.csxiong.library.integration.sys.XDialog;
import me.csxiong.library.utils.ImmersiveModeUtil;
import me.csxiong.library.utils.ThreadExecutor;
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

    }

    @Override
    public void initData() {

    }

    public void change1(View v) {
        new XDialog.Builder()
                .setContent("SDLFJSDLFJLSDJF")
                .setTitle("DLSFJLSDJFLDS")
                .setPositiveText("OK")
                .setNegativeText("CANCEL")
                .build()
                .show();
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
