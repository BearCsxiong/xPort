package com.base.project.di;

import android.app.Activity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import me.csxiong.library.di.scope.ActivityScope;

/**
 * @Desc : Activity注入
 * @Author : csxiong create on 2019/7/17
 */
@Module
public abstract class MainActivityModule {

    @ActivityScope
    @ContributesAndroidInjector
    abstract Activity inject();
}
