package com.base.project.di.utils;

import android.app.Activity;

import com.base.project.di.component.ActivityComponent;
import com.base.project.di.component.DaggerActivityComponent;
import com.base.project.di.component.DaggerUtilComponent;
import com.base.project.di.component.UtilComponent;
import com.base.project.di.module.ActivityModule;

import me.csxiong.library.base.APP;

/**
 * Created by walten on 2018/4/27.
 */

public class DaggerUtils {

    public static ActivityComponent getActivityComponent(Activity activity) {
        return DaggerActivityComponent.builder()
                .activityModule(new ActivityModule(activity))
                .appComponent(APP.getInstance().getAppComponent())
                .build();
    }

    public static UtilComponent getUtilComponent() {
        return DaggerUtilComponent.builder()
                .build();
    }

}
