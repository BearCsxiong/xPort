package me.csxiong.library.integration.config;

import android.app.Application;
import android.content.Context;

import androidx.annotation.Keep;
import androidx.fragment.app.FragmentManager;

import java.util.List;

import me.csxiong.library.base.GlobalConfig;
import me.csxiong.library.base.IAppDelegate;

/**
 * @Desc : 项目内主要注入
 * @Author : Bear - 2020/9/7
 */
@Keep
public class XPortGlobalConfig implements GlobalConfig {

    @Override
    public void injectAppLifecycle(Context context, List<IAppDelegate> lifecycles) {
        lifecycles.add(new XPortAppDelegate());
    }

    @Override
    public void injectActivityLifecycle(Context context, List<Application.ActivityLifecycleCallbacks> lifecycles) {

    }

    @Override
    public void injectFragmentLifecycle(Context context, List<FragmentManager.FragmentLifecycleCallbacks> lifecycles) {

    }
}
