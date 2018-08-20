package me.csxiong.library.base;

import android.support.annotation.NonNull;

import me.csxiong.library.di.component.AppComponent;

/**-------------------------------------------------------------------------------
*|
*| desc : 提供dagger2注入,提供已配置模块
*|
*|--------------------------------------------------------------------------------
*| on 2018/8/14 created by csxiong
*|--------------------------------------------------------------------------------
*/
public interface IApp {
    @NonNull
    AppComponent getAppComponent();
}
