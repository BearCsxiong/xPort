package me.csxiong.library.base.delegate;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;

/**
 * @Desc : Application生命周期代理
 * @Author : csxiong create on 2019/7/17
 */
public interface IAppDelegate {

    void attachBaseContext(@NonNull Context context);

    void onCreate(@NonNull Application application);

    void onTerminate(@NonNull Application application);
}
