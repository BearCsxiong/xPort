package com.base.multi.di.module;

import android.app.Activity;

import me.csxiong.library.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;


@Module
public class ActivityModule {
    private Activity mActivity;

    public ActivityModule(Activity activity) {
        this.mActivity = activity;
    }

    @Provides
    @ActivityScope
    public Activity provideActivity() {
        return mActivity;
    }
}
