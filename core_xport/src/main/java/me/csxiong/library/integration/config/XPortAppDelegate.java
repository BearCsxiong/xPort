package me.csxiong.library.integration.config;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;

import me.csxiong.library.base.IAppDelegate;
import me.csxiong.library.utils.ActivityUtils;

/**
 * @Desc : 项目内主要注入
 * @Author : Bear - 2020/9/7
 */
public class XPortAppDelegate implements IAppDelegate {

    @Override
    public void attachBaseContext(@NonNull Context context) {

    }

    @Override
    public void onCreate(@NonNull Application application) {
        ActivityUtils.init(application);
    }

    @Override
    public void onTerminate(@NonNull Application application) {

    }
}
