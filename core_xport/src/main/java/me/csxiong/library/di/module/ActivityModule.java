package me.csxiong.library.di.module;

import android.app.Activity;

import dagger.Module;
import dagger.Provides;
import me.csxiong.library.di.scope.ActivityScope;

/**
 * @Desc : 活动模块
 * @Author : csxiong create on 2019/7/16
 */
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
