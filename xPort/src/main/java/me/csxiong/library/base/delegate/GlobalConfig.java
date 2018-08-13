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
package me.csxiong.library.base.delegate;

import android.app.Application;
import android.content.Context;
import android.support.v4.app.FragmentManager;

import java.util.List;

import me.csxiong.library.di.module.GlobalConfigModule;

public interface GlobalConfig {

    String NAME = "GlobalConfig";

    /**
     * 注入部分本框架提供的建造方式,完成dagger2的注入
     *
     * @param context
     * @param builder
     */
    void applyOptions(Context context, GlobalConfigModule.Builder builder);

    /**
     * 注入Application生命周期
     *
     * @param context
     * @param lifecycles
     */
    void injectAppLifecycle(Context context, List<IAppDelegate> lifecycles);

    /**
     * 注入Activity生命周期
     *
     * @param context
     * @param lifecycles
     */
    void injectActivityLifecycle(Context context, List<Application.ActivityLifecycleCallbacks> lifecycles);


    /**
     * 注入Fragment生命周期
     *
     * @param context
     * @param lifecycles
     */
    void injectFragmentLifecycle(Context context, List<FragmentManager.FragmentLifecycleCallbacks> lifecycles);
}
