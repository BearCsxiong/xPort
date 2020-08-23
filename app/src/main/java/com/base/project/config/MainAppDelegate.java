package com.base.project.config;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.ViewUtils;
import androidx.multidex.MultiDex;

import com.alibaba.android.arouter.launcher.ARouter;
import com.base.project.BuildConfig;

import me.csxiong.library.base.ActivityImmersiveConfig;
import me.csxiong.library.base.BaseActivity;
import me.csxiong.library.base.IAppDelegate;
import me.csxiong.library.utils.ImmersiveModeUtil;
import me.csxiong.library.widget.title.XTitleBar;
import me.csxiong.library.widget.title.XTitleBarConfig;

/**
 * @Desc : Application Main代理
 * @Author : csxiong create on 2019/7/17
 */
public class MainAppDelegate implements IAppDelegate {
    @Override
    public void attachBaseContext(@NonNull Context context) {
        MultiDex.install(context);
    }

    @Override
    public void onCreate(@NonNull Application application) {
        if (BuildConfig.DEBUG) {
            ARouter.openLog();
            ARouter.openDebug();
        }
        ARouter.init(application);

        XTitleBar.config = new XTitleBarConfig() {
            @Override
            public void onFinishInflate(XTitleBar xTitleBar) {
            }
        };

        BaseActivity.config = new ActivityImmersiveConfig() {
            @Override
            public void beforeContentView(Activity activity) {
//                ImmersiveModeUtil.setStatusBarTransparent(activity);
                ImmersiveModeUtil.setStatusBarBgColor(activity,0xff2C77EC);
            }

            @Override
            public void afterContentView(Activity activity) {

            }
        };
    }

    @Override
    public void onTerminate(@NonNull Application application) {

    }
}
