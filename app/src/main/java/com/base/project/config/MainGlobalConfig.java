package com.base.project.config;

import android.app.Application;
import android.content.Context;
import android.support.v4.app.FragmentManager;

import com.orhanobut.logger.PrettyFormatStrategy;

import java.util.List;

import me.csxiong.library.base.GlobalConfig;
import me.csxiong.library.base.IAppDelegate;
import me.csxiong.library.di.module.ClientModule;
import me.csxiong.library.di.module.GlobalConfigModule;

/**
 * @Desc : Main全局配置
 * @Author : csxiong create on 2019/7/17
 */
public class MainGlobalConfig implements GlobalConfig {

    @Override
    public void applyOptions(Context context, GlobalConfigModule.Builder builder) {
        builder.loggerConfiguration(new ClientModule.LoggerConfiguration() {
            @Override
            public void configLogger(Context context, PrettyFormatStrategy.Builder builder) {
                builder.build();
            }
        });
    }

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
