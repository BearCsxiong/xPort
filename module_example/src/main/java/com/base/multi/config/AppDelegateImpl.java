package com.base.multi.config;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;

import com.alibaba.android.arouter.launcher.ARouter;
import me.csxiong.library.base.delegate.IAppDelegate;
import com.kehua.pile.BuildConfig;

/**
 * Created by csxiong on 2018/8/8.
 */

public class AppDelegateImpl implements IAppDelegate {
    @Override
    public void attachBaseContext(@NonNull Context context) {
        ARouter.init((Application) context);
        if (BuildConfig.DEBUG) {
            ARouter.openDebug();
            ARouter.openLog();
        }
    }

    @Override
    public void onCreate(@NonNull Application application) {

    }

    @Override
    public void onTerminate(@NonNull Application application) {

    }
}
