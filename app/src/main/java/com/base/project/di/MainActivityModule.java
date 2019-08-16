package com.base.project.di;

import com.base.project.ui.MainActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import me.csxiong.library.di.scope.ActivityScope;

/**
 * @Desc : 模块内部Activity注册 注册需要注入的Activity
 * @Author : csxiong create on 2019/7/17
 */
@Module
public abstract class MainActivityModule {

    @ActivityScope
    @ContributesAndroidInjector
    abstract MainActivity injectMain();

}
