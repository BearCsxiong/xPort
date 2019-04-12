package com.base.multi.di.module;

import android.app.Activity;

import dagger.Module;
import dagger.Provides;
import me.csxiong.library.di.scope.ActivityScope;

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
