package me.csxiong.library.base;

import android.support.annotation.NonNull;

import me.csxiong.library.di.component.AppComponent;

/**
 * -------------------------------------------------------------------------------
 * |
 * | desc : clean mode to provide component from xPort module
 * |
 * |--------------------------------------------------------------------------------
 * | on 2018/8/14 created by csxiong
 * |--------------------------------------------------------------------------------
 */
public interface IApp {

    @NonNull
    AppComponent getAppComponent();
}
