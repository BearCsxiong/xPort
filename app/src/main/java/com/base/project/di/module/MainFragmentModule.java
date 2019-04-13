package com.base.project.di.module;

import android.support.v4.app.Fragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import me.csxiong.library.di.scope.FragmentScope;


@Module
public abstract class MainFragmentModule {

    @FragmentScope
    @ContributesAndroidInjector
    abstract Fragment injectFragment();
}
