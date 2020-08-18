package com.base.project.config;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.multidex.MultiDex;

import com.alibaba.android.arouter.launcher.ARouter;
import com.base.project.BuildConfig;
import me.csxiong.library.base.IAppDelegate;

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
    }

    @Override
    public void onTerminate(@NonNull Application application) {

    }
}
