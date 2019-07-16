package me.csxiong.library.base.delegate;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;

/**
 * @Desc : Application生命周期代理
 * @Author : csxiong create on 2019/7/17
 */
public interface IAppDelegate {

    /**
     * Application生命周期
     *
     * @param context
     */
    void attachBaseContext(@NonNull Context context);

    /**
     * Application生命周期
     *
     * @param application
     */
    void onCreate(@NonNull Application application);

    /**
     * Application生命周期
     *
     * @param application
     */
    void onTerminate(@NonNull Application application);
}
