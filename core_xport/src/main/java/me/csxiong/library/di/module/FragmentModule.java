package me.csxiong.library.di.module;

import android.app.Activity;
import android.support.v4.app.Fragment;

import dagger.Module;
import dagger.Provides;
import me.csxiong.library.di.scope.FragmentScope;

/**
 * @Desc : 统一Fragment的Module
 * @Author : csxiong create on 2019/7/16
 */

@Module
public class FragmentModule {
    private Fragment fragment;

    public FragmentModule(Fragment fragment) {
        this.fragment = fragment;
    }

    @Provides
    @FragmentScope
    public Activity provideActivity() {
        return fragment.getActivity();
    }

}
