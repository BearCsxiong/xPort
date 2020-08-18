package me.csxiong.library.base;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import me.csxiong.library.R;
import me.csxiong.library.integration.ManifestParser;

/**
 * @Desc : Application代理管理器,管理所有注入GlobalConfig的配置
 * @Author : csxiong create on 2019/7/17
 */
public class AppDelegateManager implements IAppDelegate {

    /**
     * 当前Application
     */
    private Application mApplication;

    /**
     * module 集合
     */
    private List<GlobalConfig> mModules;

    /**
     * AppLifecycle 集合
     */
    private List<IAppDelegate> mIAppLifecycles = new ArrayList<>();

    /**
     * ActivityLifecycle 集合
     */
    private List<Application.ActivityLifecycleCallbacks> mActivityLifecycles = new ArrayList<>();

    /**
     * 构造
     *
     * @param context
     */
    public AppDelegateManager(@NonNull Context context) {
        this.mModules = new ManifestParser<GlobalConfig>(context).parse(context.getResources().getString(R.string.GlobalConfig));
        for (GlobalConfig module : mModules) {
            module.injectAppLifecycle(context, mIAppLifecycles);
            module.injectActivityLifecycle(context, mActivityLifecycles);
        }
    }

    @Override
    public void attachBaseContext(@NonNull Context base) {
        for (IAppDelegate lifecycle : mIAppLifecycles) {
            lifecycle.attachBaseContext(base);
        }
    }

    @Override
    public void onCreate(@NonNull Application application) {
        this.mApplication = application;
        this.mModules = null;
        for (Application.ActivityLifecycleCallbacks lifecycle : mActivityLifecycles) {
            mApplication.registerActivityLifecycleCallbacks(lifecycle);
        }
        for (IAppDelegate lifecycle : mIAppLifecycles) {
            lifecycle.onCreate(mApplication);
        }
    }

    @Override
    public void onTerminate(@NonNull Application application) {
        if (mActivityLifecycles != null && mActivityLifecycles.size() > 0) {
            for (Application.ActivityLifecycleCallbacks lifecycle : mActivityLifecycles) {
                mApplication.unregisterActivityLifecycleCallbacks(lifecycle);
            }
        }
        if (mIAppLifecycles != null && mIAppLifecycles.size() > 0) {
            for (IAppDelegate lifecycle : mIAppLifecycles) {
                lifecycle.onTerminate(mApplication);
            }
        }
        this.mActivityLifecycles = null;
        this.mIAppLifecycles = null;
        this.mApplication = null;
    }

}

