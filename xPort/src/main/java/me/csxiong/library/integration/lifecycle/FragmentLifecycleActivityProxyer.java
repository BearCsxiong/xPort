/*
 * Copyright 2017 JessYan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package me.csxiong.library.integration.lifecycle;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import me.csxiong.library.R;
import me.csxiong.library.base.delegate.GlobalConfig;
import me.csxiong.library.integration.ManifestParser;

/**
 * @Desc : Fragment生命周期监控{@link me.csxiong.library.utils.RxLifecycleUtil}
 * @Author : csxiong create on 2019/7/17
 */
@Singleton
public class FragmentLifecycleActivityProxyer implements Application.ActivityLifecycleCallbacks {

    @Inject
    Application mApplication;

    private List<GlobalConfig> mModules;

    private List<FragmentManager.FragmentLifecycleCallbacks> mFragmentLifecycles = new ArrayList<>();

    @Inject
    public FragmentLifecycleActivityProxyer() {
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        if (mModules == null) {
            mModules = new ManifestParser<GlobalConfig>(mApplication).parse(activity.getResources().getString(R.string.GlobalConfig));
            for (GlobalConfig module : mModules) {
                //将框架外部, 开发者实现的 Fragment 的生命周期回调存入 mFragmentLifecycles 集合 (此时还未注册回调)
                module.injectFragmentLifecycle(mApplication, mFragmentLifecycles);
            }

        }

        if (activity instanceof FragmentActivity) {
            for (FragmentManager.FragmentLifecycleCallbacks callback : mFragmentLifecycles) {
                FragmentActivity aty = (FragmentActivity) activity;
                FragmentManager fragmentManager = aty.getSupportFragmentManager();
                fragmentManager.registerFragmentLifecycleCallbacks(callback, true);
            }
        }

    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }

}
