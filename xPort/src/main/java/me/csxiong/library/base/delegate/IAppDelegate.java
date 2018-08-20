package me.csxiong.library.base.delegate;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;

/**-------------------------------------------------------------------------------
*|
*| desc : Application生命周期回调
*|
*|--------------------------------------------------------------------------------
*| on 2018/8/14 created by csxiong
*|--------------------------------------------------------------------------------
*/
public interface IAppDelegate {
    void attachBaseContext(@NonNull Context context);

    void onCreate(@NonNull Application application);

    void onTerminate(@NonNull Application application);
}
