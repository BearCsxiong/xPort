package com.base.project.config;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.multidex.MultiDex;

import com.alibaba.android.arouter.launcher.ARouter;
import me.csxiong.library.base.delegate.IAppDelegate;
import com.base.project.BuildConfig;

/**
 * Created by csxiong on 2018/8/8.
 */

public class AppDelegateImpl implements IAppDelegate {
    @Override
    public void attachBaseContext(@NonNull Context context) {
        MultiDex.install(context);
    }

    @Override
    public void onCreate(@NonNull Application application) {
        ARouter.init(application);
        if (BuildConfig.DEBUG) {
            ARouter.openLog();
            ARouter.openDebug();
        }
    }

    @Override
    public void onTerminate(@NonNull Application application) {

    }
}
