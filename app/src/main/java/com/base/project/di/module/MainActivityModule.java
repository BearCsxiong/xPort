package com.base.project.di.module;

import com.base.project.main.MainActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import me.csxiong.library.di.scope.ActivityScope;


@Module
public abstract class MainActivityModule {

    @ActivityScope
    @ContributesAndroidInjector
    abstract MainActivity injectMain();
}
