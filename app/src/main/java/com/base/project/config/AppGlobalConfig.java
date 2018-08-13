package com.base.project.config;

import android.app.Application;
import android.content.Context;
import android.support.v4.app.FragmentManager;

import me.csxiong.library.base.delegate.GlobalConfig;
import me.csxiong.library.base.delegate.IAppDelegate;
import me.csxiong.library.di.module.ClientModule;
import me.csxiong.library.di.module.GlobalConfigModule;
import com.orhanobut.logger.PrettyFormatStrategy;

import java.util.List;

/**
 * Created by csxiong on 2018/8/8.
 */

public class AppGlobalConfig implements GlobalConfig {

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
        lifecycles.add(new AppDelegateImpl());
    }

    @Override
    public void injectActivityLifecycle(Context context, List<Application.ActivityLifecycleCallbacks> lifecycles) {

    }

    @Override
    public void injectFragmentLifecycle(Context context, List<FragmentManager.FragmentLifecycleCallbacks> lifecycles) {

    }
}
