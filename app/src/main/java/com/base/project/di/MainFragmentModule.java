package com.base.project.di;

import android.support.v4.app.Fragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import me.csxiong.library.di.scope.FragmentScope;

/**
 * @Desc : Fragment注入
 * @Author : csxiong create on 2019/7/17
 */
@Module
public abstract class MainFragmentModule {

    @FragmentScope
    @ContributesAndroidInjector
    abstract Fragment inject();

}
