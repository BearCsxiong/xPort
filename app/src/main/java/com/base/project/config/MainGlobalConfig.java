package com.base.project.config;

import android.app.Application;
import android.content.Context;

import androidx.fragment.app.FragmentManager;

import java.util.List;

import me.csxiong.library.base.GlobalConfig;
import me.csxiong.library.base.IAppDelegate;

/**
 * @Desc : Main全局配置
 * @Author : csxiong create on 2019/7/17
 */
public class MainGlobalConfig implements GlobalConfig {

    @Override
    public void injectAppLifecycle(Context context, List<IAppDelegate> lifecycles) {
        lifecycles.add(new MainAppDelegate());
    }

    @Override
    public void injectActivityLifecycle(Context context, List<Application.ActivityLifecycleCallbacks> lifecycles) {

    }

    @Override
    public void injectFragmentLifecycle(Context context, List<FragmentManager.FragmentLifecycleCallbacks> lifecycles) {

    }
}
